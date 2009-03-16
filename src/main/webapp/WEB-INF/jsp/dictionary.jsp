<%@ page contentType="text/html" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server" %>
<c:set var="n"><portlet:namespace/></c:set>
<c:url var="url" value="/ajax/dictionary"/>

<script src="<rs:resourceURL value="/rs/jquery/1.3.1/jquery-1.3.1.min.js"/>" type="text/javascript"></script>
<script src="<rs:resourceURL value="/rs/jqueryui/1.6rc6/jquery-ui-1.6rc6.min.js"/>" type="text/javascript"></script>
<script type="text/javascript">
    var ${n} = {};
    ${n}.jQuery = jQuery.noConflict(true);
    ${n}.jQuery(document).ready(function(){
        ${n}.jQuery("#${n}tabs").tabs();
        ${n}.jQuery("#${n}dictionaryTab > form").submit(function(){ return ${n}searchDictionary(this); });
    });
    
    function ${n}searchDictionary(form) {
         var $ = ${n}.jQuery;
         $("#${n}defs").html("");
         $.get('${url}', {word: $(form.word).val(), dictId: $(form.dict).val()}, function(xml){
            $(xml).find("Definition > WordDefinition").each(function(){
                $(form).parent().find("div.defContainer").append($(document.createElement("div")).html($(this).text()));
            });
         });
         return false;
    };

</script>

<div id="${n}tabs">
    <ul>
       <li><a href="#${n}dictionaryTab">Dictionary</a></li>
       <li><a href="#${n}thesaurusTab">Thesaurus</li></a>
    </ul>
    
    <div id="${n}dictionaryTab">
		<form>
		    <input type="hidden" name="dict" value="wn"/>
		    <input name="word"/> 
		    <input type="submit" value="Go!"/>
		</form>
		<div class="defContainer"></div>
	</div>

	<div id="${n}thesaurusTab">
        <form onsubmit="return ${n}searchDictionary(this);">
            <input type="hidden" name="dict" value="moby-thes"/> 
            <input name="word"/>
            <input type="submit" value="Go!"/>
        </form>
        <div class="defContainer"></div>
	</div>

</div>
	
