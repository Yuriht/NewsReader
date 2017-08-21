package com.kuzmych.newsreader.Api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface KorrespondentAPI {
	@GET("/rss/ru/all_news2.0.xml")
	Call<RSSFeed> loadRSSFeed();
}
