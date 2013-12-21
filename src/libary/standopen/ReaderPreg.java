package libary.standopen;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ReaderPreg extends Activity {
    /** Called when the activity is first created. */
	private ListView list;
	private ArrayList<bookitem> items = null;
	protected static int timeout = 500*60;
	protected final static String user_agent = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
	protected static String LOGIN_FLAG = "PHPSESSID";
	private HttpClient httpClient=null;
	static final String LOGON_SITE = "localhost";
    static final int    LOGON_PORT = 8080;
    private String url=null;
    private Elements geted;
    private ImageButton left=null;
    private Button Preg=null;
    private TextView book=null;
    private Dialog mDialog=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bookinfo);
        book=(TextView)findViewById(R.id.name_book);
        list= (ListView)findViewById(R.id.list);
        
        Preg=(Button)findViewById(R.id.preg);
        left=(ImageButton)findViewById(R.id.back);
        Preg.setVisibility(8);
        book.setText(this.getIntent().getExtras().getString("name"));
        httpClient=SdutLibary.instance.httpClient;
        left.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        url=this.getIntent().getExtras().getString("url");
       
				if(SdutLibary.instance.IsLogin)
				{
					showRoundProcessDialog(ReaderPreg.this,R.layout.loading_process_dialog_color);
					readthread mythread=new readthread(url);
					mythread.start();
				}
				else
				{
					SdutLibary.instance.OtherLogin();
				}
	
        
        
        list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//if(arg2%8==0&&arg2!=0)
				//{
					if(items.get(arg2).getText().length()==0)
					{
					YuYue((arg2+1)/9);
					showRoundProcessDialog(ReaderPreg.this,R.layout.loading_process_dialog_color);
					readthread mythread=new readthread(url);
					mythread.start();
					}
				//}
			}
		});
        }	

	
	
	public void YuYue(int code)
	{
		int number;
	String urlpreg=url.replaceFirst("userpreg.php?","userpreg_result.php?");
	urlpreg+="&count="+(list.getCount()+1)/9;
	
	
	
	
	ArrayList<String> listpreg=new ArrayList<String>();
	for(int i=0;i<geted.size();i++)
	{
		
		if(geted.get(i).attr("name").toString().indexOf("callno")>=0)
		{
    		
    		listpreg.add(geted.get(i).attr("value").toString());
		}
    else if(geted.get(i).attr("name").toString().indexOf("location")>=0)
    		{
    
        		listpreg.add(geted.get(i).attr("value").toString());
    		}
	}
	for(int j=0;j<listpreg.size()/2;)
	{
		
		String input_utf;
		String input_gbk = null;
		try {
			input_utf = URLEncoder.encode(listpreg.get(j).toString(), "utf-8");
			input_gbk=new String(input_utf.getBytes( "utf-8" ), "gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		number=j/2+1;
		String str;
		if(number==code)
		{
		str="&preg_days"+number+"=7&take_loca"+number+"="+listpreg.get(j+1).toString()+"&callno"+number+"="+input_gbk+"&location"+number+"="+listpreg.get(j+1).toString()+"&pregKeepDays"+number+"=7"+"&check="+code;
	  
		}
		else
		{
		str="&preg_days"+number+"=7&take_loca"+number+"="+listpreg.get(j+1).toString()+"&callno"+number+"="+input_gbk+"&location"+number+"="+listpreg.get(j+1).toString()+"&pregKeepDays"+number+"=7";	
		}
		  urlpreg+=str;
	    j+=2;
	}
      
	GoYuyue(urlpreg);
	    	
	    }
	public void GoYuyue(String url)
	{
		
	
		httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1");
        httpClient.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        httpClient.getParams().setSoTimeout(timeout);
        PostMethod postMethod = new PostMethod(url);     
       
     try {
       int statusCode = httpClient.executeMethod(postMethod);
      if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                      Header locationHeader = postMethod.getResponseHeader("location");
                      @SuppressWarnings("unused")
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
	
	
	
	
	
	
	
	 public class readthread extends Thread
	    {
	    	String url;
	       public readthread(String text)
	       {
	    	   this.url=text;

	       }
	       
	       
	       Handler handler=new Handler(){

	   		@SuppressLint("HandlerLeak")
			@Override
	   		public void handleMessage(Message msg) {
	   			// TODO Auto-generated method stub
	   			super.handleMessage(msg);
	   		  
	   			
	   			list.setAdapter(new BookAdapter(ReaderPreg.this,items));
	      	   
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
					GetMethod get = new GetMethod(url);
			    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
					httpClient.executeMethod(get);
					byte[] responseBody = get.getResponseBody();
			         String str=new String(responseBody);
			        // doc=Jsoup.parse(str);
			    
					//Element div=doc.getElementById("td");
					//Document doc1=Jsoup.parse(div.toString());
					//Elements trs=doc.getElementsByTag("td");
			        doc=Jsoup.parse(str);
			     	Elements ele=doc.getElementsByTag("div");
			     	Document doc1=Jsoup.parse(ele.toString());
			     	Elements trs=doc1.getElementsByTag("td");
			     	geted=trs.select("input");
					for (int i=8;i<trs.size()/2-8;) {
						
						
					
						if(i>=8)
						{
			             
							if(i%8==0)
							{
							bookitem info=new bookitem(R.drawable.bookitembg,"书籍"+i/8+"：", R.layout.bookitem);
			                items.add(info);
							}
					            
						bookitem position1=new bookitem(R.drawable.bottom1,"索书号:  "+trs.get(i).text().toString().trim(), R.layout.bookitem);
						bookitem position2=new bookitem(R.drawable.bottom1,"馆藏地:  "+trs.get(i+1).text().toString().trim(), R.layout.bookitem);
							
							
						bookitem position3=new bookitem(R.drawable.bottom1,"可借复本:  "+trs.get(i+2).text().toString().trim(), R.layout.bookitem);
							
						bookitem	  position4=new bookitem(R.drawable.bottom1,"在馆复本:  "+trs.get(i+3).text().toString().trim(), R.layout.bookitem);
							
						bookitem	  position5=new bookitem(R.drawable.bottom1,"已预约数:  "+trs.get(i+4).text().toString().trim(), R.layout.bookitem);
						bookitem	  position6=new bookitem(R.drawable.bottom1,"可否预约:  "+trs.get(i+5).text().toString().trim(), R.layout.bookitem);
						bookitem	  position7=new bookitem(R.drawable.bottom1,"取书地:  "+trs.get(i+6).text().toString().trim(), R.layout.bookitem);
						bookitem	  position9=new bookitem(R.drawable.yuyue,"", R.layout.bookitem);
							i+=8;
				             
				             items.add(position1);
				             items.add(position2);
				             items.add(position3);
				             items.add(position4);
				             items.add(position5);
				             items.add(position6);
				             items.add(position7);
				             items.add(position9);
				    }
					}
			   
					
                    if(trs.toString().length()>10)
                    {
					Message msg=handler.obtainMessage();
					msg.obj="f";
					
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
        
