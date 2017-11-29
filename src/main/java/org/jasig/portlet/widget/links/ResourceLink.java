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
import lombok.Data;
import lombok.NonNull;

/**
 * Data object for Resource Links.They have the following Attributes:
 *  - title
 *  - description
 *  - icon
 *  - url
 *  - groups
 */
@Data
public class ResourceLink {

    @NonNull private String title = "Title";
    private String description = "Short description";
    private String icon = "fa-link";
    @NonNull private String url = "/";
    private final List<String> groups = new ArrayList<>();

    @JsonIgnore
    public boolean isIconFontAwesome() {
        return icon.startsWith("fa-");
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

}
