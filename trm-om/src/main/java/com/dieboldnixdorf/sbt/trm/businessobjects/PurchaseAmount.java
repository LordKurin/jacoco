package com.dieboldnixdorf.sbt.trm.businessobjects;

import com.dieboldnixdorf.txm.core.businessmodel.BusinessObject;
import com.dieboldnixdorf.txm.core.businessmodel.BusinessObjectBuilder;
import com.dieboldnixdorf.txm.core.businessmodel.BusinessObjectMarker;
import com.dieboldnixdorf.txm.core.businessmodel.Factory;
import com.dieboldnixdorf.txm.core.businessmodel.facets.FacetMarker;
import com.dieboldnixdorf.txm.core.businessobjects.financial.amount.Amount;
import com.fasterxml.jackson.annotation.JsonProperty;

@FacetMarker(PurchaseAmount.FACET_ID)
@BusinessObjectMarker
public class PurchaseAmount extends BusinessObject {

    public static final String FACET_ID = "purchaseAmount";

    @JsonProperty(required = true)
    protected Amount requestedAmount;

    @JsonProperty
    protected Amount fee;

    public static class Builder extends AbstractBuilder<Builder, PurchaseAmount> {

        @Override
        protected PurchaseAmount createInstance() {
            return Factory.createInstance(PurchaseAmount.class);
        }
    }

    protected abstract static class AbstractBuilder<B extends BusinessObjectBuilder<B, T>, T extends PurchaseAmount> extends BusinessObjectBuilder<B, T> {

        public B requestedAmount(Amount requestedAmount) {
            newObject.requestedAmount = requestedAmount;
            return getThis();
        }

        public B fee(Amount fee) {
            newObject.fee = fee;
            return getThis();
        }
    }

    public Amount getRequestedAmount() {
        return requestedAmount;
    }

    public Amount getFee() {
        return fee;
    }
}