package com.samagra.swayamwebscrapper.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WebScraperHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String pageUrl;
    private Integer pageParseTimeoutMillis;
    private List<String> detailsSearchTag;
    private String linksSearchTags;

    public WebScraperHelper(String pageUrl, Integer pageParseTimeoutMillis, List<String> detailsSearchTag,
                            String linksSearchTags) {
        super();
        this.pageUrl = pageUrl;
        this.pageParseTimeoutMillis = pageParseTimeoutMillis;
        this.detailsSearchTag = detailsSearchTag;
        this.linksSearchTags = linksSearchTags;
    }

    public CompletableFuture<List<Map<String, String>>> fetchAllLinkMetaDetailsFromPage(){
        List<Map<String, String>> metaDetailsList = new ArrayList<>();
        return CompletableFuture.supplyAsync(()->{
            try {
                Set<String> links = getAllLinksFromPage();

                return links;
            } catch (IOException e) {
                LOGGER.error("Error in getting links.", e);
            }
            return null;
        }).thenApplyAsync(links->{
            links.forEach(lnk->{
                CompletableFuture<Map<String, String>> detailsFuture = CompletableFuture.supplyAsync(()->{
                    try {
                        return getLinkDetails(lnk);
                    } catch (IOException e) {
                        LOGGER.error("Error in getting link details.", e);
                    }
                    return null;
                });
                try {
                    metaDetailsList.add(detailsFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.error("Error in extracting results after task completion.", e);
                }
            });
            return metaDetailsList;
        }).toCompletableFuture();
    }

    private Map<String, String> getLinkDetails(String url) throws IOException{
        Document doc = Jsoup.parse(new URL(url), pageParseTimeoutMillis);
        Map<String, String> tagDetails = new HashMap<>();
        detailsSearchTag.forEach(tag->{
            tagDetails.put(tag, doc.select(tag).get(0).attr("content"));
        });
        return tagDetails;
    }

    private Set<String> getAllLinksFromPage() throws IOException {
        Document doc = Jsoup.parse(new URL(pageUrl), pageParseTimeoutMillis);
        return searchLinkTags(doc, linksSearchTags);
    }


    private Set<String> searchLinkTags(Document doc, String searchTags){
        Set<String> links = new HashSet<>();
        //course-list style-scope course-cards
        //Elements elemt = doc.getElementByClass("div.course-list style-scope course-cards");
           Elements elems = doc.getElementsByClass("course-list");
        for (Element link : elems) {
            links.add(link.attr("abs:href"));
        }

       // for(int i = 0; i < links.size(); i++) {
        //    LOGGER.error((String) links.toArray()[i]);
        //}
        return links;
    }
}
