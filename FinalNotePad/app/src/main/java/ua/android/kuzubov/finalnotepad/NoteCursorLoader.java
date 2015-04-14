package ua.android.kuzubov.finalnotepad;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Kuzia on 22.01.2015.
 */
public class NoteCursorLoader extends SQLCursorLoader {
    private long mCategoryId;

    public NoteCursorLoader(Context context, long categoryId) {
        super(context);
        mCategoryId = categoryId;
    }

    @Override
    protected Cursor loadCursor() {
        return DataManager.get(getContext()).queryNotes(mCategoryId);
    }
}
