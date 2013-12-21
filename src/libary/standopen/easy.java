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
	private EditText input=null;//��������
	private Button search=null;
	private ListView list;
	private ArrayList<listitem> items = null;
	final int []image={R.drawable.ic_bookmark_draw};
	private int num_book;
	private int page=1;
	private HttpClient httpClient=null;//��������ȡ��ҳԴ����ı���
	 int position; 
	 private Dialog mDialog;//��������ʾ�ȴ��Ի���ı���
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
				 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);//�������������
	             imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
				 page=1;
				 items= new ArrayList<listitem>();
				//num_book=GetMsg(input.getText().toString());
				showRoundProcessDialog(easy.this.getParent(),R.layout.loading_process_dialog_color);//��ʾ�ȴ��Ի���
				  readthread mythread=new readthread(input.getText().toString());//��ȡ��Ϣ�߳���������ȡ������Ϣ
				  mythread.start();
			}
		});
        
        
        list.setOnItemClickListener(new OnItemClickListener() {//����listview���item�¼����������ת���鼮�������

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//�����ǵ������ת������ҳ��
				Intent intent=new Intent();
				intent.putExtra("link",items.get(arg2).getUrl());//���鼮������
				intent.putExtra("name",items.get(arg2).getText());//���鼮����
				intent.setClass(easy.this, BookInfo.class);
				startActivity(intent);
			
			}
		});
     
        
        //��������ʾ�����������
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
					
					//��������׶˵�ʱ����ظ����鼮��Ϣ
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

    public class updatethread extends Thread//������ȡ�����鼮��Ϣ���߳�
    {
    	String content;//�������������
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
   			list.setAdapter(new MyAdapter(easy.this,items));//��ʾ�������
   			list.setSelection(position+3);
   			}
   			else
   			{
   				//û���鼮����û��������µ�����ʾ
   				Toast toast = Toast.makeText(getApplicationContext(),
		        	     "û���������ƥ����鼮�����������", Toast.LENGTH_LONG);
		        	   toast.setGravity(Gravity.CENTER, 0, 0);
		        	   LinearLayout toastView = (LinearLayout) toast.getView();
		        	   ImageView imageCodeProject = new ImageView(getApplicationContext());
		        	   imageCodeProject.setImageResource(R.drawable.menu_about);
		        	   toastView.addView(imageCodeProject, 0);
		        	   toast.show();		
   			}
   			mDialog.dismiss();//ȡ���ȴ��Ի���
   		}
   		
   	};
       
       
		@Override
		public void run() {
		
			super.run();
			
			//�������������������ת��gbk����
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
				GetMethod get = new GetMethod(url);//��ʼ��ģ�����������
		    	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");//���ñ���
		    	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);//����cookie
		    	httpClient.getParams().setSoTimeout(500*60);//���ó�ʱʱ��
				httpClient.executeMethod(get);//����ִ�з�����ȡ��ҳԴ����
				byte[] responseBody = get.getResponseBody();
				
		        String str=new String(responseBody);//��ȡ��ҳԴ����
				
				
				Document doc1=Jsoup.parse(str);//jsoup��һ���ⲿ����������������ҳԴ����
				@SuppressWarnings("unused")
			//	Elements divs = doc1.getElementsByClass("list_books");//������ȡÿ���鼮��Ϣ����Ϣ�� ÿ���鼮��class����һ����
				Elements geted=doc1.getElementsByTag("h3");//��ȡÿ���鼮����
				Document doc2=Jsoup.parse(geted.toString());
				Elements geted1=doc2.getElementsByTag("a");
				//ͨ��������ʽ��ȡÿ���鼮�ĳ�����
				ArrayList<String> linklist=new ArrayList<String>();	
				String patternString = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))"; //����ƥ�䳬���ӵ�������ʽ
				Pattern pattern = Pattern.compile(patternString,Pattern.CASE_INSENSITIVE);

				Matcher matcher = pattern.matcher(geted.toString());

				while (matcher.find()) {

					String link=matcher.group();
					link=link.replaceAll("href\\s*=\\s*(['|\"]*)", "");
					link=link.replaceAll("['|\"]", "");
					linklist.add("http://222.206.65.12/opac/"+link.trim());
					
				}
				
				
				for(int i=0;i<geted1.size();i++){
					listitem it = new listitem(image[0],geted1.get(i).text().toString(),linklist.get(i).toString(),R.layout.list_item);//listitemʹ�ñ���ÿ���鼮����Ϣ����
					items.add(it);
					}
				
				//��ʾ�������
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
    
    
    public class readthread extends Thread//����߳����������ײ���ʾ����
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
 	     "�������������������ԡ�", Toast.LENGTH_LONG);
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
     	     "û���������ƥ����鼮", Toast.LENGTH_LONG);
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
   	      	     "���������ϵ���"+number+"�����", Toast.LENGTH_LONG);
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
    
    public void showRoundProcessDialog(Context mContext, int layout)//������ʾ�ȴ��Ի���
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
        // ע��˴�Ҫ����show֮�� ����ᱨ�쳣
        mDialog.setContentView(layout);
    }
    
    private boolean checkNetWorkStatus() {   //����ֻ�����
        boolean netSataus = false;   
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);   
        cwjManager.getActiveNetworkInfo();   
        if (cwjManager.getActiveNetworkInfo() != null) {   
            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();   
        }   
        return netSataus;   
    } 
    
   
}
