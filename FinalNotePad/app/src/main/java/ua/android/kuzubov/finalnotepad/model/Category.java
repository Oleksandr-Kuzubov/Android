package ua.android.kuzubov.finalnotepad.model;

import android.os.Parcel;
import android.text.TextUtils;

public class Category extends Entity {
	private String name;

	public Category(String name) {
		super(-1);
		this.name = name;
		check();
	}

	private Category(Parcel source) {
		super(source);
		name = source.readString();
	}

	private void check() {
		if (TextUtils.isEmpty(name.trim())) {
			throw new IllegalStateException("no category name");
		}
	}

	public String getName() {
		return name;
	}

    public void setName(String name) {
        this.name = name;
    }

    @Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(name);
	}

	public static final Creator<Category> CREATOR = new Creator<Category>() {

		@Override
		public Category createFromParcel(Parcel source) {
			return new Category(source);
		}

		@Override
		public Category[] newArray(int size) {
			return new Category[size];
		}
	};
}
