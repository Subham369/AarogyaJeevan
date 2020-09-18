package com.example.aarogyajeevan;

public class Member {
    private String videoName;
    private String Videourl;
    private String search;


    public Member() {
    }

    public Member(String videoName, String videourl, String search) {
        this.videoName = videoName;
        Videourl = videourl;
        this.search = search;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getVideourl() {
        return Videourl;
    }

    public void setVideourl(String videourl) {
        Videourl = videourl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
