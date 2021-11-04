package com.emrislm.yuiidroid;

public class Anime {

    private int mal_id;
    private String url;
    private String image_url;
    private String title;
    private String type;
    private int episodes;
    private String status;
    private String duration;
    private String rating;
    private float score;
    private int members;
    private int favorites;
    private String synopsis;

    public void setMal_id(int mal_id) {
        this.mal_id = mal_id;
    }
    public int getMal_id() {
        return mal_id;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public String getImage_url() {
        return image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getEpisodes() {
        return episodes;
    }
    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }

    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }

    public int getMembers() {
        return members;
    }
    public void setMembers(int members) {
        this.members = members;
    }

    public int getFavorites() {
        return favorites;
    }
    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public String getSynopsis() {
        return synopsis;
    }
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
}
