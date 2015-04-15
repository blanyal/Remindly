/*
 * Copyright 2015 Blanyal D'souza.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */





package com.blanyal.remindme;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainActivity extends ActionBarActivity {
    private RecyclerView mList;
    private SimpleAdapter mAdapter;
    private Toolbar mToolbar;
    private FloatingActionButton mAddReminderButton;
    private MultiSelector mMultiSelector = new MultiSelector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAddReminderButton = (FloatingActionButton) findViewById(R.id.add_reminder);
        mList = (RecyclerView) findViewById(R.id.reminder_list);


        mList.setLayoutManager(getLayoutManager());
        registerForContextMenu(mList);

        mAdapter = new SimpleAdapter();
        mAdapter.setItemCount(getDefaultItemCount());
        mList.setAdapter(mAdapter);


        mToolbar.setTitle(R.string.app_name);

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddReminderActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
    }


    private android.support.v7.view.ActionMode.Callback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.discard_reminder:

                    actionMode.finish();

                    mMultiSelector.clearSelections();

                    Toast.makeText(getApplicationContext(),
                            "Deleted",
                            Toast.LENGTH_SHORT).show();


                    return true;


                case R.id.save_reminder:


                    actionMode.finish();
                    mMultiSelector.clearSelections();

                    //mAdapter.setItemCount(getDefaultItemCount());

                    return true;

                default:
                    break;
            }
            return false;
        }


    };


    @Override
    public void onResume(){
        super.onResume();
        mAdapter.setItemCount(getDefaultItemCount());
    }


    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }


    protected int getDefaultItemCount() {
        return 100;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VerticalItemHolder> {

        private ArrayList<ReminderItem> mItems;
        private SparseBooleanArray selectedItems;
        private AdapterView.OnItemClickListener mOnItemClickListener;


        public SimpleAdapter() {
            mItems = new ArrayList<ReminderItem>();
            selectedItems = new SparseBooleanArray();
        }


        public void setItemCount(int count) {
            mItems.clear();
            mItems.addAll(generateDummyData(count));
            notifyDataSetChanged();
        }


        public void onDeleteItem(int count) {
            mItems.clear();
            mItems.addAll(generateDummyData(count));
        }


        public void addItem() {
            mItems.add(1, generateDummyItem());
            notifyItemInserted(1);
        }


        public void removeItem() {
            if (mItems.isEmpty()) return;
            mItems.remove(0);
            notifyItemRemoved(0);
        }


        public void removeItemSelected(int selected) {
            if (mItems.isEmpty()) return;
            mItems.remove(selected);
            notifyItemRemoved(selected);
        }


        @Override
        public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View root = inflater.inflate(R.layout.recycle_item, container, false);

            return new VerticalItemHolder(root, this);
        }

        @Override
        public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
            ReminderItem item = mItems.get(position);

            itemHolder.setReminderTitle(item.mTitle);
            itemHolder.setReminderDateTime(item.mDate, item.mTime);
            itemHolder.setReminderRepeatInfo(item.mRepeat, item.mRepeatNo, item.mRepeatType);
            itemHolder.setActiveImage(item.mActive);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }


        public  class ReminderItem {

            public String mTitle;
            public String mDate;
            public String mTime;
            public String mRepeat;
            public String mRepeatNo;
            public String mRepeatType;
            public String mActive;


            public ReminderItem(String Title, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active) {

                this.mTitle = Title;
                this.mDate = Date;
                this.mTime = Time;
                this.mRepeat = Repeat;
                this.mRepeatNo = RepeatNo;
                this.mRepeatType = RepeatType;
                this.mActive = Active;
            }
        }


        public  class VerticalItemHolder extends SwappingHolder
                implements View.OnClickListener, View.OnLongClickListener {

            private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;
            private ImageView mActiveImage , mThumbnailImage;
            private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
            private TextDrawable mDrawableBuilder;
            private SimpleAdapter mAdapter;


            public VerticalItemHolder(View itemView, SimpleAdapter adapter) {

                super(itemView, mMultiSelector);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                itemView.setLongClickable(true);

                mAdapter = adapter;

                mTitleText = (TextView) itemView.findViewById(R.id.recycle_title);
                mDateAndTimeText = (TextView) itemView.findViewById(R.id.recycle_date_time);
                mRepeatInfoText = (TextView) itemView.findViewById(R.id.recycle_repeat_info);
                mActiveImage = (ImageView) itemView.findViewById(R.id.active_image);
                mThumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            }

            @Override
            public void onClick(View v) {

                if (!mMultiSelector.tapSelection(this)) {
                    //selectCrime(mCrime);

                    mTempPost = mList.getChildPosition(v);


                    //Log.d("LOG", "Position " + mTempPost);

                    int mSubjectClickID = IDmap.get(mTempPost);

                    //Log.d("LOG", "ID " + mSubjectClickID);


                    selectSubject(mSubjectClickID);


                }else if(mMultiSelector.getSelectedPositions().isEmpty()){
                    //Log.d("LOG","CLEAR!!!!!!!!!!! ");
                    mAdapter.setItemCount(getDefaultItemCount());
                }
            }


            @Override
            public boolean onLongClick(View v) {
                ActionBarActivity activity = (ActionBarActivity)getApplicationContext();
                activity.startSupportActionMode(mDeleteMode);
                mMultiSelector.setSelected(this, true);
                return true;
            }


            public void setReminderTitle(String title) {
                mTitleText.setText(title);
                String letter = "A";

                if(title != null && !title.isEmpty()) {
                    letter = title.substring(0, 1);
                }

                int color = mColorGenerator.getRandomColor();

                mDrawableBuilder = TextDrawable.builder()
                        .buildRound(letter, color);
                mThumbnailImage.setImageDrawable(mDrawableBuilder);
            }


            public void setReminderDateTime(String date, String time) {
                mDateAndTimeText.setText(date + "  " + time);
            }


            public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {

                if(repeat.equals("true")){
                    mRepeatInfoText.setText("Every " + repeatNo + " " + repeatType + "(s)");
                }else if (repeat.equals("false")) {
                    mRepeatInfoText.setText("Repeat Off");
                }
            }



            public void setActiveImage(String active){

                if(active.equals("true")){
                    mActiveImage.setImageResource(R.drawable.ic_toggle_star);
                }else if (active.equals("false")) {
                    mActiveImage.setImageResource(R.drawable.ic_star_outline_grey600_24dp);
                }
            }
        }

        public  ReminderItem generateDummyItem() {
            Random random = new Random();
            return new ReminderItem("1", "2", "3", "4", "5", "6", "7");
        }

        public List<ReminderItem> generateDummyData(int count) {
            ArrayList<SimpleAdapter.ReminderItem> items = new ArrayList<SimpleAdapter.ReminderItem>();


            List<Reminder> subjects = db.getAllSubjects();

            List<String> subjectTitles =new ArrayList<String>();
            List<String> startTime =new ArrayList<String>();
            List<String> endTime =new ArrayList<String>();
            List<String> starList =new ArrayList<String>();
            List<String> daysList= new ArrayList<String>();
            List<String> valList= new ArrayList<String>();
            List<String> valList2= new ArrayList<String>();

            Map<String, String> map = new LinkedHashMap<String, String>();



            List<Integer> IDList= new ArrayList<Integer>();



            for (Subject cn : subjects) {

                subjectTitles.add(cn.getSubjectTitle());
                startTime.add(cn.getStartTime());
                endTime.add(cn.getEndTime());
                starList.add(cn.getStar());
                daysList.add(cn.getDay());
                IDList.add(cn.getID());

            }


            List<TimeSort> timeSortList=new ArrayList<TimeSort>();


            int key=0;

            for(int k=0;k<subjectTitles.size();k++){
                valList.add(Integer.toString(key));

                map.put(startTime.get(k),Integer.toString(key));
                String log =Integer.toString((key))+"*******"+startTime.get(k);
                //Log.d("Name: ", log);


                timeSortList.add(new TimeSort(key,startTime.get(k)));


                key++;
            }



            for(TimeSort item:timeSortList){
                String log =item.getIndex()+"!!!!!!!!"+item.getSortStartTime();
                //Log.d("Name: ", log);
            }


            Collections.sort(timeSortList, new TimeComparator());



            for(TimeSort item:timeSortList){
                String log =item.getIndex()+"^^^^^^^^"+item.getSortStartTime();
                //Log.d("Name: ", log);
            }





            int i=0;

            for(TimeSort item:timeSortList) {
                int k=item.getIndex();


                String log =k + " || " + subjectTitles.get(k) + " || " + startTime.get(k) + " || " + endTime.get(k) + " || " + IDList.get(k);
                //Log.d("Name: ", log);

                if(daysList.get(k).equals("Monday")) {
                    items.add(new SimpleAdapter.TimeTableItem(subjectTitles.get(k), startTime.get(k) + " to " + endTime.get(k), starList.get(k)));
                    IDmap.put(i,IDList.get(k));
                    i++;
                }

            }






            return items;
        }
    }



}
