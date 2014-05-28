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
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.widget.dao.PluggableDataDao;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

@Controller
@RequestMapping("VIEW")
public class PluggableDataJspPortletController {

    public static final String PREF_JSP_NAME = "PluggableDataJspPortlet.Controller.jspName";
    public static final String PREF_EXPOSE_DATA_AS_JSON = "PluggableDataJspPortlet.Controller.asJson";
    public static final String PREF_SECURITY_ROLE_NAMES = "PluggableDataJspPortlet.Controller.securityRolesToTest";
    public static final String INSTRUCTIONS_VIEW = "pluggableData-jsp-instructions";

    PluggableDataDao service;

    // Instance Members.
    private final Log log = LogFactory.getLog(getClass());

    /*
     * Public API.
     */

    @RenderMapping()
    public String doView(RenderRequest req, Model model) {

        addSecurityRoleChecksToModel(req, model);

        List<? extends Object> data = service.getData(req);
        model.addAttribute("data", data);

        boolean asJson = Boolean.valueOf(req.getPreferences().getValue(PREF_EXPOSE_DATA_AS_JSON, "false"));
        if (asJson) {
            // WARNING:  Graph must not be cyclic or JSONSerializer will fail; e.g. a node can't point to
            // another node in the graph.
            JSON jsonData = JSONSerializer.toJSON(data);
            model.addAttribute("jsonData", jsonData.toString(4));
        }

        // Choose a JSP based on a portlet preference
        String jspName = req.getParameter("nextJspPage");
        if (jspName == null) {

            final PortletPreferences prefs = req.getPreferences();
            jspName = prefs.getValue(PREF_JSP_NAME, INSTRUCTIONS_VIEW);

            /*
             * TODO:  In the future, we'll likely want to provide JSPs with access to things like...
             *   - Specific beans defined in the context
             *
             * Arbitrary portlet preferences are already available via defineObjects tag as portletPreferences
             * and portletPreferencesValues.
             */
        }

        return jspName;

    }

    /**
     * This method allows JSPs used with this portlet to provide links that track user interactions in uPortal
     * Statistics.  Use an actionURL with a 'redirect' parameter.  Not needed as much now that Google Analytics
     * integration tends to supplant uPortal statistics tracking.
     * @throws java.io.IOException
     */
    @ActionMapping
    public void sendRedirect(@RequestParam("redirect") String redirect, ActionRequest req, ActionResponse res)
            throws IOException {

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

    /**
     * Invoke the service to obtain the data (which would be cached if using the default
     * CachingConfigurableDataServiceImpl) and return the results as JSON.
     */
    @ResourceMapping
    public String getData(ResourceRequest req, ResourceResponse resp, Model model) {

        addSecurityRoleChecksToModel(req, model);
        List<? extends Object> data = service.getData(req);
        model.addAttribute("data", data);
        return "jsonView";
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

    @Required
    public void setService(PluggableDataDao service) {
        this.service = service;
    }
}
