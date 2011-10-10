package org.jasig.portlet.widget.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.ehcache.annotations.Cacheable;

public class YouTubeService {
    
    protected final Log log = LogFactory.getLog(getClass());
    
    @Cacheable(cacheName="youTubeCache")
    public String getYouTubeResponse(String userName) {
        String url = "http://gdata.youtube.com/feeds/api/videos?author=" + userName + "&v=2&alt=jsonc";
        HttpClient client = new HttpClient();
        GetMethod get = null;

        try {
    
            if(log.isDebugEnabled()) {
                log.debug("Retrieving proxy url " + url);
            }
            
            get = new GetMethod(url);
            int rc = client.executeMethod(get);
            if(rc == HttpStatus.SC_OK) {
                
                // get the response body
                log.debug("request completed successfully");
                InputStream in = get.getResponseBodyAsStream();
                
                String response = IOUtils.toString(in);
                return response;
                
            }
            else {
                log.warn("HttpStatus for " + url + ":" + rc);
            }
        } catch (HttpException e) {
            log.warn("Error proxying url", e);
        } catch (IOException e) {
            log.warn("Error proxying url", e);
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
        return null;
    }

}
