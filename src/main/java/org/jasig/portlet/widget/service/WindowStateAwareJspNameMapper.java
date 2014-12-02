/*
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

import org.springframework.stereotype.Service;

import javax.portlet.WindowState;

/**
 * Default Mapper that builds the JSP name for state-specific JSPs.
 * This impl will return baseName in the case of WindowState being null
 * else, it will return <baseName>.<windowState.toLowerCase()>
 *
 * @author Josh Helmer, jhelmer.unicon.net
 * @since 1.4.2
 */
@Service
public class WindowStateAwareJspNameMapper implements IWindowStateAwareJspNameMapper {
    @Override
    public String getName(String baseName, WindowState state) {
        if (state == null) {
            return baseName;
        }

        return baseName + "." + state.toString().toLowerCase();
    }
}
