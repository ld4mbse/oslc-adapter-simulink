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
package edu.gatech.mbsec.adapter.simulink.clients;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.apache.wink.client.handlers.BasicAuthSecurityHandler;
import org.apache.wink.client.handlers.ClientHandler;
import org.eclipse.lyo.adapter.simulink.resources.SimulinkBlock;
import org.eclipse.lyo.adapter.simulink.resources.SimulinkInputPort;
import org.eclipse.lyo.adapter.simulink.resources.SimulinkLine;
import org.eclipse.lyo.adapter.simulink.resources.SimulinkOutputPort;
import org.eclipse.lyo.adapter.simulink.resources.SimulinkParameter;
import org.eclipse.lyo.oslc4j.client.OslcRestClient;
import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
//import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;

import edu.gatech.mbsec.adapter.simulink.services.OSLC4JSimulinkApplication;







/**
 * The main() method of OSLCWebClient4CreatingSimulinkModel creates several new Simulink elements 
 * in a Simulink model using the OSLC Simulink adapter. First, the Simulink elements 
 * to be added to the model are created as POJOs. Second, the POJOs are transformed 
 * into RDF/XML by OSLC4J, and then sent as the body of HTTP requests to the creationfactory services
 * of the OSLC Simulink adapter. OSLCWebClient4CreatingSimulinkModel 
 * adds Simulink elements to a Simulink model through individual HTTP requests.
 *  
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
public class OSLCWebClient4CreatingSimulinkModel {

	private static final Set<Class<?>> PROVIDERS = new HashSet<Class<?>>();

	static {
		PROVIDERS.addAll(JenaProvidersRegistry.getProviders());
		// PROVIDERS.addAll(Json4JProvidersRegistry.getProviders());
	}

	public static void main(String[] args) {

		String baseHTTPURI = "http://localhost:" + OSLC4JSimulinkApplication.portNumber + "/oslc4jsimulink";
		String projectId = "model11";

		// URI of the HTTP request
		String simulinkBlockCreationFactoryURI = baseHTTPURI + "/services/"
				+ projectId + "/blocks";
		String simulinkParametersURI = baseHTTPURI + "/services/"
				+ projectId + "/parameters";		
		String simulinkLineCreationFactoryURI = baseHTTPURI + "/services/"
				+ projectId + "/lines";

		// expected mediatype
		String mediaType = "application/rdf+xml";

		// readTimeout specifies how long the RestClient object waits (in
		// milliseconds) for a response before timing out
		int readTimeout = 2400000;

		// set up the credentials for the basic authentication
		BasicAuthSecurityHandler basicAuthHandler = new BasicAuthSecurityHandler();

		// creating the OSLC REST clients
		final OslcRestClient oslcSimulinkBlockCreationRestClient = new OslcRestClient(
				PROVIDERS, simulinkBlockCreationFactoryURI, mediaType,
				readTimeout, basicAuthHandler);

		final OslcRestClient oslcSimulinkParametersRestClient = new OslcRestClient(
				PROVIDERS, simulinkParametersURI, mediaType, readTimeout,
				basicAuthHandler);

		// final OslcRestClient oslcSimulinkParameterModel_ModelNameRestClient =
		// new OslcRestClient(
		// PROVIDERS, simulinkParameterModel_ModelNameURI, mediaType,
		// readTimeout, basicAuthHandler);

		final OslcRestClient oslcSimulinkLineCreationRestClient = new OslcRestClient(
				PROVIDERS, simulinkLineCreationFactoryURI, mediaType,
				readTimeout, basicAuthHandler);

		try {

			// ********************************
			// *** Creating Simulink Blocks ***

			// create the Constant Simulink block
			SimulinkBlock ConstantSimulinkBlock = new SimulinkBlock();
			ConstantSimulinkBlock.setName("Constant");
			ConstantSimulinkBlock.setType("Constant");
			URI ConstantSimulinkBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/" + "Constant");
			ConstantSimulinkBlock.setAbout(ConstantSimulinkBlockURI);

			// create the Step Simulink block
			SimulinkBlock StepSimulinkBlock = new SimulinkBlock();
			StepSimulinkBlock.setName("Step");
			StepSimulinkBlock.setType("Step");
			URI StepSimulinkBlockURI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/blocks/" + "Step");
			StepSimulinkBlock.setAbout(StepSimulinkBlockURI);

			// create the Subsystem1 Simulink block
			SimulinkBlock Subsystem1SimulinkBlock = new SimulinkBlock();
			Subsystem1SimulinkBlock.setName("Subsystem1");
			Subsystem1SimulinkBlock.setType("Subsystem");
			URI Subsystem1SimulinkBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/" + "Subsystem1");
			Subsystem1SimulinkBlock.setAbout(Subsystem1SimulinkBlockURI);

			// create the Subsystem2 Simulink block
			SimulinkBlock Subsystem2SimulinkBlock = new SimulinkBlock();
			Subsystem2SimulinkBlock.setName("Subsystem2");
			Subsystem2SimulinkBlock.setType("Subsystem");
			URI Subsystem2SimulinkBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/" + "Subsystem2");
			Subsystem2SimulinkBlock.setAbout(Subsystem2SimulinkBlockURI);

			// create the Model Simulink block
			SimulinkBlock ModelSimulinkBlock = new SimulinkBlock();
			ModelSimulinkBlock.setName("Model");
			ModelSimulinkBlock.setType("ModelReference");
			URI ModelSimulinkBlockURI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/blocks/" + "Model");
			ModelSimulinkBlock.setAbout(ModelSimulinkBlockURI);

			// create the Out1 Simulink block
			SimulinkBlock Out1SimulinkBlock = new SimulinkBlock();
			Out1SimulinkBlock.setName("Out1");
			Out1SimulinkBlock.setType("Outport");
			URI Out1SimulinkBlockURI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/blocks/" + "Out1");
			Out1SimulinkBlock.setAbout(Out1SimulinkBlockURI);

			// create the Subsystem1::In1 Simulink block
			SimulinkBlock Subsystem1In1SimulinkBlock = new SimulinkBlock();
			Subsystem1In1SimulinkBlock.setName("In1");
			Subsystem1In1SimulinkBlock.setType("Inport");
			URI Subsystem1In1SimulinkBlockURI = URI
					.create(baseHTTPURI + "/services/" + projectId + "/blocks/"
							+ "Subsystem1::In1");
			Subsystem1In1SimulinkBlock.setAbout(Subsystem1In1SimulinkBlockURI);

			// create the Subsystem1::Out1 Simulink block
			SimulinkBlock Subsystem1Out1SimulinkBlock = new SimulinkBlock();
			Subsystem1Out1SimulinkBlock.setName("Out1");
			Subsystem1Out1SimulinkBlock.setType("Outport");
			URI Subsystem1Out1SimulinkBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/"
					+ "Subsystem1::Out1");
			Subsystem1Out1SimulinkBlock
					.setAbout(Subsystem1Out1SimulinkBlockURI);

			// create the Subsystem2::In1 Simulink block
			SimulinkBlock Subsystem2In1SimulinkBlock = new SimulinkBlock();
			Subsystem2In1SimulinkBlock.setName("In1");
			Subsystem2In1SimulinkBlock.setType("Inport");
			URI Subsystem2In1SimulinkBlockURI = URI
					.create(baseHTTPURI + "/services/" + projectId + "/blocks/"
							+ "Subsystem2::In1");
			Subsystem2In1SimulinkBlock.setAbout(Subsystem2In1SimulinkBlockURI);

			// create the Subsystem2::xyz Simulink block
			SimulinkBlock Subsystem2xyzSimulinkBlock = new SimulinkBlock();
			Subsystem2xyzSimulinkBlock.setName("xyz");
			Subsystem2xyzSimulinkBlock.setType("Inport");
			URI Subsystem2xyzSimulinkBlockURI = URI
					.create(baseHTTPURI + "/services/" + projectId + "/blocks/"
							+ "Subsystem2::xyz");
			Subsystem2xyzSimulinkBlock.setAbout(Subsystem2xyzSimulinkBlockURI);

			// create the Subsystem2::In3 Simulink block
			SimulinkBlock Subsystem2In3SimulinkBlock = new SimulinkBlock();
			Subsystem2In3SimulinkBlock.setName("In3");
			Subsystem2In3SimulinkBlock.setType("Inport");
			URI Subsystem2In3SimulinkBlockURI = URI
					.create(baseHTTPURI + "/services/" + projectId + "/blocks/"
							+ "Subsystem2::In3");
			Subsystem2In3SimulinkBlock.setAbout(Subsystem2In3SimulinkBlockURI);

			// create the Subsystem2::Out1 Simulink block
			SimulinkBlock Subsystem2Out1SimulinkBlock = new SimulinkBlock();
			Subsystem2Out1SimulinkBlock.setName("Out1");
			Subsystem2Out1SimulinkBlock.setType("Outport");
			URI Subsystem2Out1SimulinkBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/"
					+ "Subsystem2::Out1");
			Subsystem2Out1SimulinkBlock
					.setAbout(Subsystem2Out1SimulinkBlockURI);

			// create the Subsystem1::Gain Simulink block
			SimulinkBlock Subsystem1GainSimulinkBlock = new SimulinkBlock();
			Subsystem1GainSimulinkBlock.setName("Gain");
			Subsystem1GainSimulinkBlock.setType("Gain");
			URI Subsystem1GainSimulinkBlockURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/blocks/"
					+ "Subsystem1::Gain");
			Subsystem1GainSimulinkBlock
					.setAbout(Subsystem1GainSimulinkBlockURI);

			// create the Subsystem2::Sum Simulink block
			SimulinkBlock Subsystem2SumSimulinkBlock = new SimulinkBlock();
			Subsystem2SumSimulinkBlock.setName("Sum");
			Subsystem2SumSimulinkBlock.setType("Sum");
			URI Subsystem2SumSimulinkBlockURI = URI
					.create(baseHTTPURI + "/services/" + projectId + "/blocks/"
							+ "Subsystem2::Sum");
			Subsystem2SumSimulinkBlock.setAbout(Subsystem2SumSimulinkBlockURI);

			// ***********************************
			// *** Setting Simulink Parameters ***

			// set the modelBlock ModelName Simulink parameter
			SimulinkParameter modelBlockModelNameParameter = new SimulinkParameter();
			modelBlockModelNameParameter.setName("ModelName");
			modelBlockModelNameParameter.setValue("model3");
			URI modelBlockModelNameParameterURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/parameters/"
					+ "Model::ModelName");
			modelBlockModelNameParameter
					.setAbout(modelBlockModelNameParameterURI);

			// set the Sum inputs Simulink parameter
			SimulinkParameter sumBlockInputsParameter = new SimulinkParameter();
			sumBlockInputsParameter.setName("Inputs");
			sumBlockInputsParameter.setValue("|+++");
			URI sumBlockInputsParameterURI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/parameters/"
					+ "Subsystem2::Sum::Inputs");
			sumBlockInputsParameter.setAbout(sumBlockInputsParameterURI);

			// ****************************************************
			// *** Creating Simulink Input and Output Ports ***

			// create the Constant Output Port 1
			SimulinkOutputPort constantOutputPort1 = new SimulinkOutputPort();
			constantOutputPort1.setId("out1");
			URI constantOutputPort1URI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/outputports/" + "Constant::outport::1");
			constantOutputPort1.setAbout(constantOutputPort1URI);

			// create the Step Output Port 1
			SimulinkOutputPort stepOutputPort1 = new SimulinkOutputPort();
			stepOutputPort1.setId("out1");
			URI stepOutputPort1URI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/outputports/" + "Step::outport::1");
			stepOutputPort1.setAbout(stepOutputPort1URI);

			// create the Subsystem1 Output Port 1
			SimulinkOutputPort subsystem1OutputPort1 = new SimulinkOutputPort();
			subsystem1OutputPort1.setId("out1");
			URI subsystem1OutputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/outputports/"
					+ "Subsystem1::outport::1");
			subsystem1OutputPort1.setAbout(subsystem1OutputPort1URI);

			// create the Subsystem2 Output Port 1
			SimulinkOutputPort subsystem2OutputPort1 = new SimulinkOutputPort();
			subsystem2OutputPort1.setId("out1");
			URI subsystem2OutputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/outputports/"
					+ "Subsystem2::outport::1");
			subsystem2OutputPort1.setAbout(subsystem2OutputPort1URI);

			// create the Model Output Port 1
			SimulinkOutputPort modelOutputPort1 = new SimulinkOutputPort();
			modelOutputPort1.setId("out1");
			URI modelOutputPort1URI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/outputports/" + "Model::outport::1");
			modelOutputPort1.setAbout(modelOutputPort1URI);

			// create the Model Input Port 1
			SimulinkInputPort modelInputPort1 = new SimulinkInputPort();
			modelInputPort1.setId("in1");
			URI modelInputPort1URI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/inputports/" + "Model::inport::1");
			modelInputPort1.setAbout(modelInputPort1URI);

			// create the Subsystem1 Input Port 1
			SimulinkInputPort subsystem1InputPort1 = new SimulinkInputPort();
			subsystem1InputPort1.setId("in1");
			URI subsystem1InputPort1URI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/inputports/" + "Subsystem1::inport::1");
			subsystem1InputPort1.setAbout(subsystem1InputPort1URI);

			// create the Subsystem2 Input Port 1
			SimulinkInputPort subsystem2InputPort1 = new SimulinkInputPort();
			subsystem2InputPort1.setId("in1");
			URI subsystem2InputPort1URI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/inputports/" + "Subsystem2::inport::1");
			subsystem2InputPort1.setAbout(subsystem2InputPort1URI);

			// create the Subsystem2 Input Port 2
			SimulinkInputPort subsystem2InputPort2 = new SimulinkInputPort();
			subsystem2InputPort2.setId("in2");
			URI subsystem2InputPort2URI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/inputports/" + "Subsystem2::inport::2");
			subsystem2InputPort2.setAbout(subsystem2InputPort2URI);

			// create the Subsystem2 Input Port 3
			SimulinkInputPort subsystem2InputPort3 = new SimulinkInputPort();
			subsystem2InputPort3.setId("in3");
			URI subsystem2InputPort3URI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/inputports/" + "Subsystem2::inport::3");
			subsystem2InputPort3.setAbout(subsystem2InputPort3URI);

			// create the Out1 Input Port 3
			SimulinkInputPort out1InputPort1 = new SimulinkInputPort();
			out1InputPort1.setId("in1");
			URI out1InputPort1URI = URI.create(baseHTTPURI + "/services/"
					+ projectId + "/inputports/" + "Out1::inport::1");
			out1InputPort1.setAbout(out1InputPort1URI);

			// create the Subsystem1::In1 Output Port 1
			SimulinkOutputPort subsystem1In1OutputPort1 = new SimulinkOutputPort();
			subsystem1In1OutputPort1.setId("out1");
			URI subsystem1In1OutputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/outputports/"
					+ "Subsystem1::In1::outport::1");
			subsystem1In1OutputPort1.setAbout(subsystem1In1OutputPort1URI);

			// create the Subsystem2::In1 Output Port 1
			SimulinkOutputPort subsystem2In1OutputPort1 = new SimulinkOutputPort();
			subsystem2In1OutputPort1.setId("out1");
			URI subsystem2In1OutputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/outputports/"
					+ "Subsystem2::In1::outport::1");
			subsystem2In1OutputPort1.setAbout(subsystem2In1OutputPort1URI);

			// create the Subsystem2::xxy Output Port 1
			SimulinkOutputPort subsystem2xyzOutputPort1 = new SimulinkOutputPort();
			subsystem2xyzOutputPort1.setId("out1");
			URI subsystem2xyzOutputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/outputports/"
					+ "Subsystem2::xyz::outport::1");
			subsystem2xyzOutputPort1.setAbout(subsystem2xyzOutputPort1URI);

			// create the Subsystem2::In3 Output Port 1
			SimulinkOutputPort subsystem2In3OutputPort1 = new SimulinkOutputPort();
			subsystem2In3OutputPort1.setId("out1");
			URI subsystem2In3OutputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/outputports/"
					+ "Subsystem2::In3::outport::1");
			subsystem2In3OutputPort1.setAbout(subsystem2In3OutputPort1URI);

			// create the Subsystem1::Out1 Input Port 1
			SimulinkInputPort subsystem1Out1InputPort1 = new SimulinkInputPort();
			subsystem1Out1InputPort1.setId("in1");
			URI subsystem1Out1InputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/inputports/"
					+ "Subsystem1::Out1::inport::1");
			subsystem1Out1InputPort1.setAbout(subsystem1Out1InputPort1URI);

			// create the Subsystem2::Out1 Input Port 1
			SimulinkInputPort subsystem2Out1InputPort1 = new SimulinkInputPort();
			subsystem2Out1InputPort1.setId("in1");
			URI subsystem2Out1InputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/inputports/"
					+ "Subsystem2::Out1::inport::1");
			subsystem2Out1InputPort1.setAbout(subsystem2Out1InputPort1URI);

			// create the Subsystem1::Gain Output Port 1
			SimulinkOutputPort subsystem1GainOutputPort1 = new SimulinkOutputPort();
			subsystem1GainOutputPort1.setId("out1");
			URI subsystem1GainOutputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/outputports/"
					+ "Subsystem1::Gain::outport::1");
			subsystem1GainOutputPort1.setAbout(subsystem1GainOutputPort1URI);

			// create the Subsystem2::Out1 Input Port 1
			SimulinkInputPort subsystem1GainInputPort1 = new SimulinkInputPort();
			subsystem1GainInputPort1.setId("in1");
			URI subsystem1GainInputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/inputports/"
					+ "Subsystem1::Gain::inport::1");
			subsystem1GainInputPort1.setAbout(subsystem1GainInputPort1URI);

			// create the Subsystem2::Sum Input Port 1
			SimulinkInputPort subsystem2SumInputPort1 = new SimulinkInputPort();
			subsystem2SumInputPort1.setId("in1");
			URI subsystem2SumInputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/inputports/"
					+ "Subsystem2::Sum::inport::1");
			subsystem2SumInputPort1.setAbout(subsystem2SumInputPort1URI);

			// create the Subsystem2::Sum Input Port 2
			SimulinkInputPort subsystem2SumInputPort2 = new SimulinkInputPort();
			subsystem2SumInputPort2.setId("in2");
			URI subsystem2SumInputPort2URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/inputports/"
					+ "Subsystem2::Sum::inport::2");
			subsystem2SumInputPort2.setAbout(subsystem2SumInputPort2URI);

			// create the Subsystem2::Sum Input Port 3
			SimulinkInputPort subsystem2SumInputPort3 = new SimulinkInputPort();
			subsystem2SumInputPort3.setId("in3");
			URI subsystem2SumInputPort3URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/inputports/"
					+ "Subsystem2::Sum::inport::3");
			subsystem2SumInputPort3.setAbout(subsystem2SumInputPort3URI);

			// create the Subsystem2::Sum Output Port 1
			SimulinkOutputPort subsystem2SumOutputPort1 = new SimulinkOutputPort();
			subsystem2SumOutputPort1.setId("out1");
			URI subsystem2SumOutputPort1URI = URI.create(baseHTTPURI
					+ "/services/" + projectId + "/outputports/"
					+ "Subsystem2::Sum::outport::1");
			subsystem2SumOutputPort1.setAbout(subsystem2SumOutputPort1URI);

			// ****************************************************
			// *** Creating Simulink Lines ***

			SimulinkLine constantModelLine = new SimulinkLine();
			constantModelLine.setSourcePort(constantOutputPort1URI);
			Link[] constantModelLineTargetPortsLink = new Link[1];
			constantModelLineTargetPortsLink[0] = new Link(modelInputPort1URI);
			constantModelLine.setTargetPorts(constantModelLineTargetPortsLink);
			constantModelLine.setAbout(URI.create(baseHTTPURI + "/services/"
					+ projectId + "/lines/" + "Constant::outport::1"));

			SimulinkLine modelSubsystem2Line = new SimulinkLine();
			modelSubsystem2Line.setSourcePort(modelOutputPort1URI);
			Link[] modelSubsystem2LineTargetPortsLink = new Link[1];
			modelSubsystem2LineTargetPortsLink[0] = new Link(
					subsystem2InputPort1URI);
			modelSubsystem2Line
					.setTargetPorts(modelSubsystem2LineTargetPortsLink);
			modelSubsystem2Line.setAbout(URI.create(baseHTTPURI + "/services/"
					+ projectId + "/lines/" + "Model::outport::1"));

			SimulinkLine stepSubsystem1And2Line = new SimulinkLine();
			stepSubsystem1And2Line.setSourcePort(stepOutputPort1URI);
			Link[] stepSubsystem1LineTargetPortsLink = new Link[2];
			stepSubsystem1LineTargetPortsLink[0] = new Link(
					subsystem1InputPort1URI);
			stepSubsystem1LineTargetPortsLink[1] = new Link(
					subsystem2InputPort3URI);
			stepSubsystem1And2Line
					.setTargetPorts(stepSubsystem1LineTargetPortsLink);
			stepSubsystem1And2Line.setAbout(URI
					.create(baseHTTPURI + "/services/" + projectId + "/lines/"
							+ "Step::outport::1"));

			SimulinkLine subsystem1subsystem2Line = new SimulinkLine();
			subsystem1subsystem2Line.setSourcePort(subsystem1OutputPort1URI);
			Link[] subsystem1subsystem2LineTargetPortsLink = new Link[1];
			subsystem1subsystem2LineTargetPortsLink[0] = new Link(
					subsystem2InputPort2URI);
			subsystem1subsystem2Line
					.setTargetPorts(subsystem1subsystem2LineTargetPortsLink);
			subsystem1subsystem2Line.setAbout(URI.create(baseHTTPURI
					+ "/services/" + projectId + "/lines/"
					+ "Subsystem1::outport::1"));

			SimulinkLine subsystem2out1Line = new SimulinkLine();
			subsystem2out1Line.setSourcePort(subsystem2OutputPort1URI);
			Link[] subsystem2out1LineTargetPortsLink = new Link[1];
			subsystem2out1LineTargetPortsLink[0] = new Link(out1InputPort1URI);
			subsystem2out1Line
					.setTargetPorts(subsystem2out1LineTargetPortsLink);
			subsystem2out1Line.setAbout(URI.create(baseHTTPURI + "/services/"
					+ projectId + "/lines/" + "Subsystem2::outport::1"));

			SimulinkLine subsystem1In1GainLine = new SimulinkLine();
			subsystem1In1GainLine.setSourcePort(subsystem1In1OutputPort1URI);
			Link[] subsystem1In1Gain1LineTargetPortsLink = new Link[1];
			subsystem1In1Gain1LineTargetPortsLink[0] = new Link(
					subsystem1GainInputPort1URI);
			subsystem1In1GainLine
					.setTargetPorts(subsystem1In1Gain1LineTargetPortsLink);
			subsystem1In1GainLine.setAbout(URI.create(baseHTTPURI
					+ "/services/" + projectId + "/lines/"
					+ "Subsystem1::In1::outport::1"));

			SimulinkLine subsystem1GainOut1Line = new SimulinkLine();
			subsystem1GainOut1Line.setSourcePort(subsystem1GainOutputPort1URI);
			Link[] subsystem1GainOut1LineTargetPortsLink = new Link[1];
			subsystem1GainOut1LineTargetPortsLink[0] = new Link(
					subsystem1Out1InputPort1URI);
			subsystem1GainOut1Line
					.setTargetPorts(subsystem1GainOut1LineTargetPortsLink);
			subsystem1GainOut1Line.setAbout(URI.create(baseHTTPURI
					+ "/services/" + projectId + "/lines/"
					+ "Subsystem1::Gain::outport::1"));

			SimulinkLine subsystem2In1SumLine = new SimulinkLine();
			subsystem2In1SumLine.setSourcePort(subsystem2In1OutputPort1URI);
			Link[] subsystem2In1SumLineTargetPortsLink = new Link[1];
			subsystem2In1SumLineTargetPortsLink[0] = new Link(
					subsystem2SumInputPort1URI);
			subsystem2In1SumLine
					.setTargetPorts(subsystem2In1SumLineTargetPortsLink);
			subsystem2In1SumLine.setAbout(URI.create(baseHTTPURI + "/services/"
					+ projectId + "/lines/" + "Subsystem2::In1::outport::1"));

			SimulinkLine subsystem2xyzSumLine = new SimulinkLine();
			subsystem2xyzSumLine.setSourcePort(subsystem2xyzOutputPort1URI);
			Link[] subsystem2xyzSumLineTargetPortsLink = new Link[1];
			subsystem2xyzSumLineTargetPortsLink[0] = new Link(
					subsystem2SumInputPort2URI);
			subsystem2xyzSumLine
					.setTargetPorts(subsystem2xyzSumLineTargetPortsLink);
			subsystem2xyzSumLine.setAbout(URI.create(baseHTTPURI + "/services/"
					+ projectId + "/lines/" + "Subsystem2::xyz::outport::1"));

			SimulinkLine subsystem2In3SumLine = new SimulinkLine();
			subsystem2In3SumLine.setSourcePort(subsystem2In3OutputPort1URI);
			Link[] subsystem2In3SumLineTargetPortsLink = new Link[1];
			subsystem2In3SumLineTargetPortsLink[0] = new Link(
					subsystem2SumInputPort3URI);
			subsystem2In3SumLine
					.setTargetPorts(subsystem2In3SumLineTargetPortsLink);
			subsystem2In3SumLine.setAbout(URI.create(baseHTTPURI + "/services/"
					+ projectId + "/lines/" + "Subsystem2::In3::outport::1"));

			SimulinkLine subsystem2SumOut1Line = new SimulinkLine();
			subsystem2SumOut1Line.setSourcePort(subsystem2SumOutputPort1URI);
			Link[] subsystem2SumOut1LineTargetPortsLink = new Link[1];
			subsystem2SumOut1LineTargetPortsLink[0] = new Link(
					subsystem2Out1InputPort1URI);
			subsystem2SumOut1Line
					.setTargetPorts(subsystem2SumOut1LineTargetPortsLink);
			subsystem2SumOut1Line.setAbout(URI.create(baseHTTPURI
					+ "/services/" + projectId + "/lines/"
					+ "Subsystem2::Sum::outport::1"));

			// ****************************************************
			// *** Invoking the OSLC adapter creation factories ***

			// creating Simulink Blocks
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(ConstantSimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(StepSimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(ModelSimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Subsystem1SimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Subsystem2SimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Out1SimulinkBlock);

			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Subsystem1In1SimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Subsystem1Out1SimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Subsystem2In1SimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Subsystem2xyzSimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Subsystem2In3SimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Subsystem2Out1SimulinkBlock);

			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Subsystem1GainSimulinkBlock);
			oslcSimulinkBlockCreationRestClient
					.addOslcResource(Subsystem2SumSimulinkBlock);

			// creating Simulink Block Parameters
			oslcSimulinkParametersRestClient
					.addOslcResource(modelBlockModelNameParameter);
			oslcSimulinkParametersRestClient
					.addOslcResource(sumBlockInputsParameter);

			// // get the parameter
			// // retrieving and converting Simulink block parameters
			// final SimulinkParameter model_ModelName_SimulinkParameter =
			// oslcSimulinkParameterModel_ModelNameRestClient
			// .getOslcResource(SimulinkParameter.class);
			// // update it
			// model_ModelName_SimulinkParameter.setValue("model3");
			// // set the new parameter value
			// oslcSimulinkParameterModel_ModelNameRestClient
			// .updateOslcResourceReturnClientResponse(model_ModelName_SimulinkParameter);

			// creating Simulink lines
			oslcSimulinkLineCreationRestClient
					.addOslcResource(constantModelLine);
			oslcSimulinkLineCreationRestClient
					.addOslcResource(stepSubsystem1And2Line);
			oslcSimulinkLineCreationRestClient
					.addOslcResource(modelSubsystem2Line);
			oslcSimulinkLineCreationRestClient
					.addOslcResource(subsystem1subsystem2Line);
			oslcSimulinkLineCreationRestClient
					.addOslcResource(subsystem2out1Line);

			oslcSimulinkLineCreationRestClient
					.addOslcResource(subsystem1In1GainLine);
			oslcSimulinkLineCreationRestClient
					.addOslcResource(subsystem1GainOut1Line);
			oslcSimulinkLineCreationRestClient
					.addOslcResource(subsystem2In1SumLine);
			oslcSimulinkLineCreationRestClient
					.addOslcResource(subsystem2xyzSumLine);
			oslcSimulinkLineCreationRestClient
					.addOslcResource(subsystem2In3SumLine);
			oslcSimulinkLineCreationRestClient
					.addOslcResource(subsystem2SumOut1Line);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
