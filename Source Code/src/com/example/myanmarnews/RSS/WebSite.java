package com.example.myanmarnews.RSS;

/**
 * This class file used while inserting data or retrieving data from 
 * SQLite database
 * **/
public class WebSite {
    Integer _id;
    String _title;
    String _link;
    //String _rss_link;
    String _description;
    //byte[] _img;
    String image_link;
    String pub_date;
     
    // constructor
    public WebSite(){
         
    }
 
    // constructor with parameters
    public WebSite(String title, String link,String description, String pub_date,String image_link){
        this._title = title;
        this._link = link;
        //this._img = _img;
        this.image_link = image_link;
        this._description = description;
        this.pub_date = pub_date;
    }
     
    /**
     * All set methods
     * */
    public void setId(Integer id){
        this._id = id;
    }
     
    public void setTitle(String title){
        this._title = title;
    }
     
    public void setLink(String link){
        this._link = link;
    }
     
//    public void setRSSLink(String rss_link){
//        this._rss_link = rss_link;
//    }
     
    public void setDescription(String description){
        this._description = description;
    }
    
    public void setImageLink(String image_link){
    	this.image_link = image_link;
    }
    
    public void setPubDate(String pubDate){
    	this.pub_date = pubDate;
    }
    
     
    /**
     * All get methods
     * */
    public Integer getId(){
        return this._id;
    }
     
    public String getTitle(){
        return this._title;
    }
     
    public String getLink(){
        return this._link;
    }
     
//    public String getRSSLink(){
//        return this._rss_link;
//    }
     
    public String getDescription(){
        return this._description;
    }
    
    public String getImageLink(){
    	return this.image_link;
    }
    
    public String getPubDate(){
    	return this.pub_date;
    }
    
}