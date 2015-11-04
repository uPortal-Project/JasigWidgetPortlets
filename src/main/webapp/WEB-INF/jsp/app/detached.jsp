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

<c:set var="n"><portlet:namespace/></c:set>

<script type="text/javascript">

    // Decided to simply use the portal's jQuery since this is all basic jQuery and for detached view
    // it is pretty safe since there is no other content on the page and limited dependencies.
    widgets_detached_size_frame = function () {
        $ = up.jQuery;
        // For IE it helps to subtract 5 to avoid a double vertical scrollbar
        $('#${n}iframeContainer').height($(window).height() - $('#${n}iframeContainer').offset().top - 5);
    };
    up.jQuery(window).resize(widgets_detached_size_frame);
    up.jQuery(widgets_detached_size_frame);

</script>

<div id="${n}iframeContainer">
<iframe id="${n}frame" src="<c:out value="${appUrl}"/>" style="width:100%; height:100%;">
    This browser does not support inline frames.<br/> 
    <a href="<c:out value="${appUrl}"/>" target="_blank">Click here to view content</a> in a separate window.
</iframe>
</div>