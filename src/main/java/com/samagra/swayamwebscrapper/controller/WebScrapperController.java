package com.samagra.swayamwebscrapper.controller;

import com.samagra.swayamwebscrapper.service.WebScraperService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("api//courses")
public class WebScrapperController {

    @Autowired
    WebScraperService scraperService;

    //List all the courses
    @RequestMapping(value="/courseList", method = RequestMethod.GET, produces = "application/json")
    public List<String> listCourses() {
        return scraperService.listCourses();
    }
}
