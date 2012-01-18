/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.portlet.widget.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("VIEW")
public class GoogleMapsController {
    
    public static final String PREFERENCE_STARTING_LOCATION = "startingLocation";
    public static final String PREFERENCE_STARTING_ZOOM = "startingZoom";

	@RequestMapping
	public ModelAndView getView(RenderRequest request) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		
		PortletPreferences preferences = request.getPreferences();
		
		// Optional starting location & zoom level
		String startingLocation = preferences.getValue(PREFERENCE_STARTING_LOCATION, null);
        map.put(PREFERENCE_STARTING_LOCATION, startingLocation);
        String startingZoom = preferences.getValue(PREFERENCE_STARTING_ZOOM, "13");
        map.put(PREFERENCE_STARTING_ZOOM, startingZoom);
		
		return new ModelAndView("googleMaps", map);
	}

}
