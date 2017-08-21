package com.kuzmych.newsreader;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.kuzmych.newsreader.DB.SQLiteDatabaseHandler;
import com.kuzmych.newsreader.Fragments.DetailsFragment;
import com.kuzmych.newsreader.Fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

	private MainFragment mainFragment;
	public SQLiteDatabaseHandler db;
	private Toolbar mToolbar;
	public ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new SQLiteDatabaseHandler(this);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		mActionBar = getSupportActionBar();

		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}
			ShowMainFragment();
		}
	}

	public void ShowMainFragment() {
		mainFragment = new MainFragment();
		mainFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainFragment).commit();
	}

	public void ShowDetailsFragment(int item_id) {
		DetailsFragment fragment = DetailsFragment.newInstance(item_id);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.fragment_container, fragment);
		transaction.addToBackStack(fragment.TAG);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		MenuItem menuOpenExternal = menu.findItem(R.id.menu_open_external);
		menuOpenExternal.setVisible(false);

		mActionBar.setDisplayHomeAsUpEnabled(false);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				mainFragment.LoadRssList();
				Toast.makeText(this, R.string.toast_refresh_msg, Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_clearDB:
				db.deleteAllItems();
				mainFragment.UpdateList();
				return true;
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


}
