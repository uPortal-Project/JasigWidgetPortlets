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
package org.jasig.portlet.widget.links;

import java.util.List;
import java.util.Set;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.context.PortletConfigAware;

@Slf4j
public class ResourceLinksBaseController implements PortletConfigAware {

    protected static final String PREF_LINK_ATTR = "resource-link";
    protected static final String PREF_ICON_SIZE_PIXELS = "icon-size";
    protected static final String DEFAULT_ICON_SIZE_PIXELS = "200";

    protected PortletConfig portletConfig;
    @Autowired protected PortletXmlGroupService groupService;

    public List<ResourceLink> getResourceLinks(PortletRequest req) {
        final PortletPreferences preferences = req.getPreferences();
        final String[] prefLinks = preferences.getValues(PREF_LINK_ATTR, null);
        return ResourceLinkService.createResourceLinkList(prefLinks);
    }

    public String getResourceLinksJson(PortletRequest req) {
        final PortletPreferences preferences = req.getPreferences();
        final String[] prefLinks = preferences.getValues(PREF_LINK_ATTR, null);
        return ResourceLinkService.convertStringArrayToJsonArray(prefLinks);
    }

    public String getIconSizePixels(PortletRequest req) {
        final PortletPreferences preferences = req.getPreferences();
        return preferences.getValue(PREF_ICON_SIZE_PIXELS, DEFAULT_ICON_SIZE_PIXELS);
    }

    protected Set<String> getGroups() {
        final String portletName = portletConfig.getPortletName();
        return groupService.getGroups(portletName);
    }

    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }
}
