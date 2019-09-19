/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.widget.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.env.PropertyResolver;
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
    private Properties properties;
    @Autowired
    private PropertyResolver propertyResolver;

    /**
     * Set the properties.
     *
     * @param properties the properties loader
     */
    @Required
    public void setProperties(final Properties properties) {
        this.properties = properties;
    }

    /*
     * Public API.
     */

    @RenderMapping()
    public String doView(RenderRequest req, Model model) {

        addSecurityRoleChecksToModel(req, model);

        // load properties into the Model
        model.addAttribute("property", properties);

        model.addAttribute("resolver", propertyResolver);

        final PortletPreferences prefs = req.getPreferences();

        /* We're going to construct a list of JSP pages
         * that are allowable and choose one from it
         */
        final List<String> allowableJspPages = new ArrayList<String>();
        final String[] jspNamesAnyStateAnyMode = prefs.getValues(JSP_NAME_PREFERENCE, new String[] {INSTRUCTIONS_VIEW});
        allowableJspPages.addAll(Arrays.asList(jspNamesAnyStateAnyMode));
        final String[] jspNamesThisWindowState = prefs.getValues(JSP_NAME_PREFERENCE + "." + req.getWindowState().toString().toUpperCase(), new String[0]);
        allowableJspPages.addAll(Arrays.asList(jspNamesThisWindowState));

        /* The first JSP specified is the default.
         */
        String rslt = allowableJspPages.get(0);

        /* Now that we know all psosible JSPs, was a
         * jspName specified as a request parameter?
         */
        final String jspNameParam = req.getParameter("nextJspPage");
        if (jspNameParam != null) {
            // Yes;  is it allowed?
            if (allowableJspPages.contains(jspNameParam)) {
                rslt = jspNameParam;
            } else {
                // The default will be used;  but log the occurance
                log.warn("User '" + req.getRemoteUser() + "' requested a JSP not in the whitelist:  " + jspNameParam);
            }
        }

        return rslt;

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
            model.addAttribute("is"+securityRoles[i].replace(" ", "_"), req.isUserInRole(securityRoles[i]));
        }
    }
}
