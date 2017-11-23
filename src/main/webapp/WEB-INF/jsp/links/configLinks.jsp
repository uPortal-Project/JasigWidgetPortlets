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
<rs:aggregatedResources path="${ usePortalJsLibs ? '/app-skin-shared.xml' : '/app-skin.xml' }"/>
<c:set var="n"><portlet:namespace/></c:set>


<script type="text/javascript">
    var ${n} = {}; // create a unique variable for our JS namespace
<c:choose>
    <c:when test="${portletPreferencesValues['usePortalJsLibs'][0] != 'true'}">
        ${n}.jQuery = jQuery.noConflict(true)
    </c:when>
    <c:otherwise>
        ${n}.jQuery = up.jQuery;
    </c:otherwise>
</c:choose>

    ${n}.jQuery(function () {
        var $ = ${n}.jQuery; //reassign $ for normal use of jQuery

        // Display and use the attachments feature only if it's present
        if(typeof upAttachments != "undefined") {
            var setAttachment = function(attachment) {
                $('#${n}config #iconUrl').val(attachment.path);
            };
            ${n}.addAttachmentCallback = function(result) {
                setAttachment(result);
                upAttachments.hide();
            };
            $('#${n}config #upload').show();
        }
    });
</script>

<style>
#${n}config .field-error {
    display: none;
    padding: 3px;
    margin: 0 5px;
}
<c:forEach items="${invalidFields}" var="fieldName"> 
#${n}config .${fieldName} .field-error { display: block; }
</c:forEach>
</style>

<div id="${n}config">

    <ul class="list-group">
    <c:forEach items="${links}" var="item">
        <li class="list-group-item list-group-item-action" draggable="true">
        <a class="app-link"
           rel="noopener noreferrer"
           href='<c:out value="${item.url}"/>'
           target="_blank"
           title='<c:out value="${item.title}"/>'>
            <i class="fa fa-pull-left fa-fw fa-3x ${item.icon}" aria-hidden="true"></i>
            <dl>
                <dt><c:out value="${item.title}"/></dt>
                <dd><c:out value="${item.description}"/></dd>
                <dd>(<c:out value="${item.url}"/>)</dd>
                <dd>[<c:out value="${item.groups}"/>]</dd>
            </dl>
        </a>
        </li>
    </c:forEach>
    </ul>

<c:set var="item" value="${links[0]}"/>
    <form role="form" class="form-horizontal" method="POST" action="<portlet:actionURL/>">
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
            <label for="icon" class="col-sm-2 control-label"><spring:message code="resource-links.icon"/></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="icon" id="icon" value="${item.icon}" placeholder="fa-link">
                <i class="fa fa-pull-left fa-fw fa-3x ${item.icon || 'fa-link'}" aria-hidden="true"></i>
                <div class="field-error bg-danger"><spring:message code="resource-links.icon.invalid"/></div>
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
            <div class="col-sm-10">
                <input type="text" class="form-control" name="groups" id="groups" value="${item.groups}" placeholder="staff, students">
                <div class="field-error bg-danger"><spring:message code="resource-links.groups.invalid"/></div>
            </div>
        </div>
        <div class="buttons text-right">
            <input type="submit" class="button primary btn btn-primary" name="save" value="Save"/>
            <input type="submit" class="button secondary btn btn-link" name="cancel" value="Cancel"/>
        </div>
    </form>
</div>
