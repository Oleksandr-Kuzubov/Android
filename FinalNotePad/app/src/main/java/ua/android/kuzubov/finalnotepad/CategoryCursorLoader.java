package ua.android.kuzubov.finalnotepad;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Kuzia on 22.01.2015.
 */
public class CategoryCursorLoader extends SQLCursorLoader {
    public CategoryCursorLoader(Context context) {
        super(context);
    }

    @Override
    protected Cursor loadCursor() {
        return DataManager.get(getContext()).queryCategories();
    }
}
