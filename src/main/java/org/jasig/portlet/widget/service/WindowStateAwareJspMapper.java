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

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.annotation.Resource;
import javax.portlet.WindowState;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation that maps the JSP to a name using a IWindowStateAwareJspNameMapper and
 * then checks if the mapped JSP exists.  If so, use it, otherwise use the default JSP
 *
 * @author Josh Helmer, jhelmer.unicon.net
 * @since 1.4.2
 */
@Service
public class WindowStateAwareJspMapper implements IWindowStateAwareJspMapper, ServletContextAware {
    private ServletContext servletContext;
    private IWindowStateAwareJspNameMapper nameMapper;
    private UrlBasedViewResolver jspResolver;
    // should be a very small # of these and there is no real reason for
    // cache expiration.  So, for now just use a map.
    private Map<String, Boolean> stateAwareJspExists = new HashMap<String, Boolean>();


    @Autowired
    public void setNameMapper(final IWindowStateAwareJspNameMapper nameMapper) {
        this.nameMapper = nameMapper;
    }


    @Autowired
    public void setJspResolver(final UrlBasedViewResolver jspResolver) {
        this.jspResolver = jspResolver;
    }


    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getJspName(String baseName, WindowState windowState, Locale locale) {
        String stateName = nameMapper.getName(baseName, windowState);
        if (stateSpecificJspExists(stateName, locale)) {
            return stateName;
        }

        return baseName;
    }


    /**
     * Given a window-state-aware JSP name, check if the JSP exists.
     *
     * @param stateJsp the JSP name
     * @param locale the locale of the request
     * @return true if the JSP exists, else false
     */
    private boolean stateSpecificJspExists(String stateJsp, Locale locale) {
        // check the "cache"...
        if (stateAwareJspExists.containsKey(stateJsp)) {
            return stateAwareJspExists.get(stateJsp);
        }

        try {
            View view = jspResolver.resolveViewName(stateJsp, locale);
            if (!(view instanceof AbstractUrlBasedView)) {
                return false;
            }

            String url = ((AbstractUrlBasedView)view).getUrl();
            String path = servletContext.getRealPath(url);
            File f = new File(path);
            boolean exists = f.exists();

            // cache the results...
            stateAwareJspExists.put(stateJsp, exists);

            return exists;

        } catch (Exception e) {
            return false;
        }
    }
}
