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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
@Slf4j
public class ResourceLinksController extends ResourceLinksBaseController {

    @RenderMapping
    public String view(final PortletRequest req) throws Exception {
        Set<String> groups = getGroups();
        return "links/links";
    }

    @ModelAttribute("links")
    public List<ResourceLink> getResourceLinks(PortletRequest req) {
        final List<ResourceLink> filteredLinks = new ArrayList<>();
        final List<ResourceLink> allLinks = super.getResourceLinks(req);
        for (ResourceLink link : allLinks) {
            if (link.getGroups().isEmpty()) {
                filteredLinks.add(link);
            } else {
                boolean inGroup = link.getGroups().stream().anyMatch(req::isUserInRole);
                if (inGroup) {
                    filteredLinks.add(link);
                }
            }
        }
        return filteredLinks;
    }

    @ModelAttribute("iconSizePixels")
    public String getIconSizePixels(PortletRequest req) {
        return super.getIconSizePixels(req);
    }
}
