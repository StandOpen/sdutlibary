package libary.standopen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;


public class preginfo extends Activity {
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
    private ArrayList<String> Data=null;
    private ArrayList<String> numbers=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history);
        list= (ListView)findViewById(R.id.list);
       
        GetInfo=(Button)findViewById(R.id.gotosearch);
        httpClient=SdutLibary.instance.httpClient;
        codes=new ArrayList<String>();
        GetInfo.setText("点击获取预约信息");
       
        numbers=new ArrayList<String>();
        
        
        
        GetInfo.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(SdutLibary.instance.IsLogin)
				{
					showRoundProcessDialog(preginfo.this.getParent(),R.layout.loading_process_dialog_color);
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
				if(items.get(arg2).getText().length()<1)
				{
					    if(arg2/9<=Data.size())
					    {
						YuYue(Data.get(arg2/9-1).toString());
						showRoundProcessDialog(preginfo.this.getParent(),R.layout.loading_process_dialog_color);
						  readthread mythread=new readthread();
						  mythread.start();
					   }
					    else
					    {
						Toast.makeText(preginfo.this,"已到书不能取消",Toast.LENGTH_SHORT).show();
					    }
					
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
	@SuppressLint("ParserError")
	public void GetMsg()
	{
		 items= new ArrayList<bookitem>();
		Document doc;
	try {
		GetMethod get = new GetMethod("http://222.206.65.12/reader/preg.php");
    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient.executeMethod(get);
		byte[] responseBody = get.getResponseBody();
         String str=new String(responseBody);
         doc=Jsoup.parse(str);
    
		Element div=doc.getElementById("mylib_content");
		Document doc1=Jsoup.parse(div.toString());
		Elements trs=doc1.getElementsByTag("td");
		
		
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
		int i;
		for (i=9;i<trs.size();) {
			
			
			
			if(i>=9)
			{
             
				if(i%9==0)
				{
					bookitem info=new bookitem(R.drawable.bookitembg,"预约书籍"+i/8+": ", R.layout.bookitem);
                items.add(info);
				}
		            
			bookitem position1=new bookitem(R.drawable.item_position,"索书号:  "+trs.get(i).text().toString().trim(), R.layout.bookitem);
			codes.add(trs.get(i).text().toString().trim());
			bookitem position2=new bookitem(R.drawable.item_position,"题名:  "+trs.get(i+1).text().toString().trim(), R.layout.bookitem);
				
				
			bookitem position3=new bookitem(R.drawable.item_position,"责任者:  "+trs.get(i+2).text().toString().trim(), R.layout.bookitem);
				
		
			bookitem	  position4=new bookitem(R.drawable.item_position,"馆藏地:  "+trs.get(i+3).text().toString().trim(), R.layout.bookitem);
			bookitem	  position5=new bookitem(R.drawable.item_position,"预约到书日:  "+trs.get(i+4).text().toString().trim(), R.layout.bookitem);
			bookitem	  position6=new bookitem(R.drawable.item_position,"截止日期:  "+trs.get(i+5).text().toString().trim(), R.layout.bookitem);
			bookitem	  position7=new bookitem(R.drawable.item_position,"取书地:  "+trs.get(i+6).text().toString().trim(), R.layout.bookitem);
			bookitem	  position8=new bookitem(R.drawable.item_position,"状态:  "+trs.get(i+7).text().toString().trim(), R.layout.bookitem);
			
				i+=9;
	             
	             items.add(position1);
	             items.add(position2);
	             items.add(position3);
	             items.add(position4);
	             items.add(position5);
	             items.add(position6);
	             items.add(position7);
	             items.add(position8);
//	             if(trs.get(i+7).text().toString().indexOf("已到书")>=0)
//	 			{
//	 			bookitem	  position9=new bookitem(R.drawable.cancelyuyue,"", R.layout.bookitem);
//	             items.add(position9);
//	 			}
	    }

   
		}
		if(i==0)
			Toast.makeText(preginfo.this,"获取信息失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
		list.setAdapter(new BookAdapter(preginfo.this,items));
	
	
		
	
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
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
	   	    	list.setAdapter(new BookAdapter(preginfo.this,items));
	   	    		list.setVisibility(0);
	   	    	}
	   	    	else
	   	    	{
	   	    		Toast toast = Toast.makeText(getApplicationContext(),
			        	     "你还没有预约书籍！", Toast.LENGTH_LONG);
			        	   toast.setGravity(Gravity.CENTER, 0, 0);
			        	   LinearLayout toastView = (LinearLayout) toast.getView();
			        	   ImageView imageCodeProject = new ImageView(getApplicationContext());
			        	   imageCodeProject.setImageResource(R.drawable.menu_about);
			        	   toastView.addView(imageCodeProject, 0);
			        	   toast.show();	
			        	   list.setVisibility(8);
	   	    	}
	   			mDialog.dismiss();
	      	   
	      	   
	      	   

	  	 
	   		}
	   		
	   	};
	       
	       
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				items= new ArrayList<bookitem>();
				 Data=new ArrayList<String>();
				Document doc;
			try {
				
				GetMethod get = new GetMethod("http://222.206.65.12/reader/preg.php");
		    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
				httpClient.executeMethod(get);
				byte[] responseBody = get.getResponseBody();
		         String str=new String(responseBody);
		         doc=Jsoup.parse(str);
		    
				Element div=doc.getElementById("mylib_content");
				Document doc1=Jsoup.parse(div.toString());
				Elements trs=doc1.getElementsByTag("td");
				
				Document doc2=Jsoup.parse(trs.toString());
				Element input1 = null;
				Element input2 = null;
				Element input3 = null;
				if(trs.size()>9&&trs.size()<20)
				{
		    	input1=doc1.getElementById("1");
				}
				else if(trs.size()>18&&trs.size()<28)
				{
				input1=doc1.getElementById("1");
		    	input2=doc1.getElementById("2");
				}
				else if(trs.size()>27)
				{
				input1=doc1.getElementById("1");
			    input2=doc1.getElementById("2");
		    	input3=doc1.getElementById("3");
				}
//		    	
//		    	
//
//				if(input1.toString().length()>0)
//				{
//					numbers.add("1");
//				}
//				else
//				{
//					numbers.add("0");
//				}
//				if(input2.toString().length()>0)
//				{
//					numbers.add("1");
//				}
//				else
//				{
//					numbers.add("0");
//				}
//				if(input3.toString().length()>0)
//				{
//					numbers.add("1");
//				}
//				else
//				{
//					numbers.add("0");
//				}
		    	
		    	
		    	for(int k=1;k<trs.size()/9;k++)
		    	{
		    		String str_data="";
		    		if(k==1)
		    		{
		    	    if(input1!=null)
		    	     str_data=input1.toString();
		    		}
		    		else if(k==2)
		    		{
		    			if(input2!=null)
		    			str_data=input2.toString();
		    		}
		    		else if(k==3)
		    		{
		    			if(input3!=null)
		    			str_data=input3.toString();
		    		}
		    	if(str_data.length()>0)
		    	{
		    	String patternString = "'.+'"; 
				Pattern pattern = Pattern.compile(patternString,Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(str_data);
				str_data="";
				while (matcher.find()) {
					String link=matcher.group();
					str_data+=link;
				}
				
				ArrayList<String> list1=new ArrayList<String>();
				String []result=str_data.split("','");
			
				for(int i=0;i<result.length;i++)
				{
					if(i==0)
					{	
						list1.add(result[i].substring(1, result[i].length()));
					}
					else if(i==result.length-1)
					{
						list1.add(result[i].substring(0, result[i].length()-1));
					}
					else
					{
					list1.add(result[i]);
					}
				}
		    
		    	
		    	for(int j=0;j<list1.size();)
		    	{
		    		
		    		String url="http://222.206.65.12/reader/ajax_preg.php?call_no=";
		    		url+=list1.get(j+1).toString()+"&marc_no="+list1.get(j).toString()+"&loca="+list1.get(j+3).toString()+"&time=1363769875438";
		    		
		    		Data.add(url);
		    	
		    		j+=4;
		    	}
		    	}
		    	}
				int i;
				for (i=9;i<trs.size();) {
					
					
					
					if(i>=9)
					{
		             
						if(i%9==0)
						{
							bookitem info=new bookitem(R.drawable.bookitembg,"预约书籍"+i/8+": ", R.layout.bookitem);
		                items.add(info);
						}
				            
					bookitem position1=new bookitem(R.drawable.item_position,"索书号:  "+trs.get(i).text().toString().trim(), R.layout.bookitem);
					codes.add(trs.get(i).text().toString().trim());
					bookitem position2=new bookitem(R.drawable.item_position,"题名:  "+trs.get(i+1).text().toString().trim(), R.layout.bookitem);
						
						
					bookitem position3=new bookitem(R.drawable.item_position,"责任者:  "+trs.get(i+2).text().toString().trim(), R.layout.bookitem);
						
				
					bookitem	  position4=new bookitem(R.drawable.item_position,"馆藏地:  "+trs.get(i+3).text().toString().trim(), R.layout.bookitem);
					bookitem	  position5=new bookitem(R.drawable.item_position,"预约到书日:  "+trs.get(i+4).text().toString().trim(), R.layout.bookitem);
					bookitem	  position6=new bookitem(R.drawable.item_position,"截止日期:  "+trs.get(i+5).text().toString().trim(), R.layout.bookitem);
					bookitem	  position7=new bookitem(R.drawable.item_position,"取书地:  "+trs.get(i+6).text().toString().trim(), R.layout.bookitem);
					bookitem	  position8=new bookitem(R.drawable.item_position,"状态:  "+trs.get(i+7).text().toString().trim(), R.layout.bookitem);
					
					bookitem	  position9=new bookitem(R.drawable.cancelyuyue,"", R.layout.bookitem);
						i+=9;
			             
			             items.add(position1);
			             items.add(position2);
			             items.add(position3);
			             items.add(position4);
			             items.add(position5);
			             items.add(position6);
			             items.add(position7);
			             items.add(position8);
				         items.add(position9);
				 			
			    }

		   
				}
			   
					if(items.toString().length()>30)
					{
						Message msg=handler.obtainMessage();
						msg.obj="15456465456";					
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
	 public void YuYue(String code)
		{
			
		    	 httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1");
		         httpClient.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
		         httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		         httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		         PostMethod postMethod = new PostMethod(code);     
		        
		      try {
		        int statusCode = httpClient.executeMethod(postMethod);
		       if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
		                       Header locationHeader = postMethod.getResponseHeader("location");
		                       String location = null;
		                       if (locationHeader != null) {
		                              location = locationHeader.getValue();
		                           
		                         
		                       } else {
		                           //   text.setText("Location field value is null.");
		                    	
		                       }
		                       return;
		                }                        
		                if (statusCode != HttpStatus.SC_OK) {
		                      // text.setText("Method failed: "+ postMethod.getStatusLine());
		                
		                }                    
		                
		         } catch (HttpException e) {
		                // text.setText("Please check your provided http    address!");
		        	 
		                e.printStackTrace();
		         } catch (IOException e) {
		                e.printStackTrace();
		         } finally {
		        	    postMethod.releaseConnection();
		         	
		 }
		    }
	}
