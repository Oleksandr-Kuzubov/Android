package ua.android.kuzubov.finalnotepad;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import ua.android.kuzubov.finalnotepad.model.Category;
import ua.android.kuzubov.finalnotepad.model.DbHelper.CategoryCursor;
import ua.android.kuzubov.finalnotepad.model.DbHelper.NoteCursor;
import ua.android.kuzubov.finalnotepad.model.Note;

/**
 * Created by Kuzia on 16.01.2015.
 */
public class FinalNotePadFragment extends Fragment {
    private static final String TAG = "FinalNotePadFragment";

    private static final int REQUEST_CATEGORY_THEME = 0;
    private static final int REQUEST_NEW_NOTE = 1;
    private static final int REQUEST_EXIST_NOTE = 2;

    private static final int CATEGORY_LOADER = 10;
    private static final int NOTE_LOADER = 11;

    private static final String DIALOG_NEWCATEGORY = "category";
    private static final String CATEGORY_ID = "Category Id";

    private ExpandableListView mListView;
    private DataManager mDataManager;
    private CategoryCursor mCategoryCursor;
    private NoteCursor mNoteCursor;
    private FinalNotePadAdapter mAdapter;
    private boolean actionModeEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mDataManager = DataManager.get(getActivity());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_final_note_pad, container, false);
        mListView = (ExpandableListView)view.findViewById(R.id.fragment_expandable_list_view);
        mCategoryCursor = mDataManager.queryCategories();

        mAdapter = new FinalNotePadAdapter(getActivity(), mCategoryCursor);
        mListView.setAdapter(mAdapter);

        mListView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,long id,
                                                                                boolean checked) {
                if(mListView.getCheckedItemCount() == 1)
                    mode.setSubtitle("1 item selected");
                else
                    mode.setSubtitle(mListView.getCheckedItemCount() + " items selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_category_item_context, menu);
                actionModeEnabled = true;
                mode.setTitle("Selected Items");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.menu_category_item_delete:
                        if (mListView.getCheckedItemCount() > 0) {
                            SparseBooleanArray checkedPositions = mListView.getCheckedItemPositions();
                            for (int i = 0; i < checkedPositions.size(); i++) {
                                int position = checkedPositions.keyAt(i);
                                if(mListView.isItemChecked(position)){
                                    mDataManager.deleteCategory(mAdapter.getGroupId(position));
                                }
                            }
                        }
                        mode.finish();
                        mCategoryCursor.requery();
                        mAdapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionModeEnabled = false;
            }
        });

        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                                                                                        long id) {
                if(actionModeEnabled){
                    parent.setItemChecked(groupPosition, !parent.isItemChecked(groupPosition));
                }
                return actionModeEnabled;
            }
        });

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                                                    int childPosition, long id) {

                Note note = mNoteCursor.getNote();
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                intent.putExtra(NoteActivity.CURRENT_NOTE, note);
                startActivityForResult(intent, REQUEST_EXIST_NOTE);
                return true;
            }
        });

        return mListView;
    }

    class FinalNotePadAdapter extends SimpleCursorTreeAdapter {
        private CategoryCursor mCurrentCursor;

        public FinalNotePadAdapter(Context context, CategoryCursor cursor) {
            super(context,
                    cursor,
                    R.layout.group,
                    new String[]{"name"},
                    new int[]{R.id.layout_group_text_category},
                    R.layout.child,
                    new String[]{"title", "content", "creation_time"},
                    new int[]{ R.id.layout_note_text_title,
                               R.id.layout_note_text_content,
                               R.id.layout_note_text_creation_time});
            mCurrentCursor = cursor;
        }

        @Override
        protected Cursor getChildrenCursor(Cursor cursor) {
            long id = cursor.getLong(cursor.getColumnIndex("_id"));
            mNoteCursor = mDataManager.queryNotes(id);
            return mNoteCursor;
        }

        @Override
        public View newGroupView(Context context, Cursor cursor, boolean isExpanded,
                                                                                ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.group, parent, false);

            return view;
        }

        @Override
        protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
            final Category category = mCurrentCursor.getCategory();

            TextView categoryTheme = (TextView)view
                    .findViewById(R.id.layout_group_text_category);
            categoryTheme.setText(category.getName());

            ImageView addNoteView = (ImageView)view.findViewById(R.id.fragment_group_image_view_add);
            addNoteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), InputNoteActivity.class);
                    intent.putExtra(InputNoteActivity.CATEGORY_ID, category.getId());
                    startActivityForResult(intent, REQUEST_NEW_NOTE);
                }
            });
        }

        @Override
        public View newChildView(Context context, Cursor cursor,
                                 boolean isLastChild, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.child, parent, false);
        }

        @Override
        protected void bindChildView(View view, Context context,
                                     Cursor cursor, boolean isLastChild) {
            Note note = ((NoteCursor)cursor).getNote();

            TextView mTitle = (TextView)view
                    .findViewById(R.id.layout_note_text_title);
            mTitle.setText(note.getTitle());

            TextView mContent = (TextView)view
                    .findViewById(R.id.layout_note_text_content);
            mContent.setText(note.getContent());

            TextView mCreationTime = (TextView)view
                    .findViewById(R.id.layout_note_text_creation_time);
            mCreationTime.setText(note.getCreatioTime());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_note_pad, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_note_pad_add_category:
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                AddCategoryFragmentDialog dialog = new AddCategoryFragmentDialog();
                dialog.setTargetFragment(FinalNotePadFragment.this, REQUEST_CATEGORY_THEME);
                dialog.show(fm, DIALOG_NEWCATEGORY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_CATEGORY_THEME){
            mCategoryCursor.requery();
            updateUI();
        } else if((requestCode == REQUEST_NEW_NOTE && resultCode == Activity.RESULT_OK)
                || requestCode == REQUEST_EXIST_NOTE){
            mNoteCursor.requery();
            updateUI();
        }
    }

    private void updateUI(){
        if(mCategoryCursor.getCount() < 1){
            if(mAdapter != null) mAdapter = null;
        } else {
            if(mAdapter == null){
                mAdapter = new FinalNotePadAdapter(getActivity(), mCategoryCursor);
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        mCategoryCursor.close();
        super.onDestroyView();
    }

    /*private class CategoryCursorLoaderCallbacks implements LoaderCallbacks<Cursor>{

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CategoryCursorLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            if(mAdapter == null){
                mAdapter = new FinalNotePadAdapter(getActivity(), (CategoryCursor)cursor);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mAdapter.setGroupCursor(null);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class NoteCursorLoaderCallbacks implements LoaderCallbacks<Cursor>{

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new NoteCursorLoader(getActivity(), mCurrentCategoryId);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            mNoteCursor = (NoteCursor)cursor;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mNoteCursor = null;
        }
    }*/
}
