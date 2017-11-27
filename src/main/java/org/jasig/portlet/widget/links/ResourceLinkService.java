package org.jasig.portlet.widget.links;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLinkService {

    private static final Logger log = LoggerFactory.getLogger(ResourceLinkService.class);

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
        } catch (IOException e) {
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

    public static String linkListToOrderString(final List<ResourceLink> links) {
        return links
                .stream()
                .map(e -> e.getTitle())
                .collect(joining(","));
    }

    public static Map<String, ResourceLink> jsonStringArrayToMapByTitle(String[] jsonStrs) {
        if (jsonStrs == null) {
            return null;
        }

        Map<String, ResourceLink> linksByTitle =
                Arrays.asList(jsonStrs)
                    .stream()
                    .map(e -> jsonToLink(e))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(e -> e.getTitle(), e -> e));
        if (jsonStrs.length != linksByTitle.size()) {
            log.warn("jsonStr.length (%d) != linksByTitle.size (%d)", jsonStrs.length, linksByTitle.size());
        }
        return linksByTitle;
    }

    public static List<ResourceLink> createOrderedLinkList(String[] orderByTitle, Map<String, ResourceLink> linksByTitle) {
        if (orderByTitle == null || orderByTitle.length == 0 || linksByTitle == null) {
            return Collections.EMPTY_LIST;
        }
        List<ResourceLink> list =
                Arrays.asList(orderByTitle)
                    .stream()
                    .map(title -> linksByTitle.get(title))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        if (orderByTitle.length != list.size()) {
            log.warn("order list (%d) != order list (%d)", orderByTitle.length, list.size());
        }
        return list;
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
