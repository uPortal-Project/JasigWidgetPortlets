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
<%--
  -- Previously used jQuery Mobile, Fluid selfRender, and YouTube Data API v2.
  -- Replaced with a simple Bootstrap list view using YouTube Data API v3.
  -- Requires 'apiKey' portlet preference (YouTube Data API v3 key).
  --%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="n"><portlet:namespace/></c:set>

<div id="${n}" class="portlet">
    <ul class="list-group feed"></ul>
</div>

<script type="text/javascript">
'use strict';

(function () {
    var $ = (typeof up !== 'undefined' && up.jQuery) ? up.jQuery : jQuery;
    var listEl = document.querySelector('#${n} .feed');

    $.getJSON('<c:url value="/ajax/youtube"><c:param name="user" value="${usernames[0]}"/><c:param name="apiKey" value="${apiKey}"/></c:url>',
        function (data) {
            var videos = data.videos || [];
            videos.forEach(function (video) {
                var li = document.createElement('li');
                li.className = 'list-group-item';
                li.innerHTML =
                    '<div class="d-flex gap-2">' +
                    (video.thumbnail ? '<img src="' + video.thumbnail + '" style="width:80px;height:auto" alt=""/>' : '') +
                    '<div><a href="' + video.link + '" target="_blank"><strong>' + video.title + '</strong></a>' +
                    (video.description ? '<p class="mb-0 small">' + video.description + '</p>' : '') +
                    '</div></div>';
                listEl.appendChild(li);
            });
        }
    );
}());
</script>
