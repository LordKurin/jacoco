package com.dieboldnixdorf.sbt.report.services;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.dieboldnixdorf.txm.core.businessmodel.BusinessObject;
import com.dieboldnixdorf.txm.core.businessmodel.container.Request;
import com.dieboldnixdorf.txm.core.businessmodel.container.Response;
import com.dieboldnixdorf.txm.core.businessmodel.container.TransactionStep;
import com.dieboldnixdorf.txm.core.businessmodel.services.Service;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceImplementation;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceResponse;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.TransactionService;

@ApplicationScoped
@TransactionService(id=SbtEis.SERVICE_ID,
                    usage="SBT EIS connection",
                    serviceGroup="SBT")
public class SbtEis implements Service {

    public static final String SERVICE_ID = "SbtEis";

    @Inject
    private SbtEisSimplexCommunication commSimplex;

    @Override
    public ServiceResponse apply(TransactionStep step, Map<String, String> parameter) {
        return ServiceImplementation.create().apply(this::implementation, step, parameter);
    }

    private ServiceResponse implementation(TransactionStep step, Map<String, String> parameter) throws Exception {
        Request  txmReq = step.getRequest();

        Response txmRsp = commSimplex.sendAndReceive(txmReq);

        for (BusinessObject facet : txmRsp.getFacets().values()) {
            step.getResponse().addFacet(facet);
        }

        return new ServiceResponse.Builder()
                    .responseCode(txmRsp.getHeader().getResponseCode())
                    .extendedResponseCode(txmRsp.getHeader().getExtendedResponseCode())
                    .build();
    }
}