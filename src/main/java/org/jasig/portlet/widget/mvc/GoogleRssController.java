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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.web.service.AjaxPortletSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("VIEW")
public class GoogleRssController {
	
	private Log log = LogFactory.getLog(GoogleRssController.class);
	
	private static final String DEFAULT_GOOGLE_API_KEY = "ABQIAAAA1LMBgN_YMQm8gHtNDD0PHBT8V3EeC0kvvMKhUfRICeG0j5XTvxR7twPk2H016LpKy1O2yngKoCTt6g";
	private static final String[] DEFAULT_FEED_URLS = new String[]{
			"http://www.nytimes.com/services/xml/rss/nyt/HomePage.xml", 
			"http://newsrss.bbc.co.uk/rss/newsonline_world_edition/front_page/rss.xml"};
	private static final String[] DEFAULT_FEED_NAMES = new String[]{"NY Times", "BBC"};
	
	@Autowired(required=true)
	private AjaxPortletSupportService ajaxService;
	
	public void setAjaxPortletSupportService(AjaxPortletSupportService service) {
		this.ajaxService = service;
	}

	@RequestMapping()
	public ModelAndView getView(PortletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		PortletPreferences preferences = request.getPreferences();
		map.put("key", preferences.getValue("key", this.DEFAULT_GOOGLE_API_KEY));
		map.put("feedUrls", 
				preferences.getValues("feedUrls", DEFAULT_FEED_URLS));
		map.put("feedNames", 
				preferences.getValues("feedNames", DEFAULT_FEED_NAMES));
		
		return new ModelAndView("googleRss", map);
	}
	
	@RequestMapping(params = "action=savePreferences")
	public void savePreferences(ActionRequest request, ActionResponse response) throws Exception {

		Map<Object, Object> model = new HashMap<Object, Object>();
		try {
			// save the preferences
			PortletPreferences preferences = request.getPreferences();
			preferences.setValues("feedNames", request.getParameterValues("feedNames"));
			preferences.setValues("feedUrls", request.getParameterValues("feedUrls"));
			preferences.store();

		} catch (Exception e) {
			log.error("Error storing Google rss preferences", e);
			model = Collections.<Object,Object>singletonMap("status", "failure");
		}

        model = Collections.<Object,Object>singletonMap("status", "success");
        ajaxService.redirectAjaxResponse("ajax/json", model, request, response);

	}
	
}
