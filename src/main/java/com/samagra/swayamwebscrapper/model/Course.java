package com.samagra.swayamwebscrapper.model;

import java.io.Serializable;

public class Course implements Serializable {

        private static final long serialVersionUID = 1L;

        private String title;
        private String description;
        private String authorName;

        public Course() {}
        public Course(String title, String description, String authorName) {
            super();
            this.title = title;
            this.description = description;
            this.authorName = authorName;
        }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}
