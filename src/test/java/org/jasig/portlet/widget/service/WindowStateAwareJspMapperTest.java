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

import org.jasig.portlet.widget.mvc.SimpleJspPortletController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.ui.Model;
import org.springframework.web.servlet.View;
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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Josh Helmer, jhelmer.unicon.net
 */
public class WindowStateAwareJspMapperTest {
    private static String BASE_JSP_NAME = "baseName";

    @Mock
    private ServletContext servletContext;
    private WindowStateAwareJspMapper windowStateAwareJspMapper;
    private Locale locale = Locale.getDefault();

    @Before
    public void setup() throws Exception {
        initMocks(this);

        UrlBasedViewResolver viewResolver = spy(new UrlBasedViewResolver());
        Answer<View> answer = new Answer<View>() {
            @Override
            public View answer(InvocationOnMock invocationOnMock) throws Throwable {
                String jspName = (String)invocationOnMock.getArguments()[0];
                return new JstlView("/WEB-INF/jsp/" + jspName + ".jsp");
            }
        };
        doAnswer(answer).when(viewResolver).resolveViewName(anyString(), any(Locale.class));

        windowStateAwareJspMapper = new WindowStateAwareJspMapper();
        windowStateAwareJspMapper.setNameMapper(new WindowStateAwareJspNameMapper());
        windowStateAwareJspMapper.setJspResolver(viewResolver);
        windowStateAwareJspMapper.setServletContext(servletContext);
    }


    @Test
    public void testNoWindowStateJSP() {
        // given window-state specific JSP does not exist
        when(servletContext.getRealPath(anyString())).thenReturn("INVALID_FILENAME");

        String jsp = windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.MAXIMIZED, locale);

        assertThat(jsp, is(BASE_JSP_NAME));
    }


    @Test
    public void testWindowStateJSPExists() throws IOException {
        String maxJspName = BASE_JSP_NAME + ".maximized";

        // given window-state specific JSP does exist
        File testFile = File.createTempFile("windowStateAwareJspMapperTest", "txt");
        when(servletContext.getRealPath(eq(fullPath(maxJspName))))
                .thenReturn(testFile.getAbsolutePath());

        // when I resolve the view
        String jsp = windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.MAXIMIZED, locale);

        // then I should get the state specific JSP
        assertThat(jsp, is(maxJspName));
    }


    @Test
    public void testWindowStateJSPCaching() throws IOException {
        // given I setup a window-state-aware mapper
        when(servletContext.getRealPath(anyString())).thenReturn("INVALID_FILENAME");

        // when I make repeated requests for a JSP/state combination
        windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.MAXIMIZED, locale);
        windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.NORMAL, locale);
        windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.MINIMIZED, locale);

        windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.MAXIMIZED, locale);
        windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.NORMAL, locale);
        windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.NORMAL, locale);
        windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.MINIMIZED, locale);
        windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.MINIMIZED, locale);
        windowStateAwareJspMapper.getJspName(BASE_JSP_NAME, WindowState.MINIMIZED, locale);

        // then I see that file check is only performed once per JSP/state combination
        verify(servletContext, times(1)).getRealPath(fullPath(BASE_JSP_NAME + ".maximized"));
        verify(servletContext, times(1)).getRealPath(fullPath(BASE_JSP_NAME + ".normal"));
        verify(servletContext, times(1)).getRealPath(fullPath(BASE_JSP_NAME + ".minimized"));
    }


    private String fullPath(String jspPart) {
        return "/WEB-INF/jsp/" + jspPart + ".jsp";
    }
}
