package org.jasig.portlet.widget.servlet.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class DictionaryDataController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		String word = request.getParameter("word");
		String dict = request.getParameter("dictId");
		String url = "http://services.aonaware.com/DictService/DictService.asmx/DefineInDict?dictId=" + dict + "&word=" + word;
		
		map.put(ProxyView.URL, url);
		return new ModelAndView("proxyView", map);
	}

}
