/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.widget.servlet.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ajax/stock")
public class StockDataController {

	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView getStockQuotes(HttpServletRequest request) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		String stock = request.getParameter("stock");
		String url = "http://quote.yahoo.com/d/quotes.csv?s=" + stock +"&f=sl1d1t1c1ohgvj1pp2wern";
		
		map.put(ProxyView.URL, url);
		return new ModelAndView("proxyView", map);
	}

}
