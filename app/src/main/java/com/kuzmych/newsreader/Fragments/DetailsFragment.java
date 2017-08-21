package com.kuzmych.newsreader.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuzmych.newsreader.Api.RSSItem;
import com.kuzmych.newsreader.Classes.ImageGetter;
import com.kuzmych.newsreader.DB.SQLiteDatabaseHandler;
import com.kuzmych.newsreader.MainActivity;
import com.kuzmych.newsreader.R;
import com.squareup.picasso.Picasso;


public class DetailsFragment extends Fragment {

	public static String TAG = "TAG_DetailsFragment";
	public final static String ITEM_ID = "ITEM_ID";

	private MainActivity mainActivity;
	private View this_view;
	private SQLiteDatabaseHandler db;

	private int item_id;
	private RSSItem news_item;

	//ui elements
	private TextView text_title;
	private TextView text_fulltext;
	private TextView text_date;
	private ImageView image;


	public DetailsFragment() {
	}

	//Static method to pass data for this fragment
	public static DetailsFragment newInstance(int item_id){
		DetailsFragment detailsFragment = new DetailsFragment();
		Bundle args = new Bundle();
		args.putInt(ITEM_ID, item_id);
		detailsFragment.setArguments(args);
		return detailsFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainActivity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		item_id = getArguments().getInt(ITEM_ID);

		this_view = inflater.inflate(R.layout.fragment_details, container, false);

		text_title = (TextView) this_view.findViewById(R.id.textView_title);
		text_fulltext = (TextView) this_view.findViewById(R.id.textView_fulltext);
		text_date = (TextView) this_view.findViewById(R.id.textView_date);
		image = (ImageView) this_view.findViewById(R.id.imageView_image);

		news_item = mainActivity.db.getItem(item_id);
		text_title.setText(news_item.getTitle());
		text_date.setText(news_item.getPubDate());

		ImageGetter ImgGetter = new ImageGetter(text_fulltext, this_view.getContext());
		Spanned htmlAsSpanned = Html.fromHtml(news_item.getFulltext(), ImgGetter, null);
		text_fulltext.setText(htmlAsSpanned);
		text_fulltext.setMovementMethod(LinkMovementMethod.getInstance());

		//WebView
		//webView_fulltext = (WebView) this_view.findViewById(R.id.webView_fulltext);
		//webView_fulltext.loadDataWithBaseURL("", news_item.getFulltext(), "text/html", "UTF-8", "");

		if (! news_item.getImage().isEmpty()) {
			Picasso.with(this_view.getContext())
					.load(news_item.getImage())
					.fit()
					.centerCrop()
					.placeholder(R.drawable.ic_loading)
					.error(R.drawable.ic_error)
					.into(image);
		}

		//ActionBar
		setHasOptionsMenu(true);

		return this_view;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem menuReftesh = menu.findItem(R.id.menu_refresh);
		menuReftesh.setVisible(false);

		MenuItem menuClearDB = menu.findItem(R.id.menu_clearDB);
		menuClearDB.setVisible(false);

		MenuItem menuOpenExternal = menu.findItem(R.id.menu_open_external);
		menuOpenExternal.setVisible(true);

		mainActivity.mActionBar.setDisplayHomeAsUpEnabled(true);

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_open_external:
				Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(news_item.getLink()));
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);

		}
	}

}