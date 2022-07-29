package com.dieboldnixdorf.sbt.trm.services.test;

import java.time.Instant;
import java.util.HashMap;

import javax.inject.Inject;

import org.jboss.arquillian.testng.Arquillian;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dieboldnixdorf.sbt.trm.businessobjects.CustomerIdentification;
import com.dieboldnixdorf.sbt.trm.services.TimestampReader;
import com.dieboldnixdorf.sbt.trm.services.TimestampWriter;
import com.dieboldnixdorf.txm.core.businessmodel.businessobjects.ExtendedResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.cdi.util.ServiceTestHandler;
import com.dieboldnixdorf.txm.core.businessmodel.container.ClientContext;
import com.dieboldnixdorf.txm.core.businessmodel.container.Request;
import com.dieboldnixdorf.txm.core.businessmodel.container.SessionHandlingInstruction;
import com.dieboldnixdorf.txm.core.businessmodel.container.TransactionStep;
import com.dieboldnixdorf.txm.core.businessmodel.services.ResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceResponse;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.TransactionService;

public class TimestampReaderWriterTest extends Arquillian {

    @Inject
    @TransactionService(id = TimestampWriter.SERVICE_ID)
    private TimestampWriter timestampWriterService;

    @Inject
    @TransactionService(id = TimestampReader.SERVICE_ID)
    private TimestampReader timestampReaderService;

    @Inject
    private ServiceTestHandler testHandler;

    @Test
    public void timestampReaderWriterTest() {
        // create new request
        Request request = new Request.Builder()
                .sessionHandlingInstruction(SessionHandlingInstruction.INTERMEDIATE_REQUEST)
                .transactionStepId(1)
                .clientId("TEST_CLIENT")
                .time(Instant.now())
                .transactionId(1)
                .build();

        // create customer id facet
        CustomerIdentification customerId = new CustomerIdentification.Builder()
                .id("1979-06-11-abcdef")
                .build();

        // add facet to request
        request.addFacet(customerId);

        // create transaction step
        ClientContext clientContext = new ClientContext(request.getHeader().getClientId());
        TransactionStep step = clientContext.createConsumerSession().createTransaction().createTransactionStep(request);

        // call service
        ServiceResponse response = testHandler.callWithTestContext(timestampReaderService, step, new HashMap<>());

        // check service response
        Assert.assertEquals(response.getResponseCode(), ResponseCode.OK);
        Assert.assertEquals(response.getExtendedResponseCode(), ExtendedResponseCode.OK);

        // call service
        response = testHandler.callWithTestContext(timestampWriterService, step, new HashMap<>());

        // check service response
        Assert.assertEquals(response.getResponseCode(), ResponseCode.OK);
        Assert.assertEquals(response.getExtendedResponseCode(), ExtendedResponseCode.OK);

        // call service
        response = testHandler.callWithTestContext(timestampReaderService, step, new HashMap<>());

        // check service response
        Assert.assertEquals(response.getResponseCode(), ResponseCode.OK);
        Assert.assertEquals(response.getExtendedResponseCode(), ExtendedResponseCode.OK);
    }
}