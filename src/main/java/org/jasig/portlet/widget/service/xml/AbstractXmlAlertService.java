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

package org.jasig.portlet.widget.service.xml;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.jasig.portlet.widget.service.IAlertService;

public abstract class AbstractXmlAlertService implements IAlertService {

    private static final String XPATH_FLAG = "//AlertFlag[text() = '1']";
    private static final String XPATH_HEAD = "//AlertHead";
    private static final String XPATH_MESSAGE = "//AlertMsg";
    private static final String XPATH_LINK = "//AlertLink/*";
    
    private static final long MILLISECONDS_IN_TWO_MINUTES = 2L * 60L * 1000L;
    public static final String ENABLED_BY_DEFAULT_PREFERENCE = "AbstractXmlAlertService.enabledByDefault";

    // Instance Members.
    private Boolean enabled = null;
    private long cacheTimeout = MILLISECONDS_IN_TWO_MINUTES;
    private volatile long whenGotten = 0L;
    private final Map<String, String> feed = new ConcurrentHashMap<String, String>();
    private final Log log = LogFactory.getLog(getClass());

    /*
     * Public API.
     */

    /**
     * Important -- may be null.
     */
    @Override
    public final Boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public final void setEnabled(Boolean b) {
        this.enabled = b;
    }
    
    public final void setCacheTimeout(long cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
    }

    /**
     * Return the current alert, if there is one, or <code>null</code> otherwise.
     */
    @Override
    public final Map<String, String> fetch(final PortletRequest req) {
        
        PortletPreferences prefs = req.getPreferences();
        
        // Provide a means to disable the system
        boolean doFetch = enabled != null
                    ? enabled
                    : Boolean.valueOf(prefs.getValue(ENABLED_BY_DEFAULT_PREFERENCE, "true"));
        if (!doFetch) return Collections.emptyMap();

        // The alerts system is active;  refresh if required
        if (isFeedExpired()) {
            if (log.isDebugEnabled()) {
                log.debug("About to start a thread to refreash the feed");
            }
            
            // Fetch in a separate Thread & return what we already have
            Thread refreshThread = new Thread() {
                public void run() {
                    try {
                        Document doc = getFeedFromSource(req);
                        if (log.isDebugEnabled()) {
                            String xml = doc != null 
                                            ? "\n" + doc.asXML() 
                                            : "  [null]";
                            log.debug("Received the following Alert XML:" + xml);
                        }
                        // Any valid response from the service means clear 
                        // the existing alert and show new info
                        feed.clear();  
                        if (doc != null && doc.selectSingleNode(XPATH_FLAG) != null) {
                            String alertHead = doc.valueOf(XPATH_HEAD);
                            feed.put("alertHead", alertHead);
                            String alertMsg = doc.valueOf(XPATH_MESSAGE);
                            feed.put("alertMsg", alertMsg);
                            String alertLink = doc.selectSingleNode(XPATH_LINK).asXML();
                            feed.put("alertLink", alertLink);
                        }
                    } catch (Exception e) {
                        // Catch-all for any other problem
                        log.error("Failed to update the Alert Feed", e);
                    }
                    // Even if we failed, don't try move than the TTL value
                    whenGotten = System.currentTimeMillis();
                }
            };
            refreshThread.setDaemon(true);
            refreshThread.start();
        }
        
        return Collections.unmodifiableMap(feed);

    }

    /*
     * Implementation
     */
    
    protected abstract Document getFeedFromSource(PortletRequest req);

    protected final boolean isFeedExpired() {
        final long whenExpires = whenGotten + cacheTimeout;
        // Even if the feed is bad, we don't want to request it more 
        // often than the TTL;  constantly requesting a bad feed leads 
        // to 'Too many open files' and makes the portal unusable. 
        return System.currentTimeMillis() > whenExpires;
    }

}
