package com.dieboldnixdorf.sbt.trm.db;

import java.sql.Timestamp;
import java.sql.Types;

import com.myproclassic.server.db.sql.PCEColumnMap;
import com.myproclassic.server.db.sql.PCETableDescriptor;
import com.myproclassic.server.dblayer.dto.PCEDataTransferObject;

public class SBTPurchaseDTO extends PCEDataTransferObject {

    private static final long serialVersionUID = -5265884890293763417L;

    /** The Constant DESC. */
    public static final PCETableDescriptor DESC;
    static {
        DESC = new PCETableDescriptor("SBTPURCHASE");

        DESC.addColumn("CUSTOMERID", Types.VARCHAR, true);
        DESC.addColumn("LASTTXNTIMESTAMP", Types.TIMESTAMP);

        DESC.setModelClass(SBTPurchaseDTO.class.getName());
        DESC.computeStatements();
        DESC.isCached(false);
    }

    public SBTPurchaseDTO() {
        super(DESC);
    }

    public SBTPurchaseDTO(String customerId) {
        super(DESC);
        setCustomerId(customerId);
    }

    /**
     * Gets the customerId.
     *
     * @return the customerId
     */
    public String getCustomerId() {
        return getColumn("CUSTOMERID").StringValue();
    }

    /**
     * Sets the customerId.
     *
     * @param customerId
     *            the new customerId
     */
    public void setCustomerId(String customerId) {
        getColumn("CUSTOMERID").StringValue(customerId);
    }

    /**
     * Gets the last transaction time.
     *
     * @return the last transaction time
     */
    public Timestamp getLastTransactionTime() {
        return getColumn("LASTTXNTIMESTAMP").TimestampValue();
    }

    /**
     * Sets the last transaction time.
     *
     * @param lastTransactionTime
     *            the new last transaction time
     */
    public void setLastTransactionTime(Timestamp lastTransactionTime) {
        getColumn("LASTTXNTIMESTAMP").TimestampValue(lastTransactionTime);
    }
}