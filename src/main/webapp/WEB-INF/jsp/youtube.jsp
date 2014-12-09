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
    <%-- Use jQuery 1.8.3 until https://issues.jasig.org/browse/WIDGPT-51 is fixed --%>
    <script src="<rs:resourceURL value="/rs/jquery/1.8.3/jquery-1.8.3.min.js"/>" type="text/javascript"></script>
    <script src="<rs:resourceURL value="/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js"/>" type="text/javascript"></script>
    <script src="<rs:resourceURL value="/rs/fluid/1.4.0/js/fluid-all-1.4.0.min.js"/>" type="text/javascript"></script>
</c:if>

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
<c:choose>
    <c:when test="${portletPreferencesValues['includeJsLibs'][0] != 'false'}">
        ${n}.jQuery = jQuery.noConflict(true)
        ${n}.fluid = fluid;
        fluid = null;
        fluid_1_4 = null; // Still using fluid 1.4 so don't set fluid_1_5
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
