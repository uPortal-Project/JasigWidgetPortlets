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

package org.jasig.portlet.widget.dao;

import java.util.List;

import javax.portlet.PortletRequest;

/**
 * The PluggableDataPortlet can be configured to call any bean that implements this interface to provide a simple,
 * one-off DAO implementation to render a simple portlet view.  The intent of this approach is for simple, single
 * view portlets that provide a limited amount of data for the user without need for a rich interface (create an
 * actual portlet for anything richer than a single view).
 *
 * The DAO can access databases, call web services, access file system resources, or whatever it needs to return
 * data for the view.
 *
 * @author James Wennmacher, jwennmacher@unicon.net
 */

public interface PluggableDataDao {

    List<? extends Object> getData(PortletRequest req);
}
