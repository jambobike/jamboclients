package com.example.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.placefinderapp.R;
import com.example.item.Item_Category;
import com.example.util.Constant;
import com.squareup.picasso.Picasso;


public class Category_Adapter extends  ArrayAdapter<Item_Category> {


	private Activity activity;
	private List<Item_Category> itemsCategory;
	private Item_Category objCategoryBean;
	private int row;
 
	public Category_Adapter(Activity act, int resource, List<Item_Category> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.itemsCategory = arrayList;
 
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

		if ((itemsCategory == null) || ((position + 1) > itemsCategory.size()))
			return view;

		objCategoryBean = itemsCategory.get(position);


		holder.imgv_category=(ImageView)view.findViewById(R.id.img_category);
		holder.txt_category=(TextView)view.findViewById(R.id.text_cate_imgtitle);

 		Picasso.with(activity).load(Constant.SERVER_IMAGE_UPFOLDER+objCategoryBean.getImageurl().toString().replace(" ", "%20")).into(holder.imgv_category);
  		holder.txt_category.setText(objCategoryBean.getCategoryName().toString());

		return view;

	}

	public class ViewHolder {

		public ImageView imgv_category;
		public  TextView txt_category;

	}
} 



