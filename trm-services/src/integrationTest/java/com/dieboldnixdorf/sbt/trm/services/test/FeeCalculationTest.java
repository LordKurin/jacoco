package com.dieboldnixdorf.sbt.trm.services.test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.arquillian.testng.Arquillian;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dieboldnixdorf.sbt.trm.businessobjects.PurchaseAmount;
import com.dieboldnixdorf.sbt.trm.services.FeeCalculation;
import com.dieboldnixdorf.txm.core.businessmodel.businessobjects.ExtendedResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.cdi.util.ServiceTestHandler;
import com.dieboldnixdorf.txm.core.businessmodel.container.ClientContext;
import com.dieboldnixdorf.txm.core.businessmodel.container.Request;
import com.dieboldnixdorf.txm.core.businessmodel.container.SessionHandlingInstruction;
import com.dieboldnixdorf.txm.core.businessmodel.container.TransactionStep;
import com.dieboldnixdorf.txm.core.businessmodel.services.ResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceResponse;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.TransactionService;
import com.dieboldnixdorf.txm.core.businessobjects.financial.amount.Amount;
import com.dieboldnixdorf.txm.core.businessobjects.financial.amount.Currency;
import com.myproclassic.server.servicelocator.PCEServiceLocator;

public class FeeCalculationTest extends Arquillian {

    @Inject
    @TransactionService(id = FeeCalculation.SERVICE_ID)
    private FeeCalculation service;

    @Inject
    private ServiceTestHandler testHandler;

    @Test
    public void feeCalculationTest() {
        // TODO: Implement test
    	
    	ServiceTestHandler testHandler = PCEServiceLocator.getLocator(ServiceTestHandler.class).get();
    	FeeCalculation service = PCEServiceLocator.getLocator(FeeCalculation.class, new TransactionService.Literal(FeeCalculation.SERVICE_ID)).get();

    	// prepare request
    	Request request = new Request.Builder()
    	        .sessionHandlingInstruction(SessionHandlingInstruction.INTERMEDIATE_REQUEST)
    	        .transactionStepId(1)
    	        .clientId("TEST_CLIENT")
    	        .time(Instant.now())
    	        .transactionId(1)
    	        .build();

    	// prepare input facets
    	PurchaseAmount purchaseAmount = new PurchaseAmount.Builder()
    	                .requestedAmount(new Amount.Builder()
    	                        .value(10000)
    	                        .currency(new Currency("USD", 840, 2))
    	                        .build())
    	                .build();

    	// add facet to request
    	request.addFacet(purchaseAmount);

    	// define parameters
    	Map<String, String> params = new HashMap<>();
    	        params.put("feePercentValue", "5");

    	// set up context
    	ClientContext clientContext = new ClientContext(request.getHeader().getClientId());
    	TransactionStep step = clientContext.createConsumerSession().createTransaction().createTransactionStep(request);

    	// call service
    	ServiceResponse response = testHandler.callWithTestContext(service, step, params);

        // check service response
        Assert.assertEquals(response.getResponseCode(), ResponseCode.OK);
        Assert.assertEquals(response.getExtendedResponseCode(), ExtendedResponseCode.OK);

        // check fee calculated by service
        Assert.assertEquals(step.getResponse().getFacet(PurchaseAmount.class).get().getFee().getValue(), 200l);
    }

    @Test
    public void feeCalculationTestNoFacet() {
        // create new request
        Request request = new Request.Builder()
                .sessionHandlingInstruction(SessionHandlingInstruction.INTERMEDIATE_REQUEST)
                .transactionStepId(1)
                .clientId("TEST_CLIENT")
                .time(Instant.now())
                .transactionId(1)
                .build();


        // skip facet creation

        // create transaction step
        ClientContext clientContext = new ClientContext(request.getHeader().getClientId());
        TransactionStep step = clientContext.createConsumerSession().createTransaction().createTransactionStep(request);

        // call service (without purchaseAmount facet)
        Map<String, String> params = new HashMap<>();
        params.put("feePercentValue", "5");
        ServiceResponse response = testHandler.callWithTestContext(service, step, params);

        // check service response
        Assert.assertEquals(response.getResponseCode(), ResponseCode.FAIL);
        Assert.assertEquals(response.getExtendedResponseCode(), ExtendedResponseCode.SERVER_FAILURE);
    }
}