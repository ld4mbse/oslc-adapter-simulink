<!DOCTYPE html>
<%--
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
--%>

<%@ page contentType="text/html" language="java" pageEncoding="UTF-8"%>
<%@ page
	import="org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog"%>
<%@ page import="org.eclipse.lyo.oslc4j.core.model.ServiceProvider"%>
<%@ page import="org.eclipse.lyo.oslc4j.core.model.Link"%>
<%@ page import="java.net.URI"%>
<%@ page
	import="edu.gatech.mbsec.adapter.simulink.resources.SimulinkBlock"%>
<%

SimulinkBlock block = (SimulinkBlock)request.getAttribute("block");
String requestURL = (String)request.getAttribute("requestURL");
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>Simulink OSLC Adapter: Simulink Block</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/simple.css">
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700'
	rel='stylesheet' type='text/css'>
<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/100px_white-oslc-favicon.ico">


</head>
<body onload="">

	<!-- header -->
	<p id="title">Simulink OSLC Adapter: Simulink Block</p>

	<!-- main content -->
	<div id="main-body">
		
		<!-- oslc logo and adapter details -->
		<a id="oslclogo" href="http://open-services.net/" target="_blank"><img
			src="<%=request.getContextPath()%>/images/oslcLg.png"></a>
		<div id="adapter-details">
			<p class="word-break">
				This document: <a href="<%= requestURL %>"> <%= requestURL %>
				</a><br> Adapter Publisher: <a class="notfancy"
					href="http://www.mbsec.gatech.edu/research/oslc" target="_blank">Georgia
					Institute of Technology OSLC Tools Project</a><br> Adapter
				Identity: edu.gatech.mbsec.adapter.simulink
			</p>
		</div>
		<br>

		<!-- resource type and name -->
		<h1><span id="metainfo">Simulink Block </span><%= block.getName() %></h1>
		<br>

		<!-- resource attributes and relationships -->
		<p><span id="metainfo">Type</span>: <%= block.getType() %></p>

		<p><span id="metainfo">Parameters</span></p>

		<!-- table representation of resource relationships -->
		<% Link[] links =  block.getParameters();  %>
		<% int linksSize =  links.length;  %>
		<% int i =  0;  %>
		<% if( linksSize > 0) {  %>
		<table>
			<tr>
				<% while(linksSize > 0) {;  %>
				<% Link link = links[i]; %>
				<td><a href="<%= link.getValue() %>"> <%=getElementName(link.getValue())%></a></td>
				<%i++;%>
				<!-- change here maximum number of cells to be displayed in each table row -->
				<% if( i % 3 == 0) {  %>
			</tr>
			<tr>
				<% }  %>
				<%linksSize--;%>
				<% };  %>
			</tr>
		</table>
		<% } %>
				
		<!-- list representation of resource relationships -->
		<% if(block.getInputPorts().length > 0) { %>
			<% if(block.getInputPorts().length > 1) { %>
				<p><span id="metainfo">Input Ports</span></p>			
			<% } else {%>
				<p><span id="metainfo">Input Port</span></p>
			<% } %>
			<% for (Link link : block.getInputPorts()) { %>
				<p><a href="<%= link.getValue() %>"> <%=getElementQualifiedName(link.getValue())%></a></p>
			<% } %>
		<% } %>

		<% if(block.getOutputPorts().length > 0) { %>
			<% if(block.getOutputPorts().length > 1) { %>
				<p><span id="metainfo">Output Ports</span></p>			
			<% } else {%>
				<p><span id="metainfo">Output Port</span></p>
			<% } %>
			<% for (Link link : block.getOutputPorts()) { %>
				<p><a href="<%= link.getValue() %>"> <%=getElementQualifiedName(link.getValue())%></a>
			</p>
			<% } %>
		<% } %>

	</div>


	<!-- footer -->
	<p id="footer">OSLC Simulink Adapter 1.1</p>
	 
</body>
</html>


<!-- Java functions -->
<%!
	public String getElementName(URI uri){
		String[] names = uri.getPath().split("::");
	    String last_name = names[names.length - 1]; 	    	
	    return last_name; 
	}
%>

<%!
	public String getElementQualifiedName(URI uri){
		String[] names = uri.getPath().split("/");
	    String last_name = names[names.length - 1]; 	    	
	    return last_name; 
	}
%>