package org.tthomas.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    private ArrayList<MovieTrailer> movieTrailer;

    private Context context;

    public MovieTrailerAdapter(Context context, ArrayList<MovieTrailer> movieTrailer ) {
        this.movieTrailer = movieTrailer;
        this.context = context;
    }

    @Override
    public MovieTrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_trailer, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieTrailerAdapter.ViewHolder viewHolder, int i) {

        if( viewHolder != null && movieTrailer.get(i).getType().toString().equals("Trailer") ){
            i = viewHolder.getAdapterPosition();
            viewHolder.movieTrailerName.setText(movieTrailer.get(i).getName().toString());
            viewHolder.movieTrailerImage.setImageResource(R.drawable.ic_movie);

            viewHolder.movieTrailerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = viewHolder.itemView.getContext();
                    int i = viewHolder.getAdapterPosition();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + movieTrailer.get(i).getKey().toString()));
                    Intent chooser = Intent.createChooser(intent, "Open the trailer using:");

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(chooser);
                    }
                }
            });

            Log.d("TRAILER NAME" ,movieTrailer.get(i).getType().toString());
        }

    }

    @Override
    public int getItemCount() {
        return movieTrailer.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView movieTrailerImage;
        private TextView movieTrailerName;

        public ViewHolder(View view) {
            super(view);
            movieTrailerImage = view.findViewById(R.id.iv_movie_trailer_image);
            movieTrailerName = view.findViewById(R.id.tv_movie_trailer_name);
        }
    }
}
