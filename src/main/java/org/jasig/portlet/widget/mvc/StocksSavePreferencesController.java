package org.jasig.portlet.widget.mvc;

import java.util.Collections;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.web.portlet.mvc.AbstractAjaxController;

public class StocksSavePreferencesController extends AbstractAjaxController {

	private Log log = LogFactory.getLog(StocksSavePreferencesController.class);
	
	@Override
	protected Map<Object, Object> handleAjaxRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		
		try {
			// save the preferences
			PortletPreferences preferences = request.getPreferences();
			preferences.setValues("stocks", request.getParameterValues("stocks"));
			preferences.store();

		} catch (RuntimeException e) {
			log.error("Error storing stocks preferences", e);
			return Collections.<Object,Object>singletonMap("status", "failure");
		}

        return Collections.<Object,Object>singletonMap("status", "success");

	}
	
}
