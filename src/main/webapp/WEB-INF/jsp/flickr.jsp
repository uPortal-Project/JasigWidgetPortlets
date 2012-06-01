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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server" %>
<portlet:defineObjects/>
<c:set var="n"><portlet:namespace/></c:set>

<style type="text/css">
    .image-grid ul {
        list-style: none;
        margin: 0;
        padding: 0;
    }
    
    .image-grid li {
        display: block;
        float: left;
        margin: 0px 3px 3px 3px;
        padding: 0;
    }
        
    /* iphone */
    @media only screen and (max-device-width: 480px) {
        .image-grid img {
            width: 70px;
            height: 70px;
        }
    }
</style>

<script type="text/javascript">
up.jQuery(function() {
    var $ = up.jQuery;

    $(document).ready(function() { 
        
        $("#${n} .images-back-div a").click(function () {
            $("#${n} .focused-image").hide();
            $("#${n} .images-back-div").hide();
            $("#${n} ul").show();
        });
        
        $("#${n} li img").click(function () {
            $("#${n} ul").hide();
            $("#${n} .focused-image").attr("src", $(this).attr("src").replace("s.jpg", "m.jpg")).show();
            $("#${n} .images-back-div").show();
        });
    });
});
</script>

<div id="${n}" class="portlet">
    <div data-role="header" class="titlebar portlet-titlebar images-back-div" style="display:none">
        <a data-role="button"  data-icon="back" data-inline="true" href="javascript:;">Back</a>
        <h2>Detail</h2>
    </div>
    
    <div data-role="content" class="portlet-content">

        <ul class="image-grid">
            <c:forEach items="${ images }" var="image">
                <li><img src="${ image }"/></li>
            </c:forEach>
        </ul>
   
        <img class="focused-image" style="display:none"/>

    </div>
</div>