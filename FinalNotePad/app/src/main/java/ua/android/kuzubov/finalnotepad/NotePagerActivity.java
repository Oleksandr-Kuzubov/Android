package ua.android.kuzubov.finalnotepad;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import ua.android.kuzubov.finalnotepad.model.DbHelper.NoteCursor;
import ua.android.kuzubov.finalnotepad.model.Note;

/**
 * Created by Kuzia on 22.01.2015.
 */
public class NotePagerActivity extends FragmentActivity {
    private final static String TAG = "NotePagerActivity";
    public final static String CURRENT_NOTE = "CURRENT_NOTE";
    private ViewPager mViewPager;
    private NoteCursor mNoteCursor;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);

        mNoteCursor = DataManager.get(this).queryAllNotes();
        Log.d(TAG, "Count of NoteCursor: " + mNoteCursor.getCount());


        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int i) {
                Note note = mNoteCursor.getNote();
                if(note != null){
                    Log.d(TAG, "Title note: " + note.getTitle() + ", id note: " + note.getId());
                    return NoteFragment.newInstance(note);
                } else {
                    Log.d(TAG, "Title note: " + note.getTitle() + ", id note: " + note.getId());
                    return new NoteFragment();
                }
            }

            @Override
            public int getCount() {
                return mNoteCursor.getCount();
            }
        });

        /*Note otherNote = (Note)getIntent().getParcelableExtra(CURRENT_NOTE);
        int counter = 0;
        while(counter < mCursor.getCount()){
            if(mCursor.getNote().getId() == otherNote.getId()){
                mViewPager.setCurrentItem(mCursor.getPosition());
                break;
            }
            counter++;
            mCursor.moveToNext();
        }*/
    }

    public class NotePagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mNoteCursor.getCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return false;
        }
    }
}
