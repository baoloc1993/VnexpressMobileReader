package com.vnexpressandroidreader.Items;

public class NewsItem {

	private String title;
	private int imageID;
	private String content;
	private String publicDate;
	private String imageURL;
	
	public NewsItem() {
		// TODO Auto-generated constructor stub
	}
	public NewsItem(String title, int imageID, String content, String publicDate){
		this.setTitle(title);
		this.imageID = imageID;
		this.content = content;
		this.setPublicDate(publicDate);
		this.setImageURL("");				//"" if no image url
	
	}
	
	public NewsItem(String title, String imageURL, String content, String publicDate){
		this.setTitle(title);
		this.setImageURL(imageURL);
		this.content = content;
		this.setPublicDate(publicDate);
		this.imageID = -1;						//-1 if no image id
	
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublicDate() {
		return publicDate;
	}
	public void setPublicDate(String publicDate) {
		this.publicDate = publicDate;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

}
