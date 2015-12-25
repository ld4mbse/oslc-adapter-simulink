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
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.lyo.oslc4j.core.annotation.OslcDescription;
import org.eclipse.lyo.oslc4j.core.annotation.OslcName;
import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespace;
import org.eclipse.lyo.oslc4j.core.annotation.OslcOccurs;
import org.eclipse.lyo.oslc4j.core.annotation.OslcPropertyDefinition;
import org.eclipse.lyo.oslc4j.core.annotation.OslcRdfCollectionType;
import org.eclipse.lyo.oslc4j.core.annotation.OslcReadOnly;
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape;
import org.eclipse.lyo.oslc4j.core.annotation.OslcTitle;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.Occurs;

@OslcNamespace(Constants.SIMULINK_NAMESPACE)
@OslcName("SimulinkElementsToCreate")
@OslcResourceShape(title = "SimulinkElementsToCreate Resource Shape", describes = Constants.TYPE_SIMULINK_ELEMENTSTOCREATE)
public class SimulinkElementsToCreate extends AbstractResource{
	
	public SimulinkElementsToCreate() throws URISyntaxException {
		super();
	}
	public SimulinkElementsToCreate(URI about) throws URISyntaxException {
		super(about);
	}
	
	List<SimulinkBlock> blocksToCreate = new LinkedList<SimulinkBlock>();
	List<SimulinkLine> linesToCreate = new ArrayList<SimulinkLine>();
	List<SimulinkParameter> parametersToCreate = new ArrayList<SimulinkParameter>();
	
	public SimulinkElementsToCreate(List<SimulinkBlock> blocksToCreate,
			List<SimulinkLine> linesToCreate,
			List<SimulinkParameter> parametersToCreate) {
		super();
		this.blocksToCreate = blocksToCreate;
		this.linesToCreate = linesToCreate;
		this.parametersToCreate = parametersToCreate;
	}

	@OslcDescription("Description of ElementsToCreate::blocksToCreate")
	@OslcName("blocksToCreate")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://mathworks.com/simulink/rdf#Elementstocreate/blocksToCreate")
	@OslcTitle("blocksToCreate")
	@OslcReadOnly(false)
	@OslcRdfCollectionType(collectionType = "List")
	public List<SimulinkBlock> getBlocksToCreate() {
		return blocksToCreate;
	}

	public void setBlocksToCreate(List<SimulinkBlock> blocksToCreate) {
		this.blocksToCreate = blocksToCreate;
	}

	@OslcDescription("Description of ElementsToCreate::linesToCreate")
	@OslcName("linesToCreate")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://mathworks.com/simulink/rdf#Elementstocreate/linesToCreate")
	@OslcTitle("linesToCreate")
	@OslcReadOnly(false)
	@OslcRdfCollectionType(collectionType = "List")
	public List<SimulinkLine> getLinesToCreate() {
		return linesToCreate;
	}

	public void setLinesToCreate(List<SimulinkLine> linesToCreate) {
		this.linesToCreate = linesToCreate;
	}

	@OslcDescription("Description of ElementsToCreate::parametersToCreate")
	@OslcName("parametersToCreate")
	@OslcOccurs(Occurs.ZeroOrMany)
	@OslcPropertyDefinition("http://mathworks.com/simulink/rdf#Elementstocreate/parametersToCreate")
	@OslcTitle("parametersToCreate")
	@OslcReadOnly(false)
	@OslcRdfCollectionType(collectionType = "List")
	public List<SimulinkParameter> getParametersToCreate() {
		return parametersToCreate;
	}

	public void setParametersToCreate(List<SimulinkParameter> parametersToCreate) {
		this.parametersToCreate = parametersToCreate;
	}
	

}
