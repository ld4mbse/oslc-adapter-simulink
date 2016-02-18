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
 *     Michael Fiedler      - Bugzilla adpater implementations
 *     
 * Modifications performed by:    
 *     Axel Reichwein		- implementation for Simulink adapter
 *     (axel.reichwein@koneksys.com) 
 *******************************************************************************/
package edu.gatech.mbsec.adapter.simulink.resources;


import org.eclipse.lyo.oslc4j.core.model.OslcConstants;

public interface Constants
{
		
//	public static String CHANGE_MANAGEMENT_DOMAIN                    = "http://open-services.net/ns/cm#";
//    public static String CHANGE_MANAGEMENT_NAMESPACE                 = "http://open-services.net/ns/cm#";
//    public static String CHANGE_MANAGEMENT_NAMESPACE_PREFIX          = "oslc_cm";
//    public static String FOAF_NAMESPACE                              = "http://xmlns.com/foaf/0.1/";
//    public static String FOAF_NAMESPACE_PREFIX                       = "foaf";
//    public static String QUALITY_MANAGEMENT_NAMESPACE                = "http://open-services.net/ns/qm#";
//    public static String QUALITY_MANAGEMENT_PREFIX                   = "oslc_qm";
//    public static String REQUIREMENTS_MANAGEMENT_NAMESPACE           = "http://open-services.net/ns/rm#";
//    public static String REQUIREMENTS_MANAGEMENT_PREFIX              = "oslc_rm";
//    public static String SOFTWARE_CONFIGURATION_MANAGEMENT_NAMESPACE = "http://open-services.net/ns/scm#";
//    public static String SOFTWARE_CONFIGURATION_MANAGEMENT_PREFIX    = "oslc_scm";   

	public static String MBSE_PREFIX							 = "mbse";
    public static String MBSE_NAMESPACE							 = "http://eclipse.org/MBSE/";
	
    public static String SIMULINK_PREFIX							 = "simulink";
    public static String SIMULINK_NAMESPACE							 = "http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#";	
    
    public static String SIMULINK_MODEL_PREFIX                    	 = "simulink_model";
    public static String SIMULINK_BLOCK_PREFIX                     	 = "simulink_block";
    public static String SIMULINK_INPUTPORT_PREFIX                 	 = "simulink_inputport";
    public static String SIMULINK_OUTPUTPORT_PREFIX                     = "simulink_outputport";
    public static String SIMULINK_PORT_PREFIX                     = "simulink_port";
    public static String SIMULINK_LINE_PREFIX               		 = "simulink_line";
    public static String SIMULINK_PARAMETER_PREFIX					 = "simulink_parameter";
    
    public static String SIMULINK_MODEL_NAMESPACE                    = SIMULINK_NAMESPACE + "Model/";
    public static String SIMULINK_BLOCK_NAMESPACE                    = SIMULINK_NAMESPACE + "Block/";
    public static String SIMULINK_INPUTPORT_NAMESPACE                 	 = SIMULINK_NAMESPACE + "Inputport/";
    public static String SIMULINK_OUTPUTPORT_NAMESPACE                  = SIMULINK_NAMESPACE + "Outputport/";
    public static String SIMULINK_PORT_NAMESPACE                  = SIMULINK_NAMESPACE + "Port/";
    public static String SIMULINK_LINE_NAMESPACE            		 = SIMULINK_NAMESPACE + "Line/";
    public static String SIMULINK_PARAMETER_NAMESPACE				 = SIMULINK_NAMESPACE + "Parameter/";
    
    
    public static String TYPE_SIMULINK_MODEL                    	= SIMULINK_NAMESPACE + "Model";
    public static String TYPE_SIMULINK_BLOCK                    	= SIMULINK_NAMESPACE + "Block";
    public static String TYPE_SIMULINK_INPUTPORT                    	= SIMULINK_NAMESPACE + "Inputport";
    public static String TYPE_SIMULINK_OUTPUTPORT                   	= SIMULINK_NAMESPACE + "Outputport";
    public static String TYPE_SIMULINK_LINE               	 		= SIMULINK_NAMESPACE + "Line";
    public static String TYPE_SIMULINK_PARAMETER					= SIMULINK_NAMESPACE + "Parameter";
    public static String TYPE_SIMULINK_ELEMENTSTOCREATE					= SIMULINK_NAMESPACE + "Elementstocreate";
    public static String TYPE_SIMULINK_PORT                    	= SIMULINK_NAMESPACE + "Port";
    public static String TYPE_SIMULINK_WORKINGDIRECTORY                    	= SIMULINK_NAMESPACE + "Workingdirectory";
    
    
    public static String SIMULINK_MODEL_DOMAIN                    	 = "http://mathworks.com/simulink/r2013a/model/rdf#";
    public static String SIMULINK_BLOCK_DOMAIN                    	 = "http://mathworks.com/simulink/r2013a/block/rdf#";
    public static String SIMULINK_INPUTPORT_DOMAIN                     = "http://mathworks.com/simulink/r2013a/inputport/rdf#";
    public static String SIMULINK_OUTPUTPORT_DOMAIN                    = "http://mathworks.com/simulink/r2013a/outputport/rdf#";
    public static String SIMULINK_LINE_DOMAIN               		= "http://mathworks.com/simulink/r2013a/line/rdf#";
    public static String SIMULINK_PARAMETER_DOMAIN					= "http://mathworks.com/simulink/r2013a/parameter/rdf#";
    

    public static String  PATH_SIMULINK_MODEL						= "model";
    public static String  PATH_SIMULINK_BLOCK						= "block";
    public static String  PATH_SIMULINK_INPUTPORT					= "inputport";
    public static String  PATH_SIMULINK_OUTPUTPORT					= "outputport";
    public static String  PATH_SIMULINK_LINE						= "line";
    public static String  PATH_SIMULINK_PARAMETER					= "parameter";

    
//    public static String CHANGE_REQUEST             = "ChangeRequest";
//    public static String TYPE_CHANGE_REQUEST        = CHANGE_MANAGEMENT_NAMESPACE + "ChangeRequest";
//    public static String TYPE_CHANGE_SET            = SOFTWARE_CONFIGURATION_MANAGEMENT_NAMESPACE + "ChangeSet";
//    public static String TYPE_DISCUSSION            = OslcConstants.OSLC_CORE_NAMESPACE + "Discussion";
//    public static String TYPE_PERSON                = FOAF_NAMESPACE + "Person";
//    public static String TYPE_REQUIREMENT           = REQUIREMENTS_MANAGEMENT_NAMESPACE + "Requirement";
//    public static String TYPE_TEST_CASE             = QUALITY_MANAGEMENT_NAMESPACE + "TestCase";
//    public static String TYPE_TEST_EXECUTION_RECORD = QUALITY_MANAGEMENT_NAMESPACE + "TestExecutionRecord";
//    public static String TYPE_TEST_PLAN             = QUALITY_MANAGEMENT_NAMESPACE + "TestPlan";
//    public static String TYPE_TEST_RESULT           = QUALITY_MANAGEMENT_NAMESPACE + "TestResult";
//    public static String TYPE_TEST_SCRIPT           = QUALITY_MANAGEMENT_NAMESPACE + "TestScript";

    

    
    
    

   
    
    public static final String HDR_OSLC_VERSION = "OSLC-Core-Version";
    
    
}
