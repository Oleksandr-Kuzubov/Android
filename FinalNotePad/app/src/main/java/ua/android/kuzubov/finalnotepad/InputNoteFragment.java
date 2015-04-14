package ua.android.kuzubov.finalnotepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import ua.android.kuzubov.finalnotepad.model.Note;

/**
 * Created by Kuzia on 20.01.2015.
 */
public class InputNoteFragment extends Fragment {
    private final static String TAG = "InputNoteFragment";
    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    private static final String EXTRA_NOTE_FOR_EDIT = "EXTRA_NOTE_FOR_EDIT";
    public static final String EXTRA_NOTE = "EXTRA_NOTE";

    private long mCurrentCategoryId;
    private EditText mTitleEditText;
    private EditText mContentEditText;
    private Button mAddNewNote;
    private String mTitle;
    private String mContent;
    private Note mEditNote;
    /*
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        Bundle args = getArguments();
        if(args.getLong(EXTRA_CATEGORY_ID) > 0){
            mCurrentCategoryId = args.getLong(EXTRA_CATEGORY_ID);
        }
        if(args.getParcelable(EXTRA_NOTE_FOR_EDIT) != null){
            mEditNote = args.getParcelable(EXTRA_NOTE_FOR_EDIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_note, container, false);

        mTitleEditText = (EditText)view.findViewById(R.id.activity_input_note_edit_text_title);
        mContentEditText = (EditText)view.findViewById(R.id.activity_input_note_edit_text_note_text);
        if(mEditNote != null){
            mTitleEditText.setText(mEditNote.getTitle());
            mContentEditText.setText(mEditNote.getContent());
        }

        mAddNewNote = (Button)view.findViewById(R.id.fragment_input_note_button);
        mAddNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note currentNote = makeNote();
                if(currentNote != null){
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_NOTE, currentNote);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
                Log.d(TAG, "Current note is null ");
            }
        });

        return view;
    }

    private Note makeNote() {
        mTitle = mTitleEditText.getText().toString();
        mContent = mContentEditText.getText().toString();
        Log.d(TAG, "mTitle: " + mTitle + " mContent: " + mContent);

        if (TextUtils.getTrimmedLength(mTitle) == 0) {
            Toast.makeText(getActivity(), R.string.message_no_title, Toast.LENGTH_SHORT)
                    .show();
            mTitleEditText.requestFocus();
            return null;
        }

        if (TextUtils.getTrimmedLength(mContent) == 0) {
            Toast.makeText(getActivity(), R.string.message_no_title, Toast.LENGTH_SHORT)
                    .show();
            mContentEditText.requestFocus();
            return null;
        }
        Log.d(TAG, "After chick trimmedLength");

        SimpleDateFormat dateStyle = new SimpleDateFormat("dd.MM.yyyy");
        String creationTime = dateStyle.format(new Date(System.currentTimeMillis())).toString();

        if(mEditNote != null){
            mEditNote.setTitle(mTitle);
            mEditNote.setContent(mContent);
            mEditNote.setCreationTime(creationTime);
            if(DataManager.get(getActivity()).updateNote(mEditNote)){
                Toast.makeText(getActivity(), "Note update is success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Note update is not success", Toast.LENGTH_SHORT).show();
            }
        } else if(mCurrentCategoryId != -1){
            mEditNote = DataManager.get(getActivity())
                    .insertNewNote(mTitle, mContent, creationTime, mCurrentCategoryId);
        }
        return mEditNote;
    }

    public static InputNoteFragment newInstance(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_NOTE_FOR_EDIT, note);
        InputNoteFragment fragment = new InputNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static InputNoteFragment newInstance(long id){
        Bundle args = new Bundle();
        args.putLong(EXTRA_CATEGORY_ID, id);
        InputNoteFragment fragment = new InputNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
