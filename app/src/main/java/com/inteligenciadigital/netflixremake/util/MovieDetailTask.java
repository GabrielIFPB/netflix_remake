package com.inteligenciadigital.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.inteligenciadigital.netflixremake.model.Movie;
import com.inteligenciadigital.netflixremake.model.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MovieDetailTask extends AsyncTask<String, Void, MovieDetail> {

	private ProgressDialog dialog;
	private MovieDetailLoader movieDetailLoader;
	private final WeakReference<Context> context;

	public MovieDetailTask(Context context) {
		this.context = new WeakReference<>(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Context context = this.context.get();
		if (context != null)
			this.dialog = ProgressDialog.show(context, "Carregando", "", true);
	}

	@Override
	protected MovieDetail doInBackground(String... strings) {
		String url = strings[0];
		HttpsURLConnection urlConnection = null;
		try {
			URL requestUrl = new URL(url);
			urlConnection = (HttpsURLConnection) requestUrl.openConnection();
			urlConnection.setReadTimeout(2000);
			urlConnection.setConnectTimeout(2000);

			int responseCode = urlConnection.getResponseCode();
			if (responseCode > 400)	throw new IOException("Errro na comunicação do servidor");

			InputStream inputStream = urlConnection.getInputStream();

			BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

			String jsonAsString = this.toString(in);
			Log.i("Teste", jsonAsString);

			MovieDetail movieDetail = this.getMovieDetail(new JSONObject((jsonAsString)));

			in.close();
			return movieDetail;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return null;
	}

	private MovieDetail getMovieDetail(JSONObject jsonObject) throws JSONException {
		int id = jsonObject.getInt("id");
		String title = jsonObject.getString("title");
		String desc = jsonObject.getString("desc");
		String cast = jsonObject.getString("cast");
		String coverUrl = jsonObject.getString("cover_url");

		List<Movie> movies = new ArrayList<>();
		JSONArray jsonArray = jsonObject.getJSONArray("movie");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject movie = jsonArray.getJSONObject(i);

			String cover_url = movie.getString("cover_url");
			int idSimilar = movie.getInt("id");

			Movie similar = new Movie();
			similar.setId(idSimilar);
			similar.setCoverUrl(cover_url);
			movies.add(similar);
		}

		Movie movie = new Movie();
		movie.setId(id);
		movie.setTitle(title);
		movie.setDesc(desc);
		movie.setCast(cast);
		movie.setCoverUrl(coverUrl);
		return new MovieDetail(movie, movies);
	}

	@Override
	protected void onPostExecute(MovieDetail movieDetail) {
		super.onPostExecute(movieDetail);
		this.dialog.dismiss();

		if (this.movieDetailLoader != null) {
			this.movieDetailLoader.onResult(movieDetail);
		}
	}

	private String toString(InputStream is) throws IOException {
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int lidos;
		while ((lidos = is.read(bytes)) > 0) {
			baos.write(bytes, 0, lidos);
		}
		return new String(baos.toByteArray());
	}

	public void setMovieDetailLoader(MovieDetailLoader movieDetailLoader) {
		this.movieDetailLoader = movieDetailLoader;
	}

	public interface MovieDetailLoader {
		void onResult(MovieDetail movieDetail);
	}
}
