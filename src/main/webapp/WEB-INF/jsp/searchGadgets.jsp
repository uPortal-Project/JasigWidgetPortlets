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

<portlet:renderURL var="gadgetUrl">
    <portlet:param name="action" value="configure"/>
    <portlet:param name="gadgetUrl" value="GADGETURL"/>
</portlet:renderURL>

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

    <div id="${n}gadgetPager">
        <nav class="d-flex align-items-center gap-3 mb-2">
            <ul id="${n}pager-top" class="pagination mb-0">
                <li class="page-item pager-previous"><a class="page-link" href="#">prev</a></li>
                <li class="page-item pager-next"><a class="page-link" href="#">next</a></li>
            </ul>
            <span class="pager-summary text-muted small"></span>
            <div class="d-flex align-items-center gap-1 small">
                <span>show</span>
                <select class="form-select form-select-sm pager-page-size" style="width:auto">
                    <option value="8">8</option>
                    <option value="16">16</option>
                    <option value="24">24</option>
                </select>
                <span>per page</span>
            </div>
        </nav>
        <ul class="gadget-listings"></ul>
    </div>
</div>

<script type="text/javascript">
'use strict';

(function () {
    var $ = (typeof up !== 'undefined' && up.jQuery) ? up.jQuery : jQuery;
    var gadgetUrl = '${gadgetUrl}'.replace(/&amp;/g, '&');
    var container = document.getElementById('${n}gadgetPager');
    var listEl = container.querySelector('.gadget-listings');
    var pageSizeSelect = container.querySelector('.pager-page-size');
    var prevBtn = container.querySelector('.pager-previous a');
    var nextBtn = container.querySelector('.pager-next a');
    var summaryEl = container.querySelector('.pager-summary');

    var gadgets = [];
    var currentPage = 0;
    var pageSize = parseInt(pageSizeSelect.value, 10);

    function renderPage() {
        var start = currentPage * pageSize;
        var end = Math.min(start + pageSize, gadgets.length);
        var totalPages = Math.ceil(gadgets.length / pageSize);

        listEl.innerHTML = '';
        gadgets.slice(start, end).forEach(function (gadget) {
            var li = document.createElement('li');
            var url = gadgetUrl.replace('GADGETURL', encodeURIComponent(gadget.configUrl || ''));
            li.innerHTML =
                '<h3><a href="' + url + '">' + gadget.name + '</a></h3>' +
                (gadget.imageUrl ? '<img src="' + gadget.imageUrl + '" alt=""/>' : '');
            listEl.appendChild(li);
        });

        summaryEl.textContent = gadgets.length > 0 ? (start + 1) + ' - ' + end + ' / ' + gadgets.length : '';
        prevBtn.parentElement.classList.toggle('disabled', currentPage === 0);
        nextBtn.parentElement.classList.toggle('disabled', currentPage >= totalPages - 1);
    }

    prevBtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (currentPage > 0) { currentPage--; renderPage(); }
    });

    nextBtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (currentPage < Math.ceil(gadgets.length / pageSize) - 1) { currentPage++; renderPage(); }
    });

    pageSizeSelect.addEventListener('change', function () {
        pageSize = parseInt(this.value, 10);
        currentPage = 0;
        renderPage();
    });

    $.ajax({
        url: '<c:url value="/ajax/gadgets"/>',
        dataType: 'json',
        success: function (data) {
            gadgets = data.gadgets || [];
            renderPage();
        }
    });
}());
</script>
