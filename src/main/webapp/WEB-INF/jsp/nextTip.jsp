<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>
<script type="text/javascript">

window.addEventListener('load', function() {
    var displayedTipIndex = parseInt("${displayedTipIndex}");
    var displayedTip = "${displayedTip}";

    var allTips = [];
    <c:forEach var="tip" items="${allTips}">
        allTips.push("${tip}");
    </c:forEach>

    var a = document.getElementById("nextTip");
    var p = document.getElementById("tip-p");

    a.onclick = function() {
        displayedTipIndex = displayedTipIndex + 1;

        if(displayedTipIndex == allTips.length)
            displayedTipIndex = 0;

        displayedTip = allTips[displayedTipIndex];
        p.innerHTML = "Tip #"+(displayedTipIndex+1)+": "+displayedTip;
    }
}, false);
</script>
