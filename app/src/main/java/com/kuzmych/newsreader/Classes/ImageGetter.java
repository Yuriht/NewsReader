package com.kuzmych.newsreader.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.kuzmych.newsreader.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageGetter implements Html.ImageGetter {
	private TextView textView = null;
	private Context context;

	public ImageGetter() {}

	public ImageGetter(TextView target, Context context) {
		textView = target;
		this.context = context;
	}

	@Override
	public Drawable getDrawable(String source) {
		String url = source;
		if (url.indexOf("http") == -1){
			url = url.replace("//", "http://");
		}
		BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();

		Picasso.with(context)
				.load(url)
				.resize(600,500)
				.centerCrop()
				.placeholder(R.drawable.ic_loading)
				.error(R.drawable.ic_error)
				.into(drawable);

		return drawable;
	}

	private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {
		protected Drawable drawable;

		@Override
		public void draw(final Canvas canvas) {
			if (drawable != null) {
				drawable.draw(canvas);
			}
		}

		public void setDrawable(Drawable drawable) {
			this.drawable = drawable;
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			if (textView != null) {
				textView.setText(textView.getText());
			}
		}

		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
			setDrawable(new BitmapDrawable(context.getResources(), bitmap));
		}

		@Override
		public void onBitmapFailed(Drawable errorDrawable) {
			setDrawable(errorDrawable);
		}

		@Override
		public void onPrepareLoad(Drawable placeHolderDrawable) {}
	}
}
