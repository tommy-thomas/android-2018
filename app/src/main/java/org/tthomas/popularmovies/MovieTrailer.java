package org.tthomas.popularmovies;

/**
 * Created by tommy-thomas on 1/15/18.
 */

public class MovieTrailer {
    public String ID;
//    iso_3166_1:"US"
//    iso_639_1:"en"
    public String Key;//"0sTBh7SuHDE"
    public String Name; //"80's VHS Style"
//    public String Site;  //"YouTube"
//    size:1080
    public String Type; //"Trailer"

    public String getID(){ return ID; }

    public void setID( String ID ){ this.ID = ID; }

    public String getKey(){ return Key; }

    public void setKey(String Key) { this.Key = Key; }

    public String getName() { return Name; }

    public void setName(String Name) { this.Name = Name; }

    public String getType() { return Type; }

    public void setType(String Type) { this.Type = Type; }
}
