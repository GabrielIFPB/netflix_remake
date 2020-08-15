package com.inteligenciadigital.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inteligenciadigital.netflixremake.model.Category;
import com.inteligenciadigital.netflixremake.model.Movie;
import com.inteligenciadigital.netflixremake.util.CategoryTask;
import com.inteligenciadigital.netflixremake.util.ImageDownloadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {

	private CategoryAdapter categoryAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RecyclerView recyclerView = findViewById(R.id.recycler_view_main);

		List<Category> categories = new ArrayList<>();

		this.categoryAdapter = new CategoryAdapter(categories);

		recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
		recyclerView.setAdapter(this.categoryAdapter);

		CategoryTask categoryTask = new CategoryTask(this);
		categoryTask.setCategoryLoader(this);
		categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");

	}

	@Override
	public void onResult(List<Category> categories) {
		this.categoryAdapter.setCategories(categories);
		this.categoryAdapter.notifyDataSetChanged();
	}

	private static class MovieHolder extends RecyclerView.ViewHolder {

		private final ImageView imageViewCover;

		public MovieHolder(@NonNull View itemView, final OnClickItemListener onClickItemListener) {
			super(itemView);
			this.imageViewCover = itemView.findViewById(R.id.image_view_cover);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					onClickItemListener.onClick(getAdapterPosition());
				}
			});
		}
	}

	private class MovieAdapter extends RecyclerView.Adapter<MovieHolder>
			implements OnClickItemListener {

		private List<Movie> movies;

		public MovieAdapter(List<Movie> movies) {
			this.movies = movies;
		}

		@NonNull
		@Override
		public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = getLayoutInflater().inflate(R.layout.movie_item, parent, false);
			return new MovieHolder(view, this);
		}

		@Override
		public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
			Movie movie = this.movies.get(position);
//			holder.imageViewCover.setImageResource(movie.getCoverUrl());
			new ImageDownloadTask(holder.imageViewCover).execute(movie.getCoverUrl());
		}

		@Override
		public int getItemCount() {
			return this.movies.size();
		}

		@Override
		public void onClick(int position) {
			Intent intent = new Intent(MainActivity.this, MovieActivity.class);
			intent.putExtra("id", this.movies.get(position).getId());
			startActivity(intent);
		}
	}

	private static class CategoryHolder extends RecyclerView.ViewHolder {

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

		public void setCategories(List<Category> categories) {
			this.categories.clear();
			this.categories.addAll(categories);
			this.categories.addAll(categories);
		}
	}

	interface OnClickItemListener {
		void onClick(int position);
	}
}