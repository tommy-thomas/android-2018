package org.tthomas.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.ViewHolder> {

    private ArrayList<MovieItem> movie;

    private Context context;

    final private String TAG = "poster-path";

    public MovieItemAdapter(Context context, ArrayList<MovieItem> movie) {
        this.movie = movie;
        this.context = context;
    }


    @Override
    public MovieItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_poster, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieItemAdapter.ViewHolder viewHolder, int i) {

        if(viewHolder != null ){

            i = viewHolder.getAdapterPosition();

            Picasso.with(context)
                    .load(movie.get(i).getPoster_path())
                    .into(viewHolder.moviePoster);

            Log.v(TAG , movie.get(viewHolder.getAdapterPosition()).getPoster_path() );
            
            viewHolder.moviePoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Context context = viewHolder.itemView.getContext();
                    int i = viewHolder.getAdapterPosition();
                    Intent showMovieDetails = new Intent(context, MovieDetailActivity.class);
                    showMovieDetails.putExtra("Movie" , movie.get(i));
                    context.startActivity(showMovieDetails);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return movie.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView moviePoster;

        public ViewHolder(View view) {
            super(view);
             moviePoster = view.findViewById(R.id.iv_movie_poster);
        }
    }

}
