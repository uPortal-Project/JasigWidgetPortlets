package org.jasig.portlet.widget.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class FlickrService {

    @Autowired(required = true)
    private RestTemplate restTemplate;
    
    public List<String> getImageUrls(String url) {
        @SuppressWarnings("unchecked")
        final Map<String, Object> response = (Map<String, Object>) restTemplate.getForObject(url, Map.class);
        return buildList(response);
    }
    
    protected List<String> buildList(Map<String, Object> response){
        List<String> images = new ArrayList<String>();
        
        @SuppressWarnings("unchecked")
        final Map<String, Object> photos1 = (Map<String, Object>) response.get("photos");

        @SuppressWarnings("unchecked")
        final List<Map<String,Object>> photos = (List<Map<String,Object>>) photos1.get("photo");
        
        for (Map<String,Object> photo : photos) {
            final int farmId = (Integer) photo.get("farm");
            final String serverId = (String) photo.get("server");
            final String photoId = (String) photo.get("id");
            final String secret = (String) photo.get("secret");
            images.add("http://farm" + farmId + ".staticflickr.com/" + serverId + "/" + photoId + "_" + secret + "_s.jpg");
        }
        
        return images;
    }

}
