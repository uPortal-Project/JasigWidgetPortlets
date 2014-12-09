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
<portlet:actionURL var="saveConfigurationUrl">
    <portlet:param name="action" value="saveConfiguration"/>
</portlet:actionURL>

<h2>Configure ${ module.modulePrefs.title }</h2>

<form method="post" action="${ saveConfigurationUrl }">

    <p>
        <input type="hidden" name="gadgetUrl" value="${ gadgetUrl }"/>
        Title: <input name="title"/><br/>
        Width: <input name="width"/><br/>
        Height: <input name="height"/><br/>
    </p>

    <div class="buttons">
        <input type="submit" value="Cancel" class="button secondary"/>
        <input type="submit" value="Save" class="button primary"/>
    </div>
</form>
