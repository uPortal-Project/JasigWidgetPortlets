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

package org.jasig.portlet.widget.servlet.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasig.portlet.widget.service.GoogleGadgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ajax/gadgets")
public class GoogleGadgetListingController {

    private GoogleGadgetService gadgetService;
    
    @Autowired(required = true)
    public void setGoogleGadgetService(GoogleGadgetService googleGadgetService) {
        this.gadgetService = googleGadgetService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getSearchListing(HttpServletRequest request,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "start", required = false) Integer start) {
        Map<String, Object> model = new HashMap<String, Object>();
        
        if (start == null) start = 0;
        
        GoogleGadgetService.GadgetList gadgetList = gadgetService.getGadgets(query, category, start);
        model.put("gadgets", gadgetList.getGadgets());
        model.put("total", gadgetList.getTotal());
        
        return new ModelAndView("jsonView", model);
    }
    

}
