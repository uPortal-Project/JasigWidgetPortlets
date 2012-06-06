package org.jasig.portlet.widget.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.widget.gadget.model.Slideshow;
import org.jasig.portlet.widget.gadget.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

public class SlideShareService {

    protected static final String SLIDESHOWS_KEY = "slideshows";
    
    protected final Log log = LogFactory.getLog(getClass());

    private Cache cache;
    
    /**
     * @param cache the cache to set
     */
    @Required
    public void setCache(Cache cache) {
        this.cache = cache;
    }

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
    
    @Value("${slideshare.prefetchedTags:umobile,uportal}")
    private List<String> prefetchedTags;
    
    @Scheduled(fixedRate=900000)
    public void prefetchSlideshows() {
        for (String tag : prefetchedTags) {
            retrieveSlideshows(tag);
        }
    }
    
    public List<Slideshow> retrieveSlideshows(String tagName) {
        log.debug("Retrieving slideshows for tag " + tagName);
        
        final String time = String.valueOf(System.currentTimeMillis() / 1000);
        final String hash = DigestUtils.shaHex(sharedSecret.concat(time));
        
        final Map<String, String> params = new HashMap<String, String>();
        params.put("timestamp", time);
        params.put("hash", hash);
        params.put("apiKey", apiKey);
        params.put("tag", tagName);
        
        final Tag tag = restTemplate.getForObject(slideshareUrl, Tag.class, params);
        List<Slideshow> slideshows = tag.getSlideshow();
        
        cache.put(new Element(SLIDESHOWS_KEY.concat(tagName), slideshows));
        return slideshows;
    }
    
    public List<Slideshow> getSlideshowsForTag(String tagName) {
        final Element cachedList = this.cache.get(SLIDESHOWS_KEY.concat(tagName));
        
        if (cachedList != null) {
            log.debug("Retrieving program from cache");
            return (List<Slideshow>) cachedList.getValue();
        } 
        
        else {
            return retrieveSlideshows(tagName);
        }
    }

}
