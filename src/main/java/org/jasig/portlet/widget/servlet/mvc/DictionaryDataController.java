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
import org.apache.commons.lang.StringEscapeUtils;
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

/**
 * DictionaryDataController handles AJAX requests for a word definition.
 * 
 * @author Jen Bourey
 */
@Controller
@RequestMapping("/ajax/dictionary")
public class DictionaryDataController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected final String DICT_SERVICE_URL = "http://services.aonaware.com/DictService/DictService.asmx/DefineInDict";
	protected final String DICT_ID_PARAM_NAME = "dictId";
	protected final String WORD_PARAM_NAME = "word";

	private IDictionaryParsingService service;
	
	/**
	 * Set the dictionary parsing service to be used to get a definition from 
	 * the DictService server response.
	 * 
	 * @param service
	 */
	@Autowired(required = true)
	public void setDictionaryParsingService(IDictionaryParsingService service) {
		this.service = service;
	}
	
    private Cache cache;

    /**
     * Cache of definitions.
     * 
     * @param cache
     */
    @Required
    @Resource(name = "definitionCache")
    public void setCache(Cache cache) {
            this.cache = cache;
    }

    /**
     * Get a definition for the specified word from the specified dictionary.
     * 
     * @param request
     * @param word
     * @param dict
     * @return
     * @throws Exception
     */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getDefinition(HttpServletRequest request,
			@RequestParam(value="word") String word, 
			@RequestParam(value="dictId") String dict) throws Exception {

		Map<String, String> map = new HashMap<String, String>();

		/*
		 *  Make sure the requesting user has an existing portlet session. 
		 *  This check is designed to prevent the portlet from easily becoming
		 *  an open relay for the DictService server. 
		 */
		HttpSession session = request.getSession(false);
		if (session == null || !((Boolean) session.getAttribute("hasDictionarySession"))) {
			return new ModelAndView("jsonView", map);
		}
		
		/*
		 * Attempt to get the definition from the cache.  If it's not available,
		 * pull it from the server and cache the result
		 */
		
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
		
		// return the definition as JSON data
		map.put("definition", definition);
		return new ModelAndView("jsonView", map);
	}
	
	/**
	 * Get the definition from the server.
	 * 
	 * @param word
	 * @param dict
	 * @return
	 */
	protected String getDefinition(String word, String dict) {
		
		// build the URL for this DictService request
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
				
				// parse the definition from the response
				in = get.getResponseBodyAsStream();
				String def = service.getDefinitionFromXml(in);
				
				// escape any HTML characters
				return StringEscapeUtils.escapeHtml(def);
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
	
	/**
	 * Get a cache key for the specified word and dictionary combination.
	 * 
	 * @param word
	 * @param dict
	 * @return
	 */
	protected String getCacheKey(String word, String dict) {
		return dict.concat(".").concat(word);
	}

}
