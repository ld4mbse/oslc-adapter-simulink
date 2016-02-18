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

package edu.gatech.mbsec.adapter.simulink.application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkBlock;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkElementsToCreate;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkInputPort;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkLine;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkModel;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkOutputPort;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkParameter;
import edu.gatech.mbsec.adapter.subversion.SubversionFile;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.Link;

import edu.gatech.mbsec.adapter.simulink.matlab.Simulink2XMIThread2;
import edu.gatech.mbsec.adapter.simulink.serviceproviders.ServiceProviderCatalogSingleton;
import edu.gatech.mbsec.adapter.simulink.services.OSLC4JSimulinkApplication;
import simulink.Block;
import simulink.InputPort;
import simulink.Line;
import simulink.Model;
import simulink.OutputPort;
import simulink.Parameter;
import simulink.Port;
import simulink.SimulinkPackage;
import simulink.WorkingDirectory;


/**
 * SimulinkManager is responsible for all the communication between the Simulink
 * application and the OSLC Simulink adapter. It is used to load Simulink
 * models, retrieve Simulink elements from models, and map Simulink elements to
 * OSLC resources described as POJOs
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
public class SimulinkManager {

	static int sessionID = 1;

	public static Collection<Model> simulinkModels = new ArrayList<Model>();
	public static Collection<Block> simulinkBlocks = new ArrayList<Block>();
	static Collection<InputPort> simulinkInputPorts = new ArrayList<InputPort>();
	static Collection<OutputPort> simulinkOutputPorts = new ArrayList<OutputPort>();
	static Collection<Line> simulinkLines = new ArrayList<Line>();
	static Collection<Parameter> simulinkParameters = new ArrayList<Parameter>();

	public static List<SimulinkModel> oslcSimulinkModels = new ArrayList<SimulinkModel>();
	public static List<SimulinkBlock> oslcSimulinkBlocks = new ArrayList<SimulinkBlock>();
	public static List<SimulinkInputPort> oslcSimulinkInputPorts = new ArrayList<SimulinkInputPort>();
	public static List<SimulinkOutputPort> oslcSimulinkOutputPorts = new ArrayList<SimulinkOutputPort>();
	public static List<SimulinkParameter> oslcSimulinkParameters = new ArrayList<SimulinkParameter>();

	static Map<String, SimulinkModel> qNameOslcSimulinkModelMap = new HashMap<String, SimulinkModel>();
	static Map<String, SimulinkBlock> qNameOslcSimulinkBlockMap = new HashMap<String, SimulinkBlock>();
	static Map<String, SimulinkInputPort> qNameOslcSimulinkInputPortMap = new HashMap<String, SimulinkInputPort>();
	static Map<String, SimulinkOutputPort> qNameOslcSimulinkOutputPortMap = new HashMap<String, SimulinkOutputPort>();
	static Map<String, SimulinkLine> qNameOslcSimulinkLineMap = new HashMap<String, SimulinkLine>();
	static Map<String, SimulinkParameter> qNameOslcSimulinkParameterMap = new HashMap<String, SimulinkParameter>();

	public static WorkingDirectory simulinkWorkingDirectory = null;

	static StringBuffer buffer;

	public static String baseHTTPURI = "http://localhost:" + OSLC4JSimulinkApplication.portNumber + "/oslc4jsimulink";
	static String projectId;

	public static void main(String[] args) {
		loadSimulinkWorkingDirectory();
	}

	public static synchronized void loadSimulinkWorkingDirectory() {
		if (simulinkWorkingDirectory != null) {
			return;
		}
		Thread thread = new Thread() {
			public void start() {

				
				
				simulinkModels.clear();
				simulinkBlocks.clear();
				simulinkInputPorts.clear();
				simulinkOutputPorts.clear();
				simulinkLines.clear();
				simulinkParameters.clear();

				oslcSimulinkModels.clear();
				oslcSimulinkBlocks.clear();
				oslcSimulinkInputPorts.clear();
				oslcSimulinkOutputPorts.clear();
				oslcSimulinkParameters.clear();

				qNameOslcSimulinkModelMap.clear();
				qNameOslcSimulinkBlockMap.clear();
				qNameOslcSimulinkInputPortMap.clear();
				qNameOslcSimulinkOutputPortMap.clear();
				qNameOslcSimulinkLineMap.clear();
				qNameOslcSimulinkParameterMap.clear();

				// run matlab script
				long startTime = System.currentTimeMillis();
				Simulink2XMIThread2 simulink2XMIThread = new Simulink2XMIThread2();
				simulink2XMIThread.start();
				try {
					simulink2XMIThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long endTime = System.currentTimeMillis();
				long duration = endTime - startTime;
				//System.out.println("OSLC Adapter <-> Simulink Interaction in " + (duration) + " milli seconds");

				try{
					// load Simulink XMI file
					Resource ecoreResource = loadEcoreModel(org.eclipse.emf.common.util.URI.createFileURI(
							new File(OSLC4JSimulinkApplication.simulinkModelsDirectory + "/simulinkWorkDir.xmi")
									.getAbsolutePath()));

					// load Simulink working directory
					simulinkWorkingDirectory = (WorkingDirectory) EcoreUtil.getObjectByType(ecoreResource.getContents(),
							SimulinkPackage.eINSTANCE.getWorkingDirectory());

					for (Model model : simulinkWorkingDirectory.getModel()) {
						//System.out.println("MODEL " + model.getName());
						projectId = model.getName();

						// create OSLC Simulink model
						try {
							SimulinkModel simulinkModel = new SimulinkModel(java.net.URI.create(
									baseHTTPURI + "/services/" + projectId + "/model/" + getQualifiedName(model, null)));
							simulinkModel.setName(model.getName());
							qNameOslcSimulinkModelMap.put(getQualifiedName(model, null), simulinkModel);
							oslcSimulinkModels.add(simulinkModel);

							// map model blocks
							Link[] blockLinks = getLinkedEReferences(model.getBlock());
							simulinkModel.setBlocks(blockLinks);

							// map model lines
							Link[] linesLinks = getLinkedEReferences(model.getLine());
							simulinkModel.setLines(linesLinks);

						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						for (Block block : model.getBlock()) {
							//System.out.println("\tBLOCK " + block.getName());

							// create OSLC Simulink block
							try {
								SimulinkBlock simulinkBlock = new SimulinkBlock(java.net.URI.create(baseHTTPURI
										+ "/services/" + model.getName() + "/blocks/" + getQualifiedName(block, null)));
								String blockQName = block.getName();
								String blockName = blockQName.split("/")[blockQName.split("/").length - 1];
								simulinkBlock.setName(blockName);
								qNameOslcSimulinkBlockMap.put(model.getName() + "/blocks/" + getQualifiedName(block, null),
										simulinkBlock);
								oslcSimulinkBlocks.add(simulinkBlock);

								// block type
								simulinkBlock.setType(block.getType());

								// block parameters
								Link[] parameters = getLinkedEReferences(block.getParameter());
								simulinkBlock.setParameters(parameters);

								// block input ports
								Link[] inputPortLinks = getLinkedEReferences(block.getInputPort());
								simulinkBlock.setInputPorts(inputPortLinks);

								// block output ports
								Link[] outputPortLinks = getLinkedEReferences(block.getOutputPort());
								simulinkBlock.setOutputPorts(outputPortLinks);

							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							for (Parameter parameter : block.getParameter()) {
								//System.out.println("\t\tPARAMETER " + parameter.getName());

								// create OSLC Simulink block parameter
								try {
									SimulinkParameter simulinkParameter = new SimulinkParameter(
											java.net.URI.create(baseHTTPURI + "/services/" + model.getName()
													+ "/parameters/" + getQualifiedName(parameter, null)));
									String paramaterQName = parameter.getName();
									String blockParameterName = paramaterQName.split("/")[paramaterQName.split("/").length
											- 1];
									simulinkParameter.setName(blockParameterName);
									simulinkParameter.setValue(parameter.getValue());
									qNameOslcSimulinkParameterMap.put(
											model.getName() + "/parameters/" + getQualifiedName(parameter, null),
											simulinkParameter);
									oslcSimulinkParameters.add(simulinkParameter);
								} catch (URISyntaxException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							for (InputPort inputPort : block.getInputPort()) {
								//System.out.println("\t\tINPUTPORT " + inputPort.getId());

								// create OSLC Simulink input port
								try {
									SimulinkInputPort simulinkInputPort = new SimulinkInputPort(
											java.net.URI.create(baseHTTPURI + "/services/" + model.getName()
													+ "/inputports/" + getQualifiedName(inputPort, null)));
									simulinkInputPort.setId(inputPort.getId());
									qNameOslcSimulinkInputPortMap.put(
											model.getName() + "/inputports/" + getQualifiedName(inputPort, null),
											simulinkInputPort);
									oslcSimulinkInputPorts.add(simulinkInputPort);
								} catch (URISyntaxException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							for (OutputPort outputPort : block.getOutputPort()) {
								//System.out.println("\t\tOUTPUTPORT " + outputPort.getId());
								// create OSLC Simulink output port
								try {
									SimulinkOutputPort simulinkOutputPort = new SimulinkOutputPort(
											java.net.URI.create(baseHTTPURI + "/services/" + model.getName()
													+ "/outputports/" + getQualifiedName(outputPort, null)));
									simulinkOutputPort.setId(outputPort.getId());
									qNameOslcSimulinkOutputPortMap.put(
											model.getName() + "/outputports/" + getQualifiedName(outputPort, null),
											simulinkOutputPort);
									oslcSimulinkOutputPorts.add(simulinkOutputPort);
								} catch (URISyntaxException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						for (Line line : model.getLine()) {
							try {
								//System.out.println("\tLINE " + line.getSourcePort().getId() + " <--> ");
								// create OSLC Simulink line
								SimulinkLine simulinkLine = new SimulinkLine(java.net.URI.create(
										baseHTTPURI + "/services/" + projectId + "/lines/" + getQualifiedName(line, null)));
								URI sourcePortURI = URI.create(baseHTTPURI + "/services/" + projectId + "/outputports/"
										+ getQualifiedName(line.getSourcePort(), null));
								simulinkLine.setSourcePort(sourcePortURI);
								Link[] targetPortsLink = new Link[line.getTargetPort().size()];
								int i = 0;
								for (Port targetPort : line.getTargetPort()) {
//									System.out.println(targetPort.getId());
									URI targetPortURI = URI.create(baseHTTPURI + "/services/" + projectId + "/inputports/"
											+ getQualifiedName(targetPort, null));
									targetPortsLink[i] = new Link(targetPortURI);
									i++;
								}
								simulinkLine.setTargetPorts(targetPortsLink);

								// if a Simulink line is branched, it is represented
								// in XMI
								// as multiple lines having the same source port
								// Example below:
								// <line sourcePort="model2/Step/outport/1"
								// targetPort="model2/Subsystem1/inport/1"/>
								// <line sourcePort="model2/Step/outport/1"
								// targetPort="model2/Subsystem2/inport/3"/>
								// <line sourcePort="model2/Step/outport/1"
								// targetPort="model2/Subsystem2/inport/3
								// model2/Subsystem1/inport/1"/>
								// only the line having multiple target ports is
								// needed
								if (qNameOslcSimulinkLineMap
										.containsKey(model.getName() + "/lines/" + getQualifiedName(line, null))) {
									SimulinkLine recordedSimulinkLine = qNameOslcSimulinkLineMap
											.get(model.getName() + "/lines/" + getQualifiedName(line, null));
									if (simulinkLine.getTargetPorts().length > recordedSimulinkLine
											.getTargetPorts().length) {
										qNameOslcSimulinkLineMap.put(
												model.getName() + "/lines/" + getQualifiedName(line, null), simulinkLine);
									}
								} else {
									qNameOslcSimulinkLineMap.put(model.getName() + "/lines/" + getQualifiedName(line, null),
											simulinkLine);
								}

							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				catch(Exception e){
					// ecoreResource may not have been created because there is no Subversion file to load
				}
				
				
			}
		};
		thread.start();
		try {
			thread.join();
			System.out.println("Data read from " + OSLC4JSimulinkApplication.simulinkModelsDirectory
					+ " and converted into OSLC resources at " + new Date().toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Resource loadEcoreModel(org.eclipse.emf.common.util.URI fileURI) {
		// Create a resource set.
		ResourceSet resourceSet = new ResourceSetImpl();

		// Register the default resource factory -- only needed for stand-alone!
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		// Register the package -- only needed for stand-alone!
		SimulinkPackage simulinkEPackage = SimulinkPackage.eINSTANCE;

		resourceSet.getPackageRegistry().put("simulink", simulinkEPackage);

		// Demand load the resource for this file.
		Resource resource = resourceSet.getResource(fileURI, true);
		return resource;
	}

	public static String getQualifiedName(EObject eObject, StringBuffer qualifiedNameBuffer) {
		String qualifiedName = null;
		String eObjecClassName = eObject.eClass().getName();

		if (eObjecClassName.equals("Line")) {
			Line line = (Line) eObject;
			Port sourcePort = line.getSourcePort();
			// String shortTargetQName = "";
			// int i = 0;
			// for (Port targetPort : line.getTargetPort()) {
			// if (i == 0) {
			// shortTargetQName = getShortQualifiedName(targetPort.getId())
			// .replaceAll("/", "::");
			// } else {
			// shortTargetQName = shortTargetQName
			// + "--"
			// + getShortQualifiedName(targetPort.getId())
			// .replaceAll("/", "::");
			// }
			// i++;
			// }
			String shortSourceQName = getShortQualifiedName(sourcePort.getId()).replaceAll("/", "::");
			// qualifiedName = shortSourceQName + "---" + shortTargetQName;
			qualifiedName = shortSourceQName;
		} else if (eObjecClassName.contains("Port")) {
			for (EAttribute eAttribute : eObject.eClass().getEAllAttributes()) {
				if (eAttribute.getName().equals("id")) {
					String name = (String) eObject.eGet(eAttribute);
					qualifiedNameBuffer = new StringBuffer();
					qualifiedNameBuffer.insert(0, name);
					break;
				}
			}
			qualifiedName = qualifiedNameBuffer.toString();
			qualifiedName = getShortQualifiedName(qualifiedName);
			qualifiedName = qualifiedName.replaceAll("/", "::");

		} else if (eObjecClassName.contains("Block")) {
			for (EAttribute eAttribute : eObject.eClass().getEAllAttributes()) {
				if (eAttribute.getName().equals("name")) {
					String name = (String) eObject.eGet(eAttribute);
					qualifiedNameBuffer = new StringBuffer();
					qualifiedNameBuffer.insert(0, name);
					break;
				}
			}
			qualifiedName = qualifiedNameBuffer.toString();
			qualifiedName = getShortQualifiedName(qualifiedName);
			qualifiedName = qualifiedName.replaceAll("/", "::");
		} else if (eObjecClassName.contains("Parameter")) {
			for (EAttribute eAttribute : eObject.eClass().getEAllAttributes()) {
				if (eAttribute.getName().equals("name")) {
					String name = (String) eObject.eGet(eAttribute);
					qualifiedNameBuffer = new StringBuffer();
					qualifiedNameBuffer.insert(0, name);
					break;
				}
			}
			qualifiedName = qualifiedNameBuffer.toString();
			qualifiedName = getShortQualifiedName(qualifiedName);
			qualifiedName = qualifiedName.replaceAll("/", "::");
		} else {
			for (EAttribute eAttribute : eObject.eClass().getEAllAttributes()) {
				if (eAttribute.getName().equals("name") | eAttribute.getName().equals("id")) {
					String name = (String) eObject.eGet(eAttribute);
					if (qualifiedNameBuffer == null) {
						qualifiedNameBuffer = new StringBuffer();
						qualifiedNameBuffer.insert(0, name);
					} else {
						qualifiedNameBuffer.insert(0, name + "::");
					}
					break;
				}
			}
			if (!eObject.eContainer().eClass().getName().equals("WorkingDirectory")) {
				getQualifiedName(eObject.eContainer(), qualifiedNameBuffer);
			}
			qualifiedName = qualifiedNameBuffer.toString();
		}

		return qualifiedName;
	}

	public static String getShortQualifiedName(String qualifiedName) {
		String[] qualifiedNameSegments = qualifiedName.split("/");
		String[] newqualifiedNameSegmentsArray = new String[qualifiedNameSegments.length - 1];
		System.arraycopy(qualifiedNameSegments, 1, newqualifiedNameSegmentsArray, 0,
				(qualifiedNameSegments.length - 1));
		StringBuffer qNameBuffer = new StringBuffer();
		int i = 0;
		for (String segment : newqualifiedNameSegmentsArray) {
			if (i > 0) {
				qNameBuffer.append("/");
			}
			qNameBuffer.append(segment);
			i++;
		}
		return qNameBuffer.toString();
	}

	public static String getQualifiedNameOfOwner(String qualifiedName) {
		String[] qualifiedNameSegments = qualifiedName.split("/");
		String[] newqualifiedNameSegmentsArray = new String[qualifiedNameSegments.length - 1];
		System.arraycopy(qualifiedNameSegments, 0, newqualifiedNameSegmentsArray, 0,
				(qualifiedNameSegments.length - 1));
		StringBuffer qNameBuffer = new StringBuffer();
		int i = 0;
		for (String segment : newqualifiedNameSegmentsArray) {
			if (i > 0) {
				qNameBuffer.append("/");
			}
			qNameBuffer.append(segment);
			i++;
		}
		return qNameBuffer.toString();
	}

	public static String getQualifiedNameOfOwnerOfLine(String qualifiedName) {
		String[] qualifiedNameSegments = qualifiedName.split("/");
		if (qualifiedNameSegments.length > 2) {
			String[] newqualifiedNameSegmentsArray = new String[qualifiedNameSegments.length - 2];
			System.arraycopy(qualifiedNameSegments, 0, newqualifiedNameSegmentsArray, 0,
					(qualifiedNameSegments.length - 2));
			StringBuffer qNameBuffer = new StringBuffer();
			int i = 0;
			for (String segment : newqualifiedNameSegmentsArray) {
				if (i > 0) {
					qNameBuffer.append("/");
				}
				qNameBuffer.append(segment);
				i++;
			}
			return qNameBuffer.toString();
		}
		return null;

	}

	private static Link[] getLinkedEReferences(Collection<? extends EObject> elementCollection) {

		// counting the number of links
		int linksCount = elementCollection.size();

		String objectType = null;
		if (linksCount > 0) {
			EObject eObject = (EObject) elementCollection.toArray()[0];
			if (eObject instanceof Model) {
				objectType = "model";
			} else if (eObject instanceof Block) {
				objectType = "blocks";
			} else if (eObject instanceof InputPort) {
				objectType = "inputports";
			} else if (eObject instanceof OutputPort) {
				objectType = "outputports";
			} else if (eObject instanceof Line) {
				objectType = "lines";
			} else if (eObject instanceof Parameter) {
				objectType = "parameters";
			}
		}

		// creating linksArray
		Link[] linksArray = null;
		if (linksCount > 0) {
			linksArray = new Link[linksCount];
		}

		// populating linksArray
		int linksArrayIndex = 0;
		for (EObject element : elementCollection) {
			try {
				URI linkedElementURI = null;
				linkedElementURI = new URI(baseHTTPURI + "/services/" + projectId + "/" + objectType + "/"
						+ getQualifiedName(element, null));
				Link link = new Link(linkedElementURI);
				linksArray[linksArrayIndex] = link;
				linksArrayIndex++;
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return linksArray;
	}

	public static SimulinkModel getModelByName(String modelName) {
		return qNameOslcSimulinkModelMap.get(modelName);
	}

	public static SimulinkBlock getBlockByURI(String uri) {
		return qNameOslcSimulinkBlockMap.get(uri);
	}

	public static SimulinkParameter getParameterByURI(String uri) {
		return qNameOslcSimulinkParameterMap.get(uri);
	}

	public static SimulinkInputPort getInputPortByURI(String uri) {
		return qNameOslcSimulinkInputPortMap.get(uri);
	}

	public static SimulinkOutputPort getOutputPortByURI(String uri) {
		return qNameOslcSimulinkOutputPortMap.get(uri);
	}

	public static SimulinkLine getLineByURI(String uri) {
		return qNameOslcSimulinkLineMap.get(uri);
	}

	public static List<SimulinkBlock> getBlocksInModel(String modelName) {
		ArrayList<SimulinkBlock> elements = new ArrayList<SimulinkBlock>();
		for (SimulinkBlock simulinkBlock : oslcSimulinkBlocks) {
			String blockURI = simulinkBlock.getAbout().toString();
			if (blockURI.startsWith(baseHTTPURI + "/services/" + modelName + "/")) {
				elements.add(simulinkBlock);
			}
		}
		;
		return elements;
	}

	public static List<SimulinkInputPort> getInputPortsInModel(String modelName) {
		ArrayList<SimulinkInputPort> elements = new ArrayList<SimulinkInputPort>();
		for (SimulinkInputPort simulinkElement : oslcSimulinkInputPorts) {
			String elementURI = simulinkElement.getAbout().toString();
			if (elementURI.startsWith(baseHTTPURI + "/services/" + modelName + "/")) {
				elements.add(simulinkElement);
			}
		}
		;
		return elements;
	}

	public static List<SimulinkOutputPort> getOutputPortsInModel(String modelName) {
		ArrayList<SimulinkOutputPort> elements = new ArrayList<SimulinkOutputPort>();
		for (SimulinkOutputPort simulinkElement : oslcSimulinkOutputPorts) {
			String elementURI = simulinkElement.getAbout().toString();
			if (elementURI.startsWith(baseHTTPURI + "/services/" + modelName + "/")) {
				elements.add(simulinkElement);
			}
		}
		;
		return elements;
	}

	public static List<SimulinkLine> getLinesInModel(String modelName) {
		ArrayList<SimulinkLine> elements = new ArrayList<SimulinkLine>();
		for (SimulinkLine simulinkElement : qNameOslcSimulinkLineMap.values()) {
			String elementURI = simulinkElement.getAbout().toString();
			if (elementURI.startsWith(baseHTTPURI + "/services/" + modelName + "/")) {
				elements.add(simulinkElement);
			}
		}
		return elements;
	}

	public static List<SimulinkParameter> getParametersInModel(String modelName) {
		ArrayList<SimulinkParameter> elements = new ArrayList<SimulinkParameter>();
		for (SimulinkParameter simulinkElement : oslcSimulinkParameters) {
			String elementURI = simulinkElement.getAbout().toString();
			if (elementURI.startsWith(baseHTTPURI + "/services/" + modelName + "/")) {
				elements.add(simulinkElement);
			}
		}
		;
		return elements;
	}

	public static void createSimulinkBlock(SimulinkBlock simulinkBlock, String modelName) {
		URI simulinkBlockURI = simulinkBlock.getAbout();
		String simulinkBlockQualifiedName = simulinkBlockURI.toString()
				.replace(baseHTTPURI + "/services/" + modelName + "/blocks/", "");
		simulinkBlockQualifiedName = modelName + "/" + simulinkBlockQualifiedName.replaceAll("::", "/");
		String simulinkBlockType = simulinkBlock.getType();

		Process addBlockProcess;
		try {
			addBlockProcess = Runtime.getRuntime()
					.exec("matlab start /wait " + "-nodisplay -nosplash -nodesktop -r " + "addSimulinkBlock('"
							+ modelName + "','" + simulinkBlockType + "','" + simulinkBlockQualifiedName + "');");
			int exitValue = addBlockProcess.waitFor();
			if (exitValue == 0) {
				System.out.println("added " + simulinkBlock.getName() + " Block to model " + modelName);
			} else {
				System.err.println(
						"NOT added " + simulinkBlock.getName() + " Block to model " + modelName + "\tView log file!");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void createSimulinkParameter(SimulinkParameter simulinkParameter, String modelName) {
		URI simulinkParameterURI = simulinkParameter.getAbout();
		String simulinkParameterQualifiedName = simulinkParameterURI.toString()
				.replace(baseHTTPURI + "/services/" + modelName + "/parameters/", "");
		simulinkParameterQualifiedName = simulinkParameterQualifiedName.replaceAll("::", "/");
		String simulinkParameterOwner = getQualifiedNameOfOwner(simulinkParameterQualifiedName);

		Process setParamProcess;
		try {
			setParamProcess = Runtime.getRuntime()
					.exec("matlab start /wait " + "-nodisplay -nosplash -nodesktop -r " + "addSimulinkParameter('"
							+ modelName + "','" + simulinkParameterOwner + "','" + simulinkParameter.getName() + "','"
							+ simulinkParameter.getValue() + "');");
			int exitValue = setParamProcess.waitFor();
			if (exitValue == 0) {
				System.out.println(
						"set " + simulinkParameter.getName() + " Parameter to value " + simulinkParameter.getValue());
			} else {
				System.err.println("NOT set " + simulinkParameter.getName() + " Parameter to value "
						+ simulinkParameter.getValue() + "\tView log file!");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void createSimulinkLine(SimulinkLine simulinkLine, String modelName) {
		URI sourcePortURI = simulinkLine.getSourcePort();
		String simulinkLineSourcePortQualifiedName = sourcePortURI.toString()
				.replace(baseHTTPURI + "/services/" + modelName + "/outputports/", "");
		simulinkLineSourcePortQualifiedName = simulinkLineSourcePortQualifiedName.replace("outport::", "");
		simulinkLineSourcePortQualifiedName = simulinkLineSourcePortQualifiedName.replaceAll("::", "/");
		String simulinkLineSourcePortName = simulinkLineSourcePortQualifiedName
				.split("/")[simulinkLineSourcePortQualifiedName.split("/").length - 2] + "/"
				+ simulinkLineSourcePortQualifiedName.split("/")[simulinkLineSourcePortQualifiedName.split("/").length
						- 1];
		String SubsystemOwningLine = getQualifiedNameOfOwnerOfLine(simulinkLineSourcePortQualifiedName);
		if (SubsystemOwningLine == null) {
			SubsystemOwningLine = "";
		}

		// StringBuffer matlabCommand = new StringBuffer("matlab start /wait "
		// + "-nodisplay -nosplash -nodesktop -r openSimulinkModel('" +
		// modelName + "');");
		// for (Link targetPortLink : simulinkLine.getTargetPorts()) {
		// URI targetPortURI = targetPortLink.getValue();
		// String simulinkLineTargetPortQualifiedName =
		// targetPortURI.toString().replace(baseHTTPURI + "/services/"
		// + modelName + "/inputports/", "");
		// simulinkLineTargetPortQualifiedName =
		// simulinkLineTargetPortQualifiedName.replace("inport::", "");
		// simulinkLineTargetPortQualifiedName =
		// simulinkLineTargetPortQualifiedName.replaceAll("::", "/");
		// String simulinkLineTargetPortName =
		// simulinkLineTargetPortQualifiedName.split("/")[simulinkLineTargetPortQualifiedName.split("/").length
		// - 2] + "/" +
		// simulinkLineTargetPortQualifiedName.split("/")[simulinkLineTargetPortQualifiedName.split("/").length
		// - 1];
		// matlabCommand.append("add_line('" + modelName + "/"+
		// SubsystemOwningLine + "','" + simulinkLineSourcePortName + "','" +
		// simulinkLineTargetPortName + "','autorouting','on');");
		// }
		// matlabCommand.append("save_system('" + modelName +
		// "');close_system;exit;");

		StringBuffer matlabCommand = new StringBuffer("matlab start /wait " + "-nodisplay -nosplash -nodesktop -r ");
		StringBuffer targetPorts = new StringBuffer("{");
		int i = 0;
		for (Link targetPortLink : simulinkLine.getTargetPorts()) {
			URI targetPortURI = targetPortLink.getValue();
			String simulinkLineTargetPortQualifiedName = targetPortURI.toString()
					.replace(baseHTTPURI + "/services/" + modelName + "/inputports/", "");
			simulinkLineTargetPortQualifiedName = simulinkLineTargetPortQualifiedName.replace("inport::", "");
			simulinkLineTargetPortQualifiedName = simulinkLineTargetPortQualifiedName.replaceAll("::", "/");
			String simulinkLineTargetPortName = simulinkLineTargetPortQualifiedName
					.split("/")[simulinkLineTargetPortQualifiedName.split("/").length - 2] + "/"
					+ simulinkLineTargetPortQualifiedName
							.split("/")[simulinkLineTargetPortQualifiedName.split("/").length - 1];
			if (i > 0) {
				targetPorts.append(",");
			}
			targetPorts.append("'" + simulinkLineTargetPortName + "'");
			i++;
		}
		targetPorts.append("}");
		matlabCommand.append("addSimulinkLine('" + modelName + "','" + modelName + "/" + SubsystemOwningLine + "','"
				+ simulinkLineSourcePortName + "'," + targetPorts.toString() + ");");
		String matlabCommandString = matlabCommand.toString();

		// add_line(modelName,'Step/1','Subsystem1/1', 'autorouting','on');
		Process setParamProcess;
		try {
			setParamProcess = Runtime.getRuntime().exec(matlabCommandString);
			int exitValue = setParamProcess.waitFor();
			if (exitValue == 0) {
				System.out.println("added " + simulinkLine.getAbout());
			} else {
				System.out.println("NOT added " + simulinkLine.getAbout() + "\tView log file!");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void createSimulinkElements(SimulinkElementsToCreate newElements, String modelName) {
		StringBuffer matlabCommand = new StringBuffer(
				"matlab start /wait " + "-nodisplay -nosplash -nodesktop -r openSimulinkModel3('" + modelName + "');");

		// shuffle blocks in the right order (nested blocks after regular
		// blocks)
		List<SimulinkBlock> simulinkBlocksToCreate = newElements.getBlocksToCreate();
		Set<SimulinkBlock> orderedSimulinkBlocksToCreate = new LinkedHashSet<SimulinkBlock>();

		// find maximum number of nested levels
		int maxNumberNestedLevels = 1;
		for (SimulinkBlock simulinkBlock : simulinkBlocksToCreate) {
			if (simulinkBlock.getAbout().toString().contains("::")) {
				// nested block, make sure that higher level block has been
				// added
				String[] stringSegments = simulinkBlock.getAbout().toString().split("::");
				int segmentsNumber = stringSegments.length;
				if (segmentsNumber > maxNumberNestedLevels) {
					maxNumberNestedLevels = segmentsNumber;
				}
			}
		}

		for (SimulinkBlock simulinkBlock : simulinkBlocksToCreate) {
			if (simulinkBlock.getAbout().toString().contains("::")) {
				// nested block, make sure that higher level block has been
				// added
				String[] stringSegments = simulinkBlock.getAbout().toString().split("::");
				int segmentsNumber = stringSegments.length;
				if (segmentsNumber > maxNumberNestedLevels) {
					maxNumberNestedLevels = segmentsNumber;
				}
			}
		}

		for (int i = 1; i < maxNumberNestedLevels + 1; i++) {
			for (SimulinkBlock simulinkBlock : simulinkBlocksToCreate) {
				String[] stringSegments = simulinkBlock.getAbout().toString().split("::");
				int segmentsNumber = stringSegments.length;
				if (segmentsNumber == i) {
					orderedSimulinkBlocksToCreate.add(simulinkBlock);
				}
			}
		}

		// add all addSimulinkBlock commands
		for (SimulinkBlock simulinkBlock : orderedSimulinkBlocksToCreate) {
			URI simulinkBlockURI = simulinkBlock.getAbout();			
			String simulinkBlockQualifiedName = getSimulinkElementQualifiedName(simulinkBlockURI.toString(), modelName, "block");							
			String simulinkBlockType = simulinkBlock.getType();
			matlabCommand.append("addSimulinkBlock3('" + modelName + "','" + simulinkBlockType + "','"
					+ simulinkBlockQualifiedName + "');");

		}
		// add all addSimulinkParameter commands
		for (SimulinkParameter simulinkParameter : newElements.getParametersToCreate()) {
			URI simulinkParameterURI = simulinkParameter.getAbout();
			String simulinkParameterQualifiedName = getSimulinkElementQualifiedName(simulinkParameterURI.toString(), modelName, "parameter");
			
//			String simulinkParameterQualifiedName = simulinkParameterURI.toString()
//					.replace(baseHTTPURI + "/services/" + modelName + "/parameters/", "");
//			simulinkParameterQualifiedName = simulinkParameterQualifiedName.replaceAll("::", "/");
			
			if(simulinkParameterQualifiedName.startsWith(modelName + "/")){
				simulinkParameterQualifiedName = simulinkParameterQualifiedName.replace(modelName + "/", "");
			}
			
			String simulinkParameterOwner = getQualifiedNameOfOwner(simulinkParameterQualifiedName);
			matlabCommand.append("addSimulinkParameter3('" + modelName + "','" + simulinkParameterOwner + "','"
					+ simulinkParameter.getName() + "','" + simulinkParameter.getValue() + "');");
		}

		for (SimulinkLine simulinkLine : newElements.getLinesToCreate()) {
			URI sourcePortURI = simulinkLine.getSourcePort();
			// element uri may contain svn repo url
			String simulinkLineSourcePortQualifiedName = null;
			if(sourcePortURI.toString().contains("---")){
				simulinkLineSourcePortQualifiedName = sourcePortURI.toString().split("---")[1];
				simulinkLineSourcePortQualifiedName = simulinkLineSourcePortQualifiedName.replace(modelName + "/outputports/", "");
			}
			else{
				simulinkLineSourcePortQualifiedName = sourcePortURI.toString()
						.replace(baseHTTPURI + "/services/" + modelName + "/outputports/", "");
			}
			simulinkLineSourcePortQualifiedName = simulinkLineSourcePortQualifiedName.replace("outport::", "");
			simulinkLineSourcePortQualifiedName = simulinkLineSourcePortQualifiedName.replaceAll("::", "/");
			String simulinkLineSourcePortName = simulinkLineSourcePortQualifiedName
					.split("/")[simulinkLineSourcePortQualifiedName.split("/").length - 2] + "/"
					+ simulinkLineSourcePortQualifiedName
							.split("/")[simulinkLineSourcePortQualifiedName.split("/").length - 1];
			String SubsystemOwningLine = getQualifiedNameOfOwnerOfLine(simulinkLineSourcePortQualifiedName);
			if (SubsystemOwningLine == null) {
				SubsystemOwningLine = "";
			}

			StringBuffer targetPorts = new StringBuffer("{");
			int i = 0;
			for (Link targetPortLink : simulinkLine.getTargetPorts()) {
				URI targetPortURI = targetPortLink.getValue();
				
				// element uri may contain svn repo url
				String simulinkLineTargetPortQualifiedName = null;
				if(targetPortURI.toString().contains("---")){
					simulinkLineTargetPortQualifiedName = targetPortURI.toString().split("---")[1];
					simulinkLineTargetPortQualifiedName = simulinkLineTargetPortQualifiedName.replace(modelName + "/inputports/", "");
				}
				else{
					simulinkLineTargetPortQualifiedName = targetPortURI.toString()
							.replace(baseHTTPURI + "/services/" + modelName + "/inputports/", "");
				}
				
//				String simulinkLineTargetPortQualifiedName = targetPortURI.toString()
//						.replace(baseHTTPURI + "/services/" + modelName + "/inputports/", "");
				simulinkLineTargetPortQualifiedName = simulinkLineTargetPortQualifiedName.replace("inport::", "");
				simulinkLineTargetPortQualifiedName = simulinkLineTargetPortQualifiedName.replaceAll("::", "/");
				String simulinkLineTargetPortName = simulinkLineTargetPortQualifiedName
						.split("/")[simulinkLineTargetPortQualifiedName.split("/").length - 2] + "/"
						+ simulinkLineTargetPortQualifiedName
								.split("/")[simulinkLineTargetPortQualifiedName.split("/").length - 1];
				if (i > 0) {
					targetPorts.append(",");
				}
				targetPorts.append("'" + simulinkLineTargetPortName + "'");
				i++;
			}
			targetPorts.append("}");
			matlabCommand.append("addSimulinkLine3('" + modelName + "','" + modelName + "/" + SubsystemOwningLine
					+ "','" + simulinkLineSourcePortName + "'," + targetPorts.toString() + ");");
		}

		// change directory to Simulink model directory
		matlabCommand.append("cd('" + OSLC4JSimulinkApplication.simulinkModelsDirectory + "');");
		
		// close and save model
		matlabCommand.append("save_system('" + modelName + "');");
		matlabCommand.append("close_system;");

		// close the Matlab command window
		matlabCommand.append("exit;");

		String matlabCommandString = matlabCommand.toString();
		Process setParamProcess;
		try {
			setParamProcess = Runtime.getRuntime().exec(matlabCommandString);
			int exitValue = setParamProcess.waitFor();
			if (exitValue == 0) {
				System.out.println("added " + newElements.getAbout());
			} else {
				System.out.println("NOT added " + newElements.getAbout() + "\tView log file!");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getSimulinkElementQualifiedName(String elementURI, String modelName, String elementType) {
		// element uri may contain svn repo url
		String simulinkBlockQualifiedName = null;
		if(elementURI.toString().contains("---")){
			simulinkBlockQualifiedName = elementURI.toString().split("---")[1];
			simulinkBlockQualifiedName = simulinkBlockQualifiedName.replace(modelName + "/" + elementType + "s/", "");
		}
		else{
			simulinkBlockQualifiedName = elementURI.toString()
					.replace(baseHTTPURI + "/services/" + modelName + "/" + elementType + "s/", "");
		}
		simulinkBlockQualifiedName = modelName + "/" + simulinkBlockQualifiedName.replaceAll("::", "/");
		return simulinkBlockQualifiedName;
	}

}
