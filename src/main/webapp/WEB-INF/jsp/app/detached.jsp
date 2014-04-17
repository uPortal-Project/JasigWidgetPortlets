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

<style>
#${n}frame {
    width: 100%;
    height: 1200px;
}
</style>

<iframe id="${n}frame" src="<c:out value="${appDefinition.appUrl}"/>">
    This browser does not support inline frames.<br/> 
    <a href="<c:out value="${appDefinition.appUrl}"/>" target="_blank">Click here to view content</a> in a separate window.
</iframe>
