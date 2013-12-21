package libary.standopen;

import java.util.ArrayList;



import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class readerinfo extends Activity {
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
        GetInfo.setText("点击获取个人信息");
        GetInfo.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(SdutLibary.instance.IsLogin)
				{
					showRoundProcessDialog(readerinfo.this.getParent(),R.layout.loading_process_dialog_color);
					  readthread mythread=new readthread();
					  mythread.start();
				}
				else
				{
					SdutLibary.instance.OtherLogin();
				}
			}
		});
        
  list.setOnScrollListener(new OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(view.getLastVisiblePosition()<8)
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
	   	    	if(msg.toString().length()>0)
	   	    	{
	   			
	   	    		list.setAdapter(new BookAdapter(readerinfo.this,items));
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
				GetMethod get = new GetMethod("http://222.206.65.12/reader/redr_info.php");
		    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
				httpClient.executeMethod(get);
				byte[] responseBody = get.getResponseBody();
		         String str=new String(responseBody);
		         doc=Jsoup.parse(str);
		    
				Element div=doc.getElementById("mylib_content");
				Document doc1=Jsoup.parse(div.toString());
				Elements trs=doc1.getElementsByTag("TD");
				
				int i;
				bookitem position=new bookitem(R.drawable.bookitembg,"个人信息：", R.layout.bookitem);     
		        items.add(position);
				for (i=0;i<trs.size();i++) {  
					if(i<27)
					{
					bookitem position1=new bookitem(R.drawable.bottom1,trs.get(i).text().toString().trim(), R.layout.bookitem);     
			             items.add(position1);
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
	
	}
