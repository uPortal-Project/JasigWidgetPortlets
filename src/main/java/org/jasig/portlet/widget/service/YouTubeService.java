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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Fetches videos from a YouTube channel using the YouTube Data API v3.
 *
 * Requires a YouTube Data API v3 key configured as the 'apiKey' portlet
 * preference. The channel is identified by the 'user' parameter which should
 * be a YouTube channel ID (e.g. UCnUYZLuoy1rq1aVMwx4aTzw).
 *
 * Previously used the YouTube Data API v2 (gdata.youtube.com) which was
 * shut down in 2015.
 */
@Service
public class YouTubeService {

    protected final Log log = LogFactory.getLog(getClass());

    private static final String SEARCH_URL =
            "https://www.googleapis.com/youtube/v3/search" +
            "?part=snippet&type=video&order=date&maxResults=50" +
            "&channelId={channelId}&key={apiKey}";

    private final RestTemplate restTemplate = new RestTemplate(
            new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault()));

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Fetch videos for the given channel ID using the YouTube Data API v3.
     *
     * @param channelId the YouTube channel ID
     * @param apiKey    the YouTube Data API v3 key
     * @return JSON string with a 'videos' array, each item having title,
     *         description, link, and thumbnail fields; or null on error
     */
    public String getYouTubeResponse(String channelId, String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.warn("No YouTube Data API v3 key configured (portlet preference 'apiKey'). Cannot fetch videos.");
            return emptyResponse();
        }

        try {
            JsonNode response = restTemplate.getForObject(SEARCH_URL, JsonNode.class, channelId, apiKey);
            if (response == null) {
                return emptyResponse();
            }

            ArrayNode videos = objectMapper.createArrayNode();
            JsonNode items = response.path("items");
            for (JsonNode item : items) {
                JsonNode snippet = item.path("snippet");
                String videoId = item.path("id").path("videoId").asText("");
                if (videoId.isEmpty()) continue;

                ObjectNode video = objectMapper.createObjectNode();
                video.put("title", snippet.path("title").asText(""));
                video.put("description", snippet.path("description").asText(""));
                video.put("link", "https://www.youtube.com/watch?v=" + videoId);
                video.put("thumbnail", snippet.path("thumbnails").path("high").path("url").asText(
                        snippet.path("thumbnails").path("default").path("url").asText("")));
                videos.add(video);
            }

            ObjectNode result = objectMapper.createObjectNode();
            result.set("videos", videos);
            return objectMapper.writeValueAsString(result);

        } catch (Exception e) {
            log.warn("Failed to retrieve YouTube videos for channel: " + channelId, e);
            return emptyResponse();
        }
    }

    private String emptyResponse() {
        return "{\"videos\":[]}";
    }
}
