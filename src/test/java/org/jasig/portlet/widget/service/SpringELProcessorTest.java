/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.widget.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.portlet.PortletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.Property;
import org.springframework.core.env.PropertyResolver;
import org.springframework.mock.web.portlet.MockPortletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author Josh Helmer, jhelmer@unicon.net
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "springElProcessorContext.xml")
public class SpringELProcessorTest {

    @Autowired
    private SpringELProcessor processor;

    @Test
    public void testLookupProperties() {
        Properties testProps = new Properties();
        testProps.put("key", "value");
        testProps.put("key.with.dots", "key.with.dots");

        PortletRequest request = new MockPortletRequest();

        processor.setProperties(testProps);
        assertThat(processor.process("${key}", request), equalTo("value"));
        assertThat(processor.process("${['key.with.dots']}", request), equalTo("key.with.dots"));
    }


    @Test
    public void testLookupRequestParams() {
        Properties testProps = new Properties();

        MockPortletRequest request = new MockPortletRequest();
        request.addParameter("key", "value");
        request.addParameter("key.with.dots", "key.with.dots");

        processor.setProperties(testProps);
        assertThat(processor.process("${request.key}", request), equalTo("value"));
        assertThat(processor.process("${request['key.with.dots']}", request), equalTo("key.with.dots"));
    }

    @Test
    public void testElvisExpr() throws Exception {
        MockPortletRequest req = new MockPortletRequest();
        assertThat(processor.process("${request['foo'] ?: 'bar'}", req), equalTo("bar"));
    }

    @Test
    public void testPropertyResolver() throws Exception {
        PropertyResolver pr = mock(PropertyResolver.class);
        when(pr.getProperty("foo")).thenReturn("bar");

        processor.setPropertyResolver(pr);
        assertThat(processor.process("foo${#systemProperties['foo']}", new MockPortletRequest()), equalTo("foobar"));

        verify(pr).getProperty("foo");
    }

    @Test
    public void testLookupUserProperties() {
        Properties testProps = new Properties();

        MockPortletRequest request = new MockPortletRequest();
        Map<String, String> userProperties = new HashMap<String, String>();
        userProperties.put("key", "value");
        userProperties.put("key.with.dots", "key.with.dots");
        request.setAttribute(PortletRequest.USER_INFO, userProperties);

        processor.setProperties(testProps);
        assertThat(processor.process("${user.key}", request), equalTo("value"));
        assertThat(processor.process("${user['key.with.dots']}", request), equalTo("key.with.dots"));
    }
}
