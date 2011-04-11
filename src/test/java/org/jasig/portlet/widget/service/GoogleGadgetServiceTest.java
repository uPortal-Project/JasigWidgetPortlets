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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.jasig.portlet.widget.gadget.model.Module;
import org.jasig.portlet.widget.service.GoogleGadgetService.GadgetCategory;
import org.jasig.portlet.widget.service.GoogleGadgetService.GadgetEntry;
import org.jasig.portlet.widget.service.GoogleGadgetService.GadgetList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "gadgetTestContext.xml")
public class GoogleGadgetServiceTest {
    
    GoogleGadgetService service;
    
    @Autowired
    ApplicationContext context;
    
    @Before
    public void setUp() {
        service = spy(new GoogleGadgetService());
    }
    
    @Test
    public void testGetCategories() throws IOException {
        
        Resource mainPage = context.getResource("org/jasig/portlet/widget/service/gadgetListingMainPage.html");
        when(service.getStreamFromUrl(anyString())).thenReturn(mainPage.getInputStream());
        
        List<GadgetCategory> categories = service.getCategories();
        assertEquals(10, categories.size());
        
    }

    @Test
    public void testListGadgets() throws IOException {
        
        Resource mainPage = context.getResource("org/jasig/portlet/widget/service/gadgetListingMainPage.html");
        when(service.getStreamFromUrl(anyString())).thenReturn(mainPage.getInputStream());
        
        GadgetList list = service.getGadgets(null, null, 0);
        assertEquals(24, list.getGadgets().size());
        assertEquals(213028, list.getTotal());

        GadgetEntry first = list.getGadgets().get(0);
        assertEquals("Google Calendar Viewer", first.getName());
        assertEquals("http://ralph.feedback.googlepages.com/googlecalendarviewer_thumbnail.png", first.getImageUrl());
        assertEquals("http://ralph.feedback.googlepages.com/googlecalendarviewer.xml", first.getConfigUrl());

    }

    @Test
    public void testListGadgetsForCategory() throws IOException {
        
        Resource categoryPage = context.getResource("org/jasig/portlet/widget/service/gadgetListingFinanceCategory.html");
        when(service.getStreamFromUrl(anyString())).thenReturn(categoryPage.getInputStream());

        GadgetList list = service.getGadgets(null, "finance", 0);
        assertEquals(24, list.getGadgets().size());
        assertEquals(1813, list.getTotal());
        
        GadgetEntry first = list.getGadgets().get(0);
        assertEquals("Stock Charts", first.getName());
        assertEquals("http://igoogle.stockmarketstudio.com/StockCharts/thumbnail.png", first.getImageUrl());
        assertEquals("http://hosting.gmodules.com/ig/gadgets/file/113570023379904426818/StockCharts.xml", first.getConfigUrl());
        
    }

    @Test
    public void testGetModule() throws IOException {
        
        Resource gadgetXml = context.getResource("org/jasig/portlet/widget/service/googlecalendarviewer.xml");
        when(service.getStreamFromUrl(anyString())).thenReturn(gadgetXml.getInputStream());

        Module module = service.getModule("http://pretend");
        assertEquals("Google Calendar Viewer", module.getModulePrefs().getTitle());
        
    }

}
