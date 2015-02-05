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
<c:url var="url" value="/ajax/dictionary"/>

<c:if test="${portletPreferencesValues['includeJsLibs'][0] != 'false'}">
    <rs:aggregatedResources path="/resources.xml"/>
</c:if>

<script type="text/javascript"><rs:compressJs>
    var ${n} = {};
<c:choose>
    <c:when test="${portletPreferencesValues['includeJsLibs'][0] != 'false'}">
        ${n}.jQuery = jQuery.noConflict(true)
    </c:when>
    <c:otherwise>
        ${n}.jQuery = up.jQuery;
    </c:otherwise>
</c:choose>
    ${n}.jQuery(function(){
        var ${n}searchDictionary = function(form) {
             var $ = ${n}.jQuery;
             $("#${n}defs").html("");
             $.get('${url}', {word: $(form.word).val(), dictId: $(form.dict).val()}, function(json){
                 if (json.definition.length) {
                     $(form).parent().find("div.defContainer").append($(document.createElement("hr")));
                     $(form).parent().find("div.defContainer").append($(document.createElement("div")).html(json.definition));
                 }
             }, "json");
             return false;
        };

        ${n}.jQuery("#${n}tabs").ready(function(){
            ${n}.jQuery("#${n}dictionaryTab > form").submit(function(){ return ${n}searchDictionary(this); });
            <!-- Register the reset button's behaviour -->
            ${n}.jQuery("#${n}resetDictionary").click(function() { ${n}.jQuery("#${n}dictionaryTab > div.defContainer").html(""); });
        });
    });

</rs:compressJs></script>

<div id="${n}tabs">
    <div id="${n}dictionaryTab">
        <form>
            <input type="hidden" name="dict" value="wn"/>
            <input name="word"/> 
            <span class="buttons">
                <input type="submit" value="Go!"/>
                <input type="reset" id="${n}resetDictionary" value="Reset">
            </span>
        </form>
        <div class="defContainer"></div>
    </div>
</div>
