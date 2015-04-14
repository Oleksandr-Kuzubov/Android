package ua.android.kuzubov.finalnotepad;

import android.support.v4.app.Fragment;

import ua.android.kuzubov.finalnotepad.model.Note;

/**
 * Created by Kuzia on 20.01.2015.
 */
public class NoteActivity extends KuziaSingleFragmentActivity{
    public final static String CURRENT_NOTE = "CURRENT_NOTE";

    @Override
    protected Fragment createFragment() {
        Note note = (Note)getIntent().getParcelableExtra(CURRENT_NOTE);
        if(note != null){
            return NoteFragment.newInstance(note);
        } else {
            return new NoteFragment();
        }
    }
}
