/*********************************************************************************************
 * Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.
 *                         http://www.mbse.gatech.edu/
 *                  http://www.mbsec.gatech.edu/research/oslc
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *
 *  Contributors:
 *
 *	   Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)		
 *******************************************************************************************/
package edu.gatech.mbsec.adapter.simulink.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDescription;
import org.eclipse.lyo.oslc4j.core.annotation.OslcName;
import org.eclipse.lyo.oslc4j.core.annotation.OslcOccurs;
import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespace;
import org.eclipse.lyo.oslc4j.core.annotation.OslcReadOnly;
import org.eclipse.lyo.oslc4j.core.annotation.OslcPropertyDefinition;
import org.eclipse.lyo.oslc4j.core.annotation.OslcRange;
import org.eclipse.lyo.oslc4j.core.annotation.OslcRepresentation;
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape;
import org.eclipse.lyo.oslc4j.core.annotation.OslcTitle;
import org.eclipse.lyo.oslc4j.core.annotation.OslcValueType;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.Occurs;
import org.eclipse.lyo.oslc4j.core.model.Representation;
import org.eclipse.lyo.oslc4j.core.model.ValueType;
import org.eclipse.lyo.oslc4j.core.model.Link;

@OslcNamespace(Constants.SIMULINK_NAMESPACE)
@OslcName("Line")
@OslcResourceShape(title = "Line Resource Shape", describes = Constants.TYPE_SIMULINK_LINE)
public class SimulinkLine extends AbstractResource{

	public SimulinkLine() throws URISyntaxException {
		super();
	}
	public SimulinkLine(URI about) throws URISyntaxException {
		super(about);
	}

	// ********* sourcePort *********
	private URI sourcePort;

	public void setSourcePort(final URI sourcePort) {
		this.sourcePort = sourcePort;
	}

	@OslcDescription("Description of Line::sourcePort TBD")
	@OslcName("sourcePort")
	@OslcPropertyDefinition("http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#Line/sourcePort")
	@OslcTitle("sourcePort")
	@OslcRange("http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#Port")
	public URI  getSourcePort() {
		 return sourcePort;
	}

	// ********* targetPort *********
	private final Set<Link> targetPorts = new HashSet<Link>();

	public void setTargetPorts(final Link[] targetPorts) {
		this.targetPorts.clear();
		if (targetPorts != null)
		{
			this.targetPorts.addAll(Arrays.asList(targetPorts));
		}
	}

	@OslcDescription("Description of Line::targetPort TBD")
	@OslcName("targetPort")
	@OslcOccurs(Occurs.OneOrMany)
	@OslcPropertyDefinition("http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#Line_targetPort")
	@OslcTitle("targetPort")
	@OslcReadOnly(false)
	public Link[]  getTargetPorts() {
		 return targetPorts.toArray(new Link[targetPorts.size()]);
	}

	private URI      serviceProvider;

	public void setServiceProvider(final URI serviceProvider)
	{		this.serviceProvider = serviceProvider;
	}

	@OslcDescription("The scope of a resource is a URI for the resource's OSLC Service Provider.")
	@OslcPropertyDefinition(OslcConstants.OSLC_CORE_NAMESPACE + "serviceProvider")
	@OslcRange(OslcConstants.TYPE_SERVICE_PROVIDER)
	@OslcTitle("Service Provider")	
	public URI getServiceProvider()
	{
		return serviceProvider;
	}

	// ********* rdfType *********
	private URI[] rdfTypes = new URI[2];

	public void setRdfTypes(final URI[] rdfTypes) {
		this.rdfTypes = rdfTypes;
	}

	@OslcDescription("Additional resource type URIs ")
	@OslcName("type")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition(OslcConstants.RDF_NAMESPACE + "type")
	public URI[]  getRdfTypes() {
		 URI[] rdfTypes = {URI.create("http://eclipse.org/MBSE/Connection")};
		 return rdfTypes;
	}

}