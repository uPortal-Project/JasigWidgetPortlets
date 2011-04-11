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
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.ValidatorException;

import org.jasig.portlet.widget.gadget.model.Module;
import org.jasig.portlet.widget.service.GoogleGadgetService;
import org.jasig.portlet.widget.service.GoogleGadgetService.GadgetCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("EDIT")
public class EditGoogleGadgetController {
	
    private GoogleGadgetService gadgetService;
    
    @Autowired(required = true)
    public void setGoogleGadgetService(GoogleGadgetService googleGadgetService) {
        this.gadgetService = googleGadgetService;
    }
    
    @RequestMapping()
    public ModelAndView getSearchView(RenderRequest request) {
        List<GadgetCategory> categories = gadgetService.getCategories();
        return new ModelAndView("searchGadgets", Collections.<String,Object>singletonMap("categories", categories));
    }
    
    @RequestMapping(params = "action=configure")
    public ModelAndView getConfigurationView(@RequestParam(value = "gadgetUrl") String gadgetUrl) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("gadgetUrl", gadgetUrl);
        
        Module module = gadgetService.getModule(gadgetUrl);
        model.put("module", module);
        
        return new ModelAndView("configureGadget", model);      
    }
    
    @RequestMapping(params = "action=saveConfiguration")
    public void saveConfiguration(@RequestParam("gadgetUrl") String gadgetUrl,
            @RequestParam("width") int width,
            @RequestParam("height") int height,
            @RequestParam("title") String title,
            ActionRequest request, ActionResponse response) {

        PortletPreferences preferences = request.getPreferences();

        try {
            
            // compute the final configured URL of the gadget and store
            // it in the preferences
            StringBuffer configuredUrl = new StringBuffer();
            configuredUrl.append("http://www.gmodules.com/ig/ifr?url=").append(URLEncoder.encode(gadgetUrl, "UTF-8"));
            configuredUrl.append("&amp;title=").append(URLEncoder.encode(title, "UTF-8"));
            configuredUrl.append("&amp;w=").append(width);
            configuredUrl.append("&amp;h=").append(height);
            
            configuredUrl.append("&amp;synd=open&amp;border=&amp;output=js");
            
            preferences.setValue("configuredUrl", configuredUrl.toString());
            preferences.store();
            response.setPortletMode(PortletMode.VIEW);
            
        } catch (ValidatorException e) {
        } catch (IOException e) {
        } catch (ReadOnlyException e) {
        } catch (PortletModeException e) {
        }
    }
	
}
