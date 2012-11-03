package fi.harism.fragments;

public class Details {

	private String mAddress;
	private long mId;
	private String mName;

	public String getAddress() {
		return mAddress;
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

	public void setId(long id) {
		mId = id;
	}

	public void setName(String name) {
		mName = name;
	}

	@Override
	public String toString() {
		return mName + "\n" + mAddress;
	}

}
