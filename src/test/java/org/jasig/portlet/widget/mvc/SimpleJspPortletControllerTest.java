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

package org.jasig.portlet.widget.mvc;

import org.junit.Test;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.ui.Model;

import javax.portlet.WindowState;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * @author Josh Helmer, jhelmer.unicon.net
 */
public class SimpleJspPortletControllerTest {
    @Test
    public void testJspViewWithoutMatchingWindowStateJSP() throws Exception {
        MockPortletPreferences prefs = new MockPortletPreferences();
        prefs.setValue(SimpleJspPortletController.JSP_NAME_PREFERENCE, "normal.jsp");

        MockRenderRequest req = new MockRenderRequest();
        req.setWindowState(WindowState.MAXIMIZED);
        req.setPreferences(prefs);

        Model model = mock(Model.class);

        SimpleJspPortletController controller = new SimpleJspPortletController();
        String jsp = controller.doView(req, model);

        assertThat(jsp, is("normal.jsp"));
    }

    @Test
    public void testJspByWindowState() throws Exception {
        MockPortletPreferences prefs = new MockPortletPreferences();
        prefs.setValue(SimpleJspPortletController.JSP_NAME_PREFERENCE + ".MAXIMIZED", "maximized.jsp");
        prefs.setValue(SimpleJspPortletController.JSP_NAME_PREFERENCE, "normal.jsp");

        MockRenderRequest req = new MockRenderRequest();
        req.setWindowState(WindowState.MAXIMIZED);
        req.setPreferences(prefs);

        Model model = mock(Model.class);

        SimpleJspPortletController controller = new SimpleJspPortletController();
        String jsp = controller.doView(req, model);

        assertThat(jsp, is("maximized.jsp"));
    }
}
