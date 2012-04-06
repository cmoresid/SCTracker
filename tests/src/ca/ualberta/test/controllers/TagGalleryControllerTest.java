package ca.ualberta.test.controllers;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import ca.ualberta.controllers.TagGalleryController;
import ca.ualberta.models.TagGroup;
import ca.ualberta.persistence.SqlPhotoStorage;

public class TagGalleryControllerTest
{
	ArrayList<TagGroup> tags;
	private TagGalleryController controller;
	private SqlPhotoStorage db;
	
	public void setUp() throws Exception {
		tags = new ArrayList<TagGroup>();
		db = new SqlPhotoStorage(this);
		controller = new TagGalleryController(tags);

		super.setUp();
	}
	
	
	
	

}
