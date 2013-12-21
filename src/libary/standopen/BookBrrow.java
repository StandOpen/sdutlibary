package libary.standopen;

import java.io.IOException;
import java.util.ArrayList;



import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import org.jsoup.nodes.Element;

public class BookBrrow extends Activity {
    /** Called when the activity is first created. */
	private ListView list;
	private ArrayList<bookitem> items = null;
	protected static int timeout = 1000*60;
	protected final static String user_agent = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
	protected static String LOGIN_FLAG = "PHPSESSID";
	private Button GetInfo=null;
	private HttpClient httpClient=null;
	static final String LOGON_SITE = "localhost";
    static final int    LOGON_PORT = 8080;
    private ArrayList<String> codes=null;
    Cookie[] cookies;
    private Dialog mDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history);
        list= (ListView)findViewById(R.id.list);
       
        GetInfo=(Button)findViewById(R.id.gotosearch);
        httpClient=SdutLibary.instance.httpClient;
        codes=new ArrayList<String>();
        GetInfo.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(SdutLibary.instance.IsLogin)
				{
					showRoundProcessDialog(BookBrrow.this.getParent(),R.layout.loading_process_dialog_color);
					  readthread mythread=new readthread();
					  mythread.start();
				}
				else
				{
					SdutLibary.instance.OtherLogin();
				}
			}
		});
        
        
        list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(items.get(arg2).getText().toString().length()==0)
				{
					
					XuJie(codes.get(arg2/8).toString());
					showRoundProcessDialog(BookBrrow.this.getParent(),R.layout.loading_process_dialog_color);
					  readthread mythread=new readthread();
					  mythread.start();
				}
			}
		});
        
        
      list.setOnScrollListener(new OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(view.getLastVisiblePosition()<6)
				{
				
					GetInfo.setVisibility(0);

				}
				else
				{
					GetInfo.setVisibility(8);
			
				}
				
			
			
		}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}});
        }	
	public void GetMsg()
	{
		 items= new ArrayList<bookitem>();
		Document doc;
	try {
		GetMethod get = new GetMethod("http://222.206.65.12/reader/book_lst.php");
    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient.executeMethod(get);
		byte[] responseBody = get.getResponseBody();
         String str=new String(responseBody);
         doc=Jsoup.parse(str);
    
		Element div=doc.getElementById("mylib_content");
		Document doc1=Jsoup.parse(div.toString());
		Elements trs=doc1.getElementsByTag("td");
		for (int i=8;i<trs.size();) {
			if(i>=8)
			{
             
				if(i%8==0)
				{
					bookitem info=new bookitem(R.drawable.bookitembg,i/8+": "+trs.get(i+1).text().toString().trim()+"", R.layout.bookitem);
                items.add(info);
				}
		            
			bookitem position1=new bookitem(R.drawable.bottom1,"条码号:  "+trs.get(i).text().toString().trim(), R.layout.bookitem);
			codes.add(trs.get(i).text().toString().trim());
			bookitem position2=new bookitem(R.drawable.bottom1,"题名:  "+trs.get(i+1).text().toString().trim(), R.layout.bookitem);
				
				
			bookitem position3=new bookitem(R.drawable.bottom1,"责任者:  "+trs.get(i+2).text().toString().trim(), R.layout.bookitem);
				
			bookitem	  position4=new bookitem(R.drawable.bottom1,"借阅日期:  "+trs.get(i+3).text().toString().trim(), R.layout.bookitem);
				
			bookitem	  position5=new bookitem(R.drawable.bottom1,"应还日期:  "+trs.get(i+4).text().toString().trim(), R.layout.bookitem);
			bookitem	  position6=new bookitem(R.drawable.bottom1,"馆藏地:  "+trs.get(i+5).text().toString().trim(), R.layout.bookitem);
			bookitem	  position7=new bookitem(R.drawable.xujie,"", R.layout.bookitem);
				i+=8;
	             
	             items.add(position1);
	             items.add(position2);
	             items.add(position3);
	             items.add(position4);
	             items.add(position5);
	             items.add(position6);
	             items.add(position7);
	    }
		}
   
		
		list.setAdapter(new BookAdapter(BookBrrow.this,items));
	
	
	
	
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	}
	
	
	
	public void XuJie(String code)
	{
		Toast.makeText(BookBrrow.this, "正在续借", Toast.LENGTH_SHORT).show();
	    	 httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1");
	         httpClient.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
	         httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
	         httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
	         PostMethod postMethod = new PostMethod("http://222.206.65.12/reader/ajax_renew.php?bar_code="+code+"&time=1363175603592");     
	        
	      try {
	        int statusCode = httpClient.executeMethod(postMethod);
	        
	       if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
	                       Header locationHeader = postMethod.getResponseHeader("location");
	                       @SuppressWarnings("unused")
						String location = null;
	                       if (locationHeader != null) {
	                              location = locationHeader.getValue();
	                              Toast.makeText(BookBrrow.this,"fjkasngfkjasnfkj",Toast.LENGTH_SHORT).show();
	                         
	                       } else {
	                       
	                    	   Toast.makeText(BookBrrow.this,"Location field value is null.",Toast.LENGTH_SHORT).show();
	                       }
	                       return;
	                }                        
	                if (statusCode != HttpStatus.SC_OK) {
	                    
	                	Toast.makeText(BookBrrow.this,""+postMethod.getStatusLine(),Toast.LENGTH_SHORT).show();
	                }                    
	                
	         } catch (HttpException e) {
	              
	        	 Toast.makeText(BookBrrow.this,"Please check your provided http    address!",Toast.LENGTH_SHORT).show();
	                e.printStackTrace();
	         } catch (IOException e) {
	                e.printStackTrace();
	         } finally {
	        	    postMethod.releaseConnection();
	         	
	 }
	      GetMsg();
	    }
	
	
	public void showRoundProcessDialog(Context mContext, int layout)
    {
        OnKeyListener keyListener = new OnKeyListener()
        {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH)
                {
                    return true;
                }
                return false;
            }
        };

        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setOnKeyListener(keyListener);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }
    
    
    public class readthread extends Thread
    {
    	
       public readthread()
       {
    	  
       }
       
       
       Handler handler=new Handler(){

   		@SuppressLint("HandlerLeak")
		@Override
   		public void handleMessage(Message msg) {
   			// TODO Auto-generated method stub
   			super.handleMessage(msg);
   	    	if(msg.obj.toString().length()>0)
   	    	{
   			
   	    		list.setAdapter(new BookAdapter(BookBrrow.this,items));
   	    	}
   	    	else
   	    	{
   	    		Toast toast = Toast.makeText(getApplicationContext(),
		        	     "获取信息失败，请检查网络后重试！", Toast.LENGTH_LONG);
		        	   toast.setGravity(Gravity.CENTER, 0, 0);
		        	   LinearLayout toastView = (LinearLayout) toast.getView();
		        	   ImageView imageCodeProject = new ImageView(getApplicationContext());
		        	   imageCodeProject.setImageResource(R.drawable.menu_about);
		        	   toastView.addView(imageCodeProject, 0);
		        	   toast.show();		
   	    	}
   			mDialog.dismiss();
      	   
      	   
      	   

  	 
   		}
   		
   	};
       
       
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			 items= new ArrayList<bookitem>();
				Document doc;
			try {
				GetMethod get = new GetMethod("http://222.206.65.12/reader/book_lst.php");
		    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
				httpClient.executeMethod(get);
				byte[] responseBody = get.getResponseBody();
		         String str=new String(responseBody);
		         doc=Jsoup.parse(str);
		    
				Element div=doc.getElementById("mylib_content");
				Document doc1=Jsoup.parse(div.toString());
				Elements trs=doc1.getElementsByTag("td");
				for (int i=8;i<trs.size();) {
					if(i>=8)
					{
		             
						if(i%8==0)
						{
							bookitem info=new bookitem(R.drawable.bookitembg,i/8+": "+trs.get(i+1).text().toString().trim()+"", R.layout.bookitem);
		                items.add(info);
						}
				            
					bookitem position1=new bookitem(R.drawable.bottom1,"条码号:  "+trs.get(i).text().toString().trim(), R.layout.bookitem);
					codes.add(trs.get(i).text().toString().trim());
					bookitem position2=new bookitem(R.drawable.bottom1,"题名:  "+trs.get(i+1).text().toString().trim(), R.layout.bookitem);
						
						
					bookitem position3=new bookitem(R.drawable.bottom1,"责任者:  "+trs.get(i+2).text().toString().trim(), R.layout.bookitem);
						
					bookitem	  position4=new bookitem(R.drawable.bottom1,"借阅日期:  "+trs.get(i+3).text().toString().trim(), R.layout.bookitem);
						
					bookitem	  position5=new bookitem(R.drawable.bottom1,"应还日期:  "+trs.get(i+4).text().toString().trim(), R.layout.bookitem);
					bookitem	  position6=new bookitem(R.drawable.bottom1,"馆藏地:  "+trs.get(i+5).text().toString().trim(), R.layout.bookitem);
					bookitem	  position7=new bookitem(R.drawable.xujie,"", R.layout.bookitem);
						i+=8;
			             
			             items.add(position1);
			             items.add(position2);
			             items.add(position3);
			             items.add(position4);
			             items.add(position5);
			             items.add(position6);
			             items.add(position7);
			    }
				}
		   
				if(trs.toString().length()>10)
				{
					Message msg=handler.obtainMessage();
					msg.obj="1faf";					
					handler.sendMessage(msg);
				}
				else
				{
					Message msg=handler.obtainMessage();
					msg.obj="";					
					handler.sendMessage(msg);
				}
			
			
			
			
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			}
   
			
	      

			
			
			
		

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}
    	
    }
	
	
	}
        
    
