package ca.ualberta.test.persistence;


import java.util.Date;

import android.test.AndroidTestCase;
import ca.ualberta.models.PhotoEntry;
import ca.ualberta.persistence.SqlPhotoStorage;
import ca.ualberta.utils.ApplicationUtil;
import ca.ualberta.R;

public class SqlPhotoStorageTest extends AndroidTestCase {

	/** File reference to the first testing photo */
	private String p1;
	private String p2;

	@Override
	protected void setUp() throws Exception {
		Date d = new Date();

		p1 = ApplicationUtil.copyPhotoToSDCard(R.drawable.sample_0).getPath();
		p2 = ApplicationUtil.copyPhotoToSDCard(R.drawable.sample_1).getPath();

		PhotoEntry e1 = new PhotoEntry();
		e1.setId(1);
		e1.setTag("mole on right hand");
		e1.setTimeStamp(d.toString());
		e1.setFilePath(p1);
		
		PhotoEntry e2 = new PhotoEntry();
		e2.setId(2);
		e2.setTag("mole on right hand");
		e2.setTimeStamp(d.toString());
		e2.setFilePath(p2);

		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		new SqlPhotoStorage(getContext()).deleteAllPhotoEntries();

		super.tearDown();
	}

	public void testInsertPhotoEntry() {
		Date d = new Date();

		PhotoEntry e1 = new PhotoEntry();
		e1.setId(1);
		e1.setTag("mole on right hand");
		e1.setTimeStamp(d.toString());
		e1.setFilePath(p1);
		new SqlPhotoStorage(getContext()).insertPhotoEntry(e1);

		PhotoEntry e2 = new SqlPhotoStorage(getContext()).getPhotoEntry(e1
				.getId());

		assertEquals(1, e2.getId());
		assertEquals("mole on right hand", e2.getTag());
		assertEquals(d.toString(), e2.getTimeStamp());
		assertEquals(p1, e2.getFilePath());
	}
	
	public void testDeletePhotoEntry() {
		Date d = new Date();

		PhotoEntry e1 = new PhotoEntry();
		e1.setId(1);
		e1.setTag("mole on right hand");
		e1.setTimeStamp(d.toString());
		e1.setFilePath(p1);
		new SqlPhotoStorage(getContext()).insertPhotoEntry(e1);
		
		assertTrue(new SqlPhotoStorage(getContext()).deletePhotoEntry(e1.getId()));
	}
}
