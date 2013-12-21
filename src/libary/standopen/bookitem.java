package libary.standopen;

public class bookitem {

	public int imageid;
	public String text;
	private int layoutID;
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
	
	public bookitem(int id,String text,int layout)
	{
		this.imageid=id;
		this.text=text;
		this.layoutID=layout;
		
	}
}
