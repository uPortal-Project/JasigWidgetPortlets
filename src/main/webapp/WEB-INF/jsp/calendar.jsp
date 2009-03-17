<%@ page contentType="text/html" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server" %>
<portlet:defineObjects/>
<c:set var="n"><portlet:namespace/></c:set>

<script src="<rs:resourceURL value="/rs/jquery/1.3.1/jquery-1.3.1.min.js"/>" type="text/javascript"></script>
<script src="<rs:resourceURL value="/rs/jqueryui/1.6rc6/jquery-ui-1.6rc6.min.js"/>" type="text/javascript"></script>

<div id="${n}calendar-channel"></div>
<div style="clear:both">&nbsp;</div>

<script type="text/javascript">
    var ${n} = ${n} || {};
    ${n}.jQuery = jQuery.noConflict(true)
    ${n}.jQuery("#${n}calendar-channel").ready(function(){
        ${n}.jQuery('#${n}calendar-channel').datepicker({
            inline:true,
            changeMonth: false,
            changeYear: false
        });
    }); 
</script>
