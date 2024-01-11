package com.example.zadanie9;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book {
    @SerializedName("title")
    private String title;

    @SerializedName("author_name")
    private List<String> authors;

    @SerializedName("cover_i")
    private String cover;

    @SerializedName("number_of_pages_median")
    private String numberOfPages;

    @SerializedName("subtitle")
    private String subtitle;

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getCover() {
        return cover;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

}
