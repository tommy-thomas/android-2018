package org.tthomas.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReview implements Parcelable {

    public String id;
    public String content;
    public String author;

    public MovieReview(String id, String content, String author){
        this.id = id;
        this.content = content;
        this.author = author;
    }

    protected MovieReview(Parcel in) {
        id = in.readString();
        content = in.readString();
        author = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    public String getId(){ return id; }

    public void setId( String id ){ this.id = id; }

    public String getContent(){ return content; }

    public void setContent( String content ){ this.content = content; }

    public String getAuthor(){ return  author; }

    public void setAuthor( String author ){ this.author = author; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(author);
    }
}
