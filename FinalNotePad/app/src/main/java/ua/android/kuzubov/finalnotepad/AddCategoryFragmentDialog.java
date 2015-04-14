package ua.android.kuzubov.finalnotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ua.android.kuzubov.finalnotepad.model.Category;
import ua.android.kuzubov.finalnotepad.model.DbHelper;

/**
 * Created by Kuzia on 16.01.2015.
 */
public class AddCategoryFragmentDialog extends DialogFragment{
    public static final String EXTRA_CATEGORY = "ExtraCategory";

    private DataManager mDataManager;
    private View dialogView;
    private Category mCurrentCategory;
    private EditText mEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_input_category, null);
        mDataManager = DataManager.get(getActivity());

        mEditText = (EditText)dialogView.findViewById(R.id.dialog_edit_text_input_category);

        return new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                addResult(Activity.RESULT_OK);
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .create();

    }

    private void addResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        String entry = mEditText.getText().toString();
        if (TextUtils.getTrimmedLength(entry) == 0) {
            Toast.makeText(getActivity(), R.string.message_no_title, Toast.LENGTH_SHORT)
                    .show();
            mEditText.requestFocus();
        } else {
            mCurrentCategory = mDataManager.insertNewCategory(entry);
            Intent i = new Intent();
            i.putExtra(EXTRA_CATEGORY, mCurrentCategory);

            getTargetFragment()
                    .onActivityResult(getTargetRequestCode(), resultCode, i);
        }
    }
}
