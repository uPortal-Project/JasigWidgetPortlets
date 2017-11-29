package org.jasig.portlet.widget.links;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
public class ResourceLinksController extends ResourceLinksBaseController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

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
