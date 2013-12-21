package libary.standopen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginDialog extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    private Button cancelbutton=null;
    private Button okbutton=null;
    private EditText number,pass=null;
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.logindialog);
      number=(EditText)findViewById(R.id.number);
      pass=(EditText)findViewById(R.id.pass);
     okbutton=(Button)findViewById(R.id.ok);
     cancelbutton=(Button)findViewById(R.id.cancel);
     
     okbutton.setOnClickListener(this);
		
		
	       
	
     cancelbutton.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
             imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
			finish();
		}
	});
  }
public void onClick(View v) {
	// TODO Auto-generated method stub
	if(v.getId()==R.id.ok)
	{
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
		    if(number.getText().toString()!=""&&pass.getText().toString()!="")
		    {
			Intent intent = new Intent();   
	        Bundle b = new Bundle();   
	        b.putString("number",number.getText().toString());  
	        b.putString("password",pass.getText().toString());
	        intent.putExtras(b);
	        this.setResult(RESULT_OK,intent);
	        finish();
		    }
		    else
		    {
		    	Toast toast = Toast.makeText(getApplicationContext(),
		        	     "输入不能为空", Toast.LENGTH_LONG);
		        	   toast.setGravity(Gravity.CENTER, 0, 0);
		        	   LinearLayout toastView = (LinearLayout) toast.getView();
		        	   ImageView imageCodeProject = new ImageView(getApplicationContext());
		        	   imageCodeProject.setImageResource(R.drawable.menu_about);
		        	   toastView.addView(imageCodeProject, 0);
		        	   toast.show();		
		    }
	}
}
  
  

}