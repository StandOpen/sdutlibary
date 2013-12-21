package libary.standopen;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailAdapter implements ListAdapter{
	private ArrayList<DetailEntity> coll;
	private Context ctx;
	TextView tvText=null;
	public DetailAdapter(Context context ,ArrayList<DetailEntity> coll) {
		ctx = context;
		this.coll = coll;
	}
	
	public boolean areAllItemsEnabled() {
		return false;
	}
	public boolean isEnabled(int arg0) {
		return false;
	}
	public int getCount() {
		return coll.size();
	}
	public Object getItem(int position) {
		return coll.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
	public int getItemViewType(int position) {
		return position;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		DetailEntity entity = coll.get(position);
		int itemLayout = entity.getLayoutID();
		
		final RelativeLayout layout = new RelativeLayout(ctx);
		LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi.inflate(itemLayout, layout,true);
		
		final TextView tvName = (TextView) layout.findViewById(R.id.messagedetail_row_name);
		tvName.setText(entity.getName());
		@SuppressWarnings("unused")
		ImageView image=(ImageView)layout.findViewById(R.id.messagegedetail_rov_icon);
		final LinearLayout mylayout = (LinearLayout)layout.findViewById(R.id.chat_layout);
		tvText = (TextView) layout.findViewById(R.id.messagedetail_row_text);
		tvText.setText(entity.getText());
		mylayout.getBackground().setAlpha(200);
		mylayout.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("link",coll.get(position).getUrl());
				intent.putExtra("name",coll.get(position).getText());
				intent.setClass(ctx, BookInfo.class);
				ctx.startActivity(intent);
			}
		});
		return layout;
	}
	public int getViewTypeCount() {
		return coll.size();
	}
	public boolean hasStableIds() {
		return false;
	}
	public boolean isEmpty() {
		return false;
	}
	public void registerDataSetObserver(DataSetObserver observer) {
	}
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}
	
    
}
