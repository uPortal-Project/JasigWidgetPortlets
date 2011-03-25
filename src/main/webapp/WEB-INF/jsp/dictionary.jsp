<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.

--%>

<%@ page contentType="text/html" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server" %>
<c:set var="n"><portlet:namespace/></c:set>
<c:url var="url" value="/ajax/dictionary"/>

<script src="<rs:resourceURL value="/rs/jquery/1.4.2/jquery-1.4.2.min.js"/>" type="text/javascript"></script>
<script src="<rs:resourceURL value="/rs/jqueryui/1.8/jquery-ui-1.8.min.js"/>" type="text/javascript"></script>
<script type="text/javascript"><rs:compressJs>
    var ${n} = {};
    ${n}.jQuery = jQuery.noConflict(true);
    ${n}.jQuery(function(){

	    var ${n}searchDictionary = function(form) {
	         var $ = ${n}.jQuery;
	         $("#${n}defs").html("");
	         $.get('${url}', {word: $(form.word).val(), dictId: $(form.dict).val()}, function(json){
		         $(form).parent().find("div.defContainer")
		             .append($(document.createElement("div")).html(json.definition));
	            }, "json");
	         return false;
	    };

        ${n}.jQuery("#${n}tabs").ready(function(){
            ${n}.jQuery("#${n}tabs").tabs();
            ${n}.jQuery("#${n}dictionaryTab > form").submit(function(){ return ${n}searchDictionary(this); });
            ${n}.jQuery("#${n}thesaurusTab > form").submit(function(){ return ${n}searchDictionary(this); });
        });
        
    });

</rs:compressJs></script>

<div id="${n}tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all search-container">
    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
        <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
            <a shape="rect" href="#${n}dictionaryTab">Dictionary</a>
        </li>
        <li class="ui-state-default ui-corner-top">
            <a shape="rect" href="#${n}thesaurusTab">Thesaurus</a>
        </li>
    </ul>
    
    <div id="${n}dictionaryTab" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
		<form>
		    <input type="hidden" name="dict" value="wn"/>
		    <input class="portlet-form-input-field" name="word"/> 
		    <input class="portlet-form-button" type="submit" value="Go!"/>
		</form>
		<div class="defContainer"></div>
	</div>

	<div id="${n}thesaurusTab" class="ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide">
        <form>
            <input type="hidden" name="dict" value="moby-thes"/> 
            <input class="portlet-form-input-field" name="word"/>
            <input class="portlet-form-button" type="submit" value="Go!"/>
        </form>
        <div class="defContainer"></div>
	</div>

</div>
	
