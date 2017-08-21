package com.kuzmych.newsreader.Api;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="rss", strict=false)
public class RSSFeed {

	@ElementList(name = "item", inline = true)
	@Path("channel")
	private List<RSSItem> rssList;

	public List<RSSItem> getRssList() {
		return rssList;
	}
	public void setRssList(List<RSSItem> rssList) {
		this.rssList = rssList;
	}

}