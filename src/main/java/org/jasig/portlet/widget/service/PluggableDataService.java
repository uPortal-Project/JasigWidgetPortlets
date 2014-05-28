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

import javax.portlet.PortletRequest;

/**
 * Service interface for a very generic data accessor that returns a list of serializable objects that can be
 * returned to the view.  Users can implement any type of dao service they need.
 *
 * @author James Wennmacher, jwennmacher@unicon.net
 */

public interface PluggableDataService {

    /**
     * Return a list of objects.  The dao implementation may have specific ordering applied.  Make no assumption
     * about what type of Object is returned, though ideally it should also be serializable into JSON.
     *
     * @param req PortletRequest.
     * @return List of objects to sends to the view
     */
    List<? extends Object> fetchDataFromSource(PortletRequest req);
}
