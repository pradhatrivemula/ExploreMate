package com.example.hp.smart_city_traveller;

public class Newdraweritem {
	
	public String title;
	public int icon;
	
	public Newdraweritem(){}
	
	public Newdraweritem(String t,int i)
	{
		title=t;
		icon=i;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public int getIcon()
	{
		return icon;
	}
	
	public void setTitle(String t)
	{
		title=t;
	}
	
	public void setIcon(int i)
	{
		icon=i;
	}
}

