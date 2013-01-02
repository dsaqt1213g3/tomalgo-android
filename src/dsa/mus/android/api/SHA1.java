package dsa.mus.android.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SHA1 {
	private static MessageDigest md = null;
	private static SHA1 instance = null;

	private SHA1() {
		super();
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final static SHA1 getInstance() {
		if (instance == null)
			instance = new SHA1();
		return instance;
	}

	public final String digestToString(String toHash) {
		return Util.toHexString(md.digest(toHash.getBytes()));
	}

	public final byte[] digestToByteArray(String toHash) {
		return md.digest(toHash.getBytes());
	}
}
