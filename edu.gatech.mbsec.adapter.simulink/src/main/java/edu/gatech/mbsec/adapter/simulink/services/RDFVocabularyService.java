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

import java.net.URISyntaxException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;

@Path("rdfvocabulary")
public class RDFVocabularyService {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	@GET
	@Produces({ OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_JSON, OslcMediaType.APPLICATION_JSON_LD})
	public void getRDFVocabulary()
					throws OslcCoreApplicationException, URISyntaxException {

			RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/rdfvocabulary/simulinkRDFVocabulary.rdf");
			try {
				rd.forward(httpServletRequest, httpServletResponse);
			} catch (Exception e) {
				e.printStackTrace();
				throw new WebApplicationException(e);
			}
		

		// throw new WebApplicationException(Response.Status.NOT_FOUND);

	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public void getHtmlRDFVocabulary() throws OslcCoreApplicationException, URISyntaxException {
		String requestURL = httpServletRequest.getRequestURL().toString();
		httpServletRequest.setAttribute("requestURL", requestURL);
		RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/rdfvocabulary/simulinkRDFVocabulary.jsp");
		try {
			rd.forward(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}

		// throw new WebApplicationException(Response.Status.NOT_FOUND);

	}
}
