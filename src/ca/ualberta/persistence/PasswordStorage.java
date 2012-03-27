package ca.ualberta.persistence;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PasswordStorage {
	
	public static final String PASSWORD_FILE = "sc.pwd";
	
	private FileOutputStream mPasswordOutputStream;
	private FileInputStream mPasswordInputStream;
	private FileDescriptor mFilePath;
	private MessageDigest mDigest;
	private String hashAlgorithm = "SHA-256";
	
	public void initDigest() {
		try {
			mDigest = MessageDigest.getInstance(hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
