/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.amqp;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.RandomStringUtils;
import org.mule.api.MuleMessage;
import org.mule.util.ArrayUtils;

public class AmqpReplyToITCase extends AbstractAmqpITCase
{
    public AmqpReplyToITCase() throws IOException
    {
        super();
    }

    @Override
    protected String getConfigResources()
    {
        return "reply-to-tests-config.xml";
    }

    public void testReplyTo() throws Exception
    {
        final Future<MuleMessage> futureReceivedMessage = setupFunctionTestComponentForFlow("amqpReplyTargetService");

        final byte[] body = RandomStringUtils.randomAlphanumeric(20).getBytes();
        final String correlationId = dispatchTestMessage(body, "amqpReplierService",
            "amqpReplyTargetService-queue");

        // FIXME restore DEFAULT_MULE_TEST_TIMEOUT_SECS
        final MuleMessage receivedMessage = futureReceivedMessage.get(1L, TimeUnit.SECONDS);

        assertValidReceivedMessage(correlationId, ArrayUtils.addAll(body, "-reply".getBytes()),
            receivedMessage);
    }

}
