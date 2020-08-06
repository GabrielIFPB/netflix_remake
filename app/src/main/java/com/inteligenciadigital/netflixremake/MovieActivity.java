package com.inteligenciadigital.netflixremake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class MovieActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie);
		Toolbar toolbar = findViewById(R.id.tootbar);
		this.setSupportActionBar(toolbar);

		if (this.getSupportActionBar() != null) {
			this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
		}
	}
}