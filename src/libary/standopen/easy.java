package libary.standopen;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class easy extends Activity {
    /** Called when the activity is first created. */
	private EditText input=null;//声明变量
	private Button search=null;
	private ListView list;
	private ArrayList<listitem> items = null;
	final int []image={R.drawable.ic_bookmark_draw};
	private int num_book;
	private int page=1;
	private HttpClient httpClient=null;//是用来获取网页源代码的变量
	 int position; 
	 private Dialog mDialog;//是用来显示等待对话框的变量
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easy);
        input=(EditText)findViewById(R.id.input);
        search=(Button)findViewById(R.id.gotosearch);
        list= (ListView)findViewById(R.id.list);
        num_book=0;
        httpClient=SdutLibary.instance.httpClient;
        search.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);//把输入键盘隐藏
	             imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
				 page=1;
				 items= new ArrayList<listitem>();
				//num_book=GetMsg(input.getText().toString());
				showRoundProcessDialog(easy.this.getParent(),R.layout.loading_process_dialog_color);//显示等待对话框
				  readthread mythread=new readthread(input.getText().toString());//读取信息线程是用来读取搜索信息
				  mythread.start();
			}
		});
        
        
        list.setOnItemClickListener(new OnItemClickListener() {//监听listview点击item事件，点击后跳转向书籍详情界面

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//以下是点击后跳转向详情页面
				Intent intent=new Intent();
				intent.putExtra("link",items.get(arg2).getUrl());//传书籍超链接
				intent.putExtra("name",items.get(arg2).getText());//传书籍名字
				intent.setClass(easy.this, BookInfo.class);
				startActivity(intent);
			
			}
		});
     
        
        //是用来显示与隐藏输入框
           list.setOnScrollListener(new OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
		
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if(view.getLastVisiblePosition()<8)
					{
					
						input.setVisibility(0);
						search.setVisibility(0);
					}
					else
					{
						input.setVisibility(8);
						search.setVisibility(8);
					}
					
					//当滑到最底端的时候加载更多书籍信息
				      if (view.getLastVisiblePosition() == view.getCount()-1) {
				    	  page++;
				      if(page<num_book/50+1)
				      {
				    	  showRoundProcessDialog(easy.this.getParent(),R.layout.loading_process_dialog_color);
						  updatethread mythread=new updatethread(input.getText().toString(),page);
						  mythread.start();
						  position = list.getFirstVisiblePosition();
				      }
				     }
				
				
				
				
			}
			
			
		}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
			});

        
    }

    public class updatethread extends Thread//用来读取搜索书籍信息的线程
    {
    	String content;//输入的搜索条件
    	int page;
       public updatethread(String text,int page)
       {
    	   this.content=text;
    	   this.page=page;
       }
       
       
       Handler handler=new Handler(){

   		@SuppressLint("HandlerLeak")
		@Override
   		public void handleMessage(Message msg) {
   			
   			super.handleMessage(msg);
   			if(msg.obj.toString().length()>1)
   			{
   			list.setAdapter(new MyAdapter(easy.this,items));//显示搜索结果
   			list.setSelection(position+3);
   			}
   			else
   			{
   				//没有书籍或者没网的情况下弹出提示
   				Toast toast = Toast.makeText(getApplicationContext(),
		        	     "没有与该条件匹配的书籍或者网络错误", Toast.LENGTH_LONG);
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
		
			super.run();
			
			//用来把输入的搜索内容转成gbk编码
			String input_utf="";
	    	String input_gbk="";
			try {
				input_utf = URLEncoder.encode(content, "utf-8");
				input_gbk=new String(input_utf.getBytes("utf-8"), "gbk");
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			String url="http://222.206.65.12/opac/openlink.php?dept=ALL&title="+input_gbk+"&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=50&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&count="+num_book+"&page="+page;
			@SuppressWarnings("unused")
			Document doc;
			try {
				GetMethod get = new GetMethod(url);//初始化模拟浏览器变量
		    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");//设置编码
		    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);//设置cookie
		    	httpClient.getParams().setSoTimeout(500*60);//设置超时时间
				httpClient.executeMethod(get);//用来执行方法获取网页源代码
				byte[] responseBody = get.getResponseBody();
				
		        String str=new String(responseBody);//获取网页源代码
				
				
				Document doc1=Jsoup.parse(str);//jsoup是一个外部包，是用来分析网页源代码
				@SuppressWarnings("unused")
			//	Elements divs = doc1.getElementsByClass("list_books");//分析获取每条书籍信息的信息， 每个书籍的class变量一样的
				Elements geted=doc1.getElementsByTag("h3");//获取每条书籍标题
				Document doc2=Jsoup.parse(geted.toString());
				Elements geted1=doc2.getElementsByTag("a");
				//通过正则表达式获取每条书籍的超链接
				ArrayList<String> linklist=new ArrayList<String>();	
				String patternString = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))"; //用来匹配超链接的正则表达式
				Pattern pattern = Pattern.compile(patternString,Pattern.CASE_INSENSITIVE);

				Matcher matcher = pattern.matcher(geted.toString());

				while (matcher.find()) {

					String link=matcher.group();
					link=link.replaceAll("href\\s*=\\s*(['|\"]*)", "");
					link=link.replaceAll("['|\"]", "");
					linklist.add("http://222.206.65.12/opac/"+link.trim());
					
				}
				
				
				for(int i=0;i<geted1.size();i++){
					listitem it = new listitem(image[0],geted1.get(i).text().toString(),linklist.get(i).toString(),R.layout.list_item);//listitem使用保存每条书籍的信息的类
					items.add(it);
					}
				
				//显示搜索结果
				if(items.toString().length()>10)
				{			
					Message msg=handler.obtainMessage();
					msg.obj="fgsgsfsf";
					
					handler.sendMessage(msg);
				}
				else
				{
					
					Message msg=handler.obtainMessage();
					msg.obj="";
					
					handler.sendMessage(msg);
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}
    	
    }
    
    
    public class readthread extends Thread//这个线程用来滑到底部显示更多
    {
    	String content;
        int number=0;
       public readthread(String text)
       {
    	   this.content=text;
    
       }
       
       
       Handler handler=new Handler(){

   		@SuppressLint("HandlerLeak")
		@Override
   		public void handleMessage(Message msg) {
   			// TODO Auto-generated method stub
   			super.handleMessage(msg);
   		    number=Integer.parseInt(msg.obj.toString());
   		    num_book=number;
   	
   		if(number==-1)
		    {
		Toast toast = Toast.makeText(getApplicationContext(),
 	     "网络错误，请检查网络后重试。", Toast.LENGTH_LONG);
 	   toast.setGravity(Gravity.CENTER, 0, 0);
 	   LinearLayout toastView = (LinearLayout) toast.getView();
 	   ImageView imageCodeProject = new ImageView(getApplicationContext());
 	   imageCodeProject.setImageResource(R.drawable.menu_about);
 	   toastView.addView(imageCodeProject, 0);
 	   toast.show();	
		    }
   		else
   		{
   		    if(number==0)
   		    {
   				Toast toast = Toast.makeText(getApplicationContext(),
     	     "没有与该条件匹配的书籍", Toast.LENGTH_LONG);
     	   toast.setGravity(Gravity.CENTER, 0, 0);
     	   LinearLayout toastView = (LinearLayout) toast.getView();
     	   ImageView imageCodeProject = new ImageView(getApplicationContext());
     	   imageCodeProject.setImageResource(R.drawable.menu_about);
     	   toastView.addView(imageCodeProject, 0);
     	   toast.show();	
   		    }
   		    else
   		    {
   		    	list.setAdapter(new MyAdapter(easy.this,items));
   	   			
   	   			mDialog.dismiss();
   				Toast toast = Toast.makeText(easy.this.getParent(),
   	      	     "与条件符合的有"+number+"条结果", Toast.LENGTH_LONG);
   	      	   toast.setGravity(Gravity.CENTER, 0, 0);
   	      	   LinearLayout toastView = (LinearLayout) toast.getView();
   	      	   ImageView imageCodeProject = new ImageView(getApplicationContext());
   	      	   imageCodeProject.setImageResource(R.drawable.menu_about);
   	      	   toastView.addView(imageCodeProject, 0);
   	      	   toast.show();	
   		    }
   			
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
			String input_utf="";
	    	String input_gbk="";
			try {
				input_utf = URLEncoder.encode(content, "utf-8");
				input_gbk=new String(input_utf.getBytes("utf-8"), "gbk");
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			GetMethod get = new GetMethod("http://222.206.65.12/opac/openlink.php?historyCount=1&strText="+input_gbk+"&doctype=ALL&strSearchType=title&match_flag=forward&displaypg=50&sort=CATA_DATE&orderby=desc&showmode=list&dept=ALL");
	    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
	    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
	    	httpClient.getParams().setSoTimeout(500*60);
			try {
				httpClient.executeMethod(get);
				byte[] responseBody = get.getResponseBody();
				  String str=new String(responseBody);
				  Document doc1=Jsoup.parse(str);
					@SuppressWarnings("unused")
					//Elements divs = doc1.getElementsByClass("list_books");
					Elements geted=doc1.getElementsByTag("h3");
					Document doc2=Jsoup.parse(geted.toString());
					Elements geted1=doc2.getElementsByTag("a");
					ArrayList<String> linklist=new ArrayList<String>();	
					String patternString = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))"; //href
					Pattern pattern = Pattern.compile(patternString,Pattern.CASE_INSENSITIVE);

					Matcher matcher = pattern.matcher(geted.toString());

					while (matcher.find()) {

						String link=matcher.group();
						link=link.replaceAll("href\\s*=\\s*(['|\"]*)", "");
						link=link.replaceAll("['|\"]", "");
						linklist.add("http://222.206.65.12/opac/"+link.trim());
						
					}
					
					
					for(int i=0;i<geted1.size();i++){
						listitem it = new listitem(image[0],geted1.get(i).text().toString(),linklist.get(i).toString(),R.layout.list_item);
						items.add(it);
						}
					if(items.toString().length()>10)
					{
						Element num_get=doc1.getElementById("titlenav");
						Document doc_get=Jsoup.parse(num_get.toString());
						Elements num_geted=doc_get.getElementsByClass("red");
						
						Message msg=handler.obtainMessage();
						msg.obj=num_geted.get(0).text();					
						handler.sendMessage(msg);
					    
					}
					else
					{
						
						Message msg=handler.obtainMessage();
						msg.obj="0";					
						handler.sendMessage(msg);

					}	
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			else
			{
				Message msg=handler.obtainMessage();
				msg.obj="-1";					
				handler.sendMessage(msg);
			}
			
			
			
			
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}
    	
    }
    
    public void showRoundProcessDialog(Context mContext, int layout)//用来显示等待对话框
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
    
    private boolean checkNetWorkStatus() {   //检测手机网络
        boolean netSataus = false;   
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);   
        cwjManager.getActiveNetworkInfo();   
        if (cwjManager.getActiveNetworkInfo() != null) {   
            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();   
        }   
        return netSataus;   
    } 
    
   
}
