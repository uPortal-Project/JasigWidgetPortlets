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

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;

import javax.portlet.PortletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

/**
 * @deprecated This portlet was moved to the Apereo NotificationPortlet project.
 */
public final class HttpXmlAlertService extends AbstractXmlAlertService {
    
    private static final String URL_PREFERENCE = "HttpXmlAlertService.feedUrl";
            
    private final Log log = LogFactory.getLog(getClass());
    
    /*
     * Implementation.
     */

    @Override
    protected Document loadDocument(PortletRequest req) {

        if (log.isInfoEnabled()) {
            log.info("Updating feed from external source");
        }
        
        Document rslt = null;
        
        final String url = req.getPreferences().getValue(URL_PREFERENCE, null);

        HttpClient client = new HttpClient();
        HttpMethod get = new GetMethod(url.toString());
        Reader inpt = null;

        try {
            // Execute the method
            int statusCode = client.executeMethod(get);

            if (statusCode != HttpStatus.SC_OK) {
                String msg = "Method failed: " + get.getStatusLine();
                throw new RuntimeException(msg);
            }

            // Parse the response body into a document
            byte[] responseBody = get.getResponseBody();
            String xml = new String(responseBody).trim();
            inpt = new CharArrayReader(xml.toCharArray());
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
            // Release the connection.
            get.releaseConnection();
        }
        
        return rslt;

    }

}
