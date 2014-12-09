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
<c:set var="context" value="${pageContext.request.contextPath}"/>

<c:if test="${portletPreferencesValues['includeJsLibs'][0] != 'false'}">
    <rs:aggregatedResources path="/resources.xml"/>
</c:if>
<script src="//maps.google.com/maps/api/js?v=3.6&sensor=true"></script>

<script type="text/javascript">
    var ${n} = {};
<c:choose>
    <c:when test="${portletPreferencesValues['includeJsLibs'][0] != 'false'}">
       ${n}.jQuery = jQuery.noConflict(true)
    </c:when>
    <c:otherwise>
        ${n}.jQuery = up.jQuery;
    </c:otherwise>
</c:choose>
    ${n}.setStartingLocation = function(geoc, map) {
        <c:choose>
        <c:when test="${startingLocation != null}">
		geoc.geocode( { 'address': "${startingLocation}"}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				map.setCenter(results[0].geometry.location);
				map.setZoom(${startingZoom});
			} else {
				alert('Geocode was not successful for the following reason: ' + status);
			}
		});
        </c:when>
        <c:otherwise>
          map.setCenter("new york");
        </c:otherwise>
        </c:choose>
    }
	${n}.initializeMap = function() {
        var $ = ${n}.jQuery;
        
        var defaultPosition = new google.maps.LatLng(40.69847032728747, -73.9514422416687);//new york - this should likely be something else
        mapOptions = {
            zoom: ${ startingZoom },
            center: defaultPosition,
            mapTypeId: google.maps.MapTypeId.ROADMAP
          };
        ${n}.map = new google.maps.Map($("#${n}map_canvas").get(0), mapOptions); 
        ${n}.trafficInfo = new google.maps.TrafficLayer();
        ${n}.geocoder = new google.maps.Geocoder();
        ${n}.marker = new google.maps.Marker({position: defaultPosition, map: ${n}.map});
        ${n}.setStartingLocation(${n}.geocoder, ${n}.map);
	}
	${n}.toggleTraffic = function(input) {
	   if (input.checked) {
           ${n}.trafficInfo.setMap(${n}.map);
	   } else {
           ${n}.trafficInfo.setMap(null);
	   }
	}
	${n}.search = function(form) {
      var location = new google.maps.LatLng("${ isHttps ? 'https' : 'http' }://maps.googleapis.com/maps/api/geocode/json?address="+form.location.value+"&sensor=false");

      if (${n}.geocoder) {
        ${n}.geocoder.geocode( { 'address': form.location.value}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
            ${n}.map.setCenter(results[0].geometry.location);
            ${n}.marker.setMap(${n}.map);
            ${n}.marker.setPosition(results[0].geometry.location);
          } else {
            alert(form.location.value + " was not found. reason: " + status);
          }
        });
      }
      return false;
	}
    ${n}.jQuery(document).ready(function () {
        ${n}.initializeMap();
    });

</script>


<div id="${n}map_canvas"style="height: 300px; margin-bottom: 10px"></div>

<p><input type="checkbox" value="traffic" onClick="${n}.toggleTraffic(this);"/> Show Traffic</p>
<form onsubmit="return ${n}.search(this);">
    <p>
        <label class="portlet-form-field-label" for="${n}location">Go to:</label>
        <input class="portlet-form-input-field" id="${n}location" name="location" size="35"/>
        <input class="portlet-form-button" type="submit" value="Go!"/>
    </p>
</form>
