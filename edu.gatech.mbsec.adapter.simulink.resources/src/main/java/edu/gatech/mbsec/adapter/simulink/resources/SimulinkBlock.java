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
@OslcName("Block")
@OslcResourceShape(title = "Block Resource Shape", describes = Constants.TYPE_SIMULINK_BLOCK)
public class SimulinkBlock extends AbstractResource{

	public SimulinkBlock() throws URISyntaxException {
		super();
	}
	public SimulinkBlock(URI about) throws URISyntaxException {
		super(about);
	}

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	@OslcDescription("Description of Block::name TBD")
	@OslcName("name")
	@OslcOccurs(Occurs.ZeroOrOne)
	@OslcPropertyDefinition("http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#Block_name")
	@OslcTitle("name")
	@OslcValueType(ValueType.XMLLiteral)
	public String getName() {
		 return name;
	}
	private String type;

	public void setType(String type) {
		this.type = type;
	}

	@OslcDescription("Description of Block::type TBD")
	@OslcName("type")
	@OslcOccurs(Occurs.ZeroOrOne)
	@OslcPropertyDefinition("http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#Block_type")
	@OslcTitle("type")
	@OslcValueType(ValueType.XMLLiteral)
	public String getType() {
		 return type;
	}
	// ********* inputPort *********
	private final Set<Link> inputPorts = new HashSet<Link>();

	public void setInputPorts(final Link[] inputPorts) {
		this.inputPorts.clear();
		if (inputPorts != null)
		{
			this.inputPorts.addAll(Arrays.asList(inputPorts));
		}
	}

	@OslcDescription("Description of Block::inputPort TBD")
	@OslcName("inputPort")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#Block_inputPort")
	@OslcTitle("inputPort")
	@OslcReadOnly(false)
	public Link[]  getInputPorts() {
		 return inputPorts.toArray(new Link[inputPorts.size()]);
	}

	// ********* outputPort *********
	private final Set<Link> outputPorts = new HashSet<Link>();

	public void setOutputPorts(final Link[] outputPorts) {
		this.outputPorts.clear();
		if (outputPorts != null)
		{
			this.outputPorts.addAll(Arrays.asList(outputPorts));
		}
	}

	@OslcDescription("Description of Block::outputPort TBD")
	@OslcName("outputPort")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#Block_outputPort")
	@OslcTitle("outputPort")
	@OslcReadOnly(false)
	public Link[]  getOutputPorts() {
		 return outputPorts.toArray(new Link[outputPorts.size()]);
	}

	// ********* parameter *********
	private final Set<Link> parameters = new HashSet<Link>();

	public void setParameters(final Link[] parameters) {
		this.parameters.clear();
		if (parameters != null)
		{
			this.parameters.addAll(Arrays.asList(parameters));
		}
	}

	@OslcDescription("Description of Block::parameter TBD")
	@OslcName("parameter")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#Block_parameter")
	@OslcTitle("parameter")
	@OslcReadOnly(false)
	public Link[]  getParameters() {
		 return parameters.toArray(new Link[parameters.size()]);
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
		 URI[] rdfTypes = {URI.create("http://eclipse.org/MBSE/Block")};
		 return rdfTypes;
	}

}