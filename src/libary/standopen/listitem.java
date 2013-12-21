package libary.standopen;

public class listitem {

	
	
	public int imageid;
	public String text;
	private int layoutID;
	public String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getLayoutID() {
		return layoutID;
	}
	public void setLayoutID(int layoutID) {
		this.layoutID = layoutID;
	}

	public int getImageid() {
		return imageid;
	}
	public void setImageid(int imageid) {
		this.imageid = imageid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public listitem(int id,String text,String url,int layout)
	{
		this.imageid=id;
		this.text=text;
		this.layoutID=layout;
		this.url=url;
		
	}
	
	
}
