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
<!--
<style>
    #${n}app ul {
        list-style-type: none;
        padding-left: 0;
    }
    #${n}app li:hover {
        background-color: #ddf;
    }
</style>
-->

<div id="${n}app">
    <ul class="list-group">
    <c:forEach items="${links}" var="item">
        <li class="list-group-item list-group-item-action">
        <a class="app-link"
           rel="noopener noreferrer"
           href='<c:out value="${item.url}"/>'
           target="_blank"
           title='<c:out value="${item.title}"/>'>
            <i class="fa fa-pull-left fa-fw fa-3x ${item.icon}" aria-hidden="true"></i>
            <dl>
                <dt><c:out value="${item.title}"/></dt>
                <dd><c:out value="${item.description}"/></dd>
            </dl>
        </a>
        </li>
    </c:forEach>
    </ul>
</div>
