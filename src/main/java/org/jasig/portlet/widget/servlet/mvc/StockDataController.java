package org.jasig.portlet.widget.servlet.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.jasig.portlet.widget.servlet.mvc.ProxyView;

public class StockDataController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		String stock = request.getParameter("stock");
		String url = "http://quote.yahoo.com/d/quotes.csv?s=" + stock +"&f=sl1d1t1c1ohgvj1pp2wern";
		
		map.put(ProxyView.URL, url);
		return new ModelAndView("proxyView", map);
	}

}
