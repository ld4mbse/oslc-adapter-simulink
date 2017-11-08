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
package edu.gatech.mbsec.adapter.simulink.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import edu.gatech.mbsec.adapter.simulink.resources.Constants;
import edu.gatech.mbsec.adapter.simulink.resources.SimulinkBlock;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

import edu.gatech.mbsec.adapter.simulink.application.SimulinkManager;





/**
 * This servlet contains the implementation of OSLC RESTful web services for Simulink Block resources.
 * 
 * The servlet contains web services for: <ul margin-top: 0;>
 * <li> returning specific Simulink Block resources in HTML and other formats </li>
 * <li> returning all Simulink Block resources within a specific Simulink model
 *  in HTML and other formats </li>
 *  <li> adding new Simulink Block resources to a specific Simulink model</li>
 *  </ul>
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
@OslcService(Constants.SIMULINK_BLOCK_DOMAIN)
@Path("{modelName}/blocks")
public class SimulinkBlockService {
	
	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;
	
	@OslcQueryCapability(title = "Simulink Block Query Capability", label = "Simulink Block Catalog Query", resourceShape = OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SIMULINK_BLOCK, resourceTypes = { Constants.TYPE_SIMULINK_BLOCK }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })	
	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON, OslcMediaType.APPLICATION_JSON_LD })
	public List<SimulinkBlock> getBlocks(
			@PathParam("modelName") final String modelName,
			@QueryParam("oslc.where") final String where,
			@QueryParam("oslc.select") final String select,
			@QueryParam("oslc.prefix") final String prefix,
			@QueryParam("page") final String pageString,
			@QueryParam("oslc.orderBy") final String orderBy,
			@QueryParam("oslc.searchTerms") final String searchTerms,
			@QueryParam("oslc.paging") final String paging,
			@QueryParam("oslc.pageSize") final String pageSize)
			throws IOException, ServletException {
		SimulinkManager.loadSimulinkWorkingDirectory();
		List<SimulinkBlock> simulinkBlocks = SimulinkManager.getBlocksInModel(modelName);
		return simulinkBlocks;
	}
	
	@GET	
	@Path("{uri}")
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_JSON, OslcMediaType.APPLICATION_JSON_LD })		
	public Response getBlock(
//			@PathParam("uri") final String uri)
			@PathParam("modelName") final String modelName, @PathParam("uri") final String qualifiedName, @Context Request request)
	
			throws URISyntaxException {
		SimulinkManager.loadSimulinkWorkingDirectory();
		SimulinkBlock simulinkBlock = SimulinkManager
				.getBlockByURI(modelName + "/blocks/" + qualifiedName);
//		return simulinkBlock;
		EntityTag eTag = new EntityTag(String.valueOf(simulinkBlock.hashCode()));
		ResponseBuilder builder = request.evaluatePreconditions(eTag);
		
		//If rb is null then either it is first time request; or resource is modified
        //Get the updated representation and return with Etag attached to it
		if (builder == null) {
		    builder = Response.ok(simulinkBlock);
		}

		return builder.tag(eTag).build();
		
//		return Response.created(simulinkBlock.getAbout()).entity(simulinkBlock).build();
	}

	@GET
	@Path("{uri}")
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlBlock(@PathParam("modelName") final String modelName,
			@PathParam("uri") final String qualifiedName,
			@QueryParam("oslc.properties") final String propertiesString,
			@QueryParam("oslc.prefix") final String prefix)
			throws URISyntaxException, IOException {
		SimulinkManager.loadSimulinkWorkingDirectory();
		SimulinkBlock simulinkBlock = SimulinkManager
				.getBlockByURI(modelName + "/blocks/" + qualifiedName);
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (simulinkBlock != null) {			
			httpServletRequest.setAttribute("block", simulinkBlock);
			httpServletRequest.setAttribute("requestURL", requestURL);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/simulink/block_html.jsp");
			try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {
				e.printStackTrace();
				throw new WebApplicationException(e);
			}
		}
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlBlocks(@PathParam("modelName") final String modelName) {
		SimulinkManager.loadSimulinkWorkingDirectory();
		List<SimulinkBlock> simulinkBlocks = SimulinkManager.getBlocksInModel(modelName);
		String requestURL = httpServletRequest.getRequestURL().toString();
		if (simulinkBlocks != null) {			
			httpServletRequest.setAttribute("elements", simulinkBlocks);
			httpServletRequest.setAttribute("requestURL", requestURL);
			httpServletRequest.setAttribute("modelName", modelName);
			RequestDispatcher rd = httpServletRequest
					.getRequestDispatcher("/simulink/blocks_html.jsp");
			try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {
				e.printStackTrace();
				throw new WebApplicationException(e);
			}
		}
	}
	
	@OslcCreationFactory(title = "Simulink Block Creation Factory", label = "Simulink Block Creation", resourceShapes = { OslcConstants.PATH_RESOURCE_SHAPES
			+ "/" + Constants.PATH_SIMULINK_BLOCK }, resourceTypes = { Constants.TYPE_SIMULINK_BLOCK }, usages = { OslcConstants.OSLC_USAGE_DEFAULT })
	@POST
	@Consumes({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON, OslcMediaType.APPLICATION_JSON_LD })
	@Produces({ OslcMediaType.APPLICATION_RDF_XML,
			OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON, OslcMediaType.APPLICATION_JSON_LD })
	public Response addBlock(@PathParam("modelName") final String modelName,
			final SimulinkBlock simulinkBlock) throws IOException, ServletException {
		System.out.println(simulinkBlock.getName());		
		SimulinkManager.createSimulinkBlock(simulinkBlock, modelName);
		URI about = simulinkBlock.getAbout();
		return Response.created(about).entity(simulinkBlock).build();
	}
}
