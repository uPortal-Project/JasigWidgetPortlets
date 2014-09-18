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

<c:set var="linkHref">
    <c:choose>
        <c:when test="${appDefinition.displayStrategy == 'newWindow'}">${appUrl}</c:when>
        <c:otherwise><portlet:renderURL windowState="DETACHED"/></c:otherwise>
    </c:choose>
</c:set>
<c:set var="targetAttribute">
    <c:choose>
        <c:when test="${appDefinition.linkTarget ne null}">target=\"${appDefinition.linkTarget}\"</c:when>
        <c:otherwise></c:otherwise>
    </c:choose>
</c:set>
<c:set var="iconUrl">
    <c:choose>
        <c:when test="${appDefinition.iconUrl ne null}">${appDefinition.iconUrl}</c:when>
        <c:otherwise><c:url value="/images/default-icon.png"/></c:otherwise>
    </c:choose>
</c:set>

<style>
#${n}app .app-component {
    display: block;
    width: 100%;
}
#${n}app .app-link {
    text-decoration: none;
}
#${n}app .app-icon-wrapper {
    text-align: center;
}
#${n}app .app-icon {
    display: inline-block;
    width: <c:out value="${iconSizePixels}"/>px;
    height: <c:out value="${iconSizePixels}"/>px;
    background: url('${iconUrl}') center no-repeat;
    background-size: contain;
}
#${n}app .app-title {
    color: #fff;
    font: 1.4em bold;
    margin-bottom: 3px;
}
#${n}app .app-subtitle {
    color: #eee;
}
.app-launcher-item {
    height: 300px;
    max-height: 300px;
    width: 100%;
    position: relative;
}
.app-icon-text {
    background-color:rgba(50, 50, 50, 0.45);
    padding: 15px;
    width: 100%;
    position: absolute;
    bottom: 0;
}
</style>

<div id="${n}app" class="app-launcher-item">
    <a class="app-link" href="<c:out value="${linkHref}"/>"<c:out value="${targetAttribute}"/> title="<c:out value="${appDefinition.linkTitle}"/>">
        <span class="app-component app-icon-wrapper">
            <span class="app-icon"></span>
        </span>
        <div class="app-icon-text">
            <span class="app-component app-title"><c:out value="${appDefinition.title}"/></span>
            <span class="app-component app-subtitle"><c:out value="${appDefinition.subtitle}"/></span>
        </div>
    </a>
</div>
