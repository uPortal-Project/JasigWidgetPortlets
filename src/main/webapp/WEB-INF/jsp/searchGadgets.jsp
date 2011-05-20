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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server" %>
<c:set var="n"><portlet:namespace/></c:set>
<portlet:renderURL var="gadgetUrl">
    <portlet:param name="action" value="configure"/>
    <portlet:param name="gadgetUrl" value="GADGETURL"/>
</portlet:renderURL>

<script type="text/javascript" src="<rs:resourceURL value="/rs/jquery/1.6.1/jquery-1.6.1.min.js"/>"></script>
<script type="text/javascript" src="<rs:resourceURL value="/rs/jqueryui/1.8.13/jquery-ui-1.8.13.min.js"/>"></script>
<script type="text/javascript" src="<rs:resourceURL value="/rs/fluid/1.4-bea0041/js/fluid-all-1.4-bea0041.min.js"/>"></script>

<style type="text/css">
ul.gadget-listings li {
    display: block;
    float: left;
    list-style: none outside none;
    padding: 10px;
    position: relative;
    width: 170px;
    text-align: center;
}
ul.gadget-listings img {
    max-width: 120px;
    max-height: 60px;
}
</style>

<div class="portlet">
<form method="post" action="${ searchUrl }">
    
    <p>
        Category 
        <select name="category">
            <c:forEach items="${ categories }" var="category">
                <option value="${ category.key }">${ category.displayName }</option>
            </c:forEach>
        </select>
        Search for <input name="query"/>
    </p>
    
    <div class="buttons">
        <input type="submit" value="Search"/>
    </div>
</form>

<div id="${n}gadgetPager" class="fl-pager">
    <div class="flc-pager-top view-pager">
        <ul id="pager-top" class="fl-pager-ui">
          <li class="flc-pager-previous"><a href="javascript:;">&lt; prev</a></li>
          <li>
            <ul class="fl-pager-links flc-pager-links" style="margin:0; display:inline">
              <li class="flc-pager-pageLink"><a href="javascript:;">1</a></li>
              <li class="flc-pager-pageLink-disabled">2</li>
              <li class="flc-pager-pageLink"><a href="javascript:;">3</a></li>
            </ul>
          </li>
          <li class="flc-pager-next"><a href="javascript:;">next &gt;</a></li>
          <li>
            <span class="flc-pager-summary">show</span>
            <span> <select class="pager-page-size flc-pager-page-size">
            <option value="8">8</option>
            <option value="16">16</option>
            <option value="24">24</option>
            </select></span> per page
          </li>
        </ul>
        <ul class="gadget-listings">
            <li rsf:id="gadget:">
                <h3><a rsf:id="link" href=""></a></h3>
                <img rsf:id="image" src=""/>
            </li>
        </ul>
    </div>
</div>
</div>

<script type="text/javascript"><rs:compressJs>
     var ${n} = {};
    ${n}.jQuery = jQuery.noConflict(true);
    ${n}.fluid = fluid;
    fluid = null;
    ${n}.jQuery(function(){
         var $ = ${n}.jQuery;
        var fluid = ${n}.fluid;
        var pager, gadgets;
        
        $(document).ready(function () {
            $.ajax({
                url: '<c:url value="/ajax/gadgets"/>',
                dataType: 'json',
                async:false,
                success: function (data, textStatus, jqXHR) {
                    gadgets = data.gadgets
                }
            });
            var options = {
                dataModel: gadgets,
                annotateColumnRange: "link",
                columnDefs: [
                    { 
                        key: "link",
                        valuebinding: "*.configureUrl",
                        components: {
                            target: '${ gadgetUrl }'.replace("GADGETURL", '${"${*.configUrl}"}'),
                            linktext: '${"${*.name}"}'
                        }
                    },
                    { 
                        key: "image",
                        valuebinding: "*.name",
                        components: function (row) {
                            return {
                                decorators: [
                                    { type: "attrs", attributes: { src: '${"${*.imageUrl}"}' } }
                                ]
                            }
                        }
                    }
                ],
                bodyRenderer: {
                    type: "fluid.pager.selfRender",
                    options: {
                        selectors: {
                            root: ".gadget-listings"
                        },
                        row: "gadget:",
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
            pager = fluid.pager($("#${n}gadgetPager"), options);
         });
    });
</rs:compressJs></script>
