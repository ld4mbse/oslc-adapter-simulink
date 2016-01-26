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
 *     Michael Fiedler     - initial API and implementation for Bugzilla adapter
 *     
 *     Axel Reichwein	   - implementation for Simulink adapter (axel.reichwein@koneksys.com)
 *     
 *******************************************************************************/
package edu.gatech.mbsec.adapter.simulink.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.xerces.util.URI;
import edu.gatech.mbsec.adapter.simulink.resources.Constants;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkBlock;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkElementsToCreate;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkInputPort;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkLine;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkModel;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkOutputPort;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkParameter;
import edu.gatech.mbsec.adapter.subversion.SubversionFile;
import edu.gatech.mbsec.adapter.subversion.SubversionFileService;
import edu.gatech.mbsec.adapter.subversion.SubversionManager;
import org.eclipse.lyo.oslc4j.application.OslcResourceShapeResource;
import org.eclipse.lyo.oslc4j.application.OslcWinkApplication;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;

import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;
import org.tmatesoft.svn.core.SVNURL;

import com.opencsv.CSVReader;

import clients.SubversionClient;
import clients.SubversionFileClient;
import edu.gatech.mbsec.adapter.simulink.application.SimulinkManager;
import edu.gatech.mbsec.adapter.simulink.services.ServiceProviderCatalogService;
import edu.gatech.mbsec.adapter.simulink.services.ServiceProviderService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkBlockService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkInputPortService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkLineService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkModelService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkOutputPortService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkParameterService;
import util.FileMetadata;

