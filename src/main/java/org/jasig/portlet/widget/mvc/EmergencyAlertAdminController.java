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

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

import org.jasig.portlet.widget.service.IAlertService;

@Controller
@RequestMapping("VIEW")
public final class EmergencyAlertAdminController {

    @Autowired
    private IAlertService service;
    
    /*
     * Public API.
     */

    public static final String VIEWNAME = "alert-admin";
    
    @RequestMapping()
    public ModelAndView showAdmin(PortletRequest req) {
        
        Map<String,Object> model = new HashMap<String,Object>();
        Boolean enabled = service.isEnabled();  // might be null
        String value = "unspecified";  // default (if null)
        if (Boolean.TRUE.equals(enabled)) {
            value = "enabled";
        } else if (Boolean.FALSE.equals(enabled)) {
            value = "disabled";
        }
        model.put("value", value);
        return new ModelAndView(VIEWNAME, model);

    }
    
    @RequestMapping
    public void toggleEnabled(ActionRequest req) {
        boolean current = service.isEnabled() != null
                            ? service.isEnabled().booleanValue()
                            : false;  // Toggle while the current value is UNSPECIFIED always turns it on
        service.setEnabled(!current);
    }

}
