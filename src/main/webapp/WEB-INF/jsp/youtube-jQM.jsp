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
<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="n"><portlet:namespace/></c:set>

<c:if test="${portletPreferencesValues['includeJsLibs'][0] != 'false'}">
    <rs:aggregatedResources path="/resources.xml"/>
    <script src="<rs:resourceURL value="/rs/fluid/1.5.0/js/fluid-custom.min.js"/>" type="text/javascript"></script>
</c:if>

<div id="${n}" class="portlet">
    <div data-role="content" class="portlet-content">
        <ul data-role="listview" class="feed">
            <li class="video">
                <a href="javascript:;" class="video-link">
                    <img class="img"/>
                    <h3 class="video-title"></h3>
                    <p class="description"></p>
                </a>
            </li>
        </ul>
    </div>
</div>

<script type="text/javascript"><rs:compressJs>
    var ${n} = ${n} || {};
<c:choose>
    <c:when test="${portletPreferencesValues['includeJsLibs'][0] != 'false'}">
        ${n}.jQuery = jQuery.noConflict(true)
        ${n}.fluid = fluid;
        fluid = null;
        fluid_1_5 = null;
    </c:when>
    <c:otherwise>
        ${n}.jQuery = up.jQuery;
        ${n}.fluid = up.fluid;
    </c:otherwise>
</c:choose>
    ${n}.jQuery(document).ready(function(){
        var $ = ${n}.jQuery;
        var fluid = ${n}.fluid;
        $.get('<c:url value="/ajax/youtube"><c:param name="user" value="${usernames[0]}"/></c:url>', {},
            function (data) {
                var tree = { children: [] };
                var cutpoints = [
                    { id: "video:", selector: ".video" },
                    { id: "link", selector: ".video-link" },
                    { id: "title", selector: ".video-title" },
                    { id: "description", selector: ".description" },
                    { id: "image", selector: ".img" }
                ];
                
                $(data.data.items).each(function (idx, item){
                    tree.children.push({
                        ID: "video:",
                        children: [
                            { ID: "link", target: item.player["default"] },
                            { ID: "title", value: item.title },
                            { ID: "description", value: item.description },
                            { ID: "image", 
                                decorators: [
                                     { type: "attrs", attributes: { src: item.thumbnail.sqDefault } }
                                 ] 
                            }
                        ]
                    });
                });
                fluid.selfRender($("#${n}"), tree, { cutpoints: cutpoints });
            }, "json"
        );
    }); 
</rs:compressJs></script>
