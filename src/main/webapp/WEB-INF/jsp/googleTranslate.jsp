<%@ page contentType="text/html" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server" %>
<c:set var="namespace"><portlet:namespace/></c:set>

<script src="http://www.google.com/jsapi?key=${key}" type="text/javascript"></script>
<script type="text/javascript">
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
</script>

<form onsubmit="return ${namespace}translate(this);">
    <p>
        Translate
        <textarea name="source" style="width:100%; height: 150px; padding:2px;"></textarea>
    </p>
    <p>from 
        <select name="fromLanguage">
            <option value=""><spring:message code="language.guess"/></option>
            <c:forEach items="${ languages }" var="lang">
                <option value="${ lang }">
                    <spring:message code="language.${lang}"/>
                </option>
            </c:forEach>
        </select>
        to: 
        <select name="toLanguage">
            <c:forEach items="${ languages }" var="lang">
                <option value="${ lang }">
                    <spring:message code="language.${lang}"/>
                </option>
            </c:forEach>
        </select>
        <input type="submit" value="Go!"/>
    </p>
</form>
<div id="${namespace}translationContainer" style="display:none">
	<hr/>
	<p>Translation:</p>
	<div id="${namespace}translation"></div>
</div>
