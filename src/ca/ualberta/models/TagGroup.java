package ca.ualberta.models;

import java.util.ArrayList;

import ca.ualberta.adapters.TagGalleryListAdapter;
import ca.ualberta.persistence.SqlPhotoStorage;



/**
 * Descries a tag. Used by the {@code TagGalleryListAdapter}
 */
public class TagGroup
{
	private PhotoEntry firstImage;
	private PhotoEntry lastImage;
	private String tag;
	
	
	
	/**
	 * Constructs a TagGroup using the given tag. Finds and assignes the 
	 * first {@code PhotoEntry} taken witht that tag to firstImage, and the most recent
	 * {@code PhotoEntry} taken with that tag to lastImage.
	 * @param tag The tag that defines the TagGroup
	 */
	public TagGroup(String tag){
		SqlPhotoStorage DB = new SqlPhotoStorage();
		
		ArrayList<PhotoEntry> photos = DB.getAllPhotoEntriesWithTag(tag);
		
		this.firstImage = photos.get(0);
		this.lastImage = photos.get(photos.size()-1); //this should return the last photo in the array.
		this.tag = tag;
	}
	
	/**
	 * Returns the {@code PhotoEntry} that was first tagged with the tag
	 * @return The {@code PhotoEntry} that was first tagged with the tag
	 */
	public PhotoEntry getFirstImage()
	{
		return firstImage;
	}

	/**
	 * Returns the {@code PhotoEntry} that was last tagged with the tag
	 * @return the {@code PhotoEntry} that was last tagged with the tag
	 */
	public PhotoEntry getLastImage()
	{
		return lastImage;
	}

	/**
	 * Returns the tag that the {@code TagGroup} was built around
	 * @return The tag that the {@code TagGroup} was built around
	 */
	public String getTag()
	{
		return tag;
	}
	
	
}
