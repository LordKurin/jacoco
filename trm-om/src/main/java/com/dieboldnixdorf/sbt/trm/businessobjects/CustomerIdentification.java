package com.dieboldnixdorf.sbt.trm.businessobjects;

import com.dieboldnixdorf.txm.core.businessmodel.BusinessObject;
import com.dieboldnixdorf.txm.core.businessmodel.BusinessObjectBuilder;
import com.dieboldnixdorf.txm.core.businessmodel.BusinessObjectMarker;
import com.dieboldnixdorf.txm.core.businessmodel.Factory;
import com.dieboldnixdorf.txm.core.businessmodel.facets.FacetMarker;
import com.fasterxml.jackson.annotation.JsonProperty;

@FacetMarker(CustomerIdentification.FACET_ID)
@BusinessObjectMarker
public class CustomerIdentification extends BusinessObject {
	
	public static final long serialVersionUID = 0;
	
	
	public static final String FACET_ID = "customerIdentification";
	
	@JsonProperty(required = true)
	protected String id;

	public static class Builder extends AbstractBuilder<Builder, CustomerIdentification> {

	       @Override
	       protected CustomerIdentification createInstance() {
	           return Factory.createInstance(CustomerIdentification.class);
	       }
	   }

	   protected static abstract class AbstractBuilder<B extends BusinessObjectBuilder<B, T>, T extends CustomerIdentification> extends BusinessObjectBuilder<B, T> {

	    public B id(String id) {
	           newObject.id = id;
	           return getThis();
	       }
	   }

	public String getId() {
	    return id;
	}

}
