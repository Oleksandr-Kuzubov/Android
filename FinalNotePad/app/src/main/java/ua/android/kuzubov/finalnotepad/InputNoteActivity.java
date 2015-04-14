package ua.android.kuzubov.finalnotepad;

import android.support.v4.app.Fragment;

import ua.android.kuzubov.finalnotepad.model.Note;

/**
 * Promotes the user to input a note.
 * 
 * @author Kuzia
 * 
 */
public class InputNoteActivity extends KuziaSingleFragmentActivity {
    public static final String CATEGORY_ID = "CategoryId";
    public static final String NOTE_FOR_EDIT = "NoteForEdit";

    @Override
    protected Fragment createFragment() {
        long categoryId = getIntent().getLongExtra(CATEGORY_ID, -1);
        Note note = (Note)getIntent().getParcelableExtra(NOTE_FOR_EDIT);
        if(categoryId != -1 && note == null){
            return InputNoteFragment.newInstance(categoryId);
        } else if(note != null && categoryId == -1){
            return InputNoteFragment.newInstance(note);
        } else {
            return new InputNoteFragment();
        }
    }
}
