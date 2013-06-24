<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.

--%>

<div>
    <h2>How To Create a Simple JSP Portlet</h2>

    <p>
      These are basic instructions for creating a new portlet based on 
      SimpleJspPortlet.  This is one approach (of several) for creating a 
      simple, custom portlet with a minimum of effort or Java Portlet 
      Development experience.
    </p>

    <ol>
        <li>In your JasigWidgetortlets overlay (preferred) or project source code, create a new JSP file in the /src/main/webapp/WEB-INF/jsp directory (<i>e.g.</i> <code>/src/main/webapp/WEB-INF/jsp/myPage.jsp</code>)</li>
        <li>Put any content you like in the JSP file (follow community best practices for for using HTML, JavaScript, and CSS)</li>
        <li>Publish a new portlet based on the <code>/jasig-widget-portlets/SimpleJspPortlet</code> definition</li>
        <li>Be sure to provide the name of your JSP file, not including the file extension, as the <code>SimpleJspPortletController.jspName</code> preference (<i>e.g.</i> 'myPage')</li>
    </ol>
</div>
