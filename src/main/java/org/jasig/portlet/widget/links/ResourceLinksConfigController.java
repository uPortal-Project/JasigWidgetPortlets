package org.jasig.portlet.widget.links;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("CONFIG")
public class ResourceLinksConfigController extends ResourceLinksBaseController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RenderMapping
    public String config(final PortletRequest req) throws Exception {
        return "links/configLinks";
    }

    @ActionMapping
    public void saveResourceLinks(ActionRequest req, ActionResponse res,
                                  @RequestParam(value="save", required = false) String save,
                                  @RequestParam(value="links", required = false) String linksJson) throws PortletException, IOException {

        // validate JSON of links by converting to list of ResouceLink objects
        if (StringUtils.isNotBlank(save)) {
            log.debug(linksJson);
            log.error(linksJson);
            List<ResourceLink> links = ResourceLinkService.jsonArrayToLinkList(linksJson);
            if (links == null || links.isEmpty()) {
                return;  // send them back to config mode
            }
            String[] validLinksJson = ResourceLinkService.linkListToJsonStrArray(links);
            String linkOrder = ResourceLinkService.linkListToOrderString(links);
            // save  to preferences
            req.getPreferences().setValues(PREF_LINK_ATTR, validLinksJson);
            req.getPreferences().setValue(PREF_LINK_ORDER_ATTR, linkOrder);
            req.getPreferences().store();
        }
        res.setPortletMode(PortletMode.VIEW);
    }
/*
    @ModelAttribute("links")
    public List<ResourceLink> getResourceLinks(PortletRequest req) {
        return super.getResourceLinks(req);
    }
*/
    @ModelAttribute("linksJson")
    public String getResourceLinksJsonArray(PortletRequest req) {
        return super.getResourceLinksJson(req);
    }

    @ModelAttribute("groups")
    public Set<String> getGroups() {
        return super.getGroups();
    }

    @ModelAttribute("iconSizePixels")
    public String getIconSizePixels(PortletRequest req) {
        return super.getIconSizePixels(req);
    }
}
