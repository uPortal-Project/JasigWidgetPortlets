package org.jasig.portlet.widget.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

public class FlickrService {
    
    protected static final String IMAGE_LIST_KEY = "imageList";
    
    protected final Log log = LogFactory.getLog(getClass());

    @Autowired(required = true)
    private RestTemplate restTemplate;

    private Cache cache;
    
    /**
     * @param cache the cache to set
     */
    @Required
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    private String url;    
    
    @Value("${flickr.search.url}")
    public void setUrl(String url) {
        this.url = url;
    }
    
    @Scheduled(fixedRate=900000)
    protected void retrieveImageUrls() {
        log.debug("Retrieving images from Flickr");
        
        @SuppressWarnings("unchecked")
        final Map<String, Object> response = (Map<String, Object>) restTemplate.getForObject(url, Map.class);
        final List<String> list = buildList(response);
        cache.put(new Element(IMAGE_LIST_KEY, list));
    }
    
    public List<String> getImageUrls() {
        Element cachedList = this.cache.get(IMAGE_LIST_KEY);
        
        if (cachedList == null) {
            retrieveImageUrls();
            cachedList = this.cache.get(IMAGE_LIST_KEY);
        }
        
        log.debug("Retrieving program from cache");
        return (List<String>) cachedList.getValue();
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
