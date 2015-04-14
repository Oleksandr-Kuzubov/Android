package ua.android.kuzubov.finalnotepad.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Entity implements Parcelable {
	private long id;

	public Entity(long id) {
		this.id = id;
	}

	public Entity(Parcel source) {
		this.id = source.readLong();
	}

	public long getId() {
		return id;
	}

    public void setId(long id) {
        this.id = id;
    }

    @Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (id == -1) {
			throw new IllegalStateException();
		}

		return o != null && getClass() == o.getClass() && id == ((Entity) o).id;
	}

	@Override
	public final int hashCode() {
		return (int) id;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
	}
}
