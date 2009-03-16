package org.jasig.portlet.widget.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class GoogleTranslateController extends AbstractController {

	private String apiKey = null;
	
	public void setApiKey(String key) {
		this.apiKey = key;
	}
	
	private static final String[] languages = new String[]{"sq", "ar", "bg", "zh", "ca",
			"hr", "cs", "da", "nl", "en", "et", "fi", "fr", "gl", "de", "el",
			"he", "hi", "hu", "id", "it", "ja", "ko", "lv", "lt", "ml", "no", 
			"pl", "pt", "ro", "ru", "es", "sr", "sk", "sl", "sv", "th", "tr", 
			"uk", "vi"};
	
	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		
		PortletPreferences preferences = request.getPreferences();
		map.put("key", preferences.getValue("key", this.apiKey));
		map.put("languages", languages);
		
		return new ModelAndView("googleTranslate", map);
	}

}
