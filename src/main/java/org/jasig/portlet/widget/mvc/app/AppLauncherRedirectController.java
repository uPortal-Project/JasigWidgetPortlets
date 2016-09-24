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

package org.jasig.portlet.widget.mvc.app;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.WindowStateException;

import org.jasig.portlet.widget.service.IExpressionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

@Controller
@RequestMapping("VIEW")
public class AppLauncherRedirectController {

    public static final String ACTION_PARAMETER_NAME = "action";
    public static final String REDIRECT_ACTION = "redirect";

    private IExpressionProcessor expressionProcessor;

    @Autowired
    public void setExpressionProcessor(final IExpressionProcessor expressionProcessor) {
        this.expressionProcessor = expressionProcessor;
    }

    @ActionMapping(params=ACTION_PARAMETER_NAME+"="+REDIRECT_ACTION)
    public void redirectToApp(ActionRequest req, ActionResponse res) {

        /*
         * Admin has chosen to send this request through a redirect, which means
         * we process the SpEL now (instead of when the page renders).
         */
        try {
            final AppDefinition def = getAppDefinition(req);
            final String configuredUrl = def.getAppUrl();
            final String redirectUrl = expressionProcessor.process(configuredUrl, req);
            res.sendRedirect(redirectUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate or invoke redirectUrl", e);
        }

    }

    @ModelAttribute("appDefinition")
    public AppDefinition getAppDefinition(PortletRequest req) throws WindowStateException {
        return AppDefinition.fromPortletPreferences(req);
    }

}
