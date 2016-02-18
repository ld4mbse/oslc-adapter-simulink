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
 *     Michael Fiedler      - implementation for Bugzilla adapter
 *             
 *     Axel Reichwein 		- implementation for Simulink adapter (axel.reichwein@koneksys.com)
 *     
 *******************************************************************************/
package edu.gatech.mbsec.adapter.simulink.services;



import java.net.URI;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import edu.gatech.mbsec.adapter.subversion.SubversionManager;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialog;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.Compact;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;

import edu.gatech.mbsec.adapter.simulink.application.SimulinkManager;
import edu.gatech.mbsec.adapter.simulink.serviceproviders.ServiceProviderCatalogSingleton;


/**
 * ServiceProviderService is a servlet containing the implementation
 * of OSLC RESTful web services returning ServiceProvider resources
 * in HTML and other formats 
 * 
 * ServiceProviderService also sets the URIs of the OSLC Services contained 
 * in an OSLC Service Provider. These URIs will be represented in the HTML
 * representation of the OSLC Service Provider resource. The URIs are therefore
 * set in getHtmlServiceProvider().
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(OslcConstants.OSLC_CORE_DOMAIN)
@Path("serviceProviders")
public class ServiceProviderService
{
	@Context private HttpServletRequest httpServletRequest;
	@Context private HttpServletResponse httpServletResponse;
	
	
	/**
	 * RDF/XML, XML and JSON representations of an OSLC Service Provider collection
	 * @return
	 */
	
    @OslcDialog
    (
         title = "Service Provider Selection Dialog",
         label = "Service Provider Selection Dialog",
         uri = "",
         hintWidth = "1000px",
         hintHeight = "600px",
         resourceTypes = {OslcConstants.TYPE_SERVICE_PROVIDER},
         usages = {OslcConstants.OSLC_USAGE_DEFAULT}
    )
    @OslcQueryCapability
    (
         title = "Service Provider Query Capability",
         label = "Service Provider Query",
         resourceShape = OslcConstants.PATH_RESOURCE_SHAPES + "/" + OslcConstants.PATH_SERVICE_PROVIDER,
         resourceTypes = {OslcConstants.TYPE_SERVICE_PROVIDER},
         usages = {OslcConstants.OSLC_USAGE_DEFAULT}
    )
    @GET
    @Produces({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON})
    public ServiceProvider[] getServiceProviders()
    {
    	httpServletResponse.addHeader("Oslc-Core-Version","2.0");
        return ServiceProviderCatalogSingleton.getServiceProviders(httpServletRequest);
    }

    /**
     * RDF/XML, XML and JSON representations of a single OSLC Service Provider
     * 
     * @param serviceProviderId
     * @return
     */
    @GET
    @Path("{serviceProviderId}")
    @Produces({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON})
    public ServiceProvider getServiceProvider(@PathParam("serviceProviderId") final String serviceProviderId)
    {
    	httpServletResponse.addHeader("Oslc-Core-Version","2.0");
        return ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, serviceProviderId);
    }

    /**
     * OSLC compact XML representation of a single OSLC Service Provider
     * 
     * @param serviceProviderId
     * @return
     */
    @GET
    @Path("{serviceProviderId}")
    @Produces({OslcMediaType.APPLICATION_X_OSLC_COMPACT_XML, OslcMediaType.APPLICATION_X_OSLC_COMPACT_JSON})
    public Compact getCompact(@PathParam("serviceProviderId") final String serviceProviderId)
    {
        final ServiceProvider serviceProvider = ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, serviceProviderId);

        if (serviceProvider != null) {
        	
        	final Compact compact = new Compact();

        	compact.setAbout(serviceProvider.getAbout());
        	compact.setShortTitle(serviceProvider.getTitle());
        	compact.setTitle(serviceProvider.getTitle());

        	// TODO - Need icon for ServiceProvider compact.

        	httpServletResponse.addHeader("Oslc-Core-Version","2.0");
        	return compact;
        }
        
        throw new WebApplicationException(Status.NOT_FOUND);
    }
    
    /**
     * HTML representation of a single OSLC Service Provider
     * 
     * Forwards to serviceprovider_html.jsp to create the html document
     * 
     * @param serviceProviderId
     */
    @GET
    @Path("{serviceProviderId}")
    @Produces(MediaType.TEXT_HTML)
    public void getHtmlServiceProvider(@PathParam("serviceProviderId") final String serviceProviderId)
    {
    	ServiceProvider serviceProvider = ServiceProviderCatalogSingleton.getServiceProvider(httpServletRequest, serviceProviderId);
    	
    	Service [] services = serviceProvider.getServices();
    	
    	for (Service service : services) {
			if(service.getDomain().toString().contains("model")){
				service.setAbout(URI.create("http://localhost:" + OSLC4JSimulinkApplication.portNumber + "/oslc4jsimulink/services/" + serviceProviderId + "/model"));
			}
			else if(service.getDomain().toString().contains("block")){
				service.setAbout(URI.create("http://localhost:" + OSLC4JSimulinkApplication.portNumber + "/oslc4jsimulink/services/" + serviceProviderId + "/blocks"));
			}
			else if(service.getDomain().toString().contains("inputport")){
				service.setAbout(URI.create("http://localhost:" + OSLC4JSimulinkApplication.portNumber + "/oslc4jsimulink/services/" + serviceProviderId + "/inputports"));
			}
			else if(service.getDomain().toString().contains("outputport")){
				service.setAbout(URI.create("http://localhost:" + OSLC4JSimulinkApplication.portNumber + "/oslc4jsimulink/services/" + serviceProviderId + "/outputports"));
			}
			else if(service.getDomain().toString().contains("line")){
				service.setAbout(URI.create("http://localhost:" + OSLC4JSimulinkApplication.portNumber + "/oslc4jsimulink/services/" + serviceProviderId + "/lines"));
			}
			else if(service.getDomain().toString().contains("parameter")){
				service.setAbout(URI.create("http://localhost:" + OSLC4JSimulinkApplication.portNumber + "/oslc4jsimulink/services/" + serviceProviderId + "/parameters"));
			}
			else if(service.getDomain().toString().contains("subversion.apache.org/file")){
				service.setAbout(URI.create(SimulinkManager.baseHTTPURI + "/services/"
						+ "subversionfiles/"));
			}
			
		}
    	
    	if (services !=null && services.length > 0)
    	{
    		//Bugzilla adapter should only have one Service per ServiceProvider	        
//	        httpServletRequest.setAttribute("service", services[0]);
	        httpServletRequest.setAttribute("serviceProvider", serviceProvider);

	        RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/simulink/serviceprovider_html.jsp");
    		try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {				
				e.printStackTrace();
				throw new WebApplicationException(e);
			} 
    	}
    }


}
