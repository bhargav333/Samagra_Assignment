package com.samagra.swayamwebscrapper.service.serviceImpl;

import com.samagra.swayamwebscrapper.model.Course;
import com.samagra.swayamwebscrapper.service.WebScraperHelper;
import com.samagra.swayamwebscrapper.service.WebScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.samagra.swayamwebscrapper.model.Course;

import javax.annotation.PostConstruct;

@Service
public class WebScraperServiceImpl implements WebScraperService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private List<Course> courses = new ArrayList<>();


    @Value("${swayam.courses.url}")
    private String coursesUrl;
    @Value("${swayam.courses.parse.timeout.ms}")
    Integer parseTimeoutMillis;
    @Value("${swayam.courses.course.authortag}")
    String authorTagName;
    @Value("${swayam.courses.course.titletag}")
    String titleTagName;
    @Value("${swayam.courses.course.desctag}")
    String descTagName;

    @Value("#{'${swayam.courses.course.searchtags}'}")
            //.split(',')
    String articleLinksSearchTags;

    public WebScraperServiceImpl() {
    }
    @PostConstruct
    @Override
    public void loadContents() throws MalformedURLException, IOException {
        LOGGER.info("loadContents()...start");
        courses.clear();
        List<String> articleDetailsSearchTags = Arrays.asList(authorTagName, titleTagName, descTagName);
        WebScraperHelper scraperHelper = new WebScraperHelper(coursesUrl, parseTimeoutMillis, articleDetailsSearchTags, articleLinksSearchTags);

        LOGGER.info("Extracting article details...start");

        scraperHelper.fetchAllLinkMetaDetailsFromPage()
                .thenAccept(list->{
                    list.stream().filter(map->map.get(authorTagName)!=null && map.get(authorTagName).length()>0)
                            .forEach(map->{
                                courses.add(new Course(map.get(titleTagName), map.get(descTagName), map.get(authorTagName)));
                            });
                });

        LOGGER.info("loadContents()...completed");
    }

    @Override
    public List<String> listCourses() {
        return courses.stream().map(a->a.getTitle())
                .distinct()
                .collect(Collectors.toList());
    }

}
