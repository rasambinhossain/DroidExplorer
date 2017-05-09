package com.rasam.droid.explorer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class IconifiedTextListAdapter extends BaseAdapter {
	private Context mContext;
	String text;
	Button btn1;
	Button btn2;
	
	private List<IconifiedText> mItems = new ArrayList<IconifiedText>();

	public IconifiedTextListAdapter(Context context) {
		mContext = context;
	}

	public void addItem(IconifiedText it) { mItems.add(it); }

	public void setListItems(List<IconifiedText> lit) { mItems = lit; }

	/** @return The number of items in the */
	public int getCount() { return mItems.size(); }

	public Object getItem(int position) { return mItems.get(position); }

	public boolean areAllItemsSelectable() { return false; }

	public boolean isSelectable(int position) { 
		return mItems.get(position).isSelectable();
	}
	
	

	/** Use the array index as a unique id. */
	public long getItemId(int position) {
		return position;
	}

	/** @param convertView The old view to overwrite, if one is passed
	 * @returns a IconifiedTextView that holds wraps around an IconifiedText */
	public View getView(int position, View convertView, ViewGroup parent) {
		IconifiedTextView btv;
		//Log.i("2",String.valueOf(position));
		if (convertView == null) {
			btv = new IconifiedTextView(mContext, mItems.get(position));
		} 
		
		else { // Reuse/Overwrite the View passed
			// We are assuming(!) that it is castable! 
			//if(position==0)
		//	{
				//btv = (IconifiedTextView) convertView;
			//	btv.setText(text);
		//	}
			
			btv = (IconifiedTextView) convertView;
			btv.setText(mItems.get(position).getText());
			btv.setIcon(mItems.get(position).getIcon());
			
		}
		return btv;
	}

	public void test(String test1) {
		// TODO Auto-generated method stub
		this.text=test1;
		Log.i("1",test1);
		
	}
}
