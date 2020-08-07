package com.inteligenciadigital.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.inteligenciadigital.netflixremake.model.Category;
import com.inteligenciadigital.netflixremake.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class JsonDownloadTask extends AsyncTask<String, Void, List<Category>> {

	private ProgressDialog dialog;
	private Context context;

	public JsonDownloadTask(Context context) {
		this.context = context;
	}

	// main-thread
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.dialog = ProgressDialog.show(this.context, "Carregando", "", true);
	}

	// thread - background
	@Override
	protected List<Category> doInBackground(String... strings) {
		String url = strings[0];
		try {
			URL requestUrl = new URL(url);
			HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
			urlConnection.setReadTimeout(2000);
			urlConnection.setConnectTimeout(2000);

			int responseCode = urlConnection.getResponseCode();
			if (responseCode > 400)	throw new IOException("Errro na comunicação do servidor");

			InputStream inputStream = urlConnection.getInputStream();

			BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

			String jsonAsString = this.toString(in);
			Log.i("Teste", jsonAsString);

			List<Category> categories = this.getCategories(new JSONObject((jsonAsString)));

			in.close();
			return categories;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	// main-thread
	@Override
	protected void onPostExecute(List<Category> categories) {
		super.onPostExecute(categories);
		this.dialog.dismiss();

//
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

	private List<Category> getCategories(JSONObject jsonObject) throws JSONException {
		List<Category> categories = new ArrayList<>();

		JSONArray categoryArray = jsonObject.getJSONArray("category");
		for (int i = 0; i < categoryArray.length(); i++) {
//			categoryArrayJSONObject variável abaixo
			JSONObject category = categoryArray.getJSONObject(i);
			String title = category.getString("title");

			List<Movie> movies = new ArrayList<>();
			JSONArray movieArray = category.getJSONArray("movie");
			for (int j = 0; j < movieArray.length(); j++) {
				JSONObject movie = movieArray.getJSONObject(j);
				String coverURl = movie.getString("cover_url");
				Movie movieObj = new Movie();
				movieObj.setCoverUrl(coverURl);
				movies.add(movieObj);
			}
			Category categoryObj = new Category();
			categoryObj.setName(title);
			categoryObj.setMovies(movies);
			categories.add(categoryObj);
		}
		return categories;
	}
}
