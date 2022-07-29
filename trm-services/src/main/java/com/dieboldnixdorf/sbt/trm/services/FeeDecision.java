package com.dieboldnixdorf.sbt.trm.services;

import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.dieboldnixdorf.sbt.trm.SbtExtendedResponseCodes;
import com.dieboldnixdorf.sbt.trm.businessobjects.CustomerIdentification;
import com.dieboldnixdorf.sbt.trm.businessobjects.TimeSinceLastTransaction;
import com.dieboldnixdorf.sbt.trm.config.TRConfig;
import com.dieboldnixdorf.txm.core.businessmodel.businessobjects.ExtendedResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.container.TransactionStep;
import com.dieboldnixdorf.txm.core.businessmodel.facets.FacetConsumer;
import com.dieboldnixdorf.txm.core.businessmodel.facets.FacetProducer;
import com.dieboldnixdorf.txm.core.businessmodel.services.ResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.services.Service;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceImplementation;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceResponse;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.ExtRspCode;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.RspCode;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.TransactionService;
import com.myproclassic.common.tservices.tracelog.aspects.PCEAopTrace;



@ApplicationScoped
@PCEAopTrace
@TransactionService(
        id = FeeDecision.SERVICE_ID,
        usage = "Makes Fee Decision.",
        serviceGroup = "SBT",
        rspCodes = {
                @RspCode(value = ResponseCode.OK, extRspCodes = {
                        @ExtRspCode(
                                value = ExtendedResponseCode.OK_ID,
                                name = "Fee Decided")
                        }),
                @RspCode(value = ResponseCode.FAIL, extRspCodes = {
                        @ExtRspCode(
                                value = ExtendedResponseCode.SERVER_FAILURE_ID,
                                name = "Server Response Failed")
                        })
                }
        )
public class FeeDecision implements Service {

	public static final String SERVICE_ID = "FeeDecision";
	
	@Inject
	private FacetConsumer<TimeSinceLastTransaction> fcTimeSinceLastTransaction;
	
	@Inject
	private TRConfig configuration;
	
	public static final int MS_TO_MIN=60000;
	

	@Override
	public ServiceResponse apply(TransactionStep step, Map<String, String> parameter) {
	    return ServiceImplementation.create().apply(this::implementation, step, parameter);
	}
	
	private ServiceResponse implementation(TransactionStep step, Map<String, String> parameter) throws Exception {
	    Optional<TimeSinceLastTransaction> opt = fcTimeSinceLastTransaction.getOptional();

	    if (configuration != null && opt.isPresent()) {

	        TimeSinceLastTransaction elapsedTime = fcTimeSinceLastTransaction.get();

	        // transactions called BEFORE timeout should include a fee
	        int feeCalculationTimeout = Integer.valueOf(configuration.getFeeCalculationTimeout());

	        if (elapsedTime.getElapsedTime() == 0
	                || elapsedTime.getElapsedTime() / MS_TO_MIN > feeCalculationTimeout) {
	            // after timeout - return ok (no fee)
	            return ServiceResponse.success(ExtendedResponseCode.OK);
	        } else {
	            // before timeout - return fee required
	            return ServiceResponse.success(SbtExtendedResponseCodes.FEE_REQUIRED);
	        }
	    } else {
	        return ServiceResponse.failure();
	    }
	}
}
