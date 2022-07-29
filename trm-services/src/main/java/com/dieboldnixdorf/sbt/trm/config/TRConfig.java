package com.dieboldnixdorf.sbt.trm.config;

import com.myproclassic.config.util.annotations.PCEConfig;
import com.myproclassic.config.util.annotations.PCEConfigDefault;
import com.myproclassic.config.util.annotations.PCEConfigParam;

@PCEConfig("SBT/FeeCalculationTimeout")
public interface TRConfig {

    @PCEConfigDefault("10")
    @PCEConfigParam("feeCalculationTimeout")
    String getFeeCalculationTimeout();
}