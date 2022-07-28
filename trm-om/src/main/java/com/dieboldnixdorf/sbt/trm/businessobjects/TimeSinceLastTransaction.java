package com.dieboldnixdorf.sbt.trm.businessobjects;

import com.dieboldnixdorf.txm.core.businessmodel.BusinessObject;
import com.dieboldnixdorf.txm.core.businessmodel.BusinessObjectBuilder;
import com.dieboldnixdorf.txm.core.businessmodel.BusinessObjectMarker;
import com.dieboldnixdorf.txm.core.businessmodel.Factory;
import com.dieboldnixdorf.txm.core.businessmodel.facets.FacetMarker;
import com.fasterxml.jackson.annotation.JsonProperty;

@FacetMarker(TimeSinceLastTransaction.FACET_ID)
@BusinessObjectMarker
public class TimeSinceLastTransaction extends BusinessObject {

    private static final long serialVersionUID = 787545253524416940L;

    public static final String FACET_ID = "timeSinceLastTransaction";

    @JsonProperty
    protected Long elapsedTime;

    public static class Builder extends AbstractBuilder<Builder, TimeSinceLastTransaction> {

        @Override
        protected TimeSinceLastTransaction createInstance() {
            return Factory.createInstance(TimeSinceLastTransaction.class);
        }
    }

    protected abstract static class AbstractBuilder<B extends BusinessObjectBuilder<B, T>, T extends TimeSinceLastTransaction> extends BusinessObjectBuilder<B, T> {

        public B elapsedTime(Long elapsedTime) {
            newObject.elapsedTime = elapsedTime;
            return getThis();
        }
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }
}