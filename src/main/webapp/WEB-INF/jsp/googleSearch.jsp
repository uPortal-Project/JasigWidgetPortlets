<%@ page contentType="text/html" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<portlet:defineObjects/>

<script src="http://www.google.com/jsapi?key=${key}" type="text/javascript"></script>
<script language="Javascript" type="text/javascript">
//<![CDATA[
  google.load('search', '1.0');
  
  function <portlet:namespace/>OnLoad() {

    // create a tabbed mode search control
    var tabbed = new google.search.SearchControl();
    var options = new google.search.SearcherOptions();
    options.setExpandMode(google.search.SearchControl.EXPAND_MODE_OPEN);
    var drawOptions = new google.search.DrawOptions();
    drawOptions.setDrawMode(google.search.SearchControl.DRAW_MODE_TABBED);

    // add the web search
    <c:forEach items="${ searchEngines }" var="engine">
        <c:choose>
            <c:when test="${engine == 'web'}">
                tabbed.addSearcher(new google.search.WebSearch(), options);
            </c:when>
            <c:when test="${engine == 'video'}">
                tabbed.addSearcher(new google.search.VideoSearch(), options);
            </c:when>
            <c:when test="${engine == 'blog'}">
                tabbed.addSearcher(new google.search.BlogSearch(), options);
            </c:when>
            <c:when test="${engine == 'news'}">
                tabbed.addSearcher(new google.search.NewsSearch(), options);
            </c:when>
            <c:when test="${engine == 'image'}">
                tabbed.addSearcher(new google.search.ImageSearch(), options);
            </c:when>
        </c:choose>
    </c:forEach>

    tabbed.draw(document.getElementById("<portlet:namespace/>searchcontrol"), drawOptions);

  }
  google.setOnLoadCallback(<portlet:namespace/>OnLoad, true);

//]]>
</script>
<div id="<portlet:namespace/>searchcontrol">Loading...</div>
