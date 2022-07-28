package com.dieboldnixdorf.sbt.trm;

import com.dieboldnixdorf.txm.core.businessmodel.EnumerationMarker;
import com.dieboldnixdorf.txm.core.businessmodel.businessobjects.BusinessCase;
import com.dieboldnixdorf.txm.core.businessmodel.businessobjects.BusinessCaseStep;

@EnumerationMarker
public class SBTTrmBusinessCases {
    public static final BusinessCase BC_PURCHASE_ORDER  = new BusinessCase("purchaseOrder", SBTTrmDeclarations.COMPONENT_NUMBER_RANGE_START);
    public static final BusinessCaseStep BCS_PURCHASE_ORDER_PROCESS = new BusinessCaseStep(BC_PURCHASE_ORDER, "process");
}