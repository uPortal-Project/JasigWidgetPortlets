package org.jasig.portlet.widget.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.jasig.portlet.widget.gadget.model.Slideshow;
import org.jasig.portlet.widget.gadget.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class SlideShareService {
    
    private String slideshareUrl = "http://www.slideshare.net/api/2/get_slideshows_by_tag?api_key={apiKey}&ts={timestamp}&hash={hash}&tag={tag}";

    @Value("${slideshare.apiKey}")
    private String apiKey;
    
    @Value("${slidehsare.sharedSecret}")
    private String sharedSecret;
    
    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }
    
    private RestTemplate restTemplate;

    @Autowired(required = true)
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public List<Slideshow> getSlideshowsForTag(String tagName) {
        final String time = String.valueOf(System.currentTimeMillis() / 1000);
        final String hash = DigestUtils.shaHex(sharedSecret.concat(time));
        
        final Map<String, String> params = new HashMap<String, String>();
        params.put("timestamp", time);
        params.put("hash", hash);
        params.put("apiKey", apiKey);
        params.put("tag", tagName);
        
        final Tag tag = restTemplate.getForObject(slideshareUrl, Tag.class, params);
        List<Slideshow> slideshows = tag.getSlideshow();
        return slideshows;
    }

}