/**
 * OSLC4JSimulinkApplication registers all entity providers for converting POJOs
 * into RDF/XML, JSON and other formats. OSLC4JSimulinkApplication registers
 * also registers each servlet class containing the implementation of OSLC
 * RESTful web services.
 * 
 * OSLC4JSimulinkApplication also reads the user-defined configuration file with
 * loadPropertiesFile(). This is done at the initialization of the web
 * application, for example when the first resource or service of the OSLC
 * Simulink adapter is requested.
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
public class OSLC4JSimulinkApplication extends OslcWinkApplication {

	public static final Set<Class<?>> RESOURCE_CLASSES = new HashSet<Class<?>>();
	public static final Map<String, Class<?>> RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP = new HashMap<String, Class<?>>();

	public static String simulinkEcoreLocation = null;
	public static String simulinkModelsDirectory = null;
	public static String portNumber = null;
	public static boolean syncWithSvnRepo = false;
	public static String svnurl = null;
	public static int delayInSecondsBetweenDataRefresh = 100000;
	public static boolean useIndividualSubversionFiles = false;
	public static SubversionManager subversionManager = null;
	public static SubversionFileClient subversionFileClient;
	public static String svnUserName;
	public static String svnPassword;

	// public static String configFilePath =
	// "oslc4jsimulink configuration/config.properties";
	// public static String configFilePath = "configuration/config.properties";
	// public static String configFilePath =
	// "C:/Users/Axel/Desktop/apache-tomcat-7.0.59/configuration/config.properties";
	public static String warConfigFilePath = "../oslc4jsimulink configuration/config.properties";
	public static String localConfigFilePath = "oslc4jsimulink configuration/config.properties";
	public static String configFilePath = null;
	public static String warSVNURLsFilePath = "../oslc4jsimulink configuration/subversionfiles.csv";
	public static String localSVNURLsFilePath = "oslc4jsimulink configuration/subversionfiles.csv";
	public static String svnURLsFilePath = null;
	public static ArrayList<String> subversionFileURLs;

	static {
		RESOURCE_CLASSES.addAll(JenaProvidersRegistry.getProviders());
		RESOURCE_CLASSES.addAll(Json4JProvidersRegistry.getProviders());
		RESOURCE_CLASSES.add(ServiceProviderCatalogService.class);
		RESOURCE_CLASSES.add(ServiceProviderService.class);
		RESOURCE_CLASSES.add(SimulinkModelService.class);
		RESOURCE_CLASSES.add(SimulinkBlockService.class);
		RESOURCE_CLASSES.add(SimulinkInputPortService.class);
		RESOURCE_CLASSES.add(SimulinkOutputPortService.class);
		RESOURCE_CLASSES.add(SimulinkLineService.class);
		RESOURCE_CLASSES.add(SimulinkParameterService.class);
		RESOURCE_CLASSES.add(SimulinkBlock.class);
		RESOURCE_CLASSES.add(SimulinkLine.class);
		RESOURCE_CLASSES.add(SimulinkParameter.class);
		RESOURCE_CLASSES.add(SimulinkElementsToCreate.class);
		RESOURCE_CLASSES.add(SubversionFile.class);
		RESOURCE_CLASSES.add(SubversionFileService.class);
		RESOURCE_CLASSES.add(SimulinkSVNFileURLService.class);
		

		RESOURCE_CLASSES.add(OslcResourceShapeResource.class);
		
		RESOURCE_CLASSES.add(ResourceShapeService.class);
		RESOURCE_CLASSES.add(RDFVocabularyService.class);

		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SIMULINK_MODEL, SimulinkModel.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SIMULINK_BLOCK, SimulinkBlock.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SIMULINK_INPUTPORT, SimulinkInputPort.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SIMULINK_OUTPUTPORT, SimulinkOutputPort.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SIMULINK_LINE, SimulinkLine.class);
		RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Constants.PATH_SIMULINK_PARAMETER, SimulinkParameter.class);

		loadPropertiesFile();

		readDataFirstTime();

		readDataPeriodically();

	}

	public OSLC4JSimulinkApplication() throws OslcCoreApplicationException, URISyntaxException {
		super(RESOURCE_CLASSES, OslcConstants.PATH_RESOURCE_SHAPES, RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP);
	}

	private static void loadPropertiesFile() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			// loading properties file
			// input = new FileInputStream("./configuration/config.properties");
			input = new FileInputStream(warConfigFilePath); // for war file
			configFilePath = warConfigFilePath;
		} catch (FileNotFoundException e) {
			try {
				input = new FileInputStream(localConfigFilePath);
				configFilePath = localConfigFilePath;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // for war file
		}

		// load property file content and convert backslashes into forward
		// slashes
		String str;
		if (input != null) {
			try {
				str = readFile(configFilePath, Charset.defaultCharset());
				prop.load(new StringReader(str.replace("\\", "/")));

				// get the property value
				String simulinkEcoreLocationFromUser = prop.getProperty("simulinkEcoreLocation");
				String simulinkModelsDirectoryFromUser = prop.getProperty("simulinkModelsDirectory");
				String syncWithSvnRepoFromUser = prop.getProperty("syncWithSvnRepo");
				String delayInSecondsBetweenDataRefreshFromUser = prop.getProperty("delayInSecondsBetweenDataRefresh");
				String useIndividualSubversionFilesFromUser = prop.getProperty("useIndividualSubversionFiles");

				// add trailing slash if missing
				if (!simulinkModelsDirectoryFromUser.endsWith("/")) {
					simulinkModelsDirectoryFromUser = simulinkModelsDirectoryFromUser + "/";
				}
				simulinkModelsDirectory = simulinkModelsDirectoryFromUser;
				simulinkEcoreLocation = simulinkEcoreLocationFromUser;
				portNumber = prop.getProperty("portNumber");
				svnUserName = prop.getProperty("svnUserName");
				svnPassword = prop.getProperty("svnPassword");
				try {
					if (Boolean.parseBoolean(syncWithSvnRepoFromUser)) {
						syncWithSvnRepo = true;
					}
				} catch (Exception e) {

				}
				try {
					if (Boolean.parseBoolean(useIndividualSubversionFilesFromUser)) {
						useIndividualSubversionFiles = true;
						// delete all existing models in directory as they will be populated with new Subversion files
//						new File(OSLC4JSimulinkApplication.simulinkModelsDirectory).delete();	//only works if directory is emtpy
						Path directory = Paths.get(OSLC4JSimulinkApplication.simulinkModelsDirectory);
						   Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
							   @Override
							   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
								   Files.delete(file);
								   return FileVisitResult.CONTINUE;
							   }

							   @Override
							   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
								   Files.delete(dir);
								   return FileVisitResult.CONTINUE;
							   }

						   });
					}
				} catch (Exception e) {

				}
				svnurl = prop.getProperty("svnurl");
				try {
					delayInSecondsBetweenDataRefresh = Integer.parseInt(delayInSecondsBetweenDataRefreshFromUser);
				} catch (Exception e) {

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	private static void loadSVNURLsFile() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			// loading properties file
			// input = new FileInputStream("./configuration/config.properties");
			input = new FileInputStream(warSVNURLsFilePath); // for war file
			svnURLsFilePath = warSVNURLsFilePath;
		} catch (FileNotFoundException e) {
			try {
				input = new FileInputStream(localSVNURLsFilePath);
				svnURLsFilePath = localSVNURLsFilePath;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // for war file
		}

		// load property file content and convert backslashes into forward
		// slashes
		String str;
		if (input != null) {
			try {				
				CSVReader reader2 = new CSVReader(new FileReader(svnURLsFilePath));
				List<String[]> allElements = reader2.readAll();								
				subversionFileURLs = readSVNFileURLs(allElements);									
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	public static ArrayList<String> readSVNFileURLs(List<String[]> allElements) {
		List<String> svnurls = new ArrayList<String>();		
		
		for (String[] element : allElements) {
			if ((element.length == 1)) {
				svnurls.add(element[0]);
			}
		}
		
		ArrayList<String> subversionFileURLs = new ArrayList<String>();
//		for (String subversionFileURL : SubversionFileURLsFromUserArray) {
		for (String subversionFileURL : svnurls) {
			// make sure to delete possible space character
			if (subversionFileURL.startsWith(" ")) {
				subversionFileURL = subversionFileURL.substring(1, subversionFileURL.length());
			}
			if (subversionFileURL.endsWith(" ")) {
				subversionFileURL = subversionFileURL.substring(0, subversionFileURL.length()-1);
			}
			
			try {
				// make sure that URL is valid
				new URL(subversionFileURL);
					
				// make sure that url is not a duplicate							
					if(!subversionFileURLs.contains(subversionFileURL)){
						subversionFileURLs.add(subversionFileURL);
					}
					
				
			} catch (Exception e) {

			}
		}
		return subversionFileURLs;
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}

	public static void checkoutOrUpdateSVNWorkingCopy() {
		Thread thread = new Thread() {
			public void start() {
				ArrayList<FileMetadata> fileMetaDatas = SubversionClient.syncWorkingCopy(svnurl,
						simulinkModelsDirectory);
				// convert fileMetaDatas into OSLC POJOs
				subversionManager.convertFileMetaDataIntoRDFSubversionFileResources(fileMetaDatas);
			}
		};
		thread.start();
		try {
			thread.join();
			System.out.println(
					"Connection with Subversion repository and creation of OSLC Subversion file resources finished.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void checkoutOrUpdateSVNFiles() {
		Thread thread = new Thread() {
			public void start() {
				// read all subversion file urls
				loadSVNURLsFile();

				// initialize global list for FileMetaDatas
				ArrayList<FileMetadata> fileMetaDatas = new ArrayList<FileMetadata>();
				
				// for each subversion file url, perform checkout or update
				for (String subversionFileURLString : subversionFileURLs) {
					// create local directory to save subversionFile
					// files belonging to the same subversion repo directory will share the same local directory
					// name directory based on subversionFileURL
					// get repository directory
					String[] repParts = subversionFileURLString.split("/");
					String subversionFileName = repParts[repParts.length - 1];
					
					String subversionFileDirURL = subversionFileURLString.replace(subversionFileName, "");															
					
					String localSubversionFileDir = subversionFileDirURL.replace(":", "");
					localSubversionFileDir = localSubversionFileDir.replace("/", "");
					
					// create a new directory for the working copy of that file
					if(!new File(simulinkModelsDirectory + localSubversionFileDir).exists()){
						new File(simulinkModelsDirectory + localSubversionFileDir).mkdirs();
					}
					
					// perform checkout or update on Subversion file
					if(subversionFileClient == null){
						subversionFileClient = new SubversionFileClient();
					}
					FileMetadata specificFileMetaData = subversionFileClient.syncFile(subversionFileURLString,
							simulinkModelsDirectory + localSubversionFileDir, svnUserName, svnPassword);
					
					// save Subversion file metadata
					if(specificFileMetaData != null){
						fileMetaDatas.add(specificFileMetaData);
					}
				}
				
				
				// convert fileMetaDatas into OSLC POJOs
				subversionManager.convertFileMetaDataIntoRDFSubversionFileResources(fileMetaDatas);
			}
		};
		thread.start();
		try {
			thread.join();
			System.out.println(
					"Connection with Subversion repository and creation of OSLC Subversion file resources finished.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void readDataFirstTime() {
		Thread thread = new Thread() {
			public void start() {
				subversionManager = new SubversionManager(SimulinkManager.baseHTTPURI);
				reloadSimulinkModels();
			}
		};
		thread.start();
		try {
			thread.join();
			System.out.println("Simulink files read. Initialization of OSLC Simulink adapter finished.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readDataPeriodically() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				reloadSimulinkModels();	
			}
		}, delayInSecondsBetweenDataRefresh * 1000, delayInSecondsBetweenDataRefresh * 1000);
	}

	protected static void reloadSimulinkModels() {
		if (useIndividualSubversionFiles) {					
			checkoutOrUpdateSVNFiles();
		} else if (syncWithSvnRepo) {
			checkoutOrUpdateSVNWorkingCopy();
		}
		SimulinkManager.simulinkWorkingDirectory = null; // to reload
															// models
		SimulinkManager.loadSimulinkWorkingDirectory();
		
	}
}
