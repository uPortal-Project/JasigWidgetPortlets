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
public class GoogleSearchController {
	
	private String apiKey = "ABQIAAAA1LMBgN_YMQm8gHtNDD0PHBT8V3EeC0kvvMKhUfRICeG0j5XTvxR7twPk2H016LpKy1O2yngKoCTt6g";
	
	public void setApiKey(String key) {
		this.apiKey = key;
	}
	
	private static final String[] defaultSearchEngines = new String[]{ "web", "news" };

	@RequestMapping()
	public ModelAndView handleRenderRequest(RenderRequest request) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		
		PortletPreferences preferences = request.getPreferences();
		map.put("key", preferences.getValue("key", this.apiKey));
		map.put("searchEngines", 
				preferences.getValues("searchEngines", defaultSearchEngines));
		
		return new ModelAndView("googleSearch", map);
	}

}
