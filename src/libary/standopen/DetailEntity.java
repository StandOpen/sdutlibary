package libary.standopen;

public class DetailEntity {
    public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String name;
    private int  date;
    private String text;
    private int layoutID;
    private int temp;
    String url;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDate() {
		return date;
	}
	public int getTemp()
	{
		return temp;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getLayoutID() {
		return layoutID;
	}
	public void setLayoutID(int layoutID) {
		this.layoutID = layoutID;
	}
    
	public DetailEntity() {
	}
	public DetailEntity(int date1,String name, String text,String url, int layoutID) {
		super();
		this.date=date1;
		this.name = name;
		this.text = text;
		this.layoutID = layoutID;
		temp=date1;
		this.url=url;
	}
	
	
    
}
