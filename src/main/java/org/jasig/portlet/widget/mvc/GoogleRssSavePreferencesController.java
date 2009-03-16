package org.jasig.portlet.widget.mvc;

import java.util.Collections;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.web.portlet.mvc.AbstractAjaxController;

public class GoogleRssSavePreferencesController extends AbstractAjaxController {

	private Log log = LogFactory.getLog(GoogleRssSavePreferencesController.class);
	
	@Override
	protected Map<Object, Object> handleAjaxRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		
		try {
			// save the preferences
			PortletPreferences preferences = request.getPreferences();
			preferences.setValues("feedNames", request.getParameterValues("feedNames"));
			preferences.setValues("feedUrls", request.getParameterValues("feedUrls"));
			preferences.store();

		} catch (RuntimeException e) {
			log.error("Error storing Google rss preferences", e);
			return Collections.<Object,Object>singletonMap("status", "failure");
		}

        return Collections.<Object,Object>singletonMap("status", "success");

	}
	
}
