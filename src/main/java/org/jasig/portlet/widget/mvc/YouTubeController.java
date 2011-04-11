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
import javax.portlet.PortletRequest;

import org.jasig.portlet.widget.service.IViewHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

/**
 * @author Jen Bourey
 * @version $Revision$
 */
@Controller
@RequestMapping("VIEW")
public class YouTubeController {

    private IViewHelper viewHelper;
    
    @Autowired(required = true)
    public void setViewHelper(IViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    @RequestMapping
    public ModelAndView getYouTubeView(PortletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        PortletPreferences prefs = request.getPreferences();
        model.put("usernames", prefs.getValues("usernames", new String[]{"uPortal"}));
        
        String viewName;
        if (viewHelper.isMobile(request)) {
            viewName = "youtube-jQM";
        } else {
            viewName = "youtube";
        }
        return new ModelAndView(viewName, model);
    }

}
