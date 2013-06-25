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

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("CONFIG")
public class EditGoogleApiKeyController {
	
	public static final String GOOGLE_API_KEY_PREF_NAME = "googleApiKey";

	@RenderMapping
	public String getFormView() {
		return "editGoogleApiKey";
	}

	@ActionMapping(params = "action=updateKey")
	public void updateKey(ActionRequest request, ActionResponse response,
			@RequestParam(value = GOOGLE_API_KEY_PREF_NAME) String key)
			throws PortletModeException, ReadOnlyException, ValidatorException, IOException {
		
		PortletPreferences prefs = request.getPreferences();
		prefs.setValue(GOOGLE_API_KEY_PREF_NAME, key);
		prefs.store();
		response.setPortletMode(PortletMode.VIEW);
	}

	@ModelAttribute(GOOGLE_API_KEY_PREF_NAME)
	public String getKey(PortletRequest request) {
		PortletPreferences prefs = request.getPreferences();
		return prefs.getValue(GOOGLE_API_KEY_PREF_NAME, null);
	}

}
