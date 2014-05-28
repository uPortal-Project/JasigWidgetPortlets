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

import java.util.List;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.jasig.portlet.widget.dao.PluggableDataDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * This service implementation performs caching on a global or per user basis and invokes a configured bean that
 * provides data for the view.  The cache name, DAO bean name, and per-user caching flag are all configurable via
 * portlet preferences.
 *
 * @author James Wennmacher, jwennmacher@unicon.net
 */

@Service
public class CachingConfigurableDataServiceImpl implements PluggableDataDao {

    public static final String PREF_DATASOURCE_BEAN_NAME = "PluggableDataPortlet.Service.datasourceBeanName";
    public static final String PREF_CACHE_NAME = "PluggableDataPortlet.Service.cacheName";
    public static final String PREF_CACHE_PER_USER = "PluggableDataPortlet.Service.datasourcePerUserCache";
    public static final String DEFAULT_CACHE_NAME = "pluggableDataCache";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ApplicationContext applicationContext;

    /**
     * Return a list of objects.  The dao implementation may have specific ordering applied.  Make no assumption
     * about what type of Object is returned, though ideally it should also be serializable into JSON.
     *
     * This class is also responsible for doing caching if configured via portlet preferences.
     * @param req PortletRequest.
     * @return List of objects to sends to the view
     */
    @Override
    public List<? extends Object> getData(PortletRequest req) {
        String daoBean = req.getPreferences().getValue(PREF_DATASOURCE_BEAN_NAME,"");

        // Attempt to retrieve the data from cache
        Cache cache = getCache(req);
        final String cacheKey = createCacheKey(req, daoBean);
        Element cachedElement = cache.get(cacheKey);
        if (cachedElement == null) {
            log.debug("Item not found in cache. Invoking dao bean {}", daoBean);
            PluggableDataDao dao = (PluggableDataDao) applicationContext.getBean(daoBean);
            if (dao == null) {
                throw new RuntimeException ("Name of spring bean implementing PluggableData must be specified in"
                        + " portlet preference " + PREF_DATASOURCE_BEAN_NAME);
            }
            List<? extends Object> result = dao.getData(req);
            cachedElement = new Element(cacheKey, result);
            cache.put(cachedElement);
        } else {
            log.debug("Cache hit; returning item from cache for user {}", req.getRemoteUser());
        }
        return (List<Object>) cachedElement.getValue();
    }

    /**
     * Obtain the cache configured for this portlet instance.
     * @param req Portlet request
     * @return Cache configured for this portlet instance.
     */
    private Cache getCache(PortletRequest req) {
        String cacheName = req.getPreferences().getValue(PREF_CACHE_NAME, DEFAULT_CACHE_NAME);
        log.debug("Looking up cache '{}'", cacheName);
        Cache cache = CacheManager.getInstance().getCache(cacheName);
        if (cache == null) {
            throw new RuntimeException("Unable to find cache named " + cacheName + ". Check portlet preference value "
                + PREF_CACHE_NAME + " and configuration in ehcache.xml");
        }
        return cache;
    }

    /**
     * Create a cache key that includes the bean name and if configured the logged in userId.  This allows multiple
     * portlet instances to share the same cache (if desired but not recommended).
     * @param req Portlet request
     * @param beanName Name of the dao bean used by this portlet instance
     * @return Generated Cache key
     */
    private String createCacheKey(PortletRequest req, String beanName) {
        PortletPreferences prefs = req.getPreferences();
        boolean perUser = Boolean.valueOf(prefs.getValue(PREF_CACHE_PER_USER, "true"));
        String key = (perUser ? req.getRemoteUser() + "-": "") + beanName + "-pluggableDataService";
        log.debug("Using cache key {}", key);
        return key;
    }
}
