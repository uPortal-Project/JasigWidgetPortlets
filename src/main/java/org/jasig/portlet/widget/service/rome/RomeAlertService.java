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

package org.jasig.portlet.widget.service.rome;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.jasig.portlet.widget.service.AbstractCachingAlertService;
import org.jasig.portlet.widget.service.IAlert;
import org.springframework.util.Assert;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @deprecated This portlet was moved to the Apereo NotificationPortlet project.
 */
public class RomeAlertService extends AbstractCachingAlertService {

    public static final String URL_PREFERENCE = "RomeAlertService.url";

    private static final Object NO_FURTHER_INFORMATION = "No further information is available.";

    /*
     * Protected API.
     */
    
    @Override
    protected List<IAlert> getFeedFromSource(PortletRequest req) {

        final List<TimestampAlert> rslt = new ArrayList<TimestampAlert>();
        
        final PortletPreferences prefs = req.getPreferences();
        final String urlPref = prefs.getValue(URL_PREFERENCE, null);
        Assert.hasText(urlPref, "Preference " + URL_PREFERENCE + " must be set");
        
        try {

            final URL url = new URL(urlPref);
            
            final XmlReader reader = new XmlReader(url);
            final SyndFeedInput input = new SyndFeedInput();
            final SyndFeed feed = input.build(reader);
            
            @SuppressWarnings("unchecked")
            final List<SyndEntry> entries = feed.getEntries(); 
            for (final SyndEntry y : entries) {

                final String header = y.getTitle();
                StringBuilder body = new StringBuilder();
                
                // Prefer description
                final SyndContent desc = y.getDescription();
                if (desc != null) {
                    body.append(desc.getValue());
                }
                // Fall back to contents
                if (body.length() == 0) {
                    @SuppressWarnings("unchecked")
                    final List<SyndContent> contents = y.getContents();
                    for (final SyndContent c : contents) {
                        body.append(c.getValue());
                    }
                }
                // Last resort... 
                if (body.length() == 0) {
                    body.append(NO_FURTHER_INFORMATION);
                }

                final String link = y.getLink();
                final long timestamp = y.getPublishedDate().getTime();
                rslt.add(new TimestampAlert(header, body.toString(), link, timestamp));

            }
            
            // Items should be in reverse chronological order
            Collections.sort(rslt);
            Collections.reverse(rslt);

        } catch (Exception e) {
            String msg = "Unable to read the specified feed:  " + urlPref;
            throw new RuntimeException(msg, e);
        }
        
        return new ArrayList<IAlert>(rslt);

    }

}
