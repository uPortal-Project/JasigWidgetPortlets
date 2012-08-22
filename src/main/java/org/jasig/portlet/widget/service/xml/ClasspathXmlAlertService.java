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

import java.io.IOException;
import java.io.InputStream;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

/**
 * Especially useful for demo data.  Not sure how seful otherwise.
 * 
 * @author awills
 *
 */
public final class ClasspathXmlAlertService extends AbstractXmlAlertService implements ApplicationContextAware {
    
    private static final String LOCATION_PREFERENCE = "ClasspathXmlAlertService.feedLocation";
            
    private ApplicationContext ctx;
    private final Log log = LogFactory.getLog(getClass());
    
    /*
     * Public API.
     */

    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        this.ctx = ctx;
    }
    
    /*
     * Implementation.
     */

    @Override
    protected Document loadDocument(PortletRequest req) {

        if (log.isInfoEnabled()) {
            log.info("Updating feed from external source");
        }
        
        Document rslt = null;
        
        final String loc = req.getPreferences().getValue(LOCATION_PREFERENCE, null);

        InputStream inpt = null;

        try {
            Resource r = ctx.getResource(loc);
            inpt = r.getInputStream();

            // Parse the response body into a document
            rslt = new SAXReader().read(inpt);

        } catch (Exception e) {
            // We'll just run with what we had... either a Document or perhaps null
            throw new RuntimeException(e);
        } finally {
            // Close the inpt stream
            if (inpt != null) {
                try {
                    inpt.close();
                } catch (IOException ioe) {
                    log.warn(ioe.getMessage(), ioe);
                }
            }
        }
        
        return rslt;

    }

}
