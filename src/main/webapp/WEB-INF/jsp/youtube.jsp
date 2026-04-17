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
  -- Previously used Fluid pager, jQuery 1.8.3, jQuery UI 1.10.3, and Fluid 1.4.0
  -- loaded via rs:resourceURL (all now removed from resource-server).
  -- Also used YouTube Data API v2 (gdata.youtube.com) which was shut down in 2015.
  --
  -- Now uses YouTube Data API v3 via the server-side proxy (YouTubeService),
  -- vanilla JS Bootstrap pagination, and up.jQuery from the portal skin.
  --
  -- Requires a YouTube Data API v3 key configured as the 'apiKey' portlet preference.
  --%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="n"><portlet:namespace/></c:set>

<div id="${n}" class="container-fluid">
    <div class="d-flex align-items-center gap-3 mb-2">
        <nav>
            <ul class="pagination mb-0">
                <li class="page-item pager-previous"><a class="page-link" href="#"><spring:message code="previous"/></a></li>
                <li class="page-item pager-next"><a class="page-link" href="#"><spring:message code="next"/></a></li>
            </ul>
        </nav>
        <span class="pager-summary text-muted small"></span>
        <div class="d-flex align-items-center gap-1 small">
            <span><spring:message code="show"/></span>
            <select class="form-select form-select-sm pager-page-size" style="width:auto">
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="50">50</option>
                <option value="100">100</option>
            </select>
            <span><spring:message code="per.page"/></span>
        </div>
    </div>
    <div class="videos row"></div>
</div>

<script type="text/javascript">
'use strict';

(function () {
    var $ = (typeof up !== 'undefined' && up.jQuery) ? up.jQuery : jQuery;
    var container = document.getElementById('${n}');
    var videosEl = container.querySelector('.videos');
    var pageSizeSelect = container.querySelector('.pager-page-size');
    var prevBtn = container.querySelector('.pager-previous a');
    var nextBtn = container.querySelector('.pager-next a');
    var summaryEl = container.querySelector('.pager-summary');

    var videos = [];
    var currentPage = 0;
    var pageSize = parseInt(pageSizeSelect.value, 10);

    function renderPage() {
        var start = currentPage * pageSize;
        var end = Math.min(start + pageSize, videos.length);
        var totalPages = Math.ceil(videos.length / pageSize);

        videosEl.innerHTML = '';
        videos.slice(start, end).forEach(function (video) {
            var col = document.createElement('div');
            col.className = 'col-sm-6 col-md-4 mb-3';

            var card = document.createElement('div');
            card.className = 'card h-100';

            if (video.thumbnail && /^https?:\/\//i.test(video.thumbnail)) {
                var img = document.createElement('img');
                img.src = video.thumbnail;
                img.className = 'card-img-top';
                img.alt = '';
                card.appendChild(img);
            }

            var cardBody = document.createElement('div');
            cardBody.className = 'card-body';

            var h6 = document.createElement('h6');
            h6.className = 'card-title';
            var a = document.createElement('a');
            if (video.link && /^https?:\/\//i.test(video.link)) { a.href = video.link; }
            a.target = '_blank';
            a.rel = 'noopener noreferrer';
            a.textContent = video.title;
            h6.appendChild(a);
            cardBody.appendChild(h6);

            if (video.description) {
                var p = document.createElement('p');
                p.className = 'card-text small';
                p.textContent = video.description;
                cardBody.appendChild(p);
            }

            card.appendChild(cardBody);
            col.appendChild(card);
            videosEl.appendChild(col);
        });

        summaryEl.textContent = videos.length > 0 ? (start + 1) + ' - ' + end + ' / ' + videos.length : '';
        prevBtn.parentElement.classList.toggle('disabled', currentPage === 0);
        nextBtn.parentElement.classList.toggle('disabled', currentPage >= totalPages - 1);
    }

    prevBtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (currentPage > 0) { currentPage--; renderPage(); }
    });

    nextBtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (currentPage < Math.ceil(videos.length / pageSize) - 1) { currentPage++; renderPage(); }
    });

    pageSizeSelect.addEventListener('change', function () {
        pageSize = parseInt(this.value, 10);
        currentPage = 0;
        renderPage();
    });

    $.getJSON('<c:url value="/ajax/youtube"><c:param name="user" value="${usernames[0]}"/><c:param name="apiKey" value="${apiKey}"/></c:url>',
        function (data) {
            videos = data.videos || [];
            renderPage();
        }
    );
}());
</script>
