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
 *     Michael Fiedler      - Bugzilla adapter implementation
 *       
 *     Axel Reichwein		- implementation for simulink adapter (axel.reichwein@koneksys.com)
 *      
 *******************************************************************************/
package edu.gatech.mbsec.adapter.simulink.serviceproviders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import edu.gatech.mbsec.adapter.simulink.resources.Constants;
import edu.gatech.mbsec.adapter.subversion.SubversionConstants;
import edu.gatech.mbsec.adapter.subversion.SubversionFileService;
import org.eclipse.lyo.oslc4j.client.ServiceProviderRegistryURIs;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.PrefixDefinition;
import org.eclipse.lyo.oslc4j.core.model.Publisher;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderFactory;

import edu.gatech.mbsec.adapter.simulink.services.SimulinkBlockService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkInputPortService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkLineService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkModelService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkOutputPortService;
import edu.gatech.mbsec.adapter.simulink.services.SimulinkParameterService;

/**
 * SimulinkServiceProviderFactory registers all OSLC Services of each 
 * OSLC Simulink Service Provider. There is a OSLC Simulink Service Provider
 * for each Simulink project. An OSLC Service Provider refers to all 
 * available query and creation factory Services.
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
public class SimulinkServiceProviderFactory {
	private static Class<?>[] RESOURCE_CLASSES = {
			SimulinkBlockService.class, SimulinkInputPortService.class,
			SimulinkOutputPortService.class, SimulinkModelService.class,
			SimulinkLineService.class, SimulinkParameterService.class,			
			};

	private SimulinkServiceProviderFactory() {
		super();
	}

	/**
	 * Create a new Simulink OSLC service provider.
	 * 
	 * @param baseURI
	 * @param product
	 * @param parameterValueMap
	 *            - a map containing the path replacement value for {productId}.
	 *            See ServiceProviderCatalogSingleton.
	 *            initServiceProvidersFromProducts()
	 * @return
	 * @throws OslcCoreApplicationException
	 * @throws URISyntaxException
	 */
	public static ServiceProvider createServiceProvider(final String baseURI,
			final String product, final Map<String, Object> parameterValueMap)
			throws OslcCoreApplicationException, URISyntaxException {
		final ServiceProvider serviceProvider = ServiceProviderFactory
				.createServiceProvider(baseURI, ServiceProviderRegistryURIs
						.getUIURI(), product,
						"Service provider for Simulink model: " + product,
						new Publisher("Georgia Institute of Technology OSLC Project",
								"urn:oslc:ServiceProvider"), RESOURCE_CLASSES,
						parameterValueMap);
		URI detailsURIs[] = { new URI(baseURI + "/details") };
		serviceProvider.setDetails(detailsURIs);

		final PrefixDefinition[] prefixDefinitions = {
				new PrefixDefinition(OslcConstants.DCTERMS_NAMESPACE_PREFIX,
						new URI(OslcConstants.DCTERMS_NAMESPACE)),
				new PrefixDefinition(OslcConstants.OSLC_CORE_NAMESPACE_PREFIX,
						new URI(OslcConstants.OSLC_CORE_NAMESPACE)),
				new PrefixDefinition(OslcConstants.OSLC_DATA_NAMESPACE_PREFIX,
						new URI(OslcConstants.OSLC_DATA_NAMESPACE)),
				new PrefixDefinition(OslcConstants.RDF_NAMESPACE_PREFIX,
						new URI(OslcConstants.RDF_NAMESPACE)),
				new PrefixDefinition(OslcConstants.RDFS_NAMESPACE_PREFIX,
						new URI(OslcConstants.RDFS_NAMESPACE)),
				new PrefixDefinition(Constants.MBSE_PREFIX,
								new URI(Constants.MBSE_NAMESPACE)),
				new PrefixDefinition(Constants.SIMULINK_PREFIX, new URI(
						Constants.SIMULINK_NAMESPACE))				
				};

		serviceProvider.setPrefixDefinitions(prefixDefinitions);

		return serviceProvider;
	}
}
