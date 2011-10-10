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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server" %>
<portlet:defineObjects/>
<c:set var="n"><portlet:namespace/></c:set>

<script src="<rs:resourceURL value="/rs/jquery/1.6.1/jquery-1.6.1.min.js"/>" type="text/javascript"></script>
<script src="<rs:resourceURL value="/rs/jqueryui/1.8.13/jquery-ui-1.8.13.min.js"/>" type="text/javascript"></script>
<script src="/ResourceServingWebapp/rs/fluid/1.4-bea0041/js/fluid-all-1.4-00b5b5e.min.js" type="text/javascript"></script>

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
    ${n}.jQuery = jQuery.noConflict(true)
    ${n}.fluid = fluid;
    fluid_1_4 = null;
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
