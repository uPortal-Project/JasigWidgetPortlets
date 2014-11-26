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
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
public class SimpleJspPortletController {

    public static final String JSP_NAME_PREFERENCE = "SimpleJspPortletController.jspName";
    public static final String PREF_SECURITY_ROLE_NAMES = "SimpleJspPortletController.securityRolesToTest";
    public static final String INSTRUCTIONS_VIEW = "simple-jsp-instructions";

    // Instance Members.
    private final Log log = LogFactory.getLog(getClass());

    /*
     * Public API.
     */

    @RenderMapping()
    public String doView(RenderRequest req, Model model) {

        addSecurityRoleChecksToModel(req, model);

        // Choose a JSP based on a portlet preference
        String jspName = req.getParameter("nextJspPage");
        if (jspName == null) {

            final PortletPreferences prefs = req.getPreferences();
            WindowState state = req.getWindowState();
            jspName = prefs.getValue(JSP_NAME_PREFERENCE + "." + state.toString().toUpperCase(), null);
            if (StringUtils.isBlank(jspName)) {
                jspName = prefs.getValue(JSP_NAME_PREFERENCE, INSTRUCTIONS_VIEW);
            }

            /*
             * TODO:  In the future, we'll likely want to provide JSPs with access
             * to things like...
             *
             *   - Specific beans defined in the context
             *   - Arbitrary portlet preferences
             */
        }

        return jspName;

    }

    /**
     * This method allows JSPs used with this portlet to provide links that 
     * track user interactions in uPortal Statistics.  Use an actionURL with a 
     * 'redirect' parameter.
     * @throws IOException 
     */
    @ActionMapping
    public void sendRedirect(@RequestParam("redirect") String redirect, ActionRequest req, ActionResponse res) throws IOException {

        if (StringUtils.isBlank(redirect)) {
            String msg = "Parameter 'redirect' cannot be blank";
            throw new IllegalArgumentException(msg);
        }

        if (log.isDebugEnabled()) {
            log.debug("Redirecting user '" + req.getRemoteUser() + 
                        "' to the following URL:  " + redirect);
        }

        res.sendRedirect(redirect);

    }

    @ModelAttribute("userInfo")
    public Map<String,String> addUserInfoToModel(PortletRequest req) {
        @SuppressWarnings("unchecked")
        Map<String,String> rslt = (Map<String, String>) req.getAttribute(PortletRequest.USER_INFO);
        return rslt;
    }

    /**
     * Run through the list of configured security roles and add an "is"+Rolename to the model.  The security roles
     * must also be defined with a <code>&lt;security-role-ref&gt;</code> element in the portlet.xml.
     * @param req Portlet request
     * @param model Model object to add security indicators to
     */
    private void addSecurityRoleChecksToModel(PortletRequest req, Model model) {
        PortletPreferences prefs = req.getPreferences();
        String[] securityRoles = prefs.getValues(PREF_SECURITY_ROLE_NAMES, new String[]{});
        for (int i = 0; i < securityRoles.length; i++) {
            model.addAttribute("is"+securityRoles[i], req.isUserInRole(securityRoles[i]));
        }
    }
}
