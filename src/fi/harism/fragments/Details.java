package fi.harism.fragments;

import java.io.Serializable;

public class Details implements Serializable {

	private static final long serialVersionUID = -8553876433947723956L;

	private String mAddress;
	private long mId;
	private String mName;
	private byte[] mPhoto;
	private int mPlanet1;
	private int mPlanet2;
	private int mPlanet3;

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

	public int getPlanet1() {
		return mPlanet1;
	}

	public int getPlanet2() {
		return mPlanet2;
	}

	public int getPlanet3() {
		return mPlanet3;
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

	public void setPlanet1(int index) {
		mPlanet1 = index;
	}

	public void setPlanet2(int index) {
		mPlanet2 = index;
	}

	public void setPlanet3(int index) {
		mPlanet3 = index;
	}

}
