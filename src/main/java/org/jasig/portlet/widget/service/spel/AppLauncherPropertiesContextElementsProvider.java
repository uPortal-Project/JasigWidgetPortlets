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

package org.jasig.portlet.widget.service.spel;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.portlet.PortletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Provides properties from app-launcher.properties as SpEL context elements.
 *
 * @author drewwills
 */
@Component
public class AppLauncherPropertiesContextElementsProvider implements IContextElementsProvider {

    @Value("classpath:app-launcher.properties")
    private Resource propertiesResource;

    private final Properties properties = new Properties();

    @PostConstruct
    public void init() {
        try (InputStream inpt = propertiesResource.getInputStream()) {
            properties.load(inpt);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load app-launcher.properties from the classpath", e);
        }
    }

    @Override
    public Map<String,Object> provideElements(PortletRequest req) {
        final Map<String,Object> rslt = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            rslt.put(key, properties.getProperty(key));
        }
        return rslt;
    }

}
