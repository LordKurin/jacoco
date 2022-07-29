package com.dieboldnixdorf.sbt.trm;

import com.dieboldnixdorf.txm.core.businessmodel.EnumerationMarker;
import com.dieboldnixdorf.txm.core.businessmodel.businessobjects.ExtendedResponseCode;

@EnumerationMarker
public class SbtExtendedResponseCodes {
	
	public static final String FEE_REQUIRED_ID = "FEE_REQUIRED";
	public static final ExtendedResponseCode FEE_REQUIRED = new ExtendedResponseCode(FEE_REQUIRED_ID);

	public SbtExtendedResponseCodes() {
		// TODO Auto-generated constructor stub
	}

}
