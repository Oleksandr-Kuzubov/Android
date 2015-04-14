package ua.android.kuzubov.finalnotepad;

import android.content.Context;

import ua.android.kuzubov.finalnotepad.model.Category;
import ua.android.kuzubov.finalnotepad.model.DbHelper;
import ua.android.kuzubov.finalnotepad.model.DbHelper.CategoryCursor;
import ua.android.kuzubov.finalnotepad.model.DbHelper.NoteCursor;
import ua.android.kuzubov.finalnotepad.model.Note;

/**
 * Created by Kuzia on 18.01.2015.
 */
public class DataManager {

    private static DataManager sDataManager;
    private Context mContext;
    private DbHelper mHelper;

    private DataManager(Context appContext){
        mContext = appContext;
        mHelper = new DbHelper(mContext);
    }

    public static DataManager get(Context context){
        if(sDataManager == null){
            sDataManager = new DataManager(context);
        }
        return sDataManager;
    }

    public Category insertNewCategory(String theme){
        Category category = new Category(theme);
        category.setId(mHelper.insertCategory(category));
        return category;
    }

    public CategoryCursor queryCategories(){
        return mHelper.queryCategories();
    }

    public Category getCategory(long id){
        Category category = null;
        CategoryCursor cursor = mHelper.queryCategory(id);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            category = cursor.getCategory();
        }
        cursor.close();
        return category;
    }

    public void deleteCategory(long id){
        mHelper.deleteCategory(id);
    }

    public NoteCursor queryNotes(long id){
        return mHelper.queryNotes(id);
    }

    public NoteCursor queryAllNotes(){
        return mHelper.queryAllNotes();
    }

    public Note insertNewNote(String title, String content, String creationTime, long categoryId){
        Note note = new Note(title, content, creationTime, categoryId);
        note.setId(mHelper.insertNote(note));
        return note;
    }

    public void deleteNote(Note note){
        mHelper.deleteNote(note.getId(), note.getCategoryid());
    }

    public boolean updateNote(Note note){
        return mHelper.updateNote(note);
    }
}

