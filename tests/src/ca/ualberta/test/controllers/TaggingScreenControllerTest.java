package ca.ualberta.test.controllers;

import java.util.ArrayList;
import java.util.Date;

import android.test.AndroidTestCase;
import ca.ualberta.controllers.TaggingScreenController;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;

public class TaggingScreenControllerTest extends AndroidTestCase {
	
	private TaggingScreenController mTestController;
	private ArrayList<String> mTestArray;
	
	public void setUp() throws Exception {
		mTestArray = new ArrayList<String>();
		mTestController = new TaggingScreenController(mTestArray);

		super.setUp();
	}
	
	public void tearDown() throws Exception {
		mTestController.dispose();
		new SqlPhotoStorage(getContext()).deleteAllPhotoEntries();
		
		super.tearDown();
	}
	
	public void testGetAllTagsNoTags() {
		mTestController.handleMessage(TaggingScreenController.GET_ALL_TAGS, null);
		assertTrue(mTestArray.size() == 0);
	}
	
	public void testGetAllTagsOneTag() throws Exception {
		String testTag = "mole on face";
		
		PhotoEntry testEntry1 = new PhotoEntry();
		testEntry1.setTimeStamp(new Date().toString());
		testEntry1.setTag(testTag);
		testEntry1.setFilePath("/some/path");
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry1);
		
		mTestController.handleMessage(TaggingScreenController.GET_ALL_TAGS, null);
		
		// Must wait for the mTestArray to be populated.
		// If test fails, it's probably because the operation
		// hasn't finished yet.
		Thread.sleep(1000);
		assertEquals(1, mTestArray.size());
	}
	
	public void testGetAllTagsTwoTags() throws InterruptedException {
		String testTag1 = "mole on face";
		String testTag2 = "rash on ass";
		
		PhotoEntry testEntry1 = new PhotoEntry();
		testEntry1.setTimeStamp(new Date().toString());
		testEntry1.setTag(testTag1);
		testEntry1.setFilePath("/some/path");
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry1);
		
		PhotoEntry testEntry2 = new PhotoEntry();
		testEntry2.setTimeStamp(new Date().toString());
		testEntry2.setTag(testTag2);
		testEntry2.setFilePath("/some/path");
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry2);
		
		mTestController.handleMessage(TaggingScreenController.GET_ALL_TAGS, null);
		
		// Must wait for the mTestArray to be populated.
		// If test fails, it's probably because the operation
		// hasn't finished yet.
		Thread.sleep(1000);
		assertEquals(2, mTestArray.size());
	}
}
