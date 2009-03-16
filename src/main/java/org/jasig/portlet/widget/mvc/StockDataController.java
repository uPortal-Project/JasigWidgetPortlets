package org.jasig.portlet.widget.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.jasig.portlet.widget.servlet.mvc.ProxyView;
import org.jasig.web.portlet.mvc.AbstractAjaxController;

public class StockDataController extends AbstractAjaxController {

	@Override
	protected Map<Object, Object> handleAjaxRequestInternal(
			ActionRequest request, ActionResponse response) throws Exception {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String url = "http://quote.yahoo.com/d/quotes.csv?s=YHOO&f=sl1d1t1c1ohgvj1pp2wern";
		
		map.put(ProxyView.URL, url);
		return map;
	}

}
