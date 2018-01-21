package org.tthomas.popularmovies;


public class MovieItem {

    private String id;
    private String title;
    private String poster_path;
    private String release_date;
    private String overview;
    private String vote_average;
    private String preview;

    public String getID(){ return id; }

    public void setID(String id ) { this.id = id; }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getPoster_path(){
        return poster_path;
    }

    public String getPreview() { return this.preview; }

    public void setPreview( String preview ){ this.preview = preview; }

    public void setPoster_path(String poster_path){
        this.poster_path = "https://image.tmdb.org/t/p/w185" + poster_path;
    }

    public String getRelease_date(){
        return release_date;
    }

    public void setRelease_date(String release_date){
        this.release_date = release_date;
    }

    public String getOverview(){
        return overview;
    }

    public void setOverview(String overview){
        this.overview = overview;
    }

    public String getVote_average(){
        return vote_average;
    }

    public void setVote_average(String vote_average){
        this.vote_average = vote_average;
    }


}
