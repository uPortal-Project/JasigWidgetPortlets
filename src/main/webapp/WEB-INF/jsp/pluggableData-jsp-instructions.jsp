<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>

<div>
    <h2>How To Create a PluggableData JSP Portlet</h2>

    <p>
        These are basic instructions for creating a new portlet based on PluggableDataJspPortlet.  This is one
        approach (of several) for creating a simple, custom portlet that doesn't fit into any existing portlet
        with minimum of effort or Java Portlet Development experience.  Primarily only Java and JSP experience is
        needed.  This approach should not be used for any portlet that requires or warrants a richer experience.
        It is intended for cases where you need to perform some trivial sql queries, invocations
        of a web service, or other simple data query to display a single page of information, possibly with caching.
    </p>

    <ol>
        <li>In your JasigWidgetPortlets overlay (preferred) or project source code, create a new JSP file in the /src/main/webapp/WEB-INF/jsp directory (<i>e.g.</i> <code>/src/main/webapp/WEB-INF/jsp/myPage.jsp</code>)</li>
        <li>Create an implementation of a PluggableDataDao and instantiate it in pluggableDatajspPortletContextOverrides.xml with a bean id</li>
        <li>Put any matching content you like in a JSP file (follow community best practices for for using HTML, JavaScript, and CSS) that leverages the data from your PluggableDataDao implementation</li>
        <li>Publish a new portlet based on the <code>/jasig-widget-portlets/PluggableDataJspPortlet</code> definition. Assuming you are using the <code>CachingConfigurableDataServiceImpl</code> decorator, configure the following portlet preferences
            <ul>
                <li><code>PluggableDataPortlet.Service.datasourceBeanName</code> - the bean id of your PluggableDataDao implementation bean</li>
                <li><code>PluggableDataJspPortlet.Controller.jspName</code> - name of your JSP file, not including the file extension</li>
                <li>Optionally, create a new cache entry in an overlayed ehcache.xml and refer to it with the portlet preference <code>PluggableDataPortlet.Service.cacheName</code></li>
            </ul>
        </li>
        <li>In your JSP page, you have access to the following variables:
            <ul>
                <li>model - your list of objects returned by your DAO implementation.</li>
                <li></li>
            </ul>
    </ol>
    
</div>
