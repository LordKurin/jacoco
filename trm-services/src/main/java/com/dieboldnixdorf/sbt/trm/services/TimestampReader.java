package com.dieboldnixdorf.sbt.trm.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.dieboldnixdorf.sbt.trm.businessobjects.CustomerIdentification;
import com.dieboldnixdorf.sbt.trm.businessobjects.PurchaseAmount;
import com.dieboldnixdorf.sbt.trm.businessobjects.TimeSinceLastTransaction;
import com.dieboldnixdorf.sbt.trm.db.SBTPurchaseDAO;
import com.dieboldnixdorf.sbt.trm.db.SBTPurchaseDTO;
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
import com.myproclassic.server.db.sql.PCEDBException;




@ApplicationScoped
@PCEAopTrace
@TransactionService(
        id = TimestampReader.SERVICE_ID,
        usage = "Reads the last Timestamp.",
        serviceGroup = "SBT",
        rspCodes = {
                @RspCode(value = ResponseCode.OK, extRspCodes = {
                        @ExtRspCode(
                                value = ExtendedResponseCode.OK_ID,
                                name = "Timestamp Returned")
                        }),
                @RspCode(value = ResponseCode.FAIL, extRspCodes = {
                        @ExtRspCode(
                                value = ExtendedResponseCode.SERVER_FAILURE_ID,
                                name = "Server Response Failed")
                        })
                }
        )
public class TimestampReader implements Service {

	public static final String SERVICE_ID = "TimeStampReader";
	
	@Inject
	private FacetConsumer<CustomerIdentification> fcCustomerId;
	
	@Inject
	private FacetProducer<TimeSinceLastTransaction> fpTimeSinceLastTransaction;
	
	@Override
	public ServiceResponse apply(TransactionStep step, Map<String, String> parameter) {
	    return ServiceImplementation.create().apply(this::implementation, step, parameter);
	}
	
	   private ServiceResponse implementation(TransactionStep step, Map<String, String> parameter) throws Exception {
	        Optional<CustomerIdentification> opt = fcCustomerId.getOptional();

	        if (opt.isPresent()) {

	            CustomerIdentification customerId = opt.get();
	            try {
	                SBTPurchaseDTO dto = SBTPurchaseDAO.findByPrimaryKey(customerId.getId());
	                Timestamp now = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));
	                Timestamp lastTransactionTime;
	                if (dto != null) {
	                    lastTransactionTime = dto.getLastTransactionTime();
	                } else {
	                    lastTransactionTime = now;
	                }

	                long timeDiff = now.getTime() - lastTransactionTime.getTime();

	                TimeSinceLastTransaction timeSinceLastTransaction = new TimeSinceLastTransaction.Builder()
	                        .elapsedTime(timeDiff)
	                        .build();

	                fpTimeSinceLastTransaction.set(timeSinceLastTransaction);

	                return ServiceResponse.success();

	            } catch (PCEDBException ex) {
	                TrmServiceLogMessages.SERVICE_FAILED.log(ex.getMessage());
	                return ServiceResponse.failure();
	            }
	        } else {
	            return ServiceResponse.failure();
	        }
	    }
}
