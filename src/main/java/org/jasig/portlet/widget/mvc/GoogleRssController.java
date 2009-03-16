package org.jasig.portlet.widget.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class GoogleRssController extends AbstractController {
	
	private String apiKey = null;
	
	public void setApiKey(String key) {
		this.apiKey = key;
	}
	
	private String[] defaultFeedUrls = new String[]{
			"http://www.nytimes.com/services/xml/rss/nyt/HomePage.xml", 
			"http://newsrss.bbc.co.uk/rss/newsonline_world_edition/front_page/rss.xml"};
	private String[] defaultFeedNames = new String[]{"NY Times", "BBC"};

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		
		PortletPreferences preferences = request.getPreferences();
		map.put("key", preferences.getValue("key", this.apiKey));
		map.put("feedUrls", 
				preferences.getValues("feedUrls", defaultFeedUrls));
		map.put("feedNames", 
				preferences.getValues("feedNames", defaultFeedNames));
		
		return new ModelAndView("googleRss", map);
	}

}
