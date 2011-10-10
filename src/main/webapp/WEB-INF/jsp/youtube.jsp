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

<script src="<rs:resourceURL value="/rs/jquery/1.6.1/jquery-1.6.1.js"/>" type="text/javascript"></script>
<script src="<rs:resourceURL value="/rs/jqueryui/1.8.13/jquery-ui-1.8.13.js"/>" type="text/javascript"></script>
<script src="/ResourceServingWebapp/rs/fluid/1.4-00b5b5e/js/fluid-all-1.4-00b5b5e.js" type="text/javascript"></script>

<div id="${n}" class="portlet">
<div class="fl-pager">
    <div class="view-pager flc-pager-top portlet-section-options">
        <ul id="pager-top" class="fl-pager-ui">
          <li class="flc-pager-previous"><a href="#">&lt; <spring:message code="previous"/></a></li>
          <li style="display:none">
            <ul class="fl-pager-links flc-pager-links" style="margin:0; display:inline">
              <li class="flc-pager-pageLink"><a href="javascript:;">1</a></li>
              <li class="flc-pager-pageLink-disabled">2</li>
              <li class="flc-pager-pageLink"><a href="javascript:;">3</a></li>
            </ul>
          </li>
          <li class="flc-pager-next"><a href="#"><spring:message code="next"/> &gt;</a></li>
          <li style="display:none">
            <span class="flc-pager-summary"><spring:message code="show"/></span>
            <span> <select class="pager-page-size flc-pager-page-size">
            <option value="5">5</option>
            <option value="10">10</option>
            <option value="20">20</option>
            <option value="50">50</option>
            <option value="100">100</option>
            </select></span> <spring:message code="per.page"/>
          </li>
        </ul>
    </div><!-- end: portlet-section-options -->
    
    <div class="videos">
        <div class="video">
            <h3><a href="javascript:;" class="video-title"></a></h3>
            <img class="img"/>
            <p class="description"></p>
        </div>
    </div>
    </div>
</div>

<script type="text/javascript"><rs:compressJs>
    var ${n} = ${n} || {};
    ${n}.jQuery = jQuery.noConflict(true)
    ${n}.fluid = fluid;
    fluid = null;
    fluid_1_4 = null;
    ${n}.jQuery(document).ready(function(){
        var $ = ${n}.jQuery;
        var fluid = ${n}.fluid;
        $.get('<c:url value="/ajax/youtube"><c:param name="user" value="${usernames[0]}"/></c:url>', {},
            function (data) {
                var tree = { children: [] };
                var cutpoints = [
                    { id: "video:", selector: ".video" },
                    { id: "title", selector: ".video-title" },
                    { id: "description", selector: ".description" },
                    { id: "image", selector: ".img" }
                ];
                
                $(data.data.items).each(function (idx, item){
                    tree.children.push({
                        ID: "video:",
                        children: [
                            { ID: "title", target: item.player["default"], linktext: item.title },
                            { ID: "description", value: item.description },
                            { ID: "image", 
                                decorators: [
                                     { type: "attrs", attributes: { src: item.thumbnail.hqDefault } }
                                 ] 
                            }
                        ]
                    });
                });
                
                var columnDefs = [
                    {
                        key: "title",
                        valuebinding: "*.title",
                        components: {
                            target: '${"${*.player.default}"}',
                            linktext: '${"${*.title}"}'
                        }
                    },
                    {
                        key: "description",
                        valuebinding: "*.description"
                    },
                    {
                        key: "image",
                        valuebinding: "*.img",
                        components: function (row) {
                            return {
                                decorators: [{ type: "attrs", attributes: { src: row.thumbnail.hqDefault } }]
                            };
                        }
                    }
                ];
                                      
                var pagerOptions = {
                    dataModel: data.data.items,
                    annotateColumnRange: "title",
                    columnDefs: columnDefs,
                    bodyRenderer: {
                        type: "fluid.pager.selfRender",
                        options: {
                            selectors: {
                                root: ".videos"
                            },
                            row: "video:",
                            renderOptions: {
                                cutpoints: cutpoints
                            }
                        }
                        
                    },
                    pagerBar: {
                        type: "fluid.pager.pagerBar", 
                        options: {
                            pageList: {
                                type: "fluid.pager.renderedPageList",
                                options: { 
                                    linkBody: "a"
                                }
                            }
                        }
                    }
                };
                
                // initialize the pager and set it to 6 items per page.
                var pager = fluid.pager($("#${n}"), pagerOptions);
                pager.events.initiatePageSizeChange.fire(1);

            }, "json"
        );
    }); 
</rs:compressJs></script>
