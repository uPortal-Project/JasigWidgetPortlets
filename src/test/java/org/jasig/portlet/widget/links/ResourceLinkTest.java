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
package org.jasig.portlet.widget.links;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ResourceLinkTest {
    static final String TITLE = "My Title";
    static final String DESC = "My description.";
    static final String ICON_FA = "fa-disk";
    static final String ICON_URL = "/rs/234234.png";
    static final String URL = "http://localhost:8080/myapp";
    static final List<String> GROUPS = new ArrayList<>();

    static {
        GROUPS.add("everyone");
        GROUPS.add("students");
    }

    public ResourceLink createLink() {
        final ResourceLink link = new ResourceLink();
        link.setTitle(TITLE);
        link.setDescription(DESC);
        link.setIcon(ICON_FA);
        link.setUrl(URL);
        link.setGroups(GROUPS);
        return link;
    }

    @Test
    public void testGettersSetters() {
        final ResourceLink link = createLink();
        assertEquals(TITLE, link.getTitle());
        assertEquals(DESC, link.getDescription());
        assertEquals(ICON_FA, link.getIcon());
        link.setIcon(ICON_URL);
        assertEquals(ICON_URL, link.getIcon());
        assertEquals(URL, link.getUrl());
        assertEquals(GROUPS, link.getGroups());
    }

    @Test
    public void testIsIconFontAwesome() {
        final ResourceLink link = createLink();
        assertTrue(link.isIconFontAwesome());
        link.setIcon(ICON_URL);
        assertFalse(link.isIconFontAwesome());
    }

    @Test
    public void testEqualsAndHashCode() {
        final ResourceLink link1 = new ResourceLink();
        final ResourceLink link2 = createLink();
        final ResourceLink link3 = createLink();

        assertFalse(link1.equals(link2));
        assertFalse(link2.equals(link1));
        assertTrue(link2.equals(link3));
        final int hash2 = link2.hashCode();
        final int hash3 = link3.hashCode();
        assertEquals(hash3, hash2);
        link3.addGroup("staff");
        assertFalse(link2.equals(link3));
    }

    @Test
    public void testToString() {
        final String TO_STR = "ResourceLink{title='My Title', description='My description.', icon='fa-disk',"
        + " url=http://localhost:8080/myapp, groups=[everyone, students]}";
        final ResourceLink link = createLink();
        assertEquals(TO_STR, link.toString());
    }
}
