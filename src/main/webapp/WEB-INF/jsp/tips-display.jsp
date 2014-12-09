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

<link type="text/css" rel="stylesheet" href="<c:url value="/css/tip.css"/>"/>

<div id="portalTip">
    <div class="portal-tip-inner">
        <p id="tip-p"><spring:message code="tips.prefix"/><c:out value="${displayedTip}"/></p>
        <a id="tip-nextTip" href="#" alt="Next tip"><span><spring:message code="tips.next"/></span></a>
    </div>
</div>

<script type="text/javascript">
    window.addEventListener('load', function() {
        var displayedTipIndex = parseInt("${displayedTipIndex}");
        var displayedTip = "${displayedTip}";

        var allTips = [];
        <c:forEach var="tip" items="${allTips}">
                allTips.push("${fn:replace(tip, '"', '\\"')}");
        </c:forEach>

        var a = document.getElementById("tip-nextTip");
        var p = document.getElementById("tip-p");

        a.onclick = function() {
            displayedTipIndex = displayedTipIndex + 1;

            if(displayedTipIndex == allTips.length)
                displayedTipIndex = 0;

            displayedTip = allTips[displayedTipIndex];
            tipPrefix = '<spring:message code="tips.prefix"/>';
            p.innerHTML = tipPrefix+(displayedTipIndex+1)+": "+displayedTip;
        }
    }, false);
</script>
