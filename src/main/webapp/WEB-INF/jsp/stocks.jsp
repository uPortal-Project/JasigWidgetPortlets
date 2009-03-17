<%@ page contentType="text/html" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="rs" uri="http://www.jasig.org/resource-server" %>
<c:set var="n"><portlet:namespace/></c:set>
<c:url var="url" value="/ajax/stock"/>

<script src="<rs:resourceURL value="/rs/jquery/1.3.1/jquery-1.3.1.min.js"/>" type="text/javascript"></script>
<script src="<rs:resourceURL value="/rs/jqueryui/1.6rc6/jquery-ui-1.6rc6.min.js"/>" type="text/javascript"></script>
<script src="<rs:resourceURL value="/rs/fluid/0.8/js/fluid-all-0.8.min.js"/>" type="text/javascript"></script>
<script src="http://www.google.com/jsapi?key=${key}" type="text/javascript"></script>
<script type="text/javascript">
    google.load("feeds", "1");
    var ${n} = { 
        jQuery: jQuery.noConflict(true)
    };
    ${n}.jQuery(function(){
        var $ = ${n}.jQuery;
        var stocks = new Array(<c:forEach items="${stocks}" var="stock" varStatus="status">'${stock}'${status.last ? '' : ', '}</c:forEach>)

	    function loadFinanceFeeds() {
	      document.getElementById("${n}feed").innerHTML = "";
	      var feedControl = new google.feeds.FeedControl();
	      var url = "http://finance.yahoo.com/rss/headline?s=" + stocks.join();
	      feedControl.addFeed(url, "My Finance News");
	      feedControl.draw(document.getElementById("${n}feed"));
	    }
	    google.setOnLoadCallback(loadFinanceFeeds);
        
	    var fillTable = function() {
	        var table = $("#${n}stocks").find("tbody");
	        table.find("tr:not(:first)").remove();
	        $(stocks).each(function(){
	            $.get('${url}', {stock: this}, function(txt){
	                var items = txt.replace(/\"/g,"").split(",");
	                var img = $(document.createElement("img"))
	                  .attr("src", "<rs:resourceURL value="/rs/famfamfam/silk/1.3/chart_line.png"/>")
	                  .attr("title", "Chart").tooltip({
	                      bodyHandler: function() { 
	                          return $(document.createElement("img"))
	                              .attr("src", "http://ichart.finance.yahoo.com/t?s=" + items[0]); 
	                      },
	                      showURL: false
	                  });
	                table.append(
	                   $(document.createElement("tr"))
	                       .append($(document.createElement("td")).text(items[0]))
	                       .append($(document.createElement("td")).text(items[1]))
	                       .append($(document.createElement("td")).text(items[4]))
	                       .append($(document.createElement("td")).text(items[11]))
	                       .append($(document.createElement("td")).append(img))
	                );
	            });
	        });
	    }
	    
	    var switchMode = function(mode) {
	        $("#${n}view").css("display", (mode == "edit") ? "none" : "block");
	        $("#${n}edit").css("display", (mode == "edit") ? "block" : "none");
	        return false;
	    }
	    var updatePrefs = function(form) {
	        stocks = $(form.stock).val().split(",");
	        $.post("<portlet:actionURL><portlet:param name="action" value="savePreferences"/></portlet:actionURL>", 
	            {stocks: stocks });
	        switchMode('view');
	        fillTable();
	        loadFinanceFeeds();
	        return false;
	    }
	    
        $("#${n}stocks").ready(function(){
            $("#${n}tabs").tabs();
            fillTable();
            $("#${n}editLink").click(function(){ return switchMode('edit'); });
            $("#${n}viewLink").click(function(){ return switchMode('view'); });
            $("#${n}edit > form").submit(function(){ return updatePrefs(this); });
        });
        
    });
</script>

<div id="${n}stocks">
	<div id="${n}view" class="${n}viewMode">
		<div id="${n}tabs">
			<ul>
			   <li><a href="#${n}stockTab">Ticker</a></li>
			   <li><a href="#${n}newsTab">News</a></li>
			</ul>
			
			<div id="${n}stockTab">
				<table id="${n}stocks">
				    <tbody>
				        <tr>
				            <th>Symbol</th><th>Price</th><th>Change</th>
				            <th>&nbsp;</th><th>&nbsp;</th>
				        </tr>
				    </tbody>
				</table>
			</div>
			
			<div id="${n}newsTab">
			   <div id="${n}feed"></div>
			</div>
			
		</div>
	    <a id="${n}editLink" href="#"><img src="<rs:resourceURL value="/rs/famfamfam/silk/1.3/chart_line_edit.png"/>"/> Edit stocks</a>
	</div>
	
	<div id="${n}edit" style="display:none" class="${n}editMode">
	    <h2>Edit Stocks Preferences</h2>
	    <form name="${n}stocks">
	        <label for="stock">Enter a comma-delimited list of stocks:</label>
	        <input name="stock" value="<c:forEach items="${stocks}" var="stock" varStatus="status">${stock}${!status.last ? "," : ""}</c:forEach>"/>
		    <input type="submit" value="Save"/>
	    </form>
	    <a id="${n}viewLink" href="#"><img src="<rs:resourceURL value="/rs/famfamfam/silk/1.3/arrow_left.png"/>"/> Back</a>
	</div>
</div>
