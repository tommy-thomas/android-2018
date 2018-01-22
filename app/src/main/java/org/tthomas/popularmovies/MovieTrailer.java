package org.tthomas.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tommy-thomas on 1/15/18.
 */

public class MovieTrailer implements Parcelable {
    public String ID;
    public String Key;
    public String Name;
    public String Type;

    public MovieTrailer(String ID, String Key, String Name , String Type){
        this.ID = ID;
        this.Key = Key;
        this.Name = Name;
        this.Type = Type;

    }

    protected MovieTrailer(Parcel in) {
        ID = in.readString();
        Key = in.readString();
        Name = in.readString();
        Type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Key);
        dest.writeString(Name);
        dest.writeString(Type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    public String getID(){ return ID; }

    public void setID( String ID ){ this.ID = ID; }

    public String getKey(){ return Key; }

    public void setKey(String Key) { this.Key = Key; }

    public String getName() { return Name; }

    public void setName(String Name) { this.Name = Name; }

    public String getType() { return Type; }

    public void setType(String Type) { this.Type = Type; }
}
