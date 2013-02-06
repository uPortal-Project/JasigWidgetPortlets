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

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.jasig.portlet.widget.service.AbstractCachingAlertService;
import org.jasig.portlet.widget.service.BasicAlert;
import org.jasig.portlet.widget.service.IAlert;

/**
 * @deprecated This portlet was moved to the Apereo NotificationPortlet project.
 */
public abstract class AbstractXmlAlertService extends AbstractCachingAlertService {

    private static final String XPATH_ALERT = "//Alert";
    private static final String XPATH_ALERT_ENABLED_FLAG = ".//AlertFlag[text() = '1']";
    private static final String XPATH_HEAD = ".//AlertHead";
    private static final String XPATH_BODY = ".//AlertMsg/node()";
    private static final String XPATH_URL = ".//AlertLink/*";
    
    // Instance Members.
    private final Log log = LogFactory.getLog(getClass());

    /*
     * Protected API.
     */
    
    protected final List<IAlert> getFeedFromSource(PortletRequest req) {
        
        final List<IAlert> rslt = new ArrayList<IAlert>();

        final Document doc = loadDocument(req);
        if (log.isDebugEnabled()) {
            final String xml = doc != null 
                            ? "\n" + doc.asXML() 
                            : "  [null]";
            log.debug("Received the following Alert XML:" + xml);
        }

        if (doc != null) {
            
            @SuppressWarnings("unchecked")
            final List<Node> nodes = doc.selectNodes(XPATH_ALERT); 
            for (Node n : nodes) {
                if (n.selectSingleNode(XPATH_ALERT_ENABLED_FLAG) != null) {
                    final String head = n.valueOf(XPATH_HEAD);
                    final StringBuilder body = new StringBuilder();
                    @SuppressWarnings("unchecked")
                    final List<Node> bodyContent = n.selectNodes(XPATH_BODY);
                    for (Node b : bodyContent) {
                        body.append(b.asXML());
                    }
                    String url = null;  // default
                    final Node urlNode = n.selectSingleNode(XPATH_URL);
                    if (urlNode != null && urlNode.asXML().trim().length() != 0) {
                        url = urlNode.asXML().trim();
                    }
                    IAlert alert = new BasicAlert(head, body.toString(), url);
                    if (log.isDebugEnabled()) {
                        log.debug("Adding the following alert:  " + alert);
                    } 
                    rslt.add(alert);
                }
            }
            
        }
        
        return rslt;
        
    }
    
    protected abstract Document loadDocument(PortletRequest req);

}
