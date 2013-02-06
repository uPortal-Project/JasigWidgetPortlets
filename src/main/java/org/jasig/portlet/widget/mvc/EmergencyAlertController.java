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
import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

import org.jasig.portlet.widget.service.IAlert;
import org.jasig.portlet.widget.service.IAlertService;

/**
 * @deprecated This portlet was moved to the Apereo NotificationPortlet project.
 */
@Controller
@RequestMapping("VIEW")
public final class EmergencyAlertController {

    @Autowired
    private IAlertService service;
    
    /*
     * Public API.
     */

    public static final String VIEW_NO_CURRENT_ALERT = "no-alert";
    public static final String VIEW_SHOW_CURRENT_ALERT = "show-alert";

    private static final String AUTO_ADVANCE_PREFERENCE = "autoAdvance";
    
    @RequestMapping()
    public ModelAndView showAlert(PortletRequest req) {
        
        List<IAlert> feed = service.fetch(req);
        if (!feed.isEmpty()) {
            Map<String,Object> model = new HashMap<String,Object>(); 
            model.put("feed", feed);
            PortletPreferences prefs = req.getPreferences();            
            boolean autoAdvance = Boolean.valueOf(prefs.getValue(AUTO_ADVANCE_PREFERENCE, "true"));
            model.put("autoAdvance", autoAdvance);
            return new ModelAndView(VIEW_SHOW_CURRENT_ALERT, model);
        }
        
        return new ModelAndView(VIEW_NO_CURRENT_ALERT);  // default

    }

}
