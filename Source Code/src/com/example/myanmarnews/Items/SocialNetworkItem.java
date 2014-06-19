package com.example.myanmarnews.Items;

public class SocialNetworkItem {

	private String url;
	private int iconId;
	private int background;
	
	public SocialNetworkItem() {
		// TODO Auto-generated constructor stub
	}
	public SocialNetworkItem(String url, int iconId, int background){
		this.url = url;
		this.iconId = iconId;
		this.background = background;
		
		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getImageID() {
		return iconId;
	}

	public void setImageID(int imageID) {
		this.iconId = imageID;
	}
	/**
	 * @return the background
	 */
	public int getBackground() {
		return background;
	}
	/**
	 * @param background the background to set
	 */
	public void setBackground(int background) {
		this.background = background;
	}
	

}
