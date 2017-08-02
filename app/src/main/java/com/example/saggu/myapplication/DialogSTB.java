package com.example.saggu.myapplication;

import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Saggu on 1/25/2017.
 */

public class DialogSTB extends DialogFragment implements View.OnClickListener {

    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView listViewStb;
    String TAG = "MyApp_DialogSTB";
    TextView stbcountUA;
    int stbcountUa;
    int custId;
    Button unAssign, ok;
    private Cursor mCursor;
    EditText searchboxstb;
    MySimpleCursorAdapter adapter;
    long checkedId;
    long assignedSTBid;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_stb, null);
        dbHendler = new DbHendler(this.getActivity(), null, null, 1);
        //    stbcountUA = (TextView) view.findViewById(R.id.totalStbsUnAssigned);
        listViewStb = (ListView) view.findViewById(R.id.stb_list_dialog);


        listViewStb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RadioButton Rbutton = (RadioButton) view.findViewById(R.id.radioBtn);
                checkedId = id;
                ok.setEnabled(true);


                if (Rbutton.isChecked() == true) {
                    Log.d(TAG, "rbutton checked");
                    Rbutton.setChecked(false);


                } else Rbutton.setChecked(true);
                Log.d(TAG, " onItemClickCalled on id: " + id );
                adapter.notifyDataSetChanged();
                //endregion
            }
        });
        unAssign = (Button) view.findViewById(R.id.unassign_button);
        ok = (Button) view.findViewById(R.id.stb_ok);

        searchboxstb = (EditText) view.findViewById(R.id.searchboxstb);
        searchboxstb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displaySearchList();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchboxstb.getText().toString().equals("")) {
                    displaySTBList();
                }
            }
        });
        unAssign.setOnClickListener(this);
        ok.setOnClickListener(this);

        Bundle bundle = getArguments();
        custId = bundle.getInt("CUSTID");
        assignedSTBid = bundle.getLong("STBID");
        Log.d(TAG, "onCreateView: custid / stbid " + custId + " " + assignedSTBid);
        if (assignedSTBid<1)
            unAssign.setEnabled(false );
        registerForContextMenu(listViewStb);
        displaySTBList();
        stbcount();
        //    setPosition(50);
        return view;
    }


    //region Create all List
    public void displaySTBList() {
        try {
            Cursor cursor = dbHendler.getUnAssignedSTBs();
            if (cursor == null) {
                Toast.makeText(getActivity(), "Cursor is Null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (cursor.getCount() == 0) {
                Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_SHORT).show();
                return;
            }


            String[] columns = new String[]{DbHendler.KEY_ID, DbHendler.KEY_SN, DbHendler.KEY_VC, DbHendler.KEY_STATUS};
            int[] boundTo = new int[]{R.id.stbID, R.id.stb_sn, R.id.stb_vc, R.id.stb_status};

            adapter = new MySimpleCursorAdapter(this.getActivity(),
                    R.layout.stb_list_item,
                    cursor, columns,
                    boundTo,
                    0);

            listViewStb.setAdapter(adapter);
            Log.d(TAG, "" + listViewStb.getCount());
            //    listViewStb.smoothScrollToPosition(50);


        } catch (Exception ex) {
            Toast.makeText(getActivity(), "" + ex, Toast.LENGTH_SHORT).show();
        }
    }

    //endregion
    public void setPosition(int position) { //setting listview position
        if (listViewStb.getFirstVisiblePosition() > position || listViewStb.getLastVisiblePosition() < position)
            listViewStb.setSelection(position);
    }


    //region Create Search  List
    public void displaySearchList() {
        String searchItem = searchboxstb.getText().toString();
        try {
            Cursor cursor = dbHendler.searchSTBToList(searchItem);
            if (cursor == null) {
                //    textView4.setText("Unable to generate cursor.");
                return;
            }
            if (cursor.getCount() == 0) {
                //  textView4.setText("No Customer Found");
                return;
            } else {
                //   textView4.setText("");
                String[] columns = new String[]{
                        //DbHendler.KEY_ID,
                        DbHendler.KEY_SN,
                        DbHendler.KEY_VC,
                        DbHendler.KEY_STATUS
                        //   DbHendler.KEY_SN
                };
                int[] boundTo = new int[]{
                        //R.id.pId,
                        R.id.stb_sn,
                        R.id.stb_vc,
                        R.id.stb_status
                        //    R.id.vc_mac
                };
                adapter = new MySimpleCursorAdapter(this.getActivity(),
                        R.layout.stb_list_item,
                        cursor,
                        columns,
                        boundTo,
                        0);
                listViewStb.setAdapter(adapter);
            }
        } catch (Exception ex) {
            Log.d(TAG, "" + ex);
//            textView4.setText("There was an error!");
        }
    }
    //endregion

    private class MySimpleCursorAdapter extends android.support.v4.widget.SimpleCursorAdapter {
        public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.stb_list_item, parent, false);
            return view;
        }

        @Override
        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        public void bindView(View view, Context context, Cursor cursor) {


            // Find fields to populate in inflated template
            TextView sntxt = (TextView) view.findViewById(R.id.stb_sn);
            TextView vctxt = (TextView) view.findViewById(R.id.stb_vc);
            RadioButton Rbutton = (RadioButton) view.findViewById(R.id.radioBtn);
            TextView stbidtxt = (TextView) view.findViewById(R.id.stbID);

            // Extract properties from cursor
            int id = cursor.getInt(cursor.getColumnIndex(DbHendler.KEY_ID));


            String serial = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_SN));
            String Vcard = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_VC));
            sntxt.setText(serial);
            vctxt.setText(Vcard);
            int assigned = cursor.getInt(cursor.getColumnIndex(DbHendler.KEY_ASSIGNED));
            stbidtxt.setText(cursor.getString(cursor.getColumnIndex(DbHendler.KEY_ID)));
             if (id == checkedId) {
                Rbutton.setChecked(true);
            } else {
                Rbutton.setChecked(false);
            }


        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //get reference to the row
            View view = super.getView(position, convertView, parent);


            //check for odd or even to set alternate colors to the row background
            if (position % 2 == 0) {
                view.setBackgroundColor(Color.rgb(238, 233, 233));
            } else {
                view.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            return view;
        }

        @Override
        public boolean isEnabled(int position) {
            return super.isEnabled(position);
        }
    }


    //region context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onContextItemSelected(item);
                return true;
            }
        };
        menu.add("Assign STB");
        for (int i = 0, n = menu.size(); i < n; i++)
            menu.getItem(i).setOnMenuItemClickListener(listener);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Assign STB") {
            //entry in assigned column
            int stbId = (int) info.id;
            int assigned = custId;

            if (dbHendler.getSTBID(custId) > 0) {
                Toast.makeText(getActivity(), "Already Assigned STB", Toast.LENGTH_LONG).show();
            } else {
                dbHendler.assignSTB(new STB(stbId, assigned));
                dbHendler.SetStbID(custId, stbId);
                Toast.makeText(this.getActivity(), "Now Assigned STB", Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();

            }
        }
        return true;
    } //endregion


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stb_ok) {
            Log.d(TAG, "onClick: set");
            assignSTB();
        }
        if (v.getId() == R.id.unassign_button) {
            Log.d(TAG, "onClick: unassign");
            unAssignSTB();
        }
    }

    public void assignSTB() {
        int stbId = (int) checkedId;
        int assigned = custId;
        if (dbHendler.getSTBID(custId) > 0) {
            Toast.makeText(getActivity(), "Already Assigned STB", Toast.LENGTH_SHORT).show();
        } else {
            dbHendler.assignSTB(new STB(stbId, assigned));
            dbHendler.SetStbID(custId, stbId);
            Toast.makeText(this.getActivity(), "Now Assigned STB", Toast.LENGTH_SHORT).show();
            swapRefreshCursor();
            ok.setEnabled(false);
            unAssign.setEnabled(true);

            ViewAll activity = (ViewAll) getActivity();
            activity.refreshListView();
            dismiss();
        }
    }

    public void unAssignSTB() {
        try {
            String stbSN = dbHendler.getAssignedSN(getActivity(), custId);
            dbHendler.unAssignSTB(stbSN); //from stb table
            dbHendler.unSetId(custId);    //From cust table
            Toast.makeText(getActivity(), "Unassigned STB : " + stbSN, Toast.LENGTH_LONG).show();
            swapRefreshCursor();
            stbcount();

            ViewAll activity = (ViewAll) getActivity();
            activity.refreshListView();
            unAssign.setEnabled(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void swapRefreshCursor() {
        try {
          //  mCursor = dbHendler.getUnAssignedSTBs();
            mCursor=dbHendler.getUnAssignedSTBs();
            adapter.swapCursor(mCursor);
            stbcount();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void stbcount() {
        stbcountUa = dbHendler.countSTBsUA;
//        stbcountUA.setText("STBs:" + stbcountUa);
    }


}
