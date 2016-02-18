/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *  
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *
 *     Russell Boykin       - initial API and implementation
 *     Alberto Giammaria    - initial API and implementation
 *     Chris Peters         - initial API and implementation
 *     Gianluca Bernardini  - initial API and implementation
 *     
 * Modifications performed by:    
 *     Axel Reichwein		- implementation for Simulink adapter
 *     (axel.reichwein@koneksys.com)
 *******************************************************************************/
@OslcSchema ({
    @OslcNamespaceDefinition(prefix = OslcConstants.DCTERMS_NAMESPACE_PREFIX,             namespaceURI = OslcConstants.DCTERMS_NAMESPACE),
    @OslcNamespaceDefinition(prefix = OslcConstants.OSLC_CORE_NAMESPACE_PREFIX,           namespaceURI = OslcConstants.OSLC_CORE_NAMESPACE),
    @OslcNamespaceDefinition(prefix = OslcConstants.OSLC_DATA_NAMESPACE_PREFIX,           namespaceURI = OslcConstants.OSLC_DATA_NAMESPACE),
    @OslcNamespaceDefinition(prefix = OslcConstants.RDF_NAMESPACE_PREFIX,                 namespaceURI = OslcConstants.RDF_NAMESPACE),
    @OslcNamespaceDefinition(prefix = OslcConstants.RDFS_NAMESPACE_PREFIX,                namespaceURI = OslcConstants.RDFS_NAMESPACE),
//    @OslcNamespaceDefinition(prefix = Constants.CHANGE_MANAGEMENT_NAMESPACE_PREFIX,       namespaceURI = Constants.CHANGE_MANAGEMENT_NAMESPACE),   
//    @OslcNamespaceDefinition(prefix = Constants.FOAF_NAMESPACE_PREFIX,                    namespaceURI = Constants.FOAF_NAMESPACE),
//    @OslcNamespaceDefinition(prefix = Constants.QUALITY_MANAGEMENT_PREFIX,                namespaceURI = Constants.QUALITY_MANAGEMENT_NAMESPACE),
//    @OslcNamespaceDefinition(prefix = Constants.REQUIREMENTS_MANAGEMENT_PREFIX,           namespaceURI = Constants.REQUIREMENTS_MANAGEMENT_NAMESPACE),
//    @OslcNamespaceDefinition(prefix = Constants.SOFTWARE_CONFIGURATION_MANAGEMENT_PREFIX, namespaceURI = Constants.SOFTWARE_CONFIGURATION_MANAGEMENT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.MBSE_PREFIX, namespaceURI = Constants.MBSE_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SIMULINK_MODEL_PREFIX, namespaceURI = Constants.SIMULINK_MODEL_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SIMULINK_BLOCK_PREFIX, namespaceURI = Constants.SIMULINK_BLOCK_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SIMULINK_INPUTPORT_PREFIX, namespaceURI = Constants.SIMULINK_INPUTPORT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SIMULINK_OUTPUTPORT_PREFIX, namespaceURI = Constants.SIMULINK_OUTPUTPORT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SIMULINK_PORT_PREFIX, namespaceURI = Constants.SIMULINK_PORT_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SIMULINK_LINE_PREFIX, namespaceURI = Constants.SIMULINK_LINE_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SIMULINK_PARAMETER_PREFIX, namespaceURI = Constants.SIMULINK_PARAMETER_NAMESPACE),
    @OslcNamespaceDefinition(prefix = Constants.SIMULINK_PREFIX, namespaceURI = Constants.SIMULINK_NAMESPACE)
})
package edu.gatech.mbsec.adapter.simulink.resources;

import org.eclipse.lyo.oslc4j.core.annotation.OslcNamespaceDefinition;
import org.eclipse.lyo.oslc4j.core.annotation.OslcSchema;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;

import edu.gatech.mbsec.adapter.simulink.resources.Constants;

