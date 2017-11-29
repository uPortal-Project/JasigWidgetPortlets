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
<script src="<rs:resourceURL value="/rs/lodash/4.17.4/lodash.min.js"/>"></script>
<script src="<rs:resourceURL value="/rs/template/1.0.0/template.js"/>"></script>
<rs:aggregatedResources path="${ usePortalJsLibs ? '/app-skin-shared.xml' : '/app-skin.xml' }"/>
<c:set var="n"><portlet:namespace/></c:set>

<template id="${n}_link_item">
    <li class="list-group-item list-group-item-action" draggable="true">
        <i class="fa fa-pull-left fa-fw fa-3x" aria-hidden="true"></i>
        <img class="fa-3x fa-fw fa-pull-left" src="" style="display:none;">
        <a class='fa-pull-right close-btn'><i class="fa fa-pull-right fa-close text-danger" aria-hidden="true"></i></a>
        <dl>
            <dt class="title"></dt>
            <dd class="description"></dd>
            <dd class="url"></dd>
            <dd class="groups"></dd>
        </dl>
    </li>
</template>

<script type="text/javascript">
    var ${n} = {}; // create a unique variable for our JS namespace

    (function($) {
        const FA_LIST_CLASSES = "fa fa-fw fa-3x fa-pull-left ";
        const FA_FORM_CLASSES = "fa fa-fw fa-lg ";
        const DEFAULT_LINK = {title: "Important Site", description: "A few words about this link", icon: "fa-link",
                            url: "https://www.yourapp.edu/", groups: []};
        var drag_source;

        var cloneDefaultLink = function() {
            var newlink = {};
            for (var i in DEFAULT_LINK)
                newlink[i] = DEFAULT_LINK[i];
            newlink.groups = DEFAULT_LINK.groups.slice(0);
            return newlink;
        }

        var createLinkListItem = function(link) {
            var template = document.getElementById('${n}_link_item');
            var item = document.importNode(template.content, true);
            if (link.icon.startsWith("fa-")) {
                item.querySelector('i').classList.add(link.icon);
                item.querySelector('i').style.display = "inline";
                item.querySelector('img').style.display = "none";
            } else {
                item.querySelector('i').style.display = "none";
                item.querySelector('img').style.display = "inline";
                item.querySelector('img').src = link.icon;
            }
            var dl = item.querySelector('dl');
            dl.querySelector('dt.title').textContent = link.title;
            dl.querySelector('dd.description').textContent = link.description;
            dl.querySelector('dd.url').textContent = link.url;
            dl.querySelector('dd.groups').textContent = link.groups.length === 0 ? "Everyone" : link.groups;
            return item;
        }

        var setupLi = function(li, link) {
            // post appendChild work
            li.link = link;
            li.addEventListener('click', selectItem);
            li.addEventListener('dragstart', dragstart);
            li.addEventListener('dragover', dragover);
            li.addEventListener('drop', dropped);
            li.querySelector('.close-btn').addEventListener('click', removeLink);
        }

        var addItem = function(e) {
            var app = document.getElementById('${n}config');
            var link = cloneDefaultLink();
            app.querySelector('ul').appendChild(createLinkListItem(link));
            var last;
            _.forEach(document.getElementById('${n}config').querySelectorAll("li"), function(li) {
                li.classList.remove('active');
                last = li;
            });
            last.classList.add('active');
            setupLi(last, link);
            populateEditForm(last);
        }

        var parentLi = function(target) {
            var li = target;
            while (!li.matches('li')) {
                li = li.parentNode;
            }
            return li;
        }

        var selectItem = function(e) {
            _.forEach(document.getElementById('${n}config').querySelectorAll('li'), function(li) {
                li.classList.remove('active');
            });
            var li = parentLi(e.target);
            li.classList.add('active');
            populateEditForm(li);
        }

        var populateEditForm = function(li) {
            var form = document.getElementById('${n}_edit_form');
            form.li = li;
            var link = li.link;
            form.querySelector('input#title').value = link.title;
            form.querySelector('input#description').value = link.description;
            if (link.icon.startsWith('fa-')) {
                form.querySelector('div.icon i').className = FA_FORM_CLASSES + link.icon;
                form.querySelector('div.icon i').style.display = 'inline';
                form.querySelector('div.icon img').style.display = 'none';
            } else {
                form.querySelector('div.icon i').style.display = 'inline';
                form.querySelector('div.icon img').style.display = 'none';
                form.querySelector('div.icon img').src = link.icon;
            }
            form.querySelector('input#icon').value = link.icon;
            form.querySelector('input#url').value = link.url;
            var checked = form.querySelectorAll(".groups input[type='checkbox']");
            Array.from(checked).map(function(e) {
                    e.checked = (link.groups.indexOf(e.name) !== -1);
            });
        }

        var initEditForm = function() {
            // setup listeners
            var form = document.getElementById('${n}_edit_form');
            form.querySelector('input#title').addEventListener("input", updateTitle);
            form.querySelector('input#description').addEventListener("input", updateDescription);
            form.querySelector('input#icon').addEventListener("input", updateIcon);
            form.querySelector('input#url').addEventListener("input", updateUrl);
            var checked = form.querySelectorAll(".groups input[type='checkbox']");
            Array.from(checked).map(function(e) {
                    e.addEventListener("change", updateGroups);
            });
        }

        var updateTitle = function(e) {
            var form = document.getElementById('${n}_edit_form');
            form.li.link.title = e.target.value;
            form.li.querySelector('dt.title').textContent = e.target.value;
        }

        var updateDescription = function(e) {
            var form = document.getElementById('${n}_edit_form');
            form.li.link.description = e.target.value;
            form.li.querySelector('dd.description').textContent = e.target.value;
        }

        var updateIcon = function(e) {
            var form = document.getElementById('${n}_edit_form');
            var icon = e.target.value;
            form.li.link.icon = icon;
            if (icon.startsWith('fa-')) {
                form.li.querySelector('i').style.display = 'inline';
                form.querySelector('div.icon i').style.display = 'inline';
                form.li.querySelector('img').style.display = 'none';
                form.querySelector('div.icon img').style.display = 'none';
                form.li.querySelector('i').className = FA_LIST_CLASSES + icon;
                form.querySelector('div.icon i').className = FA_FORM_CLASSES + icon;
            } else {
                form.li.querySelector('img').style.display = 'inline';
                form.querySelector('div.icon img').style.display = 'inline';
                form.li.querySelector('i').style.display = 'none';
                form.querySelector('div.icon i').style.display = 'none';
                form.li.querySelector('img').src = icon;
                form.querySelector('div.icon img').src = icon;
            }
        }

        var updateUrl = function(e) {
            var form = document.getElementById('${n}_edit_form');
            form.li.link.url = e.target.value;
            form.li.querySelector('dd.url').textContent = e.target.value;
        }

        var updateGroups = function(e) {
            var form = document.getElementById('${n}_edit_form');
            var checked = form.querySelectorAll(".groups input[type='checkbox']:checked");
            var groups = Array.from(checked).map(function(e) { return e.name; });
            form.li.link.groups = groups;
            if (groups.length === 0) groups = ['Everyone'];
            form.li.querySelector('dd.groups').textContent = groups;
        }

        var dragstart = function(e) {
            source = e.target;
            e.dataTransfer.setData("text/plain", e.target.innerHTML);
            e.dataTransfer.effectAllowed = "move";
        }

        var dragover = function(e) {
            e.preventDefault();
            e.dataTransfer.dropEffect = "move";
        }

        var dropped = function(e) {
            e.preventDefault();
            e.stopPropagation();
            var li = parentLi(e.target);
            var liLink = li.link;
            var srcLink = source.link;
            source.innerHTML = li.innerHTML;
            source.link = liLink;
            li.innerHTML = e.dataTransfer.getData("text/plain");
            li.link = srcLink;

            var app = document.getElementById('${n}config');
            populateEditForm(app.querySelector('li.active'));
        }

        var removeLink = function(e) {
            var li = parentLi(e.target);
            li.parentNode.removeChild(li);

            var app = document.getElementById('${n}config');
            if (!app.querySelector('li.active')) {
                populateEditForm(app.querySelector('li'));
                app.querySelector('li').classList.add('active');
            }
        }

        var beforeSave = function() {
            var app = document.getElementById('${n}config');
            var links = [];
            _.forEach(app.querySelectorAll('li'), function(item) {
                links.push(item.link);
            });
            app.querySelector('input[name="links"]').value = JSON.stringify(links);
        }

        $(document).ready(function() {
            ${n}.links = JSON.parse('${linksJson}');

            if (${n}.links.length === 0) {
                ${n}.links.push(cloneDefaultLink());
            }

            var app = document.getElementById('${n}config');
            _.forEach(${n}.links, function(link) {
                var item = app.querySelector('ul').appendChild(createLinkListItem(link));
            });
            _.forEach(app.querySelectorAll('li'), function(item, i) {
                // have to iterate the list items b/c the returned new child cannot be updated
                setupLi(item, ${n}.links[i]);
            });

            initEditForm();
            populateEditForm(app.querySelector('li'));
            app.querySelector('li').classList.add('active');

            app.querySelector('input[name="new"]').addEventListener('click', addItem);
            app.querySelector('input[name="save"]').addEventListener('click', beforeSave);
        });

        $(function() {
            if (typeof upAttachments != "undefined") {
                var setAttachment = function(attachment) {
                    $('#${n}config input#icon').val(attachment.path);
                };
                ${n}.addAttachmentCallback = function(result) {
                    setAttachment(result);
                    upAttachments.hide();
                };
                $('#${n}config #upload').show();
            }
         });

    })(up.jQuery);
