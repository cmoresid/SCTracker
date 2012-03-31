package ca.ualberta.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;

/**
 * Manages the user's password. In charge of
 * the storage and retrieval of a hashed version
 * of the password. Right now, the password storage
 * mechanism is not very secure. All it does right
 * now is hash the password and store it. Weak against
 * dictionary attacks. Should probably use a salt as
 * well... 
 */
public class PasswordStorage {
	
	/** Name of the file where the password is stored. */
	public static final String PASSWORD_FILE = "sc.pwd";
	
	/** Allow password to be written to disk. */
	private FileOutputStream mPasswordOutputStream;
	/** Allow password to be read from disk. */
	private FileInputStream mPasswordInputStream;
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
	public PasswordStorage() throws IOException {
		initDigest();
	}
	
	/**
	 * Initializes a new instance of {@code PasswordStorage} with the
	 * specified {@code FileInputStream}. The {@code FileInputStream} will
	 * normally be passed from the controller, which will be obtained from
	 * a {@code Context} object and a call to 
	 * {@link Context#openFileOutput(String, int)}.
	 * 
	 * @param stream
	 * 		Input stream that will contain a reference to the password
	 * 		file.
	 * @throws IOException
	 */
	public PasswordStorage(FileInputStream stream) {
		initDigest();
		mPasswordInputStream = stream;
	}
	
	/**
	 * Initializes a new instance of {@code PasswordStorage} with the
	 * specified {@code FileOutputStream}. The {@code FileOutputStream} will
	 * normally be passed from the controller, which will be obtained from
	 * a {@code Context} object and a call to 
	 * {@link Context#openFileOutput(String, int)}.
	 * 
	 * @param stream
	 * 		Output stream that will contain a reference to the password
	 * 		file.
	 * @throws IOException
	 */
	public PasswordStorage(FileOutputStream stream) {
		initDigest();
		mPasswordOutputStream = stream;
	}
	
	/**
	 * Instantiates the mPasswordInputStream with the given
	 * input stream. Used in conjunction with the empty
	 * construct.
	 * 
	 * @param passwordFD
	 * 		The {@code FileDescriptor} used to instantiate the
	 * 		{@code FileInputStream}.
	 * @throws IOException
	 */
	public void setPasswordInputStream(FileInputStream stream) throws IOException {
		mPasswordInputStream = stream;
	}
	
	/**
	 * Instantiates the mPasswordOutputStream variable from the
	 * given {@code FileDescriptor}.
	 * 
	 * @param passwordFD
	 * 		The {@code FileDescriptor} used to instantiate the
	 * 		{@code FileOutputStream}.
	 * @throws IOException
	 */
	public void setPasswordOutputStream(FileOutputStream stream) throws IOException {
		mPasswordOutputStream = stream;
	}
	
	/**
	 * Hashes the given password and writes it to the 
	 * password file.
	 * 
	 * @param newPassword
	 * 		The password to be hashed and stored.
	 * 
	 * @throws IOException
	 */
	public void updatePassword(String newPassword) throws IOException {
		// Lazy initialization of the output stream.
		if (mPasswordOutputStream == null) {
			throw new IOException("Password output stream not initialized.");
		}
		
		mPasswordOutputStream.write(this.hashValue(newPassword));
		mPasswordOutputStream.close();
	}
	
	/**
	 * Retrieves the hash value of the given string, based
	 * on the chosen hashing algorithm.
	 * 
	 * @param value
	 * 		The string to hash.
	 * @return
	 * 		A byte array containing the hash based on the
	 * 		given string.
	 */
	public byte[] hashValue(String value) {
		// Convert string to bytes.
		byte[] valueBytes = value.getBytes();
		mDigest.update(valueBytes);
		
		return mDigest.digest();
	}
	
	/**
	 * Releases all the IO streams held by
	 * password storage.
	 * 
	 * @throws IOException
	 */
	public void dispose() throws IOException {
		if (mPasswordOutputStream != null) {
			mPasswordOutputStream.close();
		}
		
		if (mPasswordInputStream != null) {
			mPasswordInputStream.close();
		}
	}
	
	/**
	 * Verifies the hash of the given password and the hash stored
	 * in the password file match.
	 * 
	 * @param password
	 * 		The password to verify against the password in
	 * 		the password file.		
	 * 
	 * @return
	 * 		Whether or not the hashes match.
	 * @throws IOException
	 */
	public boolean verifyPassword(String password) throws IOException {
		// Assume password is validated unless proven
		// otherwise.
		boolean passwordValidated = true;
		
		if (mPasswordInputStream == null) {
			throw new IOException("Password input stream not initialized.");
		}
		
		// The hashed value of given password.
		byte[] hashPassword = this.hashValue(password);
		// The hashed password in the password file.
		// Note: Any SHA-256 checksum has the same length.
		byte[] hashFilePassword = new byte[hashPassword.length];
		mPasswordInputStream.read(hashFilePassword);
		
		// Ensure hashes match.
		int i = 0;
		while (passwordValidated && i < hashPassword.length) {
			if (hashPassword[i] != hashFilePassword[i]) {
				passwordValidated = false;
			}
			
			i++;
		}
		
		return passwordValidated;
	}
}
