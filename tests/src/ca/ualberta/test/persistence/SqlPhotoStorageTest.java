package ca.ualberta.test.persistence;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.test.AndroidTestCase;
import ca.ualberta.R;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;
import ca.ualberta.utils.ApplicationUtil;

/**
 * Test case for SqlPhotoStorage.
 * 
 * @author connorm
 *
 */

public class SqlPhotoStorageTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		Date d = new Date();

		// Create some test objects and see if
		// they can be instantiated without
		// errors.
		PhotoEntry e1 = new PhotoEntry();
		e1.setId(1);
		e1.setTag("mole on right hand");
		e1.setTimeStamp(d.toString());
		e1.setFilePath("/some/file/img.jpg");
		
		PhotoEntry e2 = new PhotoEntry();
		e2.setId(2);
		e2.setTag("mole on right hand");
		e2.setTimeStamp(d.toString());
		e2.setFilePath("/some/file/img2.jpg");

		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// Leave the database in a clean state.
		new SqlPhotoStorage(getContext()).deleteAllPhotoEntries();

		super.tearDown();
	}

	public void testInsertPhotoEntry() {
		// Give some test values
		Date d = new Date();
		String testFilePath = "/some/file/img1.jpg";
		String testTag = "mole on right hand";
		
		// Stores this photo entry
		PhotoEntry testEntry1 = new PhotoEntry();
		testEntry1.setId(1);
		testEntry1.setTag(testTag);
		testEntry1.setTimeStamp(d.toString());
		testEntry1.setFilePath(testFilePath);
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry1);

		PhotoEntry verifyTestEntry1 = new SqlPhotoStorage(getContext()).getPhotoEntry(testEntry1
				.getId());

		assertEquals(1, verifyTestEntry1.getId());
		assertEquals(testTag, verifyTestEntry1.getTag());
		assertEquals(d.toString(), verifyTestEntry1.getTimeStamp());
		assertEquals(testFilePath, verifyTestEntry1.getFilePath());
		
		new SqlPhotoStorage(getContext()).deleteAllPhotoEntries();
	}
	
	public void testDeletePhotoEntry() throws Exception {
		Date d = new Date();
		String imgPath = ApplicationUtil.copyPhotoToSDCard(R.drawable.sample_0).getAbsolutePath();
		
		PhotoEntry testEntry1 = new PhotoEntry();
		testEntry1.setId(1);
		testEntry1.setTag("mole on right hand");
		testEntry1.setTimeStamp(d.toString());
		testEntry1.setFilePath(imgPath);
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry1);
		
		assertTrue(new SqlPhotoStorage(getContext()).deletePhotoEntry(testEntry1.getId()));
		// Ensure image does not exist after deletion
		assertFalse(new File(imgPath).exists());
		
		new SqlPhotoStorage(getContext()).deleteAllPhotoEntries();
	}
	
	public void testGetAllPhotoEntriesWithTag() {
		String tag = "mole on right hand";
		Date d = new Date();
		
		// Make sure 0 case is handled.
		ArrayList<PhotoEntry> entries = new SqlPhotoStorage(getContext()).getAllPhotoEntriesWithTag("mole on right hand");
		assertEquals(entries.size(), 0);
		
		// Try adding 2 entries now
		PhotoEntry testEntry1 = new PhotoEntry();
		testEntry1.setId(1);
		testEntry1.setTag(tag);
		testEntry1.setTimeStamp(d.toString());
		testEntry1.setFilePath("/some/file/img.jpg");
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry1);
		
		PhotoEntry testEntry2 = new PhotoEntry();
		testEntry2.setId(2);
		testEntry2.setTag(tag);
		testEntry2.setTimeStamp(d.toString());
		testEntry2.setFilePath("/some/file/img2.jpg");
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry2);
		
		entries = new SqlPhotoStorage(getContext()).getAllPhotoEntriesWithTag(tag);
		assertEquals(entries.size(), 2);
		
		new SqlPhotoStorage(getContext()).deleteAllPhotoEntries();
	}
	
	public void testGetAllTags() {
		// Check empty case
		String[] emptyTagsTest = new SqlPhotoStorage(getContext()).getAllTags();
		assertNotNull(emptyTagsTest);
		assertEquals(emptyTagsTest.length, 0);
		
		// Check 2 tags
		String tag1 = "mole on right hand";
		String tag2 = "rash on left hand";
		
		PhotoEntry testEntry1 = new PhotoEntry();
		testEntry1.setId(1);
		testEntry1.setTag(tag1);
		testEntry1.setTimeStamp(new Date().toString());
		testEntry1.setFilePath("/some/file/img1.jpg");
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry1);
		
		PhotoEntry testEntry2 = new PhotoEntry();
		testEntry2.setId(2);
		testEntry2.setTag(tag2);
		testEntry2.setTimeStamp(new Date().toString());
		testEntry2.setFilePath("/some/file/img2.jpg");
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry2);
		
		String[] twoTagsTest = new SqlPhotoStorage(getContext()).getAllTags();
		assertNotNull(twoTagsTest);
		assertEquals(twoTagsTest.length, 2);
		
		// Try 2 tags, but two entries with same tag
		PhotoEntry testEntry3 = new PhotoEntry();
		testEntry3.setId(3);
		testEntry3.setTag(tag1);
		testEntry3.setTimeStamp(new Date().toString());
		testEntry3.setFilePath("/some/file/img3.jpg");
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry3);
		
		String[] twoTagsTest2 = new SqlPhotoStorage(getContext()).getAllTags();
		assertNotNull(twoTagsTest2);
		assertEquals(twoTagsTest.length, 2);
		
		new SqlPhotoStorage(getContext()).deleteAllPhotoEntries();
	}
	
	public void testGetNextAvailableID() {
		long testNextID = new SqlPhotoStorage(getContext()).getNextAvailableID();
		assertEquals(testNextID, 0);
		
		PhotoEntry testEntry1 = new PhotoEntry();
		testEntry1.setId(new SqlPhotoStorage(getContext()).getNextAvailableID());
		testEntry1.setTag("mole on right hand");
		testEntry1.setTimeStamp(new Date().toString());
		testEntry1.setFilePath("/some/file/img1.jpg");
		new SqlPhotoStorage(getContext()).insertPhotoEntry(testEntry1);
		
		testNextID = new SqlPhotoStorage(getContext()).getNextAvailableID();
		assertEquals(1, testNextID);
		
		new SqlPhotoStorage(getContext()).deleteAllPhotoEntries();
	}
}
