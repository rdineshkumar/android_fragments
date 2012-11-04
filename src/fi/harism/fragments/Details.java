package fi.harism.fragments;

public class Details {

	private String mAddress;
	private String mComments;
	private int mCondKitchen;
	private int mCondOverall;
	private int mCondToilet;
	private long mId = -1;
	private String mName;

	public String getAddress() {
		return mAddress;
	}

	public String getComments() {
		return mComments;
	}

	public int getCondKitchen() {
		return mCondKitchen;
	}

	public int getCondOverall() {
		return mCondOverall;
	}

	public int getCondToilet() {
		return mCondToilet;
	}

	public long getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public void setAddress(String address) {
		mAddress = address;
	}

	public void setComments(String comments) {
		mComments = comments;
	}

	public void setCondKitchen(int index) {
		mCondKitchen = index;
	}

	public void setCondOverall(int index) {
		mCondOverall = index;
	}

	public void setCondToilet(int index) {
		mCondToilet = index;
	}

	public void setId(long id) {
		mId = id;
	}

	public void setName(String name) {
		mName = name;
	}

}
