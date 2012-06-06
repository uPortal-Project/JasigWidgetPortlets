package org.jasig.portlet.widget.mvc;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.widget.service.FlickrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("VIEW")
public class FlickrController {

    protected final Log logger = LogFactory.getLog(getClass());
    
    @Autowired(required = true)
    private FlickrService dao;
    
    @RequestMapping
    public ModelAndView showMainView(
            final RenderRequest request, final RenderResponse response) {

        final ModelAndView mav = new ModelAndView("flickr");
        mav.addObject("images", dao.getImageUrls());
        return mav;

    }

}
