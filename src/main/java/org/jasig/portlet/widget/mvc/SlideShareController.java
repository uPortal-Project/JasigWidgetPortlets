package org.jasig.portlet.widget.mvc;

import java.util.List;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.jasig.portlet.widget.gadget.model.Slideshow;
import org.jasig.portlet.widget.service.SlideShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("VIEW")
public class SlideShareController {
    
    private SlideShareService slideShareService;
    
    @Autowired(required = true)
    public void setSlideShareService(SlideShareService slideShareService) {
        this.slideShareService = slideShareService;
    }
    
    @RequestMapping
    public ModelAndView showTag(PortletRequest request) {
        ModelAndView mv = new ModelAndView("slideshare");

        final PortletPreferences preferences = request.getPreferences();
        final String tag = preferences.getValue("tag", null);
        
        final List<Slideshow> slideshows = slideShareService.getSlideshowsForTag(tag);
        mv.addObject("slideshows", slideshows);

        return mv;
    }

}
