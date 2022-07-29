package com.dieboldnixdorf.sbt.trm.services.test;

import java.time.Instant;
import java.util.HashMap;

import javax.inject.Inject;

import org.jboss.arquillian.testng.Arquillian;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dieboldnixdorf.sbt.trm.SbtExtendedResponseCodes;
import com.dieboldnixdorf.sbt.trm.businessobjects.TimeSinceLastTransaction;
import com.dieboldnixdorf.sbt.trm.services.FeeCalculation;
import com.dieboldnixdorf.sbt.trm.services.FeeDecision;
import com.dieboldnixdorf.txm.core.businessmodel.businessobjects.ExtendedResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.cdi.util.ServiceTestHandler;
import com.dieboldnixdorf.txm.core.businessmodel.container.ClientContext;
import com.dieboldnixdorf.txm.core.businessmodel.container.Request;
import com.dieboldnixdorf.txm.core.businessmodel.container.SessionHandlingInstruction;
import com.dieboldnixdorf.txm.core.businessmodel.container.TransactionStep;
import com.dieboldnixdorf.txm.core.businessmodel.services.ResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceResponse;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.TransactionService;

public class FeeDecisionTest extends Arquillian {

    @Inject
    @TransactionService(id = FeeDecision.SERVICE_ID)
    private FeeDecision service;

    @Inject
    private ServiceTestHandler testHandler;

	
	
	
	@Test
	public void noFeeDecisionTest() {
	 // create new request
	    Request request = new Request.Builder()
	            .sessionHandlingInstruction(SessionHandlingInstruction.INTERMEDIATE_REQUEST)
	            .transactionStepId(1)
	            .clientId("TEST_CLIENT")
	            .time(Instant.now())
	            .transactionId(1)
	            .build();

	    TimeSinceLastTransaction timeSinceLastTransaction = new TimeSinceLastTransaction.Builder()
	            .elapsedTime(6000000l)
	            .build();

	    // add facet to request
	    request.addFacet(timeSinceLastTransaction);

	    // create transaction step
	    ClientContext clientContext = new ClientContext(request.getHeader().getClientId());
	    TransactionStep step = clientContext.createConsumerSession().createTransaction().createTransactionStep(request);

	    // call service
	    ServiceResponse response = testHandler.callWithTestContext(service, step, new HashMap<>());

	    // check service response
	    Assert.assertEquals(response.getResponseCode(), ResponseCode.OK);
	    Assert.assertEquals(response.getExtendedResponseCode(), ExtendedResponseCode.OK);
	}

	@Test
	public void feeDecisionTest() {
	 // create new request
	    Request request = new Request.Builder()
	            .sessionHandlingInstruction(SessionHandlingInstruction.INTERMEDIATE_REQUEST)
	            .transactionStepId(1)
	            .clientId("TEST_CLIENT")
	            .time(Instant.now())
	            .transactionId(1)
	            .build();

	    TimeSinceLastTransaction timeSinceLastTransaction = new TimeSinceLastTransaction.Builder()
	            .elapsedTime(1l)
	            .build();

	    // add facet to request
	    request.addFacet(timeSinceLastTransaction);

	    // create transaction step
	    ClientContext clientContext = new ClientContext(request.getHeader().getClientId());
	    TransactionStep step = clientContext.createConsumerSession().createTransaction().createTransactionStep(request);

	    // call service
	    ServiceResponse response = testHandler.callWithTestContext(service, step, new HashMap<>());

	    // check service response
	    Assert.assertEquals(response.getResponseCode(), ResponseCode.OK);
	    Assert.assertEquals(response.getExtendedResponseCode(), SbtExtendedResponseCodes.FEE_REQUIRED);
	}
}
