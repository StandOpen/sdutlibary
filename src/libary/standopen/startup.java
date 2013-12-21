package libary.standopen;



import java.util.Random;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("ParserError")
public class startup extends Activity {
    /** Called when the activity is first created. */
	
	
	private Handler handler = new Handler();  
	  
	private Runnable runnable = new Runnable() {  
	  
	    public void run() {  
	    	Intent intent=new Intent();
			intent.setClass(startup.this,SdutLibary.class);
			startActivity(intent);
			Random ran =new Random(System.currentTimeMillis());
			int type=ran.nextInt(3);
			switch (type) {
			case 0:
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				break;
			case 1:
				overridePendingTransition(R.anim.push_up_in,
						R.anim.push_up_out);
				break;
			case 2:
				overridePendingTransition(R.anim.slide_up_in,
						R.anim.slide_down_out);
			}
			handler.removeCallbacks(runnable);
			finish();
			  
	    }  
	  
	};  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
                requestWindowFeature(Window.FEATURE_NO_TITLE);  
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.startup);
        handler.postDelayed(runnable,2000);
    }
}