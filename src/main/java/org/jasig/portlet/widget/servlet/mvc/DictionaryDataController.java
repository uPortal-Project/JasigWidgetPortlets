/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.widget.servlet.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;


/**
 * DictionaryDataController handles AJAX requests for a word definition.
 *
 * Uses the Free Dictionary API (https://dictionaryapi.dev/) which requires
 * no API key and returns JSON directly. The dictId parameter is retained for
 * API compatibility but ignored since the Free Dictionary API does not support
 * multiple dictionary sources.
 */
@Controller
@RequestMapping("/ajax/dictionary")
public class DictionaryDataController {

    protected final Log log = LogFactory.getLog(getClass());

    private static final String DICT_API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/{word}";

    private final RestTemplate restTemplate = new RestTemplate();
    private Cache cache;

    @Required
    @Resource(name = "definitionCache")
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getDefinition(HttpServletRequest request,
            @RequestParam(value = "word") String word,
            @RequestParam(value = "dictId", required = false) String dict) throws Exception {

        Map<String, String> map = new HashMap<String, String>();

        // Ensure the requesting user has an existing portlet session to
        // prevent this endpoint from becoming an open relay.
        HttpSession session = request.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("hasDictionarySession"))) {
            return new ModelAndView("jsonView", map);
        }

        String cacheKey = "freedict." + word.toLowerCase();
        Element cachedElement = cache.get(cacheKey);
        String definition;

        if (cachedElement != null) {
            definition = (String) cachedElement.getValue();
        } else {
            definition = fetchDefinition(word);
            cache.put(new Element(cacheKey, definition));
        }

        map.put("definition", definition);
        return new ModelAndView("jsonView", map);
    }

    private String fetchDefinition(String word) {
        try {
            JsonNode[] entries = restTemplate.getForObject(DICT_API_URL, JsonNode[].class, word);
            if (entries == null || entries.length == 0) {
                return null;
            }

            StringBuilder sb = new StringBuilder();
            for (JsonNode entry : entries) {
                JsonNode meanings = entry.path("meanings");
                for (JsonNode meaning : meanings) {
                    String partOfSpeech = meaning.path("partOfSpeech").asText("");
                    if (!partOfSpeech.isEmpty()) {
                        sb.append("<strong>").append(partOfSpeech).append("</strong><br/>");
                    }
                    JsonNode definitions = meaning.path("definitions");
                    int count = 0;
                    for (JsonNode def : definitions) {
                        if (count++ >= 3) break; // limit to 3 definitions per part of speech
                        sb.append("<p>").append(def.path("definition").asText("")).append("</p>");
                    }
                }
            }
            return sb.length() > 0 ? sb.toString() : null;

        } catch (Exception e) {
            log.warn("Failed to retrieve definition for word: " + word, e);
            return null;
        }
    }
}
