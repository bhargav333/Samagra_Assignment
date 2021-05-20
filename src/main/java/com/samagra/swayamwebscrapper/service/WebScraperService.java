package com.samagra.swayamwebscrapper.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import com.samagra.swayamwebscrapper.model.Course;

public interface WebScraperService {
    public void loadContents() throws MalformedURLException, IOException;
    public List<String> listCourses();
}
