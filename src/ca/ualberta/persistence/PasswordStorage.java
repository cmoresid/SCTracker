package ca.ualberta.persistence;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;


public class PasswordStorage {
	
	/** Name of the file where the password is stored. */
	public static final String PASSWORD_FILE = "sc.pwd";
	
	/** Allow password to be written to disk. */
	private FileOutputStream mPasswordOutputStream;
	/** Allow password to be read from disk. */
	private FileInputStream mPasswordInputStream;
	/** Describes where to find the file in the sand box. */
	private FileDescriptor mFilePath;
	/** Used to calculate hash of password. */
	private MessageDigest mDigest;
	/** Hashing algorithm to use. */
	private String hashAlgorithm = "SHA-256";
	
	/**
	 * Initializes the hashing component {@code MessageDigest}. The
	 * algorithm that is used is dependent on the member variable
	 * {@code hashAlgorithm}. The method itself does not throw an
	 * exception, however the {@code NoSuchAlgorithmException} is
	 * handled internally if for some reason the algorithm specified
	 * in the {@code hashAlgorithm} member variable does not exist.
	 */
	public void initDigest() {
		try {
			mDigest = MessageDigest.getInstance(hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes a new instance of {@code PasswordStorage} with the
	 * specified {@code FileDescriptor}. The {@code FileDescriptor} will
	 * normally be passed from the controller, which will be obtained from
	 * a {@code Context} object and a call to 
	 * {@link Context#openFileOutput(String, int)#getFD().
	 * 
	 * @param fd
	 * 		The file descriptor describing where to open the file
	 * 		from.
	 * @throws IOException
	 */
	public PasswordStorage(FileDescriptor fd) throws IOException {
		mFilePath = fd;
		initDigest();
	}
	
	private void createPasswordInputStream(FileDescriptor passwordFD) throws IOException {
		mPasswordInputStream = new FileInputStream(passwordFD);
	}
	
	private void createPasswordOutputStream(FileDescriptor passwordFD) throws IOException {
		mPasswordOutputStream = new FileOutputStream(passwordFD);
	}
	
	public void updatePassword(String newPassword) throws IOException {
		if (mPasswordOutputStream == null) {
			createPasswordOutputStream(mFilePath);
		}
		
		mPasswordOutputStream.write(this.hashValue(newPassword));
		mPasswordOutputStream.close();
	}
	
	public byte[] hashValue(String value) {
		byte[] valueBytes = value.getBytes();
		mDigest.update(valueBytes);
		
		return mDigest.digest();
	}
	
	public void dispose() throws IOException {
		if (mPasswordOutputStream != null) {
			mPasswordOutputStream.close();
		}
		
		if (mPasswordInputStream != null) {
			mPasswordInputStream.close();
		}
	}
	
	public boolean verifyPassword(String password) throws IOException {
		boolean passwordValidated = true;
		int i = 0;
		
		if (mPasswordInputStream == null) {
			createPasswordInputStream(mFilePath);
		}
		
		byte[] hashPassword = this.hashValue(password);
		byte[] hashFilePassword = new byte[hashPassword.length];
		
		mPasswordInputStream.read(hashFilePassword);
		
		while (passwordValidated && i < hashPassword.length) {
			if (hashPassword[i] != hashFilePassword[i]) {
				passwordValidated = false;
			}
			
			i++;
		}
		
		return passwordValidated;
	}
}
