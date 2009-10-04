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
import javax.portlet.RenderRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.web.service.AjaxPortletSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("VIEW")
public class StocksController {
	
	private final Log log = LogFactory.getLog(StocksController.class);

	private final String defaultApiKey = "ABQIAAAA1LMBgN_YMQm8gHtNDD0PHBT8V3EeC0kvvMKhUfRICeG0j5XTvxR7twPk2H016LpKy1O2yngKoCTt6g";
	
	private final String[] defaultStocks = new String[]{"yhoo","goog"};
	
	@Autowired
	private AjaxPortletSupportService ajaxPortletSupportService;
	
	public void setAjaxPortletSupportService(
			AjaxPortletSupportService ajaxPortletSupportService) {
		this.ajaxPortletSupportService = ajaxPortletSupportService;
	}
	
	@RequestMapping()
	public ModelAndView viewStocks(RenderRequest request) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		
		PortletPreferences preferences = request.getPreferences();
		map.put("key", preferences.getValue("key", this.defaultApiKey));
		map.put("stocks", preferences.getValues("stocks", this.defaultStocks));
		
		return new ModelAndView("stocks", map);
	}
	
	@RequestMapping(params = "action=savePreferences")
	protected void handleAjaxRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		
		Map<Object,Object> model = new HashMap<Object,Object>();
		try {
			// save the preferences
			PortletPreferences preferences = request.getPreferences();
			preferences.setValues("stocks", request.getParameterValues("stocks"));
			preferences.store();
			model = Collections.<Object,Object>singletonMap("status", "success");

		} catch (RuntimeException e) {
			log.error("Error storing stocks preferences", e);
			model = Collections.<Object,Object>singletonMap("status", "failure");
		}

		ajaxPortletSupportService.redirectAjaxResponse("ajax/json", model, request, response);
	}

}
