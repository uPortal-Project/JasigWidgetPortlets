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
<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="n"><portlet:namespace/></c:set>
<c:url var="url" value="/ajax/stock"/>

<c:if test="${portletPreferencesValues['includeJsLibs'][0] != 'false'}">
    <rs:aggregatedResources path="/resources.xml"/>
</c:if>
<script src="//www.google.com/jsapi?key=${key}" type="text/javascript"></script>

<script type="text/javascript"><rs:compressJs>
    google.load("feeds", "1");
    var ${n} = { 
<c:choose>
    <c:when test="${portletPreferencesValues['includeJsLibs'][0] != 'false'}">
        jQuery: jQuery.noConflict(true)
    </c:when>
    <c:otherwise>
        jQuery: up.jQuery;
    </c:otherwise>
</c:choose>
    };
    ${n}.jQuery(function(){
        var $ = ${n}.jQuery;
        var stocks = new Array(<c:forEach items="${stocks}" var="stock" varStatus="status">'${fn:escapeXml(stock)}'${status.last ? '' : ', '}</c:forEach>)

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
	                  var tr = $(document.createElement("tr"))
	                      .append($(document.createElement("td")).text(items[0]).addClass("stock-name"))
	                      .append($(document.createElement("td")).text(items[1]))
	                      .append($(document.createElement("td")).text(items[4]))
	                      .append($(document.createElement("td")).text(items[11]))
	                      .append($(document.createElement("td")).append(img));
                      if (items[4] < 0) tr.addClass("falling-stock");
                      if (items[4] > 0) tr.addClass("rising-stock");
	                table.append(tr);
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
</rs:compressJs></script>

<style type="text/css">
	tr.rising-stock td.stock-name { color:green }
	tr.falling-stock td.stock-name { color:red }
</style>

<div id="${n}stocks">
	<div id="${n}view" class="${n}viewMode">
		<div id="${n}tabs">
			<ul>
			   <li><a href="#${n}stockTab">Ticker</a></li>
			   <li><a href="#${n}newsTab">News</a></li>
			</ul>
			
			<div id="${n}stockTab">
				<table id="${n}stocks">
				    <thead>
                        <tr>
                            <th>Symbol</th><th>Price</th><th>Change</th>
                            <th>&nbsp;</th><th>&nbsp;</th>
                        </tr>
				    </thead>
				    <tfoot/>
				    <tbody>
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
	        <input name="stock" value="<c:forEach items="${stocks}" var="stock" varStatus="status">${fn:escapeXml(stock)}${!status.last ? "," : ""}</c:forEach>"/>
		    <input type="submit" value="Save"/>
	    </form>
	    <a id="${n}viewLink" href="#"><img src="<rs:resourceURL value="/rs/famfamfam/silk/1.3/arrow_left.png"/>"/> Back</a>
	</div>
</div>
