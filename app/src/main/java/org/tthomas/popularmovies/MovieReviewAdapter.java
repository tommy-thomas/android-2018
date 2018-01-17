package org.tthomas.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private ArrayList<MovieReview> movieReview;

    private Context context;

    public MovieReviewAdapter(Context context, ArrayList<MovieReview> movieReview){
        this.movieReview = movieReview;
        this.context = context;
    }

    @Override
    public MovieReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_review, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MovieReviewAdapter.ViewHolder viewHolder, int i) {
        if( viewHolder != null){
            i = viewHolder.getAdapterPosition();
            viewHolder.content.setText( movieReview.get(i).getContent().toString() );
        }
    }


    @Override
    public int getItemCount() {
        return movieReview.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView content;
        public ViewHolder(View view) {
            super(view);

            content = view.findViewById(R.id.tv_movie_review);
        }
    }
}
