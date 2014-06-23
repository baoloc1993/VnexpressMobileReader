package ngo.vnexpress.reader.Items;

public class DrawerItem {

	private String title;
	private int imageID;
	private int notification;
	
	public DrawerItem() {
		// TODO Auto-generated constructor stub
	}
	public DrawerItem(String content, int imageID){
		this.title = content;
		this.imageID = imageID;
		notification = 0;
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
	public int getNotification() {
		return notification;
	}
	public void setNotification(int notification) {
		this.notification = notification;
	}

}
