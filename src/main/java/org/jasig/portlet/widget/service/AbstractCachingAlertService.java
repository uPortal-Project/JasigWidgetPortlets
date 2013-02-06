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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @deprecated This portlet was moved to the Apereo NotificationPortlet project.
 */
public abstract class AbstractCachingAlertService implements IAlertService {
    
    public static final String ENABLED_BY_DEFAULT_PREFERENCE = "AbstractCachingAlertService.enabledByDefault";
    public static final String MAX_ITEMS_PREFERENCE = "AbstractCachingAlertService.maxItems";

    private static final long MILLISECONDS_IN_TWO_MINUTES = 2L * 60L * 1000L;
    private static final Object LOCK = new Object(); 

    // Instance Members.
    private Boolean enabled = null;
    private long cacheTimeout = MILLISECONDS_IN_TWO_MINUTES;
    private volatile long whenGotten = 0L;
    private final List<IAlert> feed = new CopyOnWriteArrayList<IAlert>();
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
     * Return the current alerts, if any.
     */
    @Override
    public final List<IAlert> fetch(final PortletRequest req) {
        
        PortletPreferences prefs = req.getPreferences();
        
        // Provide a means to disable the system
        boolean doFetch = enabled != null
                    ? enabled
                    : Boolean.valueOf(prefs.getValue(ENABLED_BY_DEFAULT_PREFERENCE, "true"));
        if (!doFetch) return Collections.emptyList();
        
        final int maxItems = Integer.parseInt(prefs.getValue(MAX_ITEMS_PREFERENCE, "4"));

        // The alerts system is active;  refresh if required
        if (isFeedExpired()) {
            if (log.isDebugEnabled()) {
                log.debug("About to start a thread to refreash the feed");
            }
            
            // Fetch in a separate Thread & return what we already have
            Thread refreshThread = new Thread() {
                public void run() {
                    try {
                        synchronized(LOCK) {
                            if (isFeedExpired()) {

                                // Even if we fail, don't try more often than the TTL value
                                whenGotten = System.currentTimeMillis();

                                List<IAlert> list = getFeedFromSource(req);
                                if (list.size() > maxItems) {
                                    list = list.subList(0, maxItems);
                                }

                                // Any valid response from the service means
                                // clear the existing alert and show new info
                                feed.clear();  
                                feed.addAll(list);

                            }
                        }
                    } catch (Exception e) {
                        // Catch-all for any other problem
                        log.error("Failed to update the Alert Feed", e);
                    }
                }
            };
            refreshThread.setDaemon(true);
            refreshThread.start();
        }
        
        return Collections.unmodifiableList(feed);

    }

    /*
     * Protected API.
     */

    protected abstract List<IAlert> getFeedFromSource(PortletRequest req);

    /*
     * Implementation
     */

    private boolean isFeedExpired() {
        final long whenExpires = whenGotten + cacheTimeout;
        // Even if the feed is bad, we don't want to request it more 
        // often than the TTL;  constantly requesting a bad feed leads 
        // to 'Too many open files' and makes the portal unusable. 
        return System.currentTimeMillis() > whenExpires;
    }

}
