package ua.android.kuzubov.finalnotepad.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Note extends Entity {
	private String title;
	private String content;
	private String creationTime;
	private long categoryId;

	public Note(String title, String content, String creationTime, long categoryId) {
		super(-1);
		this.title = title;
		this.content = content;
		this.creationTime = creationTime;
		this.categoryId = categoryId;
		check();
	}

	public Note(Parcel source) {
		super(source);
		this.title = source.readString();
		this.content = source.readString();
		this.creationTime = source.readString();
		this.categoryId = source.readLong();
	}

	private void check() {
		if (TextUtils.isEmpty(title.trim())) {
			throw new IllegalStateException("no note title");
		}
		if (TextUtils.isEmpty(content.trim())) {
			throw new IllegalStateException("no note content");
		}
	}

	public String getContent() {
		return content;
	}

	public String getCreatioTime() {
		return creationTime;
	}

	public String getTitle() {
		return title;
	}

	public long getCategoryid() {
		return categoryId;
	}

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(creationTime);
		dest.writeLong(categoryId);
	}

	public static Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

		@Override
		public Note createFromParcel(Parcel source) {
			return new Note(source);
		}

		@Override
		public Note[] newArray(int size) {
			return new Note[size];
		}
	};

}
