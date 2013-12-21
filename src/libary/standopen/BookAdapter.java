package libary.standopen;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookAdapter implements ListAdapter{

	
	
	private ArrayList<bookitem> coll;
	private Context ctx;
	
	
	public BookAdapter(Context context ,ArrayList<bookitem> coll) {
		// TODO Auto-generated constructor stub
		
		this.ctx=context;
		this.coll=coll;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return coll.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return coll.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getItemViewType(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		bookitem item = coll.get(arg0);
		int itemLayout = item.getLayoutID();
		RelativeLayout layout = new RelativeLayout(ctx);
		LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi.inflate(itemLayout, layout,true);
		TextView tiptext=(TextView)layout.findViewById(R.id.tip);
		tiptext.setText(item.getText());
		RelativeLayout bg=(RelativeLayout)layout.findViewById(R.id.itembg);
		bg.setBackgroundResource(item.getImageid());
		
		return layout;
	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return coll.size();
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnabled(int arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	

}
