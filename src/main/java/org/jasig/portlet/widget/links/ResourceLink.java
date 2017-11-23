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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data object for Resource Links.They have the following Attributes:
 *  - title
 *  - description
 *  - icon
 *  - url
 *  - groups
 */
public class ResourceLink {

    private static final Logger log = LoggerFactory.getLogger(ResourceLink.class);

    private String title = "Title";
    private String description = "Short description";
    private String icon = "fa-link";
    private String url = "/";
    private final List<String> groups = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        assert(title != null);
        assert(!title.isEmpty());
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        assert(description != null);
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        assert(icon != null);
        this.icon = icon;
    }

    @JsonIgnore
    public boolean isIconFontAwesome() {
        return icon.startsWith("fa-");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        assert(url != null);
        this.url = url;
    }

    public List<String> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    public void setGroups(List<String> groups) {
        assert(groups != null);
        this.groups.clear();
        this.groups.addAll(groups);
    }

    public void addGroup(String group) {
        this.groups.add(group);
    }

    @Override
    public String toString() {
        return "ResourceLink{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", url=" + url +
                ", groups=" + groups +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceLink that = (ResourceLink) o;

        if (!title.equals(that.title)) return false;
        if (!description.equals(that.description)) return false;
        if (!icon.equals(that.icon)) return false;
        if (!url.equals(that.url)) return false;
        return groups.equals(that.groups);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + icon.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + groups.hashCode();
        return result;
    }
}
