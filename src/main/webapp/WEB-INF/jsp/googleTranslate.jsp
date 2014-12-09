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
<c:set var="namespace"><portlet:namespace/></c:set>

<script src="http://www.google.com/jsapi?key=${key}" type="text/javascript"></script>
<script type="text/javascript"><rs:compressJs>
 google.load("language", "1");
 function ${namespace}translate(form) {
   google.language.translate(form.source.value, form.fromLanguage.value, form.toLanguage.value, function(result) {
     if (!result.error) {
       document.getElementById("${namespace}translationContainer").style.display = "block";
       var container = document.getElementById("${namespace}translation");
       container.innerHTML = result.translation;
     }
   });
   return false;
 }
</rs:compressJs></script>

<form onsubmit="return ${namespace}translate(this);">
    <p>
        Translate
        <textarea name="source" style="width:100%; height: 150px; padding:2px;"></textarea>
    </p>
    <p>from 
        <select name="fromLanguage" class="portlet-form-input-field">
            <option value=""><spring:message code="language.guess"/></option>
            <c:forEach items="${ languages }" var="lang">
                <option value="${ lang }">
                    <spring:message code="language.${lang}"/>
                </option>
            </c:forEach>
        </select>
        to: 
        <select name="toLanguage" class="portlet-form-input-field">
            <c:forEach items="${ languages }" var="lang">
                <option value="${ lang }">
                    <spring:message code="language.${lang}"/>
                </option>
            </c:forEach>
        </select>
        <input class="portlet-form-button" type="submit" value="Go!"/>
    </p>
</form>
<div id="${namespace}translationContainer" style="display:none">
	<hr/>
	<p><label class="portlet-form-field-label" for="${namespace}translation">Translation:</label></p>
	<div id="${namespace}translation"></div>
</div>
