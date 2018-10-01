package com.example.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.placefinderapp.R;
import com.example.item.Item_Listing;
import com.example.util.Constant;
import com.squareup.picasso.Picasso;


public class Listing_Adapter extends ArrayAdapter<Item_Listing>
{
	private Activity activity;
	private List<Item_Listing> itemslisting;
	private Item_Listing objlistingBean;
	private int row;
 
	public Listing_Adapter(Activity act, int resource, List<Item_Listing> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.itemslisting = arrayList;
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

		objlistingBean = itemslisting.get(position);
 
 		holder.txt_listingname=(TextView)view.findViewById(R.id.text_subcatelist);
 		holder.txt_address=(TextView)view.findViewById(R.id.text_addres);
		holder.img_item=(ImageView)view.findViewById(R.id.imageView);
		holder.imgstar=(ImageView)view.findViewById(R.id.imageView2);

 		holder.txt_listingname.setText(objlistingBean.getHotel_name().toString());
 		holder.txt_address.setText(objlistingBean.getHotel_Address().toString());
		Picasso.with(activity).load(Constant.SERVER_IMAGE_UPFOLDER+objlistingBean.getHotel_image().toString().replace(" ", "%20")).into(holder.img_item);
		Log.e("listimg",""+Constant.SERVER_IMAGE_UPFOLDER+objlistingBean.getHotel_image().toString());
		if(TextUtils.isEmpty(objlistingBean.getHotel_rating()))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_0).into(holder.imgstar);
 		}
		if(objlistingBean.getHotel_rating().equals("1"))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_1).into(holder.imgstar);
		}
		else if(objlistingBean.getHotel_rating().equals("2"))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_2).into(holder.imgstar);
		}
		else if(objlistingBean.getHotel_rating().equals("3"))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_3).into(holder.imgstar);
		}
		else if(objlistingBean.getHotel_rating().equals("4"))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_4).into(holder.imgstar);
		}
		else if(objlistingBean.getHotel_rating().equals("5"))
		{
			Picasso.with(activity).load(R.drawable.ic_rate_5).into(holder.imgstar);
		}
		return view;

	}

	public class ViewHolder {
 
		public  TextView txt_listingname,txt_address;
		public  ImageView img_item,imgstar;

	}
} 
