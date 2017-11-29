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
package org.jasig.portlet.widget.links;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.jaxen.NamespaceContext;
import org.jaxen.SimpleNamespaceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PortletXmlGroupService {
    private static final String PORTLET_XML_PATH = "/WEB-INF/portlet.xml";

    private Document doc;
    private NamespaceContext namespaceContext;
    private XPath xpathRoleName;

    @Autowired
    private ServletContext servletContext;

    @PostConstruct
    public void init() {
        try {
            URL portletXmlUrl = servletContext.getResource(PORTLET_XML_PATH);
            SAXReader reader = new SAXReader();
            doc = reader.read(portletXmlUrl);
            HashMap<String, String> namespaces = new HashMap<>();
            namespaces.put("p", "http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd");
            namespaceContext = new SimpleNamespaceContext(namespaces);
            xpathRoleName = doc.createXPath("p:role-name");
            xpathRoleName.setNamespaceContext(namespaceContext);
        } catch (Exception e) {
            log.error("Cannot load {}", PORTLET_XML_PATH, e);
        }
    }

    Set<String> getGroups(final String portletName) {
        if (doc == null) {
            return null;
        }
        final String xpathStr = "//p:portlet[p:portlet-name='" + portletName +"']/p:security-role-ref";
        XPath xpath = doc.createXPath(xpathStr);
        xpath.setNamespaceContext(namespaceContext);
        List<Node> roles = xpath.selectNodes(doc);
        if (roles == null || roles.isEmpty()) {
            log.error("No security roles found for portlet: {}", portletName);
            return Collections.emptySet();
        } else {
            Set<String> groups = roles
                    .stream()
                    .map(e -> xpathRoleName.selectSingleNode(e).getText())
                    .collect(Collectors.toSet());
            return groups;
        }
    }

}
