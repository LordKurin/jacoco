package com.dieboldnixdorf.sbt.trm.services;


import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.dieboldnixdorf.sbt.trm.businessobjects.PurchaseAmount;
import com.dieboldnixdorf.txm.core.businessmodel.businessobjects.ExtendedResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.container.TransactionStep;
import com.dieboldnixdorf.txm.core.businessmodel.facets.FacetConsumer;
import com.dieboldnixdorf.txm.core.businessmodel.facets.FacetProducer;
import com.dieboldnixdorf.txm.core.businessmodel.services.Parameter;
import com.dieboldnixdorf.txm.core.businessmodel.services.ResponseCode;
import com.dieboldnixdorf.txm.core.businessmodel.services.Service;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceImplementation;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceResponse;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.ExtRspCode;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.RspCode;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.ServiceParameter;
import com.dieboldnixdorf.txm.core.businessmodel.services.annotations.TransactionService;
import com.dieboldnixdorf.txm.core.businessobjects.financial.amount.Amount;
import com.dieboldnixdorf.txm.core.businessobjects.financial.amount.Currency;
import com.myproclassic.common.logging.PCETrace;
import com.myproclassic.common.tservices.tracelog.aspects.PCEAopTrace;


@ApplicationScoped
@PCEAopTrace
@TransactionService(
        id = FeeCalculation.SERVICE_ID,
        usage = "Calculates purchase order fee.",
        serviceGroup = "SBT",
        rspCodes = {
                @RspCode(value = ResponseCode.OK, extRspCodes = {
                        @ExtRspCode(
                                value = ExtendedResponseCode.OK_ID,
                                name = "Fee calculated correctly")
                        }),
                @RspCode(value = ResponseCode.FAIL, extRspCodes = {
                        @ExtRspCode(
                                value = ExtendedResponseCode.SERVER_FAILURE_ID,
                                name = "Fee calculation failed")
                        })
                }
        )
public class FeeCalculation implements Service {

	public static final String SERVICE_ID = "FeeCalculation";
	
	@Inject
	private FacetConsumer<PurchaseAmount> fcPurchaseAmount;

	@Inject
	private FacetProducer<PurchaseAmount> fpPurchaseWithFeeAmount;
	
	@Inject
	@ServiceParameter(defaultValue = "2")
	private Parameter<Double> feePercentValue;
	
	@Override
	public ServiceResponse apply(TransactionStep step, Map<String, String> parameter) {
	    return ServiceImplementation.create().apply(this::implementation, step, parameter);
	}
	
	private ServiceResponse implementation(TransactionStep step, Map<String, String> parameter) throws Exception {

	    Optional<PurchaseAmount> opt = fcPurchaseAmount.getOptional();

	    
	    if (opt.isPresent()) {
	        // get requested amount
	        Amount requestedAmount = opt.get().getRequestedAmount();
	        long value = requestedAmount.getValue();

		     // direct trace
	        PCETrace.trace("Amount value: {}", value);
	        
	        // calculate fee (2% of req. amount))
	        double fee = value * feePercentValue.get().doubleValue() / 100;

	        PurchaseAmount amountWithFee = new PurchaseAmount.Builder()
	                .requestedAmount(requestedAmount)
	                .fee(new Amount.Builder().value(Math.round(fee))
	                        .currency(new Currency("USD", 840, 2))
	                        .build())
	                .build();

	        fpPurchaseWithFeeAmount.set(amountWithFee);

	     // lazy evaluation - for time demanding logging
	        PCETrace.trace(() -> "Fee value: " + amountWithFee.getFee().getValue());

	        return ServiceResponse.success();
	    } else {
	        TrmServiceLogMessages.SERVICE_FAILED.log("Facet PurchaseAmount is missing in the request.");
	        return ServiceResponse.failure();
	    }
	}
}
