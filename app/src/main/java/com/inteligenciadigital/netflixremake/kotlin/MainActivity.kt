package com.inteligenciadigital.netflixremake.kotlin

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inteligenciadigital.netflixremake.R
import com.inteligenciadigital.netflixremake.model.Category
import com.inteligenciadigital.netflixremake.model.Movie
import com.inteligenciadigital.netflixremake.util.CategoryTask
import com.inteligenciadigital.netflixremake.util.ImageDownloadTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.movie_item_similar.view.*
import kotlinx.android.synthetic.main.movie_item_similar.view.image_view_cover


class MainActivity : AppCompatActivity() {

	private lateinit var mainAdapter : MainAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		this.setContentView(R.layout.activity_main)

		val categories = arrayListOf<Category>()
		this.mainAdapter = MainAdapter(categories)
		recycler_view_main.adapter = this.mainAdapter
		recycler_view_main.layoutManager = LinearLayoutManager(this)

		val categoryTask = CategoryTask(this)
		categoryTask.setCategoryLoader { categories ->
			this.mainAdapter.categories.clear()
			this.mainAdapter.categories.addAll(categories)
			this.mainAdapter.categories.addAll(categories)
			this.mainAdapter.notifyDataSetChanged()
		}
		categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")
	}

	private inner class MainAdapter(val categories: MutableList<Category>) : RecyclerView.Adapter<CategoryHolder>() {
		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
			return CategoryHolder(
					layoutInflater.inflate(
							R.layout.category_item, parent, false)
			)
		}

		override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
			val category = categories[position]
			holder.bind(category)
		}

		override fun getItemCount(): Int = categories.size
	}

	private inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(category: Category) {
			itemView.text_view_title.text = category.name
			itemView.recycler_view_movie.adapter = MovieAdapter(category.movies)
			itemView.recycler_view_movie.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
		}
	}

	private inner class MovieAdapter(val movies: MutableList<Movie>) : RecyclerView.Adapter<MovieHolder>() {
		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
			return MovieHolder(
					layoutInflater.inflate(
							R.layout.movie_item, parent, false)
			)
		}

		override fun onBindViewHolder(holder: MovieHolder, position: Int) {
			val movie = movies[position]
			holder.bind(movie)
		}

		override fun getItemCount(): Int = movies.size
	}

	private class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(movie: Movie) {
			ImageDownloadTask(itemView.image_view_cover)
					.execute(movie.coverUrl)

			itemView.image_view_cover.setOnClickListener {

			}
		}
	}
}