package com.example.favorite;

public class Pojo {

	private int id;
	private String  H_id;
 	private String  Hotel_name;
	private String  Hotel_image;
	private String  Hotel_video;
	private String  Hotel_description;
	private String  Hotel_map_latitude;
	private String  Hotel_map_longitude;
	private String  Hotel_address;
	private String  Hotel_eamil;
	private String  Hotel_phone;
	private String  Hotel_webb;
	private String  Hotel_rate;


	public Pojo()
	{

	}

	public Pojo(String H_id)
	{
		this.H_id=H_id;
	}

	public Pojo(String hid,String hname,String himage,String hvideo,
			String hdes,String hlati,String hlongi,String hadd,String hmail,String hphone,String hweb,String hrate)
	{
		this.H_id=hid;
 		this.Hotel_name=hname;
		this.Hotel_image=himage;
		this.Hotel_video=hvideo;
		this.Hotel_description=hdes;
		this.Hotel_map_latitude=hlati;
		this.Hotel_map_longitude=hlongi;
		this.Hotel_address=hadd;
		this.Hotel_eamil=hmail;
		this.Hotel_phone=hphone;
		this.Hotel_webb=hweb;
		this.Hotel_rate=hrate;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String H_id(){
		return H_id;
	}
	public void setH_id(String h_id){
		this.H_id=h_id;
	}
	 
	public String getHotel_name() {
		return Hotel_name;
	}

	public void setHotel_name(String hotel_name) {
		this.Hotel_name = hotel_name;
	}
	public String getHotel_image() {
		return Hotel_image;
	}

	public void setHotel_image(String hotel_image) {
		this.Hotel_image = hotel_image;
	}
	public String getHotel_video() {
		return Hotel_video;
	}

	public void setHotel_video(String hotel_video) {
		this.Hotel_video = hotel_video;
	}
	public String getHotel_description() {
		return Hotel_description;
	}

	public void setHotel_description(String hotel_description) {
		this.Hotel_description = hotel_description;
	}
	public String getHotel_map_latitude() {
		return Hotel_map_latitude;
	}

	public void setHotel_map_latitude(String hotel_map_latitude) {
		this.Hotel_map_latitude = hotel_map_latitude;
	}
	public String getHotel_map_longitude() {
		return Hotel_map_longitude;
	}

	public void setHotel_map_longitude(String hotel_map_longitude) {
		this.Hotel_map_longitude = hotel_map_longitude;
	}
	public String getHotel_address() {
		return Hotel_address;
	}

	public void setHotel_address(String Hotel_address) {
		this.Hotel_address = Hotel_address;
	}
	public String getHotel_eamil() {
		return Hotel_eamil;
	}

	public void setHotel_eamil(String Hotel_eamil) {
		this.Hotel_eamil = Hotel_eamil;
	}
	public String getHotel_phone() {
		return Hotel_phone;
	}

	public void setHotel_phone(String Hotel_phone) {
		this.Hotel_phone = Hotel_phone;
	}
	public String getHotel_webb() {
		return Hotel_webb;
	}
 	public void setHotel_webb(String Hotel_webb) {
		this.Hotel_webb = Hotel_webb;
	}
	public String getHotel_rate() {
		return Hotel_rate;
	}
	public void setHotel_rate(String Hotel_rate) {
		this.Hotel_rate = Hotel_rate;
	}
}
