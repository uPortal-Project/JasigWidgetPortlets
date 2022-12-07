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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class ResourceLinkService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String linkToJson(final ResourceLink link) {
        String json = null;
        if (link != null) {
            try {
                json = mapper.writeValueAsString(link);
            } catch (JsonProcessingException e) {
                log.error("Unable to parse link to JSON: %s", link.toString(), e);
            }
        }
        return json;
    }

    public static ResourceLink jsonToLink(final String json) {
        ResourceLink link = null;
        try {
            link = mapper.readValue(json, ResourceLink.class);
        } catch (Exception e) {
            log.error("Unable to parse JSON to ResourceLink: " + json, e);
        }
        return link;
    }

    public static List<ResourceLink> jsonArrayToLinkList(final String json) {
        List<ResourceLink> links = null;
        if (json != null) {
            try {
                links = mapper.readValue(json, new TypeReference<List<ResourceLink>>(){});
            } catch (IOException e) {
                log.error("Unable to parse JSON to ResourceLink: " + json, e);
            }
        }
        return links;
    }

    public static String[] linkListToJsonStrArray(final List<ResourceLink> links) {
        return links
                .stream()
                .map(e -> linkToJson(e))
                .toArray(String[]::new);
    }

    public static List<ResourceLink> createResourceLinkList(String[] jsonStrs) {
        if (jsonStrs == null) {
            return null;
        }

        List<ResourceLink> links =
                Arrays.asList(jsonStrs)
                    .stream()
                    .map(e -> jsonToLink(e))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        if (jsonStrs.length != links.size()) {
            log.warn("jsonStr.length (%d) != linksByTitle.size (%d)", jsonStrs.length, links.size());
        }
        return links;
    }

    public static String convertStringArrayToJsonArray(String[] jsonStrs) {
        // Assume the parameter strings are already JSON
        // This is very simple, but might as well add it here with a test
        if (jsonStrs == null) {
            return "[]";
        } else {
            return "[" + StringUtils.join(jsonStrs, ',') + "]";
        }
    }
}
