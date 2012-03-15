package ca.ualberta.models;

import java.util.ArrayList;

import ca.ualberta.persistence.SqlPhotoStorage;

public class TagGroup
{
	private PhotoEntry firstImage;
	private PhotoEntry lastImage;
	private String tag;
	
	
	
	//Constructor
	public TagGroup(String tag){
		SqlPhotoStorage DB = new SqlPhotoStorage();
		
		ArrayList<PhotoEntry> photos = DB.getAllPhotoEntriesWithTag(tag);
		
		this.firstImage = photos.get(0);
		this.lastImage = photos.get(photos.size()-1); //this should return the last photo in the array.
		this.tag = tag;
	}
	
	//getter methods that provide access to the contents of the TagGroup
	//the contents shouldn't change so there aren't any setters.
	public PhotoEntry getFirstImage()
	{
		return firstImage;
	}

	public PhotoEntry getLastImage()
	{
		return lastImage;
	}

	public String getTag()
	{
		return tag;
	}
	
	
}
