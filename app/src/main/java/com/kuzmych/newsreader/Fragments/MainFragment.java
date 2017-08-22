package com.kuzmych.newsreader.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kuzmych.newsreader.Adapters.NewsAdapter;
import com.kuzmych.newsreader.Api.KorrespondentAPI;
import com.kuzmych.newsreader.Api.RSSFeed;
import com.kuzmych.newsreader.Api.RSSItem;
import com.kuzmych.newsreader.Classes.DividerItemDecoration;
import com.kuzmych.newsreader.Classes.RecyclerClickListener;
import com.kuzmych.newsreader.MainActivity;
import com.kuzmych.newsreader.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class MainFragment extends Fragment {

	private MainActivity mainActivity;
	private View this_view;
	private ProgressBar progressBar;

	private RecyclerView recyclerView;
	private NewsAdapter newsAdapter;
	private List<RSSItem> newsList = new ArrayList<>();

	private Retrofit retrofit;
	private KorrespondentAPI korrAPI;
	private static final String BASE_URL = "http://k.img.com.ua";


	public MainFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainActivity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		this_view = inflater.inflate(R.layout.fragment_main, container, false);
		progressBar = (ProgressBar) this_view.findViewById(R.id.progressBar);

		//Init List
		recyclerView = (RecyclerView) this_view.findViewById(R.id.recycler_view);
		newsAdapter = new NewsAdapter(this_view.getContext(), newsList);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mainActivity);
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.addItemDecoration(new DividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(newsAdapter);
		ListItemClickListener();

		//Init Retrofit
		retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
				.addConverterFactory(SimpleXmlConverterFactory.create())
				.build();
		korrAPI = retrofit.create(KorrespondentAPI.class);

		//Begin RSS download
		LoadRssList();

		return this_view;
	}


	public void LoadRssList(){
		progressBar.setVisibility(View.VISIBLE);

		Call<RSSFeed> call = korrAPI.loadRSSFeed();
		call.enqueue(new Callback<RSSFeed>() {
						 @Override
						 public void onResponse(Call<RSSFeed> call, Response<RSSFeed> response) {
							 if (response.isSuccessful()) {
								 RSSFeed rss = response.body();
								 SaveDB(rss.getRssList());
								 UpdateList();
								 progressBar.setVisibility(View.GONE);
							 } else {
								 //Show Error
								 progressBar.setVisibility(View.GONE);
								 Toast.makeText(this_view.getContext(), getString(R.string.list_refresh_error_text), Toast.LENGTH_SHORT).show();
								 Toast.makeText(this_view.getContext(), response.code()+" "+response.message(), Toast.LENGTH_LONG).show();
							 }
						 }

						 @Override
						 public void onFailure(Call<RSSFeed> call, Throwable t) {
							 //Show Error
							 progressBar.setVisibility(View.GONE);
							 Toast.makeText(this_view.getContext(), getString(R.string.list_refresh_error_text), Toast.LENGTH_SHORT).show();
							 Toast.makeText(this_view.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
							 //t.printStackTrace();
						 }
					 }
		);
	}

	// Saving new items to DB
	private void SaveDB(List<RSSItem> rssList) {
		String str;
		String find;
		for (int i=rssList.size()-1; i>=0; i--){
			//check for guid exist
			if (! mainActivity.db.GuidExist(rssList.get(i).getGuid())) {
				//parse some fields before Save
				//Date format
				str = rssList.get(i).getPubDate();
				Date date = new Date();
				DateFormat format_in = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
				SimpleDateFormat format_out =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				try {
					date = format_in.parse(str);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				rssList.get(i).setPubDate(String.valueOf(format_out.format(date)));

				//Image field - get link from: <img align="left" vspace="5" hspace="10" src="http://kor.ill.in.ua/m/190x120/2050181.jpg">
				str = rssList.get(i).getImage();
				find = "src=\"";
				rssList.get(i).setImage(str.substring(str.indexOf(find) + find.length(), str.lastIndexOf("\"")));

				//Description field - remove pre <img> part
				str = rssList.get(i).getDescription();
				find = "\">";
				rssList.get(i).setDescription(str.substring(str.lastIndexOf(find) + find.length(), str.length()));

				//SAVE
				mainActivity.db.addItem(rssList.get(i));
			}
		}
	}

	//Update List
	public void UpdateList() {
		newsList.clear();
		newsList.addAll(mainActivity.db.getAllItems());
		recyclerView.scrollToPosition(0);
		newsAdapter.notifyDataSetChanged();
	}

	//Recycler items click
	private void ListItemClickListener() {
		recyclerView.addOnItemTouchListener(new RecyclerClickListener(mainActivity, recyclerView, new RecyclerClickListener.ClickListener() {
			@Override
			public void onClick(View view, int position) {
				final int pos = position;
				if (newsList.get(pos).getId() >= 0) { //not system message
					mainActivity.ShowDetailsFragment(newsList.get(pos).getId());
				}
			}

			@Override
			public void onLongClick(View view, int position) {

			}
		}));
	}


}
