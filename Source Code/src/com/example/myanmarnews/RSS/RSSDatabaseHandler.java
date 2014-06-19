/**
 * Function:
 * 	Add a new row in websites table
	Returns all the rows as Website class objects
	Update existing row
	Returns single row
	Deletes a single row
	Check if a website is already existed
 */
package com.example.myanmarnews.RSS;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RSSDatabaseHandler extends SQLiteOpenHelper {
	 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "rssReader";
 
    // Contacts table name
    private static final String TABLE_RSS = "websites";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LINK = "link";
    //private static final String KEY_RSS_LINK = "rss_link";
    private static final String KEY_DESCRIPTION = "description";
    //private static final String KEY_IMG = "img_name";
    private static final String KEY_IMG_LINK = "img_link";
    private static final String KEY_PUBLIC_DATE = "public_date";
    
    private Context myContext;
 
    

	//private long insertedRowIndex;
    
    public RSSDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        //this.getWritableDatabase();
       // SQLiteDatabase sqlite;
       // sqlite = this.getWritableDatabase();
        
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
    	
        String CREATE_RSS_TABLE = "CREATE TABLE " + TABLE_RSS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY, " + KEY_TITLE + " TEXT, " + KEY_LINK
                + " TEXT, " +  KEY_IMG_LINK + " TEXT, " 
                + KEY_DESCRIPTION + " TEXT, " + KEY_PUBLIC_DATE + " TEXT" + ");";

        db.execSQL(CREATE_RSS_TABLE);
    	

    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS);
        
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Adding a new website in websites table Function will check if a site
     * already existed in database. If existed will update the old one else
     * creates a new row
     * */
    public void addSite(WebSite site) {
    	
    	
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, site.getTitle()); // site title
        values.put(KEY_LINK, site.getLink()); // site url
        values.put(KEY_IMG_LINK, site.getImageLink()); // rss img 
        values.put(KEY_DESCRIPTION, site.getDescription()); // site description
        //values.put(KEY_RSS_LINK, site.getRSSLink()); // rss rss url
        values.put(KEY_PUBLIC_DATE, site.getPubDate());   //pulic date
   
        // Check if row already existed in database
       // Log.d("IMAGE LINK", site.getImageLink());
        if (!isSiteExists(db, site.getImageLink())) {
            // site not existed, create a new row
            db.insert(TABLE_RSS, null, values);
        
           // Log.d("DATABASE", "INSERT SUCCESSFULLY");
            db.close();
        } else {
            // site already existed update the row
            updateSite(site);
            db.close();
        }
    }
 
    /**
     * Reading all rows from database
     * */
    public List<WebSite> getAllSitesByID() {
        List<WebSite> siteList = new ArrayList<WebSite>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RSS
                + " ORDER BY " + KEY_ID + " DESC";
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WebSite site = new WebSite();
                site.setId(Integer.parseInt(cursor.getString(0)));
                site.setTitle(cursor.getString(1));
                site.setLink(cursor.getString(2));
                site.setImageLink(cursor.getString(3));
                site.setDescription(cursor.getString(4));
                site.setPubDate(cursor.getString(5));
                // Adding contact to list
                siteList.add(site);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
 
        // return contact list
        
        return siteList;
    }
 
//    /**
//     * Reading all rows from database
//     * */
//    public List<WebSite> getAllSitesLimited(int limitedNumber) {
//    	int count = 0;
//        List<WebSite> siteList = new ArrayList<WebSite>();
//        // Select All Query
//        String selectQuery = "SELECT * FROM " + TABLE_RSS
//                + " ORDER BY " + KEY_ID + " DESC";
// 
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
// 
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//            	count++;
//                WebSite site = new WebSite();
//                site.setId(Integer.parseInt(cursor.getString(0)));
//                site.setTitle(cursor.getString(1));
//                site.setLink(cursor.getString(2));
//                site.setImageLink(cursor.getString(3));
//                site.setDescription(cursor.getString(4));
//                site.setPubDate(cursor.getString(5));
//                // Adding contact to list
//                siteList.add(site);
//            } while (cursor.moveToNext() && count < limitedNumber);
//        }
//        cursor.close();
//        db.close();
// 
//        // return contact list
//        
//        return siteList;
//    }

    /**
     * Updating a single row row will be identified by rss link
     * */
    public int updateSite(WebSite site) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, site.getTitle());
        values.put(KEY_LINK, site.getLink());
       // values.put(KEY_RSS_LINK, site.getRSSLink());
        values.put(KEY_DESCRIPTION, site.getDescription());
        values.put(KEY_IMG_LINK, site.getImageLink());
        values.put(KEY_PUBLIC_DATE, site.getPubDate());
 
        // updating row return
        int update = db.update(TABLE_RSS, values, KEY_IMG_LINK + " = ?",
                new String[] { String.valueOf(site.getImageLink()) });
        db.close();
        return update;
 
    }
 
    /**
     * Reading a row (website) row is identified by row id
     * */
    public WebSite getSiteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_RSS, new String[] { KEY_ID, KEY_TITLE,
                KEY_LINK, KEY_IMG_LINK, KEY_DESCRIPTION, KEY_PUBLIC_DATE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        WebSite site = new WebSite(cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
 
        site.setId(Integer.parseInt(cursor.getString(0)));
        site.setTitle(cursor.getString(1));
        site.setLink(cursor.getString(2));
        site.setImageLink(cursor.getString(3));
        site.setDescription(cursor.getString(4));
        site.setPubDate(cursor.getString(5));
        cursor.close();
        db.close();
        return site;
    }
    
    /**
     * Reading a row (website) row is identified by link 
     * */
    public WebSite getSiteByLink(String link) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_RSS, new String[] { KEY_ID, KEY_TITLE,
                KEY_LINK, KEY_IMG_LINK, KEY_DESCRIPTION, KEY_PUBLIC_DATE }, KEY_LINK + "=?",
                new String[] { link }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        WebSite site = new WebSite(cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
 
        site.setId(Integer.parseInt(cursor.getString(0)));
        site.setTitle(cursor.getString(1));
        site.setLink(cursor.getString(2));
        site.setImageLink(cursor.getString(3));
        site.setDescription(cursor.getString(4));
        site.setPubDate(cursor.getString(5));
        cursor.close();
        db.close();
        return site;
    }
 
    /**
     * Deleting single row
     * */
    public void deleteSite(WebSite site) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RSS, KEY_ID + " = ?",
                new String[] { String.valueOf(site.getId())});
        db.close();
    }
 
    /**
     * Checking whether a site is already existed check is done by matching rss
     * link
     * */
    public boolean isSiteExists(SQLiteDatabase db, String img_link) {
 
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_RSS
                + " WHERE img_link = '" + img_link + "'", new String[] {});
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }
    
    /**
     * Get the size of the database
     */
    public int getDatabaseSize(){
    	String countQuery = "SELECT  * FROM " + TABLE_RSS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
 
}
