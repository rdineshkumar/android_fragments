package fi.harism.fragments;

import java.io.Serializable;

public class Details implements Serializable {

	private static final long serialVersionUID = -8553876433947723956L;

	private String mAddress;
	private String mComments;
	private int mConditionKitchen;
	private int mConditionOverall;
	private int mConditionToilet;
	private long mId = -1;
	private String mName;
	private byte[] mPhoto;

	public String getAddress() {
		return mAddress;
	}

	public String getComments() {
		return mComments;
	}

	public int getConditionKitchen() {
		return mConditionKitchen;
	}

	public int getConditionOverall() {
		return mConditionOverall;
	}

	public int getConditionToilet() {
		return mConditionToilet;
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

	public void setComments(String comments) {
		mComments = comments;
	}

	public void setConditionKitchen(int index) {
		mConditionKitchen = index;
	}

	public void setConditionOverall(int index) {
		mConditionOverall = index;
	}

	public void setConditionToilet(int index) {
		mConditionToilet = index;
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
