package libary.standopen;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class SdutLibary extends TabActivity {
	private TabHost tabHost;
	private TabWidget tabWidget;
	Field mBottomLeftStrip;
	Field mBottomRightStrip;
	private TextView type_tab=null;
	public HttpClient httpClient=null;
	 Cookie[] cookies;
	 static final String LOGON_SITE = "localhost";
	    static final int    LOGON_PORT = 8080;
	    public static SdutLibary instance=null;
	  private final int LoginNumber=1;
	  private TextView Readername=null;
	  public Boolean IsLogin;
	  private Dialog mDialog=null;
	  final static int ONE = Menu.FIRST;
      final static int TWO = Menu.FIRST+1;
       private ImageButton menu=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        type_tab=(TextView)findViewById(R.id.type);
        Readername=(TextView)findViewById(R.id.readername);
        httpClient=new HttpClient();
        instance=this;
        makeTab();
        IsLogin=false;
        menu=(ImageButton)findViewById(R.id.share);
        menu.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 openOptionsMenu();
			}
		});
        Readername.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//login("1011122008","3608061");
				Intent intent=new Intent();
				intent.setClass(SdutLibary.this, LoginDialog.class);
				startActivityForResult(intent,LoginNumber);
				Random ran =new Random(System.currentTimeMillis());
				int type=ran.nextInt(11);
				switch (type) {
				case 0:
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case 1:
					overridePendingTransition(R.anim.my_scale_action,
							R.anim.my_alpha_action);
					break;
				case 2:
					overridePendingTransition(R.anim.scale_rotate,
							R.anim.my_alpha_action);
					break;
				case 3:
					overridePendingTransition(R.anim.scale_translate_rotate,
							R.anim.my_alpha_action);
					break;
				case 4:
					overridePendingTransition(R.anim.scale_translate,
							R.anim.my_alpha_action);
					break;
				case 5:
					overridePendingTransition(R.anim.hyperspace_in,
							R.anim.hyperspace_out);
					break;
				case 6:
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
					break;
				case 7:
					overridePendingTransition(R.anim.push_up_in,
							R.anim.push_up_out);
					break;
				case 8:
					overridePendingTransition(R.anim.slide_left,
							R.anim.slide_right);
					break;
				case 9:
					overridePendingTransition(R.anim.wave_scale,
							R.anim.my_alpha_action);
					break;
				case 10:
					overridePendingTransition(R.anim.zoom_enter,
							R.anim.zoom_exit);
					break;
				case 11:
					overridePendingTransition(R.anim.slide_up_in,
							R.anim.slide_down_out);
				}
			}
		});
    }
    public void makeTab(){
    	if(this.tabHost == null){
	    	tabHost = getTabHost();
	        tabWidget = getTabWidget();
	        tabHost.setup();
	        tabHost.bringToFront();
	        
	        TabSpec tab_search = tabHost.newTabSpec("tab_search");
	        TabSpec tab_hot = tabHost.newTabSpec("tab_hot");
	        TabSpec tab_reader = tabHost.newTabSpec("tab_reader");
	        
	        tab_search.setIndicator("书目检索",getResources().getDrawable(R.drawable.first)).setContent(new Intent(this,Search.class));
	        tab_hot.setIndicator("热门图书",getResources().getDrawable(R.drawable.third)).setContent(new Intent(this,HotBooks.class));
	        tab_reader.setIndicator("个人中心",getResources().getDrawable(R.drawable.second)).setContent(new Intent(this,Reader.class));
	        
	        tabHost.addTab(tab_search);
	        tabHost.addTab(tab_hot);
	        tabHost.addTab(tab_reader);
	        
	        if (Integer.valueOf(Build.VERSION.SDK) <= 7) {
				try {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField ("mBottomLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField ("mBottomRightStrip");
					if(!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if(!mBottomRightStrip.isAccessible()){
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(tabWidget, getResources().getDrawable (R.drawable.linee));
					mBottomRightStrip.set(tabWidget, getResources().getDrawable (R.drawable.linee));

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField("mLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField("mRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(tabWidget, getResources().getDrawable(R.drawable.linee));
					mBottomRightStrip.set(tabWidget, getResources().getDrawable(R.drawable.linee));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	        
			for (int i =0; i <tabWidget.getChildCount(); i++) {
				
				View vvv = tabWidget.getChildAt(i);
				if(tabHost.getCurrentTab()==i){
					vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.focus_bg));
				}
				else {
					vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.unfocus_bg));
				}
			}
			
			tabHost.setOnTabChangedListener(new OnTabChangeListener(){
	
				public void onTabChanged(String tabId) {
					if(tabId=="tab_search")
					{
						type_tab.setText("书目检索");
					}
					else if(tabId=="tab_hot")
					{
						type_tab.setText("热门图书");
					}
					else if(tabId=="tab_reader")
					{
						type_tab.setText("个人中心");
					}
					for (int i =0; i < tabWidget.getChildCount(); i++) {
						View vvv = tabWidget.getChildAt(i);
						if(tabHost.getCurrentTab()==i){
							vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.focus_bg));
						}
						else {
							vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.unfocus_bg));
						}
					}
				}
			});
    	}
    }
    public void login(String number,String password)
    {
    	 httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1");
         httpClient.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
         httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
         httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
         PostMethod postMethod = new PostMethod("http://222.206.65.12/reader/redr_verify.php");     
         postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
         NameValuePair[] data = { new NameValuePair("number",number),new NameValuePair("passwd",password),new NameValuePair("returnUrl",""),new NameValuePair("select","cert_no")};
         postMethod.setRequestBody(data);
      try {
        int statusCode = httpClient.executeMethod(postMethod);
       if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                       Header locationHeader = postMethod.getResponseHeader("location");
                       @SuppressWarnings("unused")
					String location = null;
                       if (locationHeader != null) {
                              location = locationHeader.getValue();
                              ReadInfo();
                              IsLogin=true;
                             cookies =httpClient.getState().getCookies();
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
    
    
    public void ReadInfo()
    {
    
    	GetMethod get = new GetMethod("http://222.206.65.12/reader/redr_info.php");
    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        try {
        	httpClient.getState().addCookies(cookies);
			httpClient.executeMethod(get);
			byte[] responseBody = get.getResponseBody();
            String str=new String(responseBody);
			GetMsg(str);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			get.releaseConnection();
		}
     
        
    }
    
    
    public void GetMsg(String content)
    {
    	Document doc=Jsoup.parse(content);
    	Elements ele=doc.getElementsByTag("div");
    	Document doc1=Jsoup.parse(ele.toString());
    	Elements info=doc1.getElementsByTag("h2");
    	Readername.setBackgroundResource(R.drawable.login_bg);
    	
    	String str=info.get(0).text().toString().substring(0,info.get(0).text().toString().length()-3);
    
    	Readername.setText(str);
    	Readername.getBackground().setAlpha(0);
    	
    	
    	
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==LoginNumber&&resultCode==RESULT_OK)
		{
		     
			Bundle b = data.getExtras();
			String number=b.getString("number");
			String pass=b.getString("password");
			//login(number,pass);
			showRoundProcessDialog(SdutLibary.this,R.layout.loading_process_dialog_anim);
			readthread mythread=new readthread(number, pass);
			mythread.start();
		}
	}
    
    
    
    
    public void OtherLogin()
    {
    	Intent intent=new Intent();
		intent.setClass(SdutLibary.this, LoginDialog.class);
		startActivityForResult(intent,LoginNumber);
		Random ran =new Random(System.currentTimeMillis());
		int type=ran.nextInt(11);
		switch (type) {
		case 0:
			overridePendingTransition(R.anim.fade, R.anim.hold);
			break;
		case 1:
			overridePendingTransition(R.anim.my_scale_action,
					R.anim.my_alpha_action);
			break;
		case 2:
			overridePendingTransition(R.anim.scale_rotate,
					R.anim.my_alpha_action);
			break;
		case 3:
			overridePendingTransition(R.anim.scale_translate_rotate,
					R.anim.my_alpha_action);
			break;
		case 4:
			overridePendingTransition(R.anim.scale_translate,
					R.anim.my_alpha_action);
			break;
		case 5:
			overridePendingTransition(R.anim.hyperspace_in,
					R.anim.hyperspace_out);
			break;
		case 6:
			overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		case 7:
			overridePendingTransition(R.anim.push_up_in,
					R.anim.push_up_out);
			break;
		case 8:
			overridePendingTransition(R.anim.slide_left,
					R.anim.slide_right);
			break;
		case 9:
			overridePendingTransition(R.anim.wave_scale,
					R.anim.my_alpha_action);
			break;
		case 10:
			overridePendingTransition(R.anim.zoom_enter,
					R.anim.zoom_exit);
			break;
		case 11:
			overridePendingTransition(R.anim.slide_up_in,
					R.anim.slide_down_out);
		}
    }
    
    
    
    
    public class readthread extends Thread
    {
    	String number1;
    	String pass1;
       public readthread(String number,String pass)
       {
    	   this.number1=number;
    	   this.pass1=pass;
    	  
       }
       
       
       Handler handler=new Handler(){

   		@SuppressLint("HandlerLeak")
		@Override
   		public void handleMessage(Message msg) {
   			// TODO Auto-generated method stub
   			super.handleMessage(msg);
   			int type=Integer.parseInt(msg.obj.toString());
   			if(type==1)
   			{
   			IsLogin=true;
            ReadInfo();
   			}
   			else if(type==0)
   			{
   				Toast.makeText(SdutLibary.this, "登录超时，请检查网络后重试！", Toast.LENGTH_SHORT).show();
   			}
   			else if(type==2)
   			{
   				Toast.makeText(SdutLibary.this, "登录失败，请检查网络后重试！", Toast.LENGTH_SHORT).show();
   			}
   			mDialog.dismiss();
   			
      	   
      	   

  	 
   		}
   		
   	};
       
       
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			if(checkNetWorkStatus())
			{
			httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1");
	         httpClient.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
	         httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
	         httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
	         httpClient.getParams().setSoTimeout(500*60);
	         PostMethod postMethod = new PostMethod("http://222.206.65.12/reader/redr_verify.php");     
	         postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
	         NameValuePair[] data = { new NameValuePair("number",number1),new NameValuePair("passwd",pass1),new NameValuePair("returnUrl",""),new NameValuePair("select","cert_no")};
	         postMethod.setRequestBody(data);
	      try {
	        int statusCode = httpClient.executeMethod(postMethod);
	       if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
	                       Header locationHeader = postMethod.getResponseHeader("location");
	                       @SuppressWarnings("unused")
						String location = null;
	                       if (locationHeader != null) {
	                              location = locationHeader.getValue();
	                              cookies =httpClient.getState().getCookies();
	                              Message msg=handler.obtainMessage();
	  							  msg.obj="1";					
	  							  handler.sendMessage(msg);
	                              
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
	      Message msg=handler.obtainMessage();
		  msg.obj="0";					
		  handler.sendMessage(msg);
			}
			
	      Message msg=handler.obtainMessage();
			  msg.obj="2";					
			  handler.sendMessage(msg);
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
    
    private boolean checkNetWorkStatus() {   
        boolean netSataus = false;   
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);   
        cwjManager.getActiveNetworkInfo();   
        if (cwjManager.getActiveNetworkInfo() != null) {   
            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();   
        }   
        return netSataus;   
    } 
    
    
    
  //创建Menu菜单
    public boolean onCreateOptionsMenu(Menu menu) {
            menu.add(0, ONE, 0, "关于").setIcon(R.drawable.menu_about);  //设置文字与图标
            menu.add(0, TWO, 0, "分享").setIcon(R.drawable.share);
            return super.onCreateOptionsMenu(menu);
    }

    //点击Menu菜单选项响应事件
    public boolean onOptionsItemSelected(MenuItem item) {
            switch(item.getItemId()){
            case 1:
                  Intent intent1=new Intent();
                  intent1.setClass(SdutLibary.this, about.class);
                  startActivity(intent1);
                    break;
            case 2:
            	Intent intent=new Intent(Intent.ACTION_SEND);  
                intent.setType("text/*");  
                intent.putExtra(Intent.EXTRA_SUBJECT, "share");  
                intent.putExtra(Intent.EXTRA_TEXT,"山理图书：现在山东理工大学图书馆出安卓客户端了，用手机就能查询，预约，续借，等等功能，欢迎下载使用！");  
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
               startActivity(Intent.createChooser(intent, "分享给好友："));
                    break;
            }
            return super.onOptionsItemSelected(item);
    }

    public void onOptionsMenuClosed(Menu menu) {
           
    }

    //菜单被显示之前的事件 
    public boolean onPrepareOptionsMenu(Menu menu) {
            
              return true;  
    }  

}