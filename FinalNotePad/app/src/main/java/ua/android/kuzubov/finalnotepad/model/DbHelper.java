package ua.android.kuzubov.finalnotepad.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Wrapper;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "DbHelper";

	public DbHelper(Context context) {
		super(context, "MyNotes", null, 1);
	}

    private static final String TABLE_CATEGORY = "Category";
    private static final String COLUMN_CATEGORY_ID = "_id";
    private static final String COLUMN_CATEGORY_THEME = "name";

    private static final String TABLE_NOTE = "Note";
    private static final String COLUMN_NOTE_ID = "_id";
    private static final String COLUMN_NOTE_TITLE = "title";
    private static final String COLUMN_NOTE_CONTENT = "content";
    private static final String COLUMN_NOTE_TIMESTAMP = "creation_time";
    private static final String COLUMN_NOTE_CATEGORY_ID = "category_id";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table Category ("
				+ "_id  integer primary key autoincrement, "
				+ "name varchar(100))");
		db.execSQL("create table Note ("
				+ "_id         	 integer primary key autoincrement, "
				+ "title         varchar(100), "
				+ "content       varchar(100), "
				+ "creation_time varchar(100), "
				+ "category_id   integer references Category(_id))");
	}

    public static class CategoryCursor extends CursorWrapper{

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public CategoryCursor(Cursor cursor) {
            super(cursor);
        }

        public Category getCategory(){
            if(isBeforeFirst() || isAfterLast()) return null;

            String currentTheme = getString(getColumnIndex(COLUMN_CATEGORY_THEME));
            Category currentCategory = new Category(currentTheme);
            currentCategory.setId(getLong(getColumnIndex(COLUMN_CATEGORY_ID)));

            return currentCategory;
        }
    }

    public CategoryCursor queryCategories(){
        Cursor cursor = getWritableDatabase().query(TABLE_CATEGORY,
                null, null, null, null, null, COLUMN_CATEGORY_ID + " asc");
        return new CategoryCursor(cursor);
    }

    public CategoryCursor queryCategory(long id){
        Cursor cursor = getWritableDatabase().query(TABLE_CATEGORY,
                null,
                COLUMN_CATEGORY_ID + " = ?",
                new String[] { String.valueOf(id)},
                null,
                null,
                null,
                "1");
        return new CategoryCursor(cursor);
    }

    public long insertCategory(Category category){
        ContentValues content = new ContentValues();
        content.put(COLUMN_CATEGORY_THEME, category.getName());
        return getWritableDatabase().insert(TABLE_CATEGORY, null, content);
    }

    public void deleteCategory(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String as[] = new String[1];
        as[0] = String.valueOf(id);
        db.beginTransaction();
        db.delete("Note", "category_id = ?", as);
        db.delete("Category", "_id = ?", as);

        try {
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            db.endTransaction();
        } finally {
            db.endTransaction();
        }
    }

    public static class NoteCursor extends CursorWrapper {
        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public NoteCursor(Cursor cursor) {
            super(cursor);
        }

        public Note getNote(){
            if(isBeforeFirst() || isAfterLast()) return null;

            String currentTitle = getString(getColumnIndex(COLUMN_NOTE_TITLE));
            String currentContent = getString(getColumnIndex(COLUMN_NOTE_CONTENT));
            String currentTime = getString(getColumnIndex(COLUMN_NOTE_TIMESTAMP));
            long currentCategoryId = getLong(getColumnIndex(COLUMN_NOTE_CATEGORY_ID));

            Note currentNote = new Note(currentTitle, currentContent, currentTime, currentCategoryId);
            long currentNoteId = getLong(getColumnIndex(COLUMN_NOTE_ID));
            currentNote.setId(currentNoteId);
            return currentNote;
        }
    }

    public NoteCursor queryNotes(long categoryId){
        Cursor cursor = getWritableDatabase().query(TABLE_NOTE,
                null,
                COLUMN_NOTE_CATEGORY_ID + " = ?",
                new String[]{ String.valueOf(categoryId)},
                null,
                null,
                COLUMN_NOTE_CATEGORY_ID + " asc");
        return new NoteCursor(cursor);
    }

    public NoteCursor queryAllNotes(){
        Cursor cursor = getWritableDatabase().query(TABLE_NOTE,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NOTE_CATEGORY_ID + " asc");
        return new NoteCursor(cursor);
    }

    public NoteCursor queryNote(long noteId, long categoryId){
        Cursor cursor = getWritableDatabase().query(TABLE_CATEGORY,
                null,
                COLUMN_NOTE_ID + " = ? AND " + COLUMN_CATEGORY_ID + " = ?",
                new String[] { String.valueOf(noteId), String.valueOf(categoryId)},
                null,
                null,
                null,
                "1");
        return new NoteCursor(cursor);
    }

    public long insertNote(Note note) {
        ContentValues content = new ContentValues();
        content.put(COLUMN_NOTE_TITLE, note.getTitle());
        content.put(COLUMN_NOTE_CONTENT, note.getContent());
        content.put(COLUMN_NOTE_TIMESTAMP, note.getCreatioTime());
        content.put(COLUMN_NOTE_CATEGORY_ID, note.getCategoryid());
        return getWritableDatabase().insert(TABLE_NOTE, null, content);
    }

    public void deleteNote(long noteId, long categoryId) {
        SQLiteDatabase db = getWritableDatabase();
        String as[] = new String[2];
        as[0] = String.valueOf(noteId);
        as[1] = String.valueOf(categoryId);
        db.beginTransaction();
        db.delete("Note", "_id = ? AND category_id = ?", as);

        try {
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            db.endTransaction();
        } finally {
            db.endTransaction();
        }
    }

    public boolean updateNote(Note note) {
        ContentValues content = new ContentValues();
        content.put(COLUMN_NOTE_TITLE, note.getTitle());
        content.put(COLUMN_NOTE_CONTENT, note.getContent());
        content.put(COLUMN_NOTE_TIMESTAMP, note.getCreatioTime());
        content.put(COLUMN_NOTE_CATEGORY_ID, note.getCategoryid());

        String as[] = new String[2];
        as[0] = String.valueOf(note.getId());
        as[1] = String.valueOf(note.getCategoryid());
        return getWritableDatabase().update("note", content, "_id = ? AND category_id = ?", as) == 1;
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS category");
		db.execSQL("DROP TABLE IF EXISTS note");
		onCreate(db);
	}

}