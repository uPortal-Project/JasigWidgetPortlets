package org.jasig.portlet.widget.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class DictionaryController extends AbstractController {

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		
		PortletPreferences preferences = request.getPreferences();
		return new ModelAndView("dictionary", map);
	}

}
