package com.wendley.room.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Wendley S
 */
@Controller
public class SiteController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
