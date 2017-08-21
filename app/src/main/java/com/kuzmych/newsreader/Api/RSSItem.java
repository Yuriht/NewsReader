package com.kuzmych.newsreader.Api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class RSSItem {

	private int id;

	@Element(name = "title")
	private String title;
	@Element(name = "link")
	private String link;
	@Element(name = "description")
	private String description;
	@Element(name = "fulltext")
	private String fulltext;
	@Element(name = "image")
	private String image;
	@Element(name = "pubDate")
	private String pubDate;
	@Element(name = "guid")
	private String guid;

	public RSSItem() {
	}

	public RSSItem(int id, String title, String link, String description, String fulltext, String image, String pubDate, String guid) {
		this.id = id;
		this.title = title;
		this.link = link;
		this.description = description;
		this.fulltext = fulltext;
		this.image = image;
		this.pubDate = pubDate;
		this.guid = guid;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFulltext() {
		return fulltext;
	}
	public void setFulltext(String fulltext) {
		this.fulltext = fulltext;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
}