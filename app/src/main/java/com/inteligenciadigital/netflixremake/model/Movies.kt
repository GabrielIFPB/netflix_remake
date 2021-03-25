package com.inteligenciadigital.netflixremake.model

import com.google.gson.annotations.SerializedName

data class Category(
		@SerializedName("title") var name: String = "",
		@SerializedName("movie") var movies: MutableList<Movie> = arrayListOf()
)

data class Categories(@SerializedName("category") val categories: MutableList<Category>)

data class Movie(
		var id: Int = 0,
		var title: String = "",
		@SerializedName("cover_url") var coverUrl: String = "",
		var desc: String = "",
		var cast: String = "",
)

data class MovieDetail(
		var id: Int = 0,
		var title: String = "",
		@SerializedName("cover_url") var coverUrl: String = "",
		var desc: String = "",
		var cast: String = "",
		@SerializedName("movie") val moviesSimilar: List<Movie>
)