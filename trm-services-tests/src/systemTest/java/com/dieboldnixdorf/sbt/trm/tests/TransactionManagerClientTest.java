package com.dieboldnixdorf.sbt.trm.tests;

import java.net.URL;
import java.time.Instant;
import java.util.UUID;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dieboldnixdorf.sbt.trm.SBTTrmBusinessCases;
import com.dieboldnixdorf.sbt.trm.businessobjects.CustomerIdentification;
import com.dieboldnixdorf.sbt.trm.businessobjects.PurchaseAmount;
import com.dieboldnixdorf.txm.core.businessmodel.businessobjects.ExtendedResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.container.Request;
import com.dieboldnixdorf.txm.core.businessmodel.container.Response;
import com.dieboldnixdorf.txm.core.businessmodel.container.SessionHandlingInstruction;
import com.dieboldnixdorf.txm.core.businessmodel.services.ResponseCode;
import com.dieboldnixdorf.txm.core.businessobjects.financial.amount.Amount;
import com.dieboldnixdorf.txm.core.businessobjects.financial.amount.Currency;
import com.dieboldnixdorf.txm.uc.communication.ConnectionFactory;
import com.dieboldnixdorf.txm.uc.communication.ServerConnectionBase;

@Test(groups = "TransactionManagerClientTest")
public class TransactionManagerClientTest extends Arquillian {

    @ArquillianResource
    private URL serverUrl;

    private ServerConnectionBase server = null;

    private UUID customerUUID;

    @BeforeMethod
    public void prepare() {
        server = ConnectionFactory.getServerContext(serverUrl, "interProcess");
        customerUUID = UUID.randomUUID();
    }


    @Test(priority=0)
    public void testPurchaseOrderProcess_OK() throws Exception {

        Request request = createRequest();

        // create purchase amount facet
        PurchaseAmount purchaseAmount = new PurchaseAmount.Builder()
                .requestedAmount(new Amount.Builder()
                        .value(10000)
                        .currency(new Currency("USD", 840, 2))
                        .build())
                .build();

        // add facet to request
        request.addFacet(purchaseAmount);

        // create customer id facet
        CustomerIdentification customerId = new CustomerIdentification.Builder()
                .id(customerUUID.toString())
                .build();

        // add facet to request
        request.addFacet(customerId);

        Response response = server.callServer(request);

        Assert.assertEquals(response.getHeader().getExtendedResponseCode(), ExtendedResponseCode.OK, "ExtendedResponseCode is not OK");
        Assert.assertEquals(response.getHeader().getResponseCode(), ResponseCode.OK, "ResponseCode is not OK");
    }

    @Test(priority=1)
    public void testPurchaseOrderProcess_Fail() throws Exception {

        Request request = createRequest();

        // no facets in request

        Response response = server.callServer(request);

        Assert.assertEquals(response.getHeader().getExtendedResponseCode(), ExtendedResponseCode.SERVER_FAILURE, "ExtendedResponseCode is not SERVER_FAILURE");
        Assert.assertEquals(response.getHeader().getResponseCode(), ResponseCode.FAIL, "ResponseCode is not FAIL");
    }

    private Request createRequest() {

        Request request = new Request.Builder()
                        .businessCaseStep(SBTTrmBusinessCases.BCS_PURCHASE_ORDER_PROCESS)
                        .clientId("TEST_CLIENT")
                        .correlationId(UUID.randomUUID().toString())
                        .sessionHandlingInstruction(SessionHandlingInstruction.INTERMEDIATE_REQUEST)
                        .transactionId(0)
                        .transactionStepId(0)
                        .time(Instant.now())
                        .businessCorrelationId(UUID.randomUUID().toString())
                        .boundedContext("trm")
                        .build();

        return request;
    }

}