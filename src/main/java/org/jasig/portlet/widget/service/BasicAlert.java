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

import org.springframework.util.Assert;

/**
 * @deprecated This portlet was moved to the Apereo NotificationPortlet project.
 */
public class BasicAlert implements IAlert {

    // Instance Members.
    private final String header;
    private final String body;
    private final String url;
    
    public BasicAlert(String header, String body) {
        this(header, body, null);
    }

    public BasicAlert(String header, String body, String url) {
        
        // Assertions.
        Assert.hasText(header, "Argument 'header' may not be null");
        Assert.hasText(body, "Argument 'body' may not be null");
        
        this.header = header;
        this.body = body;
        this.url = url;
        
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "BasicAlert [header=" + header + ", body=" + body + ", url="
                + url + "]";
    }

}
