package com.inteligenciadigital.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inteligenciadigital.netflixremake.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

	private TextView txtTitle;
	private TextView txtDesc;
	private TextView txtCast;
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie);

		this.txtTitle = findViewById(R.id.text_view_title);
		this.txtDesc = findViewById(R.id.text_view_desc);
		this.txtCast = findViewById(R.id.text_view_cast);
		this.recyclerView = findViewById(R.id.recycler_view_similar);

		Toolbar toolbar = findViewById(R.id.tootbar);
		this.setSupportActionBar(toolbar);

		if (this.getSupportActionBar() != null) {
			this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
			this.getSupportActionBar().setTitle(null);
		}

//		LayerDrawable drawable = (LayerDrawable)
//				ContextCompat.getDrawable(this, R.drawable.shadows);
//
//		if (drawable != null) {
//			Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie);
//			drawable.setDrawableByLayerId(R.id.cover_drawble, movieCover);
//			((ImageView) findViewById(R.id.image_view_cover)).setImageDrawable(drawable);
//		}

		this.txtTitle.setText("Batman Begins");
		this.txtDesc.setText("There are many variations of passages " +
				"of Lorem Ipsum available, but the majority have " +
				"suffered alteration in some form, by injected humour," +
				" or randomised words which don't look even slightly believable.");
		this.txtCast.setText(getString(
				R.string.cast,
				"adfad " + "dfasdfa " + "ertert " + "çoeorw " +
						"adfad " + "dfasdfa " + "ertert " + "çoeorw " +
						"adfad " + "dfasdfa " + "ertert " + "çoeorw "
		));

		List<Movie> movies = new ArrayList<>();
		for (int j = 0; j < 30; j++) {
			Movie movie = new Movie();
			movies.add(movie);
		}

		this.recyclerView.setAdapter(new MovieAdapter(movies));
		this.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
	}

	private class MovieHolder extends RecyclerView.ViewHolder {

		private final ImageView imageViewCover;

		public MovieHolder(@NonNull View itemView) {
			super(itemView);
			this.imageViewCover = itemView.findViewById(R.id.image_view_cover);
		}
	}

	private class MovieAdapter extends RecyclerView.Adapter<MovieActivity.MovieHolder> {

		private List<Movie> movies;

		public MovieAdapter(List<Movie> movies) {
			this.movies = movies;
		}

		@NonNull
		@Override
		public MovieActivity.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = getLayoutInflater()
					.inflate(R.layout.movie_item_similar, parent, false);
			return new MovieActivity.MovieHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull MovieActivity.MovieHolder holder, int position) {
			Movie movie = this.movies.get(position);
//			holder.imageViewCover.setImageResource(movie.getCoverUrl());
		}

		@Override
		public int getItemCount() {
			return this.movies.size();
		}
	}
}