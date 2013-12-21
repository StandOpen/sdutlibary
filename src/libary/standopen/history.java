package libary.standopen;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class history extends Activity {
    /** Called when the activity is first created. */
	

	//private EditText number,pass=null;
	private Button go=null;//控件变量
	private HttpClient httpClient=null;
	 Cookie[] cookies;
	 static final String LOGON_SITE = "localhost";
	    static final int    LOGON_PORT = 8080;
	    private ListView list;
		private ArrayList<bookitem> items = null;
		private Dialog mDialog;//等待对话框变量
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        list= (ListView)findViewById(R.id.list);
    //    number=(EditText)findViewById(R.id.number);
    //    pass=(EditText)findViewById(R.id.input);
        go=(Button)findViewById(R.id.gotosearch);
        items= new ArrayList<bookitem>();
       // httpClient=new HttpClient();
        httpClient=SdutLibary.instance.httpClient;//跟主页面变量同步
        cookies=SdutLibary.instance.cookies;
        go.setOnClickListener(new View.OnClickListener() {//监听点击事件
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 //login(number.getText().toString(),pass.getText().toString());
				if(SdutLibary.instance.IsLogin)//判断是否登录
				{
					items= new ArrayList<bookitem>();
					showRoundProcessDialog(history.this.getParent(),R.layout.loading_process_dialog_color);//显示等待对话框
					  readthread mythread=new readthread();//获取搜索历史的线程
					  mythread.start();
				
				}
				else
				{
					SdutLibary.instance.OtherLogin();//如果没登陆显示登录对话框
				}
			}
		});
        
        
        //用来显示与隐藏获取信息按钮
  list.setOnScrollListener(new OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(view.getLastVisiblePosition()<8)
				{
				
					go.setVisibility(0);

				}
				else
				{
					go.setVisibility(8);
			
				}
				
			
			
		}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}});
       
    }
    
//显示等待对话框
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
    
    
    public class readthread extends Thread//读取检索历史的线程
    {
    	 
       Handler handler=new Handler(){

   		@SuppressLint("HandlerLeak")
		@Override
   		public void handleMessage(Message msg) {
   			// TODO Auto-generated method stub
   			super.handleMessage(msg);
   			Document doc=Jsoup.parse(msg.obj.toString());//jsoup是一个引用的外部包，用来分析网页源代码
   	    	Elements geted=doc.getElementsByTag("tr");
   			Document doc2=Jsoup.parse(geted.toString());
   			Elements geted1=doc2.getElementsByTag("a");
   	    
   	    	if(geted1.toString().length()>10)
   	    	{
   			
   	    	for(Element linkStr:geted1){
   	    		
   				bookitem position1=new bookitem(R.drawable.bottom1,linkStr.text().toString(), R.layout.bookitem);     
	             items.add(position1);
   	    	
   	    	}
   	    	list.setAdapter(new BookAdapter(history.this,items));//用来显示结果
   	    	}
   	    	else
   	    	{
   	    		//如果没有检索历史进行提示
   	    		Toast toast = Toast.makeText(getApplicationContext(),
		        	     "你还没有历史记录，难道你没进过图书馆！？", Toast.LENGTH_LONG);
		        	   toast.setGravity(Gravity.CENTER, 0, 0);
		        	   LinearLayout toastView = (LinearLayout) toast.getView();
		        	   ImageView imageCodeProject = new ImageView(getApplicationContext());
		        	   imageCodeProject.setImageResource(R.drawable.menu_about);
		        	   toastView.addView(imageCodeProject, 0);
		        	   toast.show();		
   	    	}
   			mDialog.dismiss();//取消等待对话框
      	   
      	   
      	   

  	 
   		}
   		
   	};
       
       
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			//get方法获得
			GetMethod get = new GetMethod("http://222.206.65.12/reader/search_hist.php");//初始化获取网页源代码变量
	    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");//设置编码
	    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);//设置cookie
	        try {
	        	httpClient.getState().addCookies(cookies);
				httpClient.executeMethod(get);//执行获取源代码方法
				byte[] responseBody = get.getResponseBody();
	            String str=new String(responseBody);//获取网页源代码
				Message msg=handler.obtainMessage();
				msg.obj=str;					
				handler.sendMessage(msg);
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
   
			
	      

			
			
			
		

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}
    	
    }
}
