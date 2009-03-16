package org.jasig.portlet.widget.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class StocksController extends AbstractController {

	private String apiKey = null;
	
	public void setApiKey(String key) {
		this.apiKey = key;
	}
	
	public String[] stocks = new String[]{"yhoo","goog"};
	
	public void setStocks(String[] stocks) {
		this.stocks = stocks;
	}
	
	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		
		PortletPreferences preferences = request.getPreferences();
		map.put("key", preferences.getValue("key", this.apiKey));
		map.put("stocks", preferences.getValues("stocks", this.stocks));
		
		return new ModelAndView("stocks", map);
	}


}