</script>

<style>
#${n}config .field-error {
    display: none;
    padding: 3px;
    margin: 0 5px;
}

#${n}config div.new-button {
    padding-bottom: 5px;
}
</style>

<div id="${n}config">

    <div class="new-button text-center">
        <input type="button" class="button btn-sm btn-secondary" name="new" value="New Resource Link"/>
    </div>

    <ul class="list-group">
    </ul>

    <form id="${n}_edit_form" role="form" class="form-horizontal" method="POST">
        <div class="form-group title">
            <label for="title" class="col-sm-2 control-label"><spring:message code="resource-links.title"/></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="title" id="title" value="${item.title}" placeholder="Your Awesome App">
                <div class="field-error bg-danger"><spring:message code="resource-links.title.invalid"/></div>
            </div>
        </div>
        <div class="form-group description">
            <label for="description" class="col-sm-2 control-label"><spring:message code="resource-links.description"/></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="description" id="description" value="${item.description}" placeholder="A few words about your app">
                <div class="field-error bg-danger"><spring:message code="resource-links.description.invalid"/></div>
            </div>
        </div>
        <div class="form-group icon">
            <label for="icon" class="col-sm-2 control-label">
                <i class="fa fa-fw fa-lg" aria-hidden="true"></i>
                <img class="fa-lg fa-fw" src="" style="display:none;">
            </label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="icon" id="icon" value="${item.icon}" placeholder="fa-link">
                <div class="field-error bg-danger"><spring:message code="resource-links.icon.invalid"/></div>
                <a id="upload" class="btn btn-link" style="display: none;" href="javascript:upAttachments.show(${n}.addAttachmentCallback);">Upload</a>
                <a href="http://fontawesome.io/icons/" title="Font Awesome Icons" target="_blank">Font Awesome Icons</a>
            </div>
        </div>
        <div class="form-group url">
            <label for="url" class="col-sm-2 control-label"><spring:message code="resource-links.url"/></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="url" id="url" value="${item.url}" placeholder="https://www.yourapp.edu/">
                <div class="field-error bg-danger"><spring:message code="resource-links.url.invalid"/></div>
            </div>
        </div>
        <div class="form-group groups">
            <label for="groups" class="col-sm-2 control-label"><spring:message code="resource-links.groups"/></label>
            <div class="col-sm-10 form-check">
                <c:forEach var="group" items="${groups}">
                <label class="checkbox-inline">
                    <input type="checkbox" name="${group}" id="${group}"> ${group}
                </label>
                </c:forEach>
            </div>
        </div>
    </form>
    <form role="form" class="form-horizontal" method="POST" action="<portlet:actionURL/>">
        <div class="buttons text-right">
            <input type="hidden" name="links" value=""/>
            <input type="submit" class="button primary btn btn-primary" name="save" value="Save"/>
            <input type="submit" class="button secondary btn btn-link" name="cancel" value="Cancel"/>
        </div>
    </form>
</div>
