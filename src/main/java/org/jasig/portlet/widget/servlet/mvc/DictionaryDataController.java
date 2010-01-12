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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.widget.service.IDictionaryParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ajax/dictionary")
public class DictionaryDataController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected final String DICT_SERVICE_URL = "http://services.aonaware.com/DictService/DictService.asmx/DefineInDict";
	protected final String DICT_ID_PARAM_NAME = "dictId";
	protected final String WORD_PARAM_NAME = "word";

	private IDictionaryParsingService service;
	
	@Autowired(required = true)
	public void setDictionaryParsingService(IDictionaryParsingService service) {
		this.service = service;
	}
	
    private Cache cache;

    @Required
    @Resource(name = "definitionCache")
    public void setCache(Cache cache) {
            this.cache = cache;
    }

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getDefinition(HttpServletRequest request,
			@RequestParam(value="word") String word, 
			@RequestParam(value="dictId") String dict) throws Exception {

		Map<String, String> map = new HashMap<String, String>();

		HttpSession session = request.getSession(false);
		if (session == null || !((Boolean) session.getAttribute("hasDictionarySession"))) {
			return new ModelAndView("jsonView", map);
		}
		
		String cacheKey = getCacheKey(word, dict);
		Element cachedElement = cache.get(cacheKey);
		String definition;
		if (cachedElement == null) {
			definition = getDefinition(word, dict);
			cachedElement = new Element(cacheKey, definition);
			cache.put(cachedElement);
		} else {
			definition = (String) cachedElement.getValue();			
		}
		
		map.put("definition", definition);
		return new ModelAndView("jsonView", map);
	}
	
	protected String getDefinition(String word, String dict) {
		StringBuffer url = new StringBuffer();
		url.append(DICT_SERVICE_URL);
		url.append("?").append(DICT_ID_PARAM_NAME).append("=").append(dict);
		url.append("&").append(WORD_PARAM_NAME).append("=").append(word);

		HttpClient client = new HttpClient();
		GetMethod get = null;
		InputStream in = null;
		
		try {

			get = new GetMethod(url.toString());
			int rc = client.executeMethod(get);
			if(rc == HttpStatus.SC_OK) {
				in = get.getResponseBodyAsStream();
				return service.getDefinitionFromXml(in);
			}
			else {
				log.warn("Failed to retrieve dictionary feed at " + url.toString() + ":" + rc);
			}
		} catch (HttpException e) {
			log.warn("Error proxying url", e);
		} catch (IOException e) {
			log.warn("Error proxying url", e);
		} finally {
			if (get != null) {
				get.releaseConnection();
			}
		}
		return null;
	}
	
	protected String getCacheKey(String word, String dict) {
		return dict.concat(".").concat(word);
	}
	
}
