package org.tthomas.popularmovies;


import android.os.Parcel;
import android.os.Parcelable;

public class MovieItem implements Parcelable {

    private String id;
    private String title;
    private String poster_path;
    private String release_date;
    private String overview;
    private String vote_average;
    private String preview;

    public MovieItem(String id, String title, String poster_path, String release_date, String overview, String vote_average){
        this.id = id;
        this.title = title;
        this.poster_path = "https://image.tmdb.org/t/p/w185" + poster_path;
        this.release_date = release_date;
        this.overview = overview;
        this.vote_average = vote_average;
    }

    protected MovieItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        preview = in.readString();
    }

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

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(release_date);
        dest.writeString(overview);
        dest.writeString(vote_average);
        dest.writeString(preview);
    }
}
