package org.jasig.portlet.widget.links;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("CONFIG")
public class ResourceLinksConfigController extends ResourceLinksBaseController {

    private static final Logger log = LoggerFactory.getLogger(ResourceLinksConfigController.class);

    @RenderMapping
    public String config(final PortletRequest req) throws Exception {
        return "links/configLinks";
    }

    @ActionMapping
    public void saveResourceLinks(ActionRequest req, ActionResponse res) throws PortletModeException {
        // validate JSON of links

        // validate ordered list of link titles

        // save  to preferences
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
