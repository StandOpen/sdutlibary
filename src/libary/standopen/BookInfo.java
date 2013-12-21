package libary.standopen;
import java.util.ArrayList;
import java.util.Random;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BookInfo extends Activity {
    /** Called when the activity is first created. */
	private TextView book=null;
	private ListView list;
	private ArrayList<bookitem> items = null;
	protected static int timeout = 1000*60;
	protected final static String user_agent = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
	protected static String LOGIN_FLAG = "PHPSESSID";
	private ImageButton left=null;
	private Button Preg=null;
	 String name=null;
	 private Dialog mDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bookinfo);
        book=(TextView)findViewById(R.id.name_book);
        list= (ListView)findViewById(R.id.list);
        items= new ArrayList<bookitem>();
        Preg=(Button)findViewById(R.id.preg);
        left=(ImageButton)findViewById(R.id.back);
        
        final String url=this.getIntent().getStringExtra("link");//获取传来的链接
        name=this.getIntent().getStringExtra("name");//获取传来的名称
        for(int i=0;i<name.length();i++)//去除名称中的前面的编号
        {
        	char ch=name.charAt(i);
        	if(ch=='.'&&i<4)
        	{
        		name=name.substring(i+1,name.length());
        		break;
        	}
        }
        //预约按钮事件
      Preg.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(SdutLibary.instance.IsLogin)//如果已经登录
			{
				//跳向预约界面
				String pregurl=url.replaceFirst("item.php?","userpreg.php?");//替换链接
				Intent intent=new Intent();
				intent.putExtra("url", pregurl);//传预约url
				intent.putExtra("name", name);//传书籍名称
				intent.setClass(BookInfo.this, ReaderPreg.class);
				startActivity(intent);
				
				
				
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
			else
			{
				SdutLibary.instance.OtherLogin();
			}
			
		}
	});
      //关闭按钮
        left.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        book.setText(name);//显示名称
      
        showRoundProcessDialog(BookInfo.this,R.layout.loading_process_dialog_color);//等待对话框
        readthread mythread=new readthread(url);//启用读取信息线程
        mythread.start();
        
 
		        
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
   		  int type=Integer.parseInt(msg.obj.toString());
   		  if(type==1)
   		  {
   		    	list.setAdapter(new BookAdapter(BookInfo.this,items));
   	   			
   		  }else
   		  {
   			Toast toast = Toast.makeText(getApplicationContext(),
	        	     "网络错误,请检查网络后重试", Toast.LENGTH_LONG);
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
			if(checkNetWorkStatus())
			{
			Document doc;
			try {
				doc = Jsoup.connect(url)
						.data("jquery", "java")
						.userAgent("Mozilla")
						.cookie("auth", "token")
						.timeout(50000)
						.get();
				
				
				
				Elements dls = doc.getElementsByClass("booklist");//获取书籍信息
				
				if (dls!= null) {
				    for (int l=0;l<dls.size()-1;l++) {
				    	if(dls.get(l).text().toString().trim().indexOf("文摘附注")>0)
				    	{
				    		//文摘附注
				    		 bookitem info=new bookitem(R.drawable.book_content, dls.get(l).text().toString().trim(), R.layout.bookitem);
				             items.add(info);
				    	}
				    	else
				    	{
				    		//其余的
				             bookitem info=new bookitem(R.drawable.bookitembg, dls.get(l).text().toString().trim(), R.layout.bookitem);
				             items.add(info);
				    	}
				    }
				}
				
				Elements trs=doc.getElementsByTag("td");//获取书籍馆藏信息
				
				for (int i=6;i<trs.size();) {
					if(i>=6)
					{
						if(i%6==0)//书籍
						{
						bookitem info=new bookitem(R.drawable.bookitembg,"《"+name+"》"+"  位置信息-"+i/6+"-:", R.layout.bookitem);
		                items.add(info);
						}
				            
					bookitem position1=new bookitem(R.drawable.bottom1,"索书号:  "+trs.get(i).text().toString().trim(), R.layout.bookitem);
					
					bookitem      position2=new bookitem(R.drawable.bottom1,"条码号:  "+trs.get(i+1).text().toString().trim(), R.layout.bookitem);
						
						
					bookitem	  position3=new bookitem(R.drawable.bottom1,"校区   :  "+trs.get(i+3).text().toString().trim(), R.layout.bookitem);
						
					bookitem	  position4=new bookitem(R.drawable.bottom1,"馆藏地:  "+trs.get(i+4).text().toString().trim(), R.layout.bookitem);
						
					bookitem	  position5=new bookitem(R.drawable.bottom1,"书刊状态:  "+trs.get(i+5).text().toString().trim(), R.layout.bookitem);
						i+=6;
			             
			             items.add(position1);
			             items.add(position2);
			             items.add(position3);
			             items.add(position4);
			             items.add(position5);
			    }
				}
		   
				
				Message msg=handler.obtainMessage();
				msg.obj="1";
				
				handler.sendMessage(msg);
			
			
			
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			}
			else
			{
				Message msg=handler.obtainMessage();
				msg.obj="0";
				
				handler.sendMessage(msg);
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
    
  

