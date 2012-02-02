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

<script src="<rs:resourceURL value="/rs/jquery/1.6.1/jquery-1.6.1.min.js"/>" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/styles.css"/>"/>

<div id="emergencyAlert" class="portlet view-alert" style="display: none;" role="section">
    <!-- Portlet Titlebar -->
    <div class="titlebar portlet-titlebar" role="sectionhead">
        <h2 class="title" role="heading"><c:out value="${alertHead}"/></h2>
    </div>

    <!-- Portlet Body -->
    <div class="content portlet-content" role="main">
        <p><c:out value="${alertMsg}"/></p>
        <c:out value="${alertLink}" escapeXml="false"/>                                     
    </div>
</div>

<script type="text/javascript">

    var ${n} = {};
    ${n}.jQuery = jQuery.noConflict(true);

    ${n}.jQuery(function(){
        var $ = ${n}.jQuery;
        $("#emergencyAlert").slideDown("slow");
    });

</script>

