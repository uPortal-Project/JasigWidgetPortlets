<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>
<c:set var="n"><portlet:namespace/></c:set>

<portlet:actionURL var="formUrl"><portlet:param name="action" value="updateKey"/></portlet:actionURL>
<form method="POST" action="${ formUrl }">
    <h2>Edit Portlet Configuration</h2>
    
    <p>
        This portlet requires a Google API key matching the portal's hostname.
        You may request a key for your portal at <a 
        href="http://code.google.com/apis/ajaxsearch/signup.html" target="_blank">
        http://code.google.com/apis/ajaxsearch/signup.html</a>
    </p>
    
    <p>
        <label class="portlet-form-field-label" for="${n}googleApiKey">Google API key:</label>
        <input id="${n}googleApiKey" class="portlet-form-input-field" 
            name="googleApiKey" type="text" value="${ googleApiKey }" 
            style="width: 40em;"/>
    </p>
    
    <p>
        <input type="submit" class="portlet-form-button" value="Save"/>
    </p>
</form>