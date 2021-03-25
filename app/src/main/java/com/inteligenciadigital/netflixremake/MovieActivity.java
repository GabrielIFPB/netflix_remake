package com.inteligenciadigital.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inteligenciadigital.netflixremake.model.Movie;
import com.inteligenciadigital.netflixremake.model.MovieDetail;
import com.inteligenciadigital.netflixremake.util.ImageDownloadTask;
import com.inteligenciadigital.netflixremake.util.MovieDetailTask;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements MovieDetailTask.MovieDetailLoader {

	private TextView txtTitle;
	private TextView txtDesc;
	private TextView txtCast;
	private ImageView imgCover;
	private MovieAdapter movieAdapter;
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie);

		this.txtTitle = findViewById(R.id.text_view_title);
		this.txtDesc = findViewById(R.id.text_view_desc);
		this.txtCast = findViewById(R.id.text_view_cast);
		this.imgCover = findViewById(R.id.image_view_cover);
		this.recyclerView = findViewById(R.id.recycler_view_similar);

		Toolbar toolbar = findViewById(R.id.toolbar);
		this.setSupportActionBar(toolbar);

		if (this.getSupportActionBar() != null) {
			this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
			this.getSupportActionBar().setTitle(null);
		}

		List<Movie> movies = new ArrayList<>();
		this.movieAdapter = new MovieAdapter(movies);
		this.recyclerView.setAdapter(this.movieAdapter);
		this.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

		Bundle extras = this.getIntent().getExtras();
		if (extras != null) {
			int id = extras.getInt("id");
			MovieDetailTask movieDetailTask = new MovieDetailTask(this);
			movieDetailTask.setMovieDetailLoader(this);
			movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/" + id);
		}
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == android.R.id.home) this.finish();

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResult(MovieDetail movieDetail) {
		this.txtTitle.setText(movieDetail.getMovie().getTitle());
		this.txtDesc.setText(movieDetail.getMovie().getDesc());
		this.txtCast.setText(movieDetail.getMovie().getCast());

		ImageDownloadTask imageDownloadTask = new ImageDownloadTask(this.imgCover);
		imageDownloadTask.setShadowEnable(true);
		imageDownloadTask.execute(movieDetail.getMovie().getCoverUrl());

		this.movieAdapter.setMovies(movieDetail.getMoviesSimilar());
		this.movieAdapter.notifyDataSetChanged();
	}

	private static class MovieHolder extends RecyclerView.ViewHolder {

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
			return new MovieHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull MovieActivity.MovieHolder holder, int position) {
			Movie movie = this.movies.get(position);
			new ImageDownloadTask(holder.imageViewCover).execute(movie.getCoverUrl());
		}

		@Override
		public int getItemCount() {
			return this.movies.size();
		}

		public void setMovies(List<Movie> movies) {
			this.movies.clear();
			this.movies = movies;
		}
	}
}