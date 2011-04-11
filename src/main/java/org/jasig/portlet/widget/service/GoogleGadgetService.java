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

package org.jasig.portlet.widget.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jasig.portlet.widget.gadget.model.Module;

public class GoogleGadgetService {
    
    private static final String GADGET_BASE_URL = "http://www.google.com/ig/directory?synd=open";
    
    private static final Pattern categoriesPattern = Pattern.compile(
            "<a id=\"[a-zA-z]*\" href=\"/ig/directory\\?synd=open&amp;cat=(.*?)\">(.*?)</a>", 
            Pattern.DOTALL
        );

    private static final Pattern gadgetEntryPattern = Pattern.compile(
            "class=\"promoTitle\"><a class=\"\" href=\"/ig/directory\\?synd=open&amp;(?:cat=[a-z]*&amp;)?url=(.*?)\">(.*?)</a>",
            Pattern.DOTALL
        );

    private static final Pattern gadgetImagePattern = Pattern.compile(
            "<img src=\"http://www.gmodules.com/gadgets/proxy\\?refresh=86400&amp;url=(.*?)&amp;container=open&amp;gadget=(.*?)\"",
            Pattern.DOTALL
        );
    
    private static final Pattern resultCountPattern = Pattern.compile(
            "<span class=\"resultCount\">Results <b>[0-9]* - [0-9]*</b> of <b>([0-9]*?)</b></span>", 
            Pattern.DOTALL
        );
    
    protected final Log log = LogFactory.getLog(getClass());

    public List<GadgetCategory> getCategories() {
        
        List<GadgetCategory> categories = new ArrayList<GadgetCategory>();
        
        String html;
        try {
            html = getStringContentFromUrl(GADGET_BASE_URL);
            
            Matcher matcher = categoriesPattern.matcher(html);

            while (matcher.find()) {
                String categoryId = matcher.group(1);
                String name = matcher.group(2);
                categories.add(new GadgetCategory(categoryId, name));
            }
            Collections.sort(categories);

            return categories;
        } catch (ClientProtocolException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }
    
    public GadgetList getGadgets(String query, String category, int start) {
        
        StringBuffer url = new StringBuffer();
        url.append(GADGET_BASE_URL);
        
        if (StringUtils.isNotBlank(query)) {
            url.append("&q=").append(query);
        }

        if (StringUtils.isNotBlank(category)) {
            url.append("&cat=").append(category);
        }

        if (start > 0) {
            url.append("&start=").append(start);
        }

        return getGadgetsForUrl(url.toString(), gadgetEntryPattern);
    }
    
    public Module getModule(String url) {
        
        Module module = null;
        
        try {
            
            InputStream stream = getStreamFromUrl(url);        
            JAXBContext jc = JAXBContext.newInstance(Module.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            module = (Module) unmarshaller.unmarshal(stream);
            
        } catch (JAXBException e) {
            log.error(e);
        } catch (ClientProtocolException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
        
        return module;
    }
    
    protected GadgetList getGadgetsForUrl(String url, Pattern entryPattern) {
        
        try {
            String html = getStringContentFromUrl(url);
            List<GadgetEntry> entries = new ArrayList<GadgetEntry>();
            
            Map<String,String> images = new HashMap<String,String>();
            Matcher imageMatcher = gadgetImagePattern.matcher(html);
            while (imageMatcher.find()) {
                images.put(imageMatcher.group(2), imageMatcher.group(1));
            }
            
            Matcher entryMatcher = entryPattern.matcher(html);
            while (entryMatcher.find()) {
                String gadgetId = entryMatcher.group(1);
                String name = entryMatcher.group(2);
                String imageUrl = images.get(gadgetId);

                GadgetEntry entry = new GadgetEntry(gadgetId, name, imageUrl);
                entries.add(entry);
            }
            
            int results = 0;
            Matcher resultCountMatcher = resultCountPattern.matcher(html);
            if (resultCountMatcher.find()) {
                results = Integer.parseInt(resultCountMatcher.group(1));
            }
            
            GadgetList list = new GadgetList(results, entries);
            return list;
            
        } catch (ClientProtocolException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
        
        return null;
        
    }
    
    protected String getStringContentFromUrl(String url) throws ClientProtocolException, IOException {
        InputStream input = getStreamFromUrl(url);
        String content = null;
        try {
            content = IOUtils.toString(input, "UTF-8");
        } catch (IOException e) {
            log.error(e);
        } finally {
            IOUtils.closeQuietly(input);
        }
         
        return content;
    }
    protected InputStream getStreamFromUrl(String url) throws ClientProtocolException, IOException {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream stream = null;
        if (entity != null) {
            stream = entity.getContent();
        }
        return stream;
    }
    
    public final class GadgetCategory implements Comparable<GadgetCategory> {
        private final String key;
        private final String displayName;
        
        public GadgetCategory(String key, String displayName) {
            this.key = key;
            this.displayName = displayName;
        }

        public String getKey() {
            return key;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public int compareTo(GadgetCategory category) {
            return this.displayName.compareTo(category.getDisplayName());
        }

    }

    public final class GadgetList {

        private final int total;
        private final List<GadgetEntry> gadgets;

        public GadgetList(int total, List<GadgetEntry> gadgets) {
            this.total = total;
            this.gadgets = gadgets;
        }

        public int getTotal() {
            return total;
        }

        public List<GadgetEntry> getGadgets() {
            return gadgets;
        }

    }

    public final class GadgetEntry {

        private final String configUrl;
        private final String name;
        private final String imageUrl;

        public GadgetEntry(String configUrl, String name, String imageUrl) {
            this.configUrl = configUrl;
            this.name = name;
            this.imageUrl = imageUrl;
        }

        public String getConfigUrl() {
            return configUrl;
        }

        public String getName() {
            return name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

    }

}
