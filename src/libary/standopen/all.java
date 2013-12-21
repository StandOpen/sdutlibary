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
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;


public class all extends Activity {

	private Spinner option=null;
	private EditText input=null;
	private Button search=null;
	int num=0;
	String []array;
	private ListView list;
	private ArrayList<listitem> items = null;
	final int []image={R.drawable.ic_bookmark_draw};
	private int num_book;
	private int page=1;
	 int position; 
	 private HttpClient httpClient=null;
	 private Dialog mDialog;
	 private int book_number=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        View contentView = LayoutInflater.from(this.getParent()).inflate(R.layout.all, null);
        //setContentView(R.layout.search_activity);
        setContentView(contentView);
       // setContentView(R.layout.all);
        list= (ListView)findViewById(R.id.list);
        num_book=0;
        search=(Button)findViewById(R.id.gotosearch);
        option=(Spinner)findViewById(R.id.option);
        input=(EditText)findViewById(R.id.input);
        search=(Button)findViewById(R.id.gotosearch);
        Resources res =getResources();
		array=res.getStringArray(R.array.select);
		httpClient=SdutLibary.instance.httpClient;
		 ArrayAdapter target_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,new String[] { "限制条件：任意词", "限制条件：题名" ,"限制条件：责任者","限制条件：主题词","限制条件：索书号","限制条件：出版社","限制条件：丛书名"});
        //设置下拉列表的风格
		 
        target_adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        //将adapter 添加到spinner中
        option.setAdapter(target_adapter);
        option.setPrompt("请选择限制条件：");
        option.setOnItemSelectedListener(new OnItemSelectedListener() {//监听选择搜索条件事件

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				num=arg2;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
search.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	             imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
	             book_number=1;
	             items= new ArrayList<listitem>();
	             showRoundProcessDialog(all.this.getParent(),R.layout.loading_process_dialog_color);
				  readthread mythread=new readthread(input.getText().toString(),num);
				  mythread.start();
			}
		});
        
        
        list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("link",items.get(arg2).getUrl());
				intent.putExtra("name",items.get(arg2).getText());
				intent.setClass(all.this, BookInfo.class);
				startActivity(intent);
				
			}
		});
 list.setOnScrollListener(new OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(view.getLastVisiblePosition()<8)
				{
				
					input.setVisibility(0);
					search.setVisibility(0);
					option.setVisibility(0);
				}
				else
				{
					input.setVisibility(8);
					search.setVisibility(8);
					option.setVisibility(8);
				}
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					
				      if (view.getLastVisiblePosition() == view.getCount()-1) {
				    	  page++;
				      if(page<num_book/20+1)
				      {
				    	  showRoundProcessDialog(all.this.getParent(),R.layout.loading_process_dialog_color);
						  updatethread mythread=new updatethread(input.getText().toString(),page);
						  mythread.start();
						  position = list.getFirstVisiblePosition();
				      }
				     }
				
				
				
				
			}
			
			
		}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}
			});


        
        
    }
    
   
 
    public class updatethread extends Thread
    {
    	String content;
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
   			// TODO Auto-generated method stub
   			super.handleMessage(msg);
   			list.setAdapter(new MyAdapter(all.this,items));
   			list.setSelection(position+3);
   			mDialog.dismiss();
   		}
   		
   	};
       
       
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			try {
				String input_utf="";
		    	String input_gbk="";
				
					input_utf = URLEncoder.encode(content, "utf-8");
					input_gbk=new String(input_utf.getBytes("utf-8"), "gbk");
					
				
				String url;
				//下面是用来分析搜索条件
				if(num==0)//任意词
				{
				url="http://222.206.65.12/opac/search_adv_result.php?sType0=any&q0="+input_gbk+"&page="+page;
				}
				else//其它的
				{
					int number=num+1;
					url="http://222.206.65.12/opac/search_adv_result.php?sType0="+number+"&q0="+input_gbk+"&page="+page;
				}
				
				GetMethod get = new GetMethod(url);
		    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
				httpClient.executeMethod(get);
				byte[] responseBody = get.getResponseBody();
		        String str=new String(responseBody);
				
				
				Document doc1=Jsoup.parse(str);
				
				Elements geted=doc1.getElementsByTag("TD");
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
					listitem it = new listitem(image[0],book_number+" ."+geted1.get(i).text().toString(),linklist.get(i).toString(),R.layout.list_item);
					items.add(it);
					book_number++;
					}
			
					if(items.toString().length()>10)
					{			
						Message msg=handler.obtainMessage();
						msg.obj="f";
						
						handler.sendMessage(msg);
					}
					else
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
    
    
    public class readthread extends Thread
    {
    	String content;
        int number_books=0;
        int number;
       public readthread(String text,int number)
       {
    	   this.content=text;
          this.number=number;
       }
       
       
       Handler handler=new Handler(){

   		@SuppressLint("HandlerLeak")
		@Override
   		public void handleMessage(Message msg) {
   			// TODO Auto-generated method stub
   			super.handleMessage(msg);
   		    number_books=Integer.parseInt(msg.obj.toString());
   		     num_book=number_books;
   		     if(number_books==-1)
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
   		    if(number_books==0)
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
   		    	list.setAdapter(new MyAdapter(all.this,items));
   	   			
   	   			mDialog.dismiss();
   				Toast toast = Toast.makeText(all.this.getParent(),
   	      	     "与条件符合的有"+number_books+"条结果", Toast.LENGTH_LONG);
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
			if( checkNetWorkStatus())
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
			String url;
			if(number==0)
			{
			url="http://222.206.65.12/opac/search_adv_result.php?sType0=any&q0="+input_gbk;
			}
			else
			{
				int number1=number+1;
				url="http://222.206.65.12/opac/search_adv_result.php?sType0=0"+number1+"&q0="+input_gbk;
			}
			GetMethod get = new GetMethod(url);
	    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
	    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			try {
				httpClient.executeMethod(get);
				byte[] responseBody = get.getResponseBody();
		        String str=new String(responseBody);
				Document doc1=Jsoup.parse(str);
				
				Elements geted=doc1.getElementsByTag("TD");
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
					listitem it = new listitem(image[0],book_number+" ."+geted1.get(i).text().toString(),linklist.get(i).toString(),R.layout.list_item);
					items.add(it);
					book_number++;
					}
			
						if(items.toString().length()>10)
						{
							Elements num_get=doc1.getElementsByClass("box_bgcolor");
							Document doc_get=Jsoup.parse(num_get.toString());
							Elements num_geted=doc_get.getElementsByTag("font");
							
							Message msg=handler.obtainMessage();
							msg.obj=num_geted.get(2).text();					
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
    
    
    
    
    public void Items()
	{
		AlertDialog.Builder builder=new Builder(this.getParent());
		builder.setTitle("搜索条件选项");
		builder.setItems( new String[] { "任意词", "题名" ,"责任者","主题词","索书号","出版社","丛书名","关闭"}, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(which<array.length)
				{
					//option.setText("限制条件："+array[which]);
					num=which;
				}
				
			}
		});
		builder.create().show();
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
}
