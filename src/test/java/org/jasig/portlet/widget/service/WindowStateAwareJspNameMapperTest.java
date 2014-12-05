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

import org.junit.Test;

import javax.portlet.WindowState;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Josh Helmer, jhelmer.unicon.net
 */
public class WindowStateAwareJspNameMapperTest {
    @Test
    public void testNullState() {
        WindowStateAwareJspNameMapper mapper = new WindowStateAwareJspNameMapper();
        String name = mapper.getName("test", null);

        assertThat(name, is("test"));
    }


    @Test
    public void testWithWindowState() {
        WindowStateAwareJspNameMapper mapper = new WindowStateAwareJspNameMapper();
        String name = mapper.getName("test", WindowState.MAXIMIZED);

        assertThat(name, is("test.maximized"));
    }
}
