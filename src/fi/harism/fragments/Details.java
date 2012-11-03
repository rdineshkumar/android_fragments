package fi.harism.fragments;

import java.io.Serializable;

public class Details implements Serializable {

	private static final long serialVersionUID = -8553876433947723956L;

	private String mAddress;
	private long mId;
	private String mName;
	private byte[] mPhoto;

	public String getAddress() {
		return mAddress;
	}

	public long getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public byte[] getPhoto() {
		return mPhoto;
	}

	public void setAddress(String address) {
		mAddress = address;
	}

	public void setId(long id) {
		mId = id;
	}

	public void setName(String name) {
		mName = name;
	}

	public void setPhoto(byte[] photo) {
		mPhoto = photo;
	}

}
