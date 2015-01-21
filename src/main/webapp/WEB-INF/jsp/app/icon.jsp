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
<rs:aggregatedResources path="${ usePortalJsLibs ? '/app-skin-shared.xml' : '/app-skin.xml' }"/>
<c:set var="n"><portlet:namespace/></c:set>

<c:set var="linkHref">
    <c:choose>
        <%-- DETACHED Window State option is actually rendered within THIS portlet --%>
        <c:when test="${appDefinition.displayStrategy == 'iframe'}"><portlet:renderURL windowState="DETACHED"/></c:when>
        <c:otherwise>${appUrl}</c:otherwise>
    </c:choose>
</c:set>
<c:set var="targetValue">
    <c:choose>
        <c:when test="${appDefinition.linkTarget ne null}">${appDefinition.linkTarget}</c:when>
        <c:otherwise>_self</c:otherwise><%-- Standard default --%>
    </c:choose>
</c:set>
<c:set var="iconUrl">
    <c:choose>
        <c:when test="${appDefinition.iconUrl ne null}">${appDefinition.iconUrl}</c:when>
        <c:otherwise><c:url value="/images/default-icon.png"/></c:otherwise>
    </c:choose>
</c:set>

<style>
    /* These styles MUST be defined with the help of portlet preferences, so we do it here */
    #${n}app .app-icon-wrapper {
        max-width: <c:out value="${iconSizePixels}"/>px;
    }
    #${n}app .app-icon {
        background: url('${iconUrl}') center/contain no-repeat;
    }
</style>

<div id="${n}app" class="app-launcher-portlet">
    <div class="app-launcher-item icon-and-text">
        <a class="app-link" href="<c:out value="${linkHref}"/>" target="<c:out value="${targetValue}"/>" title="<c:out value="${appDefinition.linkTitle}"/>">
            <span class="app-component app-icon-wrapper">
                <span class="app-icon"></span>
            </span>
            <div class="app-icon-text">
                <span class="app-component app-title"><c:out value="${appDefinition.title}"/></span>
                <span class="app-component app-subtitle"><c:out value="${appDefinition.subtitle}"/></span>
            </div>
        </a>
    </div>
</div>
