package org.jasig.portlet.widget.links;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLinksBaseController {

    private static final Logger log = LoggerFactory.getLogger(ResourceLinksBaseController.class);
    protected static final String PREF_LINK_ORDER_ATTR = "link-order";
    protected static final String PREF_LINK_ATTR = "resource-link";
    protected static final String PREF_ICON_SIZE_PIXELS = "icon-size";
    protected static final String DEFAULT_ICON_SIZE_PIXELS = "200";

    public List<ResourceLink> getResourceLinks(PortletRequest req) {
        final PortletPreferences preferences = req.getPreferences();
        final String[] prefLinks = preferences.getValues(PREF_LINK_ATTR, null);
        final String prefLinkOrder = preferences.getValue(PREF_LINK_ORDER_ATTR, "");
        final String[] linkOrder = prefLinkOrder.split("\\s*,\\s*");
        final Map<String, ResourceLink> linksByTitle = ResourceLinkService.jsonStringArrayToMapByTitle(prefLinks);
        return ResourceLinkService.createOrderedLinkList(linkOrder, linksByTitle);
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
}
