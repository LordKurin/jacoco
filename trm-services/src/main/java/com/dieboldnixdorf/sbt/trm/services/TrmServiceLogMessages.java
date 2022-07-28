package com.dieboldnixdorf.sbt.trm.services;

import java.util.Map;

import com.dieboldnixdorf.sbt.trm.SBTTrmDeclarations;
import com.dieboldnixdorf.txm.core.businessmodel.container.TransactionStep;
import com.dieboldnixdorf.txm.core.businessmodel.services.ServiceResponse;
import com.myproclassic.common.logging.IPCELogMsg;
import com.myproclassic.common.logging.PCELogLevel;
import com.myproclassic.common.logging.PCELogMsg;
import com.myproclassic.common.logging.PCELogMsg.Target;
import com.myproclassic.common.logging.PCELogMsgBundle;

@PCELogMsgBundle(bundle = "trm-services", numberRange = SBTTrmDeclarations.COMPONENT_NUMBER_RANGE_START)
public enum TrmServiceLogMessages implements IPCELogMsg{

	
    @PCELogMsg(level = PCELogLevel.ERROR, id = 1, msg = "Service failed: {} .", targets = {Target.ErrorLog})
    SERVICE_FAILED;
    
}
