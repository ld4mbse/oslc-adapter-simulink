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

<%
String requestURL = (String)request.getAttribute("requestURL");
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>Simulink OSLC Adapter: Simulink RDF Vocabulary</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/simple.css">
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700'
	rel='stylesheet' type='text/css'>
<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/100px_white-oslc-favicon.ico">


</head>
<body onload="">

	<!-- header -->
	<p id="title">Simulink OSLC Adapter: RDF Vocabulary</p>

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
				Identity: org.eclipse.lyo.adapter.Simulink
			</p>
		</div>
		<br>

		<!-- resource type and name -->
		<h1><span id="metainfo">Simulink RDF Vocabulary </span></h1>
		<br>

		<div> 
		
		
		
		<xmp style="padding-left: 25px;">
<?xml version="1.0" encoding="UTF-8"?>
<rdf:RDF
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
	xmlns:dcterms="http://purl.org/dc/terms/"
	xmlns:simulink="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#">
	<rdfs:Class rdf:about="simulink:WorkingDirectory">
		<rdfs:label xml:lang="en-GB">WorkingDirectory</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdfs:Class>
	<rdf:Property rdf:about="simulink:WorkingDirectory_model">
		<rdfs:label xml:lang="en-GB">model</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdfs:Class rdf:about="simulink:Model">
		<rdfs:label xml:lang="en-GB">Model</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
		<rdfs:subClassOf rdf:resource="simulink:null"/>
	</rdfs:Class>
	<rdf:Property rdf:about="simulink:Model_name">
		<rdfs:label xml:lang="en-GB">name</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdf:Property rdf:about="simulink:Model_block">
		<rdfs:label xml:lang="en-GB">block</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdf:Property rdf:about="simulink:Model_line">
		<rdfs:label xml:lang="en-GB">line</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdfs:Class rdf:about="simulink:Block">
		<rdfs:label xml:lang="en-GB">Block</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
		<rdfs:subClassOf rdf:resource="simulink:null"/>
	</rdfs:Class>
	<rdf:Property rdf:about="simulink:Block_name">
		<rdfs:label xml:lang="en-GB">name</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdf:Property rdf:about="simulink:Block_type">
		<rdfs:label xml:lang="en-GB">type</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdf:Property rdf:about="simulink:Block_inputPort">
		<rdfs:label xml:lang="en-GB">inputPort</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdf:Property rdf:about="simulink:Block_outputPort">
		<rdfs:label xml:lang="en-GB">outputPort</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdf:Property rdf:about="simulink:Block_parameter">
		<rdfs:label xml:lang="en-GB">parameter</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdfs:Class rdf:about="simulink:Line">
		<rdfs:label xml:lang="en-GB">Line</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
		<rdfs:subClassOf rdf:resource="simulink:null"/>
	</rdfs:Class>
	<rdf:Property rdf:about="simulink:Line_sourcePort">
		<rdfs:label xml:lang="en-GB">sourcePort</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdf:Property rdf:about="simulink:Line_targetPort">
		<rdfs:label xml:lang="en-GB">targetPort</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdfs:Class rdf:about="simulink:Port">
		<rdfs:label xml:lang="en-GB">Port</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
		<rdfs:subClassOf rdf:resource="simulink:null"/>
	</rdfs:Class>
	<rdf:Property rdf:about="simulink:Port_id">
		<rdfs:label xml:lang="en-GB">id</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdfs:Class rdf:about="simulink:InputPort">
		<rdfs:label xml:lang="en-GB">InputPort</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
		<rdfs:subClassOf rdf:resource="simulink:Port"/>
	</rdfs:Class>
	<rdf:Property rdf:about="simulink:Port_id">
		<rdfs:label xml:lang="en-GB">id</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdfs:Class rdf:about="simulink:OutputPort">
		<rdfs:label xml:lang="en-GB">OutputPort</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
		<rdfs:subClassOf rdf:resource="simulink:Port"/>
	</rdfs:Class>
	<rdf:Property rdf:about="simulink:Port_id">
		<rdfs:label xml:lang="en-GB">id</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdfs:Class rdf:about="simulink:Parameter">
		<rdfs:label xml:lang="en-GB">Parameter</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
		<rdfs:subClassOf rdf:resource="simulink:null"/>
	</rdfs:Class>
	<rdf:Property rdf:about="simulink:Parameter_name">
		<rdfs:label xml:lang="en-GB">name</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
	<rdf:Property rdf:about="simulink:Parameter_value">
		<rdfs:label xml:lang="en-GB">value</rdfs:label>
		<rdfs:isDefinedBy rdf:resource="http://localhost:8181/oslc4jsimulink/services/rdfvocabulary#"/>
		<dcterms:issued>2014-01-05</dcterms:issued>
	</rdf:Property>
</rdf:RDF>
</xmp>
		
		
	</div>


	<!-- footer -->
	<p id="footer">OSLC Simulink Adapter 1.1</p>
	 
</body>
</html>







