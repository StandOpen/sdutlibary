package libary.standopen;

import hleper.DBHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.database.Cursor;
import android.net.ConnectivityManager;
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

@SuppressLint("HandlerLeak")
public class HotPinJia extends Activity {
    /** Called when the activity is first created. */
	private ListView list;
	private ArrayList<DetailEntity> items = null;
	final int []image={R.drawable.logo};
	 int position; 
	 private Button GetInfo=null;
	 public  DBHelper mDBHelper = null;
	 private Dialog mDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookbrow);
        list= (ListView)findViewById(R.id.list); 
        items= new ArrayList<DetailEntity>();
        GetInfo=(Button)findViewById(R.id.gotosearch);
        createDB1();
    	ViewRecords();
    	GetInfo.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRoundProcessDialog(HotPinJia.this.getParent(),R.layout.loading_process_dialog_color);
				updatethread mythread=new updatethread();
				  mythread.start();
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
   
    public class updatethread extends Thread
    {
    	
       public updatethread()
       {
    	
       }
       
       
       Handler handler=new Handler(){

   		@Override
   		public void handleMessage(Message msg) {
   			// TODO Auto-generated method stub
   			super.handleMessage(msg);
   			int type_net=Integer.parseInt(msg.obj.toString());
   			if(type_net==1)
   			{
   			list.setAdapter(new DetailAdapter(HotPinJia.this,items));
   			}
   			else
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
			String url="http://222.206.65.12/opac/user_score_rank.php";
			
			Document doc;
			try {
				doc = Jsoup.connect(url)
						.data("jquery", "java")
						.userAgent("Mozilla")
						.cookie("auth", "token")
						.timeout(50000)
						.get();
				
				
				Document doc1=Jsoup.parse(doc.toString());
				
				Elements geted=doc1.getElementsByTag("tr");
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
				
				if(geted1.size()>20)
				{
				DelAll();
				}
				
				int number;
				
				for(int i=0;i<geted1.size();i++){
					
					insertSomeRecords(geted1.get(i).text().toString(),linklist.get(i).toString());
					if(i%2==0)
					{
						number=i+1;
						DetailEntity temp= new DetailEntity(number,"排名："+number,geted1.get(i).text().toString(),linklist.get(i).toString(),R.layout.list_say_me_item);
						items.add(temp);
					}
					else
					{
						number=i+1;
						DetailEntity temp= new DetailEntity(number,"排名："+number,geted1.get(i).text().toString(),linklist.get(i).toString(),R.layout.list_say_he_item);
						items.add(temp);
					}
				}
				if(items.toString().length()>10)
				{
					
		       	Message msg=handler.obtainMessage();
				msg.obj="1";
				
				handler.sendMessage(msg);
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
    
    
    public void createDB1() {
		String DB_NAME = "hotpinjia";
		mDBHelper = new DBHelper(HotPinJia.this,DB_NAME, null, 1);
		assert(mDBHelper != null);
		
	}
	private void insertSomeRecords(String name,String url) {  
		 mDBHelper.addPeople(name,url);
		
	}
	
	public void ViewRecords() {
		ArrayList<DetailEntity> listitem = null;
		 listitem = new ArrayList<DetailEntity>();
		 ArrayList<DetailEntity> list1=null;
		 list1 = new ArrayList<DetailEntity>();
		int num=0;
		Cursor c = mDBHelper.getWritableDatabase().query(
				DBHelper.TB_NAME,null,null,null,null,null,   
		        DBHelper.NAME); 
		if (c.moveToFirst()) {
			GetInfo.setText("点击刷新数据");
			GetInfo.setVisibility(8);
			int idxID = c.getColumnIndex(DBHelper.ID);
			int idxName = c.getColumnIndex(DBHelper.NAME);
			int idxNumber = c.getColumnIndex(DBHelper.Url);
			do {
				
				if(num%2==0)
				{
					
					DetailEntity temp= new DetailEntity(c.getInt(idxID),"fklkfds",c.getString(idxName),c.getString(idxNumber),R.layout.list_say_me_item);
					listitem.add(temp);
				}
				else
				{
					
					DetailEntity temp= new DetailEntity(c.getInt(idxID),"fklkfds",c.getString(idxName),c.getString(idxNumber),R.layout.list_say_me_item);
					listitem.add(temp);
				}
				num++;
			} while (c.moveToNext());
			
			
			
			Collections.sort(listitem,new Comparator<DetailEntity>() {

				public int compare(DetailEntity lhs, DetailEntity rhs) {
					// TODO Auto-generated method stub
					 if(lhs.getDate()>rhs.getDate())
					 {
						 return 1;
					 }
					 else
					 {
						 return -1;
					 }
				
					 
				}
			});
			
			
			for(int i=0;i<listitem.size();i++)
			{
				if(i%2==0)
				{
					
					DetailEntity temp= new DetailEntity(listitem.get(i).getDate(),"排名："+(i+1),listitem.get(i).getText(),listitem.get(i).getUrl(),R.layout.list_say_me_item);
					list1.add(temp);
				}
				else
				{
					
					DetailEntity temp= new DetailEntity(listitem.get(i).getDate(),"排名："+(i+1),listitem.get(i).getText(),listitem.get(i).getUrl(),R.layout.list_say_he_item);
					list1.add(temp);
				}
				
				
				
			}
			list.setAdapter(new DetailAdapter(HotPinJia.this, list1));
			
			
			
		}
		
		
		
		c.close();

	}
	
	public void DelAll()
	{
		mDBHelper.delAllPeople();
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
