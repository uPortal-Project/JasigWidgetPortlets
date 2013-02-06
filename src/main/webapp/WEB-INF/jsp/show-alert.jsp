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

<%-- DEPRECATED:  This portlet was moved to the Apereo NotificationPortlet project.  --%>

<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>

<c:set var="n"><portlet:namespace/></c:set>

<script src="<rs:resourceURL value="/rs/jquery/1.6.1/jquery-1.6.1.min.js"/>" type="text/javascript"></script>
<script src="<rs:resourceURL value="/rs/jqueryui/1.8.13/jquery-ui-1.8.13.min.js"/>" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/alert.css"/>"/>

<div id="${n}emergencyAlert" class="emergency-alert" style="display: none;">

	<c:forEach items="${feed}" var="alert" varStatus="status">
		<div class="portlet view-alert"<c:if test="${!status.first}"> style="display: none;"</c:if> role="section">
            
		    <!-- Portlet Titlebar -->
		    <div class="titlebar portlet-titlebar" role="sectionhead">
                <c:if test="${fn:length(feed) > 1}">
                    <ul class="alerts-pager">
                        <li><a title="Previous" href="javascript:void(0);" class="alerts-previous<c:if test="${status.first}"> disabled</c:if>">&#171;</a></li>
                        <li><a title="Next" href="javascript:void(0);" class="alerts-next<c:if test="${status.last}"> disabled</c:if>">&#187;</a></li>
                    </ul>
                </c:if>
		        <h2 class="title" role="heading"><c:out value="${alert.header}" escapeXml="false"/></h2>
		    </div>
		
		    <!-- Portlet Body -->
		    <div class="content portlet-content" role="main">
		        <p><c:out value="${alert.body}" escapeXml="false"/></p>
		        <c:out value="${alert.url}" escapeXml="false"/>                                     
		    </div>
		</div>
	</c:forEach>

</div>

<script type="text/javascript">

    var ${n} = {};
    ${n}.jQuery = jQuery.noConflict(true);

    ${n}.jQuery(function(){
        var $ = ${n}.jQuery;
        
        var intervalId = -1;

        var advance = function() {
            var outgoingAlert = $('#${n}emergencyAlert .view-alert').filter(':visible');
            var incomingAlert = outgoingAlert.next();
            if (incomingAlert.size() == 0) {
            	// Cycle to the beginning...
            	incomingAlert = $('#${n}emergencyAlert .view-alert').filter(':first');
            }
            outgoingAlert.toggle('slide', { direction: 'left' });
            incomingAlert.toggle('slide', { direction: 'right' });
        };
        $('#${n}emergencyAlert .alerts-next').filter(':not(.disabled)').click(function() {
        	advance();
        	window.clearInterval(intervalId);
        });
        
        var recede = function() {
            var outgoingAlert = $('#${n}emergencyAlert .view-alert').filter(':visible');
            var incomingAlert = outgoingAlert.prev();
            outgoingAlert.toggle('slide', { direction: 'right' });
            incomingAlert.toggle('slide', { direction: 'left' });
        };
        $('#${n}emergencyAlert .alerts-previous').filter(':not(.disabled)').click(function() {
        	recede();
            window.clearInterval(intervalId);
        });
        
        $('#${n}emergencyAlert').slideDown('slow');

        <c:if test="${autoAdvance && fn:length(feed) > 1}">intervalId = window.setInterval(advance, 10000);</c:if>
        
    });

</script>

