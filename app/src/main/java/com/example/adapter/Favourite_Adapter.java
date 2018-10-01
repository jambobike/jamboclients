package com.example.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.placefinderapp.R;
import com.example.favorite.Pojo;
import com.example.util.Constant;
import com.squareup.picasso.Picasso;

public class Favourite_Adapter extends ArrayAdapter<Pojo>
{
	private Activity activity;
	private List<Pojo> itemslisting;
	private Pojo objlistingBeanpojo;
	private int row;
	private ArrayList<Pojo> arraylist;
 	 

	public Favourite_Adapter(Activity act, int resource, List<Pojo> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.itemslisting = arrayList;
 		this.arraylist = new ArrayList<Pojo>();
		arraylist.addAll(itemslisting);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((itemslisting == null) || ((position + 1) > itemslisting.size()))
			return view;

		objlistingBeanpojo = itemslisting.get(position);
 
 		holder.txt_listingname=(TextView)view.findViewById(R.id.text_subcatelist);
 		holder.txtaddress=(TextView)view.findViewById(R.id.text_addres);
		holder.img_item=(ImageView)view.findViewById(R.id.imageView);
		holder.imgstar=(ImageView)view.findViewById(R.id.imageView2);

 		holder.txt_listingname.setText(objlistingBeanpojo.getHotel_name().toString());
 		holder.txtaddress.setText(objlistingBeanpojo.getHotel_address().toString());

		Picasso.with(activity).load(Constant.SERVER_IMAGE_UPFOLDER+objlistingBeanpojo.getHotel_image().toString().replace(" ", "%20")).into(holder.img_item);
		if(TextUtils.isEmpty(objlistingBeanpojo.getHotel_rate()))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_0).into(holder.imgstar);
		}
		if(objlistingBeanpojo.getHotel_rate().equals("1"))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_1).into(holder.imgstar);
		}
		else if(objlistingBeanpojo.getHotel_rate().equals("2"))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_2).into(holder.imgstar);
		}
		else if(objlistingBeanpojo.getHotel_rate().equals("3"))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_3).into(holder.imgstar);
		}
		else if(objlistingBeanpojo.getHotel_rate().equals("4"))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_4).into(holder.imgstar);
		}
		else if(objlistingBeanpojo.getHotel_rate().equals("5"))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_5).into(holder.imgstar);
		}
		return view;

	}

	public class ViewHolder {
 
		public  TextView txt_listingname,txtaddress;
		public ImageView img_item,imgstar;

	}
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		itemslisting.clear();
		if (charText.length() == 0) {
			itemslisting.addAll(arraylist);
		} 
		else 
		{
			for (Pojo wp : arraylist) 
			{
				if (wp.getHotel_name().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					itemslisting.add(wp);
				}
			}
		}

		 notifyDataSetChanged();
	}
} 
