package org.jasig.portlet.widget.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class GoogleSearchController extends AbstractController {
	
	private String apiKey = null;
	
	public void setApiKey(String key) {
		this.apiKey = key;
	}
	
	private static final String[] defaultSearchEngines = new String[]{ "web", "news" };

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		
		PortletPreferences preferences = request.getPreferences();
		map.put("key", preferences.getValue("key", this.apiKey));
		map.put("searchEngines", 
				preferences.getValues("searchEngines", defaultSearchEngines));
		
		return new ModelAndView("googleSearch", map);
	}

}
