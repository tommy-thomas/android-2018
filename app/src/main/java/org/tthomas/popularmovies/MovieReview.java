package org.tthomas.popularmovies;

/**
 * Created by tommy-thomas on 1/17/18.
 */

public class MovieReview {

//    author:"piyushgupta69"
//    content:"Previous 2 parts were much better story wise, however effects are good in this part."
//    id:"5a4ff969c3a3681bca00082a"
//    url:"https://www.themoviedb.org/review/5a4ff969c3a3681bca00082a"
    public String id;
    public String content;
    public String author;

    public String getId(){ return id; }

    public void setId( String id ){ this.id = id; }

    public String getContent(){ return content; }

    public void setContent( String content ){ this.content = content; }

    public String getAuthor(){ return  author; }

    public void setAuthor( String author ){ this.author = author; }

}
