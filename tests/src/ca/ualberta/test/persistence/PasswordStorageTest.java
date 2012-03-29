package ca.ualberta.test.persistence;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;

import android.content.Context;
import android.test.AndroidTestCase;
import ca.ualberta.persistence.PasswordStorage;


public class PasswordStorageTest extends AndroidTestCase {

	private final static String FILE_NAME = "test_pwd";
	
	public void setUp() throws Exception {
		FileDescriptor output = getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE).getFD();
		
		PasswordStorage storage = new PasswordStorage(output);
		storage.dispose();
	}

	public void testUpdatePassword() throws Exception {
		String newPassword = "star1234";
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(newPassword.getBytes());
		byte[] hashNewPassword = digest.digest();
		
		PasswordStorage storage = new PasswordStorage(getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE).getFD());
		storage.updatePassword(newPassword);
		
		byte[] hashFilePassword = new byte[hashNewPassword.length];
		FileInputStream input = getContext().openFileInput(FILE_NAME);
		input.read(hashFilePassword);
		
		for (int i = 0; i < hashNewPassword.length; i++) {
			assertEquals(hashNewPassword[i], hashFilePassword[i]);
		}
		
		storage.dispose();
		input.close();
	}
	
	public void testVerifyPassword() throws Exception {
		String password = "star12345";
		FileDescriptor fd = getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE).getFD();
		
		FileOutputStream outputStream = new FileOutputStream(fd);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(password.getBytes());
		byte[] hashPassword = digest.digest();
		
		outputStream.write(hashPassword);
		
		PasswordStorage storage = new PasswordStorage(fd);
		
		assert(storage.verifyPassword(password));
	}
}
