package com.example.hp.smart_city_traveller;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerlistAdapter extends BaseAdapter{

	private Context con;
	private ArrayList<Newdraweritem> drawitem;
	
	public DrawerlistAdapter(Context c,ArrayList<Newdraweritem> drawitem)
	{
		con=c;
		this.drawitem=drawitem;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return drawitem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return drawitem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawerlist_item, null);
        }
		ImageView img=(ImageView) convertView.findViewById(R.id.icon);
		TextView txt=(TextView) convertView.findViewById(R.id.title);
		
		img.setImageResource(drawitem.get(position).getIcon());
		txt.setText(drawitem.get(position).getTitle());
		
		return convertView;
	}

}
