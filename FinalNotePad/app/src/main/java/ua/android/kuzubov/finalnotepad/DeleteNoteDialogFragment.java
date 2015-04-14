package ua.android.kuzubov.finalnotepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import ua.android.kuzubov.finalnotepad.model.Note;

/**
 * Created by Kuzia on 02.02.2015.
 */
public class DeleteNoteDialogFragment extends DialogFragment {
    private Note deleteCurrentNote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args.getParcelable("deleteNote") != null){
            deleteCurrentNote = args.getParcelable("deleteNote");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder theDialog = new AlertDialog.Builder(getActivity());

        theDialog.setTitle("Delete Note");
        theDialog.setMessage("You Wont Delete This Note?");
        theDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataManager.get(getActivity()).deleteNote(deleteCurrentNote);
                getActivity().finish();
                Toast.makeText(getActivity(), "Note deleted successful", Toast.LENGTH_SHORT).show();
            }
        });
        theDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return theDialog.create();
    }

    public static DeleteNoteDialogFragment newInstance(Note deleteNote){
        Bundle args = new Bundle();
        args.putParcelable("deleteNote", deleteNote);
        DeleteNoteDialogFragment fragment = new DeleteNoteDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
