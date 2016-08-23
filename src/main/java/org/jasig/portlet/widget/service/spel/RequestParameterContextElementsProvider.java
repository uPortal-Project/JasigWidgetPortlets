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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.springframework.stereotype.Component;

/**
 * Bundles request parameters into a Map and provides it as a SpEL context
 * element.
 *
 * @author drewwills
 */
@Component
public class RequestParameterContextElementsProvider implements IContextElementsProvider {

    private static final String CONTEXT_KEY = "request";

    @Override
    public Map<String, Object> provideElements(PortletRequest req) {
        final Map<String,Object> rslt = new HashMap<>();

        final Map<String,String> requestParameterMap = new HashMap<String, String>();
        final Enumeration<String> names = req.getParameterNames();
        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            requestParameterMap.put(name, req.getParameter(name));
        }
        rslt.put(CONTEXT_KEY, requestParameterMap);

        return rslt;
    }

}
