package org.jasig.portlet.widget.mvc;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("CONFIG")
public class EditGoogleApiKeyController {
	
	public static final String GOOGLE_API_KEY_PREF_NAME = "googleApiKey";

	@RequestMapping
	public String getFormView(RenderResponse response) {
		return "editGoogleApiKey";
	}

	@RequestMapping(params = "action=updateKey")
	public void updateKey(ActionRequest request, ActionResponse response,
			@RequestParam(value = GOOGLE_API_KEY_PREF_NAME) String key)
			throws PortletModeException, ReadOnlyException, ValidatorException, IOException {
		
		PortletPreferences prefs = request.getPreferences();
		prefs.setValue(GOOGLE_API_KEY_PREF_NAME, key);
		prefs.store();
		response.setPortletMode(PortletMode.VIEW);
	}

	@ModelAttribute(GOOGLE_API_KEY_PREF_NAME)
	public String getKey(PortletRequest request) {
		PortletPreferences prefs = request.getPreferences();
		return prefs.getValue(GOOGLE_API_KEY_PREF_NAME, null);
	}

}
