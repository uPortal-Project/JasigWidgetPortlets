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

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
public final class SimpleJspPortletController {

    // Instance Members.

    /*
     * Public API.
     */

    public static final String JSP_NAME_PREFERENCE = "SimpleJspPortletController.jspName";
    public static final String INSTRUCTIONS_VIEW = "simple-jsp-instructions";

    @RenderMapping()
    public String doView(PortletRequest req) {

        final PortletPreferences prefs = req.getPreferences();
        final String jspName = prefs.getValue(JSP_NAME_PREFERENCE, INSTRUCTIONS_VIEW);

        /*
         * TODO:  In the future, we'll likely want to provide JSPs with access 
         * to things like...
         * 
         *   - Specific beans defined in the context
         *   - The portlet USER_INFO map
         */

        return jspName;

    }

}
