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

import javax.portlet.WindowState;
import java.util.Locale;

/**
 * Lookup what JSP to use based on a JSP basename and a window state.
 *
 * @author Josh Helmer, jhelmer.unicon.net
 * @since 1.4.2
 */
public interface IWindowStateAwareJspMapper {
    /**
     * Given a base name, a window state and a locale, lookup the name of the JSP
     * to render.  Default impl is to look for files like <baseName>.<windowState>.jsp
     * If the file exists, use that.  If it does not exist, return baseName
     *
     * @param baseName the configured JSP name
     * @param windowState the window state for the current request
     * @param locale the locale for the current request.
     *
     * @return The jsp name to use based on basename and windowState
     */
    String getJspName(String baseName, WindowState windowState, Locale locale);
}
