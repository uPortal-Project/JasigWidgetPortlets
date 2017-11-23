package org.jasig.portlet.widget.links;

import java.util.List;

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

    private static final Logger log = LoggerFactory.getLogger(ResourceLinksController.class);

    @RenderMapping
    public String view(final PortletRequest req) throws Exception {
        return "links/links";
    }

    @ModelAttribute("links")
    public List<ResourceLink> getResourceLinks(PortletRequest req) {
        return super.getResourceLinks(req);
    }

    @ModelAttribute("iconSizePixels")
    public String getIconSizePixels(PortletRequest req) {
        return super.getIconSizePixels(req);
    }
}
