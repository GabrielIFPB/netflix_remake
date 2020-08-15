package com.inteligenciadigital.netflixremake.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.inteligenciadigital.netflixremake.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

	private boolean shadowEnable;
	private WeakReference<ImageView> imageViewWeakReference;

	public ImageDownloadTask(ImageView imageViewWeakReference) {
		this.imageViewWeakReference = new WeakReference<>(imageViewWeakReference);
	}

	public void setShadowEnable(boolean shadowEnable) {
		this.shadowEnable = shadowEnable;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Bitmap doInBackground(String... strings) {
		String urlImg = strings[0];

		try {
			URL requestUrl = new URL(urlImg);
			HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();

			int responseCode = urlConnection.getResponseCode();
			if (responseCode != 200) return null;

			InputStream inputStream = urlConnection.getInputStream();
			if (inputStream != null)
				return BitmapFactory.decodeStream(inputStream);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (this.isCancelled()) {
			bitmap = null;
		}

		ImageView imageView = this.imageViewWeakReference.get();
		if (imageView != null && bitmap != null) {

			if (this.shadowEnable) {
				LayerDrawable drawable =(LayerDrawable) ContextCompat.getDrawable(
						imageView.getContext(), R.drawable.shadows
				);
				if (drawable != null) {
					BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
					drawable.setDrawableByLayerId(R.id.cover_drawble, bitmapDrawable);

					imageView.setImageDrawable(drawable);
				}
			} else {
				if (bitmap.getWidth() < imageView.getWidth() || bitmap.getHeight() < imageView.getHeight()) {
					Matrix matrix = new Matrix();
					matrix.postScale(
							(float) imageView.getWidth() / (float) bitmap.getWidth(),
							(float) imageView.getHeight() / (float) bitmap.getHeight()
					);

					bitmap = Bitmap.createBitmap(
							bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false
					);
				}
				imageView.setImageBitmap(bitmap);
			}
		}
	}
}
