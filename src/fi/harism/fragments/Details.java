/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package fi.harism.fragments;

/**
 * Details container class.
 */
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
