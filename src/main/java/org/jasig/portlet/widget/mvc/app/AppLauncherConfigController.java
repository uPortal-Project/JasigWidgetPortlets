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

package org.jasig.portlet.widget.mvc.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("CONFIG")
public class AppLauncherConfigController {

    private static final String JSP_CONFIG = "app/config";
    private static final String INVALID_FIELDS = "invalidFields";
    private static final String APP_DEFINITION_IN_PROGRESS_PREFIX = "AppLauncherConfigController.APP_DEFINITION_IN_PROGRESS.";

    private final List<AppDefinition.DisplayStrategies> availableDisplayStrategies = Arrays.asList(
            AppDefinition.DisplayStrategies.IFRAME, 
            AppDefinition.DisplayStrategies.NEW_WINDOW);

    /**
     * This one is public and static because we want to call it in VIEW mode.
     */
    public static void clearAppDefinitionInProgress(PortletRequest req) {
        final PortletSession session = req.getPortletSession();
        final String key = APP_DEFINITION_IN_PROGRESS_PREFIX + req.getWindowID();
        session.removeAttribute(key);
    }

    @RenderMapping()
    public ModelAndView config(PortletRequest req) throws Exception {

        Map<String,Object> model = new HashMap<String,Object>();

        AppDefinition appDefinition = getAppDefinition(req);
        model.put("appDefinition", appDefinition);

        final String[] invalidSettings = req.getParameterMap().containsKey(INVALID_FIELDS)
                ? req.getParameterValues(INVALID_FIELDS)
                : new String[0];
        model.put(INVALID_FIELDS, Arrays.asList(invalidSettings));

        return new ModelAndView(JSP_CONFIG, model);

    }

    @ActionMapping
    public void saveSettings(ActionRequest req, ActionResponse res) throws PortletModeException {
        final AppDefinition appDefinition = AppDefinition.fromFormFields(req);
        final List<AppDefinition.Setting> invalidSettings = appDefinition.getInvalidSettings();
        switch (invalidSettings.size()) {
            case 0:  // success
                AppDefinition.update(req, appDefinition);
                clearAppDefinitionInProgress(req);
                res.setPortletMode(PortletMode.VIEW);
                break;
            default:  // validation errors
                setAppDefinitionInProgress(req, appDefinition);
                String[] invalidFields = new String[invalidSettings.size()];
                for (int i=0; i < invalidSettings.size(); i++) {
                    AppDefinition.Setting p = invalidSettings.get(i);
                    invalidFields[i] = p.getFieldName();
                }
                res.setRenderParameter(INVALID_FIELDS, invalidFields);
                break;
        }
    }

    /**
     * List, rather than Set, because we're going to display them in chosen
     * order and assume the first is the default.
     */
    @ModelAttribute("availableDisplayStrategies")
    public List<AppDefinition.DisplayStrategies> getAvailableDisplayStrategies() {
        return availableDisplayStrategies;
    }

    /*
     * Implementation
     */

    /**
     * Don't annotate with @ModelAttribute due to timing in the action phase.
     */
    private AppDefinition getAppDefinition(PortletRequest req) {
        /*
         * Option #1:  An in-process appDefinition
         */
        AppDefinition rslt = this.getAppDefinitionInProgress(req);
        /*
         * Option #2:  An appDefinition previously saved
         */
        if (rslt == null) {
            rslt = AppDefinition.fromPortletPreferences(req);
        }
        /*
         * Option #3:  A blank appDefinition
         */
        if (rslt.isAllDefaults()) {
            // Better to provide an empty definition than one full of defaults
            rslt = AppDefinition.BLANK_INSTANCE;
        }
        return rslt;
    }

    private AppDefinition getAppDefinitionInProgress(PortletRequest req) {
        AppDefinition rslt = null;
        final PortletSession session = req.getPortletSession();
        final String key = APP_DEFINITION_IN_PROGRESS_PREFIX + req.getWindowID();
        if (session.getAttributeMap().containsKey(key)) {
            rslt = (AppDefinition) session.getAttribute(key);
        }
        return rslt;
    }

    private void setAppDefinitionInProgress(PortletRequest req, AppDefinition appDefinition) {
        final PortletSession session = req.getPortletSession();
        final String key = APP_DEFINITION_IN_PROGRESS_PREFIX + req.getWindowID();
        session.setAttribute(key, appDefinition);
    }

}
