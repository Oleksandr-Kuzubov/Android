package ua.android.kuzubov.finalnotepad;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.android.kuzubov.finalnotepad.model.Category;
import ua.android.kuzubov.finalnotepad.model.Note;

/**
 * Created by Kuzia on 20.01.2015.
 */
public class NoteFragment extends Fragment {
    private final static String PARCEL_NOTE = "ParcelNote";
    private final static int REQUEST_EDITED_NOTE = 3;
    private static final int SETTINGS_INFO_TITLE = 4;
    private static final int SETTINGS_INFO_CONTENT = 5;

    private Note mNote;
    private TextView mTitle;
    private TextView mContent;
    private TextView mTimeStamp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        Bundle args = getArguments();
        if(args.getParcelable(PARCEL_NOTE) != null){
            mNote = args.getParcelable(PARCEL_NOTE);
        }
        Category category = DataManager.get(getActivity()).getCategory(mNote.getCategoryid());
        if(category != null){
            getActivity().getActionBar().setTitle(category.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mTitle = (TextView)view
                .findViewById(R.id.fragment_note_title_text_view);
        mTitle.setText(mNote.getTitle());
        mTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_INFO_TITLE);
                return true;
            }
        });

        mContent = (TextView)view
                .findViewById(R.id.fragment_note_content_text_view);
        mContent.setText(mNote.getContent());
        mContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_INFO_CONTENT);
                return true;
            }
        });

        mTimeStamp = (TextView)view
                .findViewById(R.id.fragment_note_timestamp_text_view);
        mTimeStamp.setText(mNote.getCreatioTime());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_fragment_note_delete:
                DeleteNoteDialogFragment fragment = DeleteNoteDialogFragment.newInstance(mNote);
                fragment.show(getFragmentManager(), "theDialog");
                return true;
            case R.id.menu_fragment_note_edit:
                Intent intent = new Intent(getActivity(), InputNoteActivity.class);
                intent.putExtra(InputNoteActivity.NOTE_FOR_EDIT, mNote);
                startActivityForResult(intent, REQUEST_EDITED_NOTE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_EDITED_NOTE && resultCode == Activity.RESULT_OK){
            Note note = (Note)data.getParcelableExtra(InputNoteFragment.EXTRA_NOTE);
            updateUI(note);
        }
        if(requestCode == SETTINGS_INFO_TITLE){
            updateText(mTitle);
        }
        if(requestCode == SETTINGS_INFO_CONTENT){
            updateText(mContent);
        }
    }

    private void updateText(TextView editTextView) {
        SharedPreferences sharedPreferences = PreferenceManager
                                                .getDefaultSharedPreferences(getActivity());
        if(sharedPreferences.getBoolean("text_bold_pref", false)){
            editTextView.setTypeface(null, Typeface.BOLD);
        } else {
            editTextView.setTypeface(null, Typeface.NORMAL);
        }

        String textSize = sharedPreferences.getString("text_size_pref", "16");
        float textSizeFloat = Float.parseFloat(textSize);
        editTextView.setTextSize(textSizeFloat);
    }

    private void updateUI(Note note) {
        mTitle.setText(note.getTitle());
        mContent.setText(note.getContent());
        mTimeStamp.setText(note.getCreatioTime());
    }

    public static Fragment newInstance(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(PARCEL_NOTE, note);
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
