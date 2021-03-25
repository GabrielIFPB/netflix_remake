package com.inteligenciadigital.netflixremake.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inteligenciadigital.netflixremake.MovieActivity
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

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder = CategoryHolder(
					layoutInflater.inflate(
							R.layout.category_item, parent, false)
			)

		override fun onBindViewHolder(holder: CategoryHolder, position: Int) = holder.bind(categories[position])

		override fun getItemCount(): Int = categories.size
	}

	private inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(category: Category) = with(itemView) {
			text_view_title.text = category.name
			recycler_view_movie.adapter = MovieAdapter(category.movies) { movie ->
				if (movie.id <= 3) {
					Toast.makeText(this@MainActivity,
							"Não foi implementado essa funcionalidade",
							Toast.LENGTH_SHORT)
				} else {
					val intent = Intent(this@MainActivity, MovieActivity::class.java)
					intent.putExtra("id", movie.id)
					startActivity(intent)
				}
			}
			recycler_view_movie.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
		}
	}

	private inner class MovieAdapter(val movies: MutableList<Movie>, private val listener: ((Movie) -> Unit)?) : RecyclerView.Adapter<MovieHolder>() {

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder = MovieHolder(
					layoutInflater.inflate(
							R.layout.movie_item, parent, false),
					listener
			)

		override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bind(movies[position])

		override fun getItemCount(): Int = movies.size
	}

	private class MovieHolder(itemView: View, val onClick: ((Movie) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
		fun bind(movie: Movie) = with(itemView) {
			ImageDownloadTask(image_view_cover).execute(movie.coverUrl)

			image_view_cover.setOnClickListener {
				onClick?.invoke(movie)
			}
		}
	}
}