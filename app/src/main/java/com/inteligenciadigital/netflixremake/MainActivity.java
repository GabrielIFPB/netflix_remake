package com.inteligenciadigital.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inteligenciadigital.netflixremake.model.Category;
import com.inteligenciadigital.netflixremake.model.Movie;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private CategoryAdapter categoryAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RecyclerView recyclerView = findViewById(R.id.recycler_view_main);

		List<Category> categories = new ArrayList<>();
		for (int i = 0; i < 10; i ++) {
			Category category = new Category();
			category.setName("Categoria " + i);

			List<Movie> movies = new ArrayList<>();
			for (int j = 0; j < 30; j++) {
				Movie movie = new Movie();
				movie.setCoverUrl(R.drawable.movie);
				movies.add(movie);
			}
			category.setMovies(movies);
			categories.add(category);
		}

		this.categoryAdapter = new CategoryAdapter(categories);

		recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
		recyclerView.setAdapter(this.categoryAdapter);

	}

	private class MovieHolder extends RecyclerView.ViewHolder {

		private final ImageView imageViewCover;

		public MovieHolder(@NonNull View itemView) {
			super(itemView);
			this.imageViewCover = itemView.findViewById(R.id.image_view_cover);
		}
	}

	private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

		private List<Movie> movies;

		public MovieAdapter(List<Movie> movies) {
			this.movies = movies;
		}

		@NonNull
		@Override
		public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = getLayoutInflater().inflate(R.layout.movie_item, parent, false);
			return new MovieHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
			Movie movie = this.movies.get(position);
			holder.imageViewCover.setImageResource(movie.getCoverUrl());

		}

		@Override
		public int getItemCount() {
			return this.movies.size();
		}
	}

	private class CategoryHolder extends RecyclerView.ViewHolder {

		private final TextView textViewTitle;
		private final RecyclerView recyclerViewMovie;

		public CategoryHolder(@NonNull View itemView) {
			super(itemView);
			this.textViewTitle = itemView.findViewById(R.id.text_view_title);
			this.recyclerViewMovie = itemView.findViewById(R.id.recycler_view_movie);
		}
	}

	private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

		private List<Category> categories;

		public CategoryAdapter(List<Category> categories) {
			this.categories = categories;
		}

		@NonNull
		@Override
		public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = getLayoutInflater().inflate(R.layout.category_item, parent, false);
			return new CategoryHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
			Category category = this.categories.get(position);
			holder.textViewTitle.setText(category.getName());
			holder.recyclerViewMovie.setAdapter(new MovieAdapter(category.getMovies()));
			holder.recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
		}

		@Override
		public int getItemCount() {
			return this.categories.size();
		}
	}
}