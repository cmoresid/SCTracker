package ca.ualberta.models;


public class TagGroup
{
	private PhotoEntry firstPhoto;
	private PhotoEntry lastPhoto;
	private String tag;
	
	
	
	//Constructor
	public TagGroup(String tag, PhotoEntry firstPhoto, PhotoEntry lastPhoto){
		this.firstPhoto = firstPhoto;
		this.lastPhoto = lastPhoto;
		this.tag = tag;
	}
	
	//getter methods that provide access to the contents of the TagGroup
	//the contents shouldn't change so there aren't any setters.
	public PhotoEntry getFirstPhoto()
	{
		return firstPhoto;
	}

	public PhotoEntry getLastPhoto()
	{
		return lastPhoto;
	}

	public String getTag()
	{
		return tag;
	}
	
	
}
