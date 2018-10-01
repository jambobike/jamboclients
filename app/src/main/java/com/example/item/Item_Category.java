package com.example.item;

public class Item_Category {

	
	private String CategoryName;
	private String CategoryId; 
	private String ImageUrl; 
	
	
	
	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryname) {
		this.CategoryName = categoryname;
	}
	
	public String getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(String categoryid) {
		this.CategoryId = categoryid;
	}
	public String getImageurl()
	{
		return ImageUrl;
		
	}
	
	public void setImageurl(String imageurl)
	{
		this.ImageUrl=imageurl;
	}
	

}
