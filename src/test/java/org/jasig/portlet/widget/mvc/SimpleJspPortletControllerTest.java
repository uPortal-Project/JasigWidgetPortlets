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

import org.jasig.portlet.widget.service.IWindowStateAwareJspMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;
import javax.servlet.ServletContext;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


/**
 * @author Josh Helmer, jhelmer.unicon.net
 */
public class SimpleJspPortletControllerTest {
    private static final String BASE_JSP_NAME = "baseName";

    private SimpleJspPortletController controller;

    @Mock private Model model;
    @Mock private PortletPreferences preferences;
    @Mock private RenderRequest renderRequest;
    @Mock private IWindowStateAwareJspMapper stateAwareJspMapper;


    @Before
    public void setup() throws Exception {
        initMocks(this);

        when(preferences.getValue(eq(SimpleJspPortletController.JSP_NAME_PREFERENCE), anyString()))
                .thenReturn(BASE_JSP_NAME);
        when(preferences.getValues(eq(SimpleJspPortletController.PREF_SECURITY_ROLE_NAMES), any(String[].class)))
                .thenReturn(new String[]{});

        when(renderRequest.getLocale()).thenReturn(Locale.getDefault());
        when(renderRequest.getPreferences()).thenReturn(preferences);

        UrlBasedViewResolver viewResolver = spy(new UrlBasedViewResolver());
        Answer<View> answer = new Answer<View>() {
            @Override
            public View answer(InvocationOnMock invocationOnMock) throws Throwable {
                String jspName = (String)invocationOnMock.getArguments()[0];
                return new JstlView("/WEB-INF/jsp/" + jspName + ".jsp");
            }
        };
        doAnswer(answer).when(viewResolver).resolveViewName(anyString(), any(Locale.class));

        controller = new SimpleJspPortletController();
        controller.setWindowStateAwareJspMapper(stateAwareJspMapper);
    }

    @Test
    public void testDefaultJspView() {
        // given window-state specific JSP does not exist
        when(renderRequest.getWindowState()).thenReturn(WindowState.NORMAL);
        when(stateAwareJspMapper.getJspName(eq(BASE_JSP_NAME), eq(WindowState.NORMAL), any(Locale.class)))
                .thenReturn(BASE_JSP_NAME);

        // when I resolve the view
        String jsp = controller.doView(renderRequest, model);

        // then I should get the configured JSP
        assertThat(jsp, is(BASE_JSP_NAME));
    }


    @Test
    public void testMaximizedJspExists() throws IOException {
        String maxJspName = BASE_JSP_NAME + ".maximized";
        // given window-state specific JSP does exist
        when(stateAwareJspMapper.getJspName(eq(BASE_JSP_NAME), eq(WindowState.MAXIMIZED), any(Locale.class)))
                .thenReturn(maxJspName);

        when(renderRequest.getWindowState()).thenReturn(WindowState.MAXIMIZED);

        // when I resolve the view
        String jsp = controller.doView(renderRequest, model);

        // then I should get the state specific JSP
        assertThat(jsp, is(maxJspName));
    }
}
