package com.kuzmych.newsreader.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuzmych.newsreader.Api.RSSItem;
import com.kuzmych.newsreader.R;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

	private List<RSSItem> items;
	private Context context;

	class ViewHolder extends RecyclerView.ViewHolder {
		public TextView tittle, description, date;
		public ImageView image;
		public ViewHolder(View itemView) {
			super(itemView);
			tittle = (TextView) itemView.findViewById(R.id.textView_tittle);
			description = (TextView) itemView.findViewById(R.id.textView_description);
			date = (TextView) itemView.findViewById(R.id.textView_date);
			image = (ImageView) itemView.findViewById(R.id.imageView_image);
		}
	}

	public NewsAdapter(Context context, List<RSSItem> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.news_list_row, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		RSSItem item = items.get(position);
		holder.tittle.setText(item.getTitle());
		holder.description.setText(item.getDescription());
		holder.date.setText(item.getPubDate());

		if ( !(item.getImage().isEmpty() || item.getImage().length() == 0) ) {
			Picasso.with(context)
					.load(item.getImage())
					.fit()
					.centerCrop()
					.placeholder(R.drawable.ic_loading)
					.error(R.drawable.ic_error)
					.into(holder.image);
		}
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public RSSItem getItem(int position) {
		return items.get(position);
	}

}