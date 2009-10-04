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
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.web.portlet.mvc.AbstractAjaxController;

public class StocksSavePreferencesController extends AbstractAjaxController {

	private Log log = LogFactory.getLog(StocksSavePreferencesController.class);
	
	@Override
	protected Map<Object, Object> handleAjaxRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		
		try {
			// save the preferences
			PortletPreferences preferences = request.getPreferences();
			preferences.setValues("stocks", request.getParameterValues("stocks"));
			preferences.store();

		} catch (RuntimeException e) {
			log.error("Error storing stocks preferences", e);
			return Collections.<Object,Object>singletonMap("status", "failure");
		}

        return Collections.<Object,Object>singletonMap("status", "success");

	}
	
}
