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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("VIEW")
public class CloudDriveController {
    public static String GOOGLE_API_KEY = "googleDriveAPIKey";
    public static String DROPBOX_API_KEY = "dropboxAPIKey";
    private static String GOOGLE_DRIVE_ENABLED = "googleDriveEnabled";
    private static String DROPBOX_ENABLED = "dropboxEnabled";
    private static String DEFAULT_VALUE = "default";

    private final Log log = LogFactory.getLog(getClass());
    @RenderMapping()
    public ModelAndView handleRenderRequest(RenderRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PortletPreferences prefs = request.getPreferences();

        final String googleDriveAPIKey = prefs.getValue(GOOGLE_API_KEY,DEFAULT_VALUE);

        if(googleDriveAPIKey.equals(DEFAULT_VALUE)) {
            map.put(GOOGLE_DRIVE_ENABLED,false);
            if(log.isDebugEnabled()) {
                log.debug("GoogleDrive is not enabled.");
            }
        } else {
            map.put(GOOGLE_API_KEY,googleDriveAPIKey);
            map.put(GOOGLE_DRIVE_ENABLED,true);
        }

        final String dropboxAPIKey = prefs.getValue(DROPBOX_API_KEY,DEFAULT_VALUE);
        if(dropboxAPIKey.equals(DEFAULT_VALUE)) {
            map.put(DROPBOX_ENABLED,false);
            if(log.isDebugEnabled()) {
                log.debug("Dropbox is not enabled.");
            }
        } else {
            map.put(DROPBOX_API_KEY,dropboxAPIKey);
            map.put(DROPBOX_ENABLED,true);
        }


        return new ModelAndView("cloudDrive", map);
    }

}
