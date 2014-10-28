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

<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>
<c:set var="n"><portlet:namespace/></c:set>
<c:set var="context" value="${pageContext.request.contextPath}"/>

<c:if test="${portletPreferencesValues['includeJsLibs'][0] != 'false'}">
    <rs:aggregatedResources path="/resources.xml"/>
</c:if>

<script type="text/javascript" src="https://apis.google.com/js/api.js?onload=onApiLoad"></script>
<script type="text/javascript" src="${context}/js/googleDrive.min.js"></script>
<script type="text/javascript" src="https://www.dropbox.com/static/api/1/dropins.js" id="dropboxjs"
        data-app-key="${dropboxAPIKey}"></script>
<script type="text/javascript" src="${context}/js/dropbox.min.js"></script>
<script type="text/javascript" src="${context}/js/onedrive.js"></script>

<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>

<link rel="stylesheet" href="<rs:resourceURL value='/rs/bootstrap-namespaced/3.1.1/css/bootstrap.min.css'/>"
      type="text/css"/>
<link rel="stylesheet" href='<c:url value="/css/googleDrive.css"/>' type="text/css"/>

<c:set var="n"><portlet:namespace/></c:set>
<c:set var="context" value="${pageContext.request.contextPath}"/>


<div id="${n}container" class="cloudDrive bootstrap-styles">
    <div class="container-fluid cloudDrive-options">
        <c:choose>
            <c:when test="${googleDriveEnabled}">
                <button id="googleDrive" class="btn btn-lg btn-default" onclick="onApiLoad(${googleApiKey})"
                        title="<spring:message code="cloudDrive-Google.tooltip"/>"/>
            </c:when>
        </c:choose>
        <c:choose>
            <c:when test="${dropboxEnabled}">
                <button id="dropbox" class="btn btn-lg btn-default" onclick="dropboxLoad()"
                        title="<spring:message code="cloudDrive-Dropbox.tooltip"/>"/>
            </c:when>
        </c:choose>
        <c:choose>
            <c:when test="${oneDriveEnabled}">
                <button id="onedrive" class="btn btn-lg btn-default" onclick="signInUser()"
                        title="<spring:message code="cloudDrive-OneDrive.tooltip"/>"/>
            </c:when>
        </c:choose>
    </div>
</div>



