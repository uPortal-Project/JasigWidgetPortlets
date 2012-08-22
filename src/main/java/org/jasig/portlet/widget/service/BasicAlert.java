package org.jasig.portlet.widget.service;

import org.springframework.util.Assert;

public class BasicAlert implements IAlert {

    // Instance Members.
    private final String header;
    private final String body;
    private final String url;
    
    public BasicAlert(String header, String body) {
        this(header, body, null);
    }

    public BasicAlert(String header, String body, String url) {
        
        // Assertions.
        Assert.hasText(header, "Argument 'header' may not be null");
        Assert.hasText(body, "Argument 'body' may not be null");
        
        this.header = header;
        this.body = body;
        this.url = url;
        
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "BasicAlert [header=" + header + ", body=" + body + ", url="
                + url + "]";
    }

}
