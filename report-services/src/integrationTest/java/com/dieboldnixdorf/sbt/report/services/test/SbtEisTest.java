package com.dieboldnixdorf.sbt.report.services.test;


import java.time.Instant;
import java.util.HashMap;

import javax.inject.Inject;

import org.jboss.arquillian.testng.Arquillian;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dieboldnixdorf.sbt.report.services.SbtEis;
import com.dieboldnixdorf.txm.core.businessmodel.businessobjects.ExtendedResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.cdi.util.ServiceTestHandler;
import com.dieboldnixdorf.txm.core.businessmodel.container.ClientContext;
import com.dieboldnixdorf.txm.core.businessmodel.container.Request;
import com.dieboldnixdorf.txm.core.businessmodel.container.SessionHandlingInstruction;
import com.dieboldnixdorf.txm.core.businessmodel.container.TransactionStep;
import com.dieboldnixdorf.txm.core.businessmodel.services.ResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceResponse;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.TransactionService;

public class SbtEisTest extends Arquillian {

	@Inject
	@TransactionService(id=SbtEis.SERVICE_ID)
	private SbtEis service;
	
    @Inject
    private ServiceTestHandler testHandler;
	
	@Test
	public void sbtEisTest() throws Exception{

	
	Request request = new Request.Builder()
			.sessionHandlingInstruction(SessionHandlingInstruction.INTERMEDIATE_REQUEST)
			.transactionStepId(1)
			.clientId("TEST_CLIENT")
			.time(Instant.now())
			.transactionId(1)
			.build();
	
	ClientContext clientContext = new ClientContext(request.getHeader().getClientId());
	TransactionStep step = clientContext.createConsumerSession().createTransaction().createTransactionStep(request);
	
	ServiceResponse response = testHandler.callWithTestContext(service, step, new HashMap<>());;
	
	System.out.println(response.toString());
	
	Assert.assertEquals(response.getResponseCode(), ResponseCode.OK);
	Assert.assertEquals(response.getExtendedResponseCode(), ExtendedResponseCode.OK);
	}
}
