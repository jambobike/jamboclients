package com.example.util;

import java.io.Serializable;

public class Constant implements Serializable{

	/**
	 * 
	 */	private static final long serialVersionUID = 1L;

	 //server url
	 public static final String SERVER_URL="http://jambo.boxingbroskenya.com//";

	 //images url
	 public static final String SERVER_IMAGE_UPFOLDER=SERVER_URL+"images/";

	//images gallery url
	public static final String SERVER_IMAGE_GALLERY=SERVER_URL+"images/gallery/";

	 //imagesthumb url
	 public static final String SERVER_IMAGE_UPFOLDER_THUMB=SERVER_URL+"images/thumb/";

	 //category  url
	 public static final String CATEGORY_URL =SERVER_URL+"api.php";

	 //category list 
	 public static final String LISTING_URL=SERVER_URL+"api.php?cat_id=";

	 //categorylist  details
	 public static final String LISTING_DETAILS_URL=SERVER_URL+"api.php?place_id=";

	 //about  details
	 public static final String RATING_URL=SERVER_URL+"api_rating.php?place_id=";

	//rating  url
	public static final String ABOUT_DETAILS_URL=SERVER_URL+"api.php?settings";

	public static final String FACEBOOK_LOGIN_URL = SERVER_URL+"user_social_register_api.php?user_type=fb&name=";

	public static final String NORMAL_LOGIN_URL = SERVER_URL+"user_login_api.php?email=";

 	public static final String REGISTER_URL = SERVER_URL+"user_register_api.php?user_type=normal&name=";

	public static final String GMAIL_LOGIN_URL = SERVER_URL+"user_social_register_api.php?user_type=google&name=";

	public static final String FORGET_PASSWORD_URL = SERVER_URL+"user_forgot_pass_api.php?email=";

	public static final String PROFILE_URL = SERVER_URL+"user_profile_api.php?id=";

	public static final String PROFILE_UPDATE_URL = SERVER_URL+"user_profile_update_api.php?user_id=";

	 public static final String CATEGORY_ARRAY_NAME="Place_App";
	 public static final String CATEGORY_CID="cid";
	 public static final String CATEGORY_NAME="category_name";
	 public static final String CATEGORY_IMAGE="category_image";
	 public static String CATEGORYID;
	 public static String CATEGORYNAME;

	 public static final String LISTING_H_ID="p_id";
	 public static final String LISTING_H_NAME="place_name";
	 public static final String LISTING_H_IMAGE="place_image";
	 public static final String LISTING_H_VIDEO="place_video";
	 public static final String LISTING_H_DES="place_description";
	 public static final String LISTING_H_MAP_LATI="place_map_latitude";
	 public static final String LISTING_H_MAP_LONGI="place_map_longitude";
	 public static final String LISTING_H_ADDRESS="place_address";
	 public static final String LISTING_H_EMAIL="place_email";
	 public static final String LISTING_H_PHONE="place_phone";
	 public static final String LISTING_H_WEBSITE="place_website";
	public static final String LISTING_H_RATING="place_rate_avg";
	 public static String LISTING_H_IDD;

	 public static final String ABOUT_NAME="app_name";
	 public static final String ABOUT_LOGO="app_logo";
	 public static final String ABOUT_EMAIL="app_email";
	 public static final String ABOUT_WEB="app_website";
	 public static final String ABOUT_DESC="app_description";

	//rate
	public static final String RATE_MSG="MSG";
	public static String DEVICE_ID;

	public static int GET_SUCCESS_MSG;
	public static final String MSG="msg";
	public static final String SUCCESS="success";
	public static final String USER_NAME="name";
	public static final String USER_ID="user_id";
	public static final String USER_EMAIL="email";
	public static final String IMAGE="image";
 }
