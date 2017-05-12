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
import android.widget.RelativeLayout;
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
    long checked1;
    int tocheck2;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_stb, null);
        dbHendler = new DbHendler(this.getActivity(), null, null, 1);
        //    stbcountUA = (TextView) view.findViewById(R.id.totalStbsUnAssigned);
        listViewStb = (ListView) view.findViewById(R.id.stb_list_dialog);

        listViewStb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                checked1 = id;
                Log.d(TAG, " onItemClickCalled on id: " + id);
                ok.setEnabled(true);


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
        registerForContextMenu(listViewStb);
        displaySTBList();
        stbcount();
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


            String[] columns = new String[]{DbHendler.KEY_SN, DbHendler.KEY_VC, DbHendler.KEY_STATUS};
            int[] boundTo = new int[]{R.id.stb_sn, R.id.stb_vc, R.id.stb_status};

            adapter = new MySimpleCursorAdapter(this.getActivity(),
                    R.layout.stb_list_item,
                    cursor, columns,
                    boundTo,
                    0);

            listViewStb.setAdapter(adapter);
            Log.d(TAG, "" + listViewStb.getCount());


        } catch (Exception ex) {
            Toast.makeText(getActivity(), "" + ex, Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

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
            TextView sn = (TextView) view.findViewById(R.id.stb_sn);
            TextView vc = (TextView) view.findViewById(R.id.stb_vc);
            RadioButton Rbutton = (RadioButton) view.findViewById(R.id.radioBtn);

            // Extract properties from cursor
            int id = cursor.getInt(cursor.getColumnIndex(DbHendler.KEY_ID));

            String serial = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_SN));
            String Vcard = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_VC));
            sn.setText(serial);
            vc.setText(Vcard);
            if (id == checked1) {
                //     Log.d(TAG, "id" + id);
                Rbutton.setChecked(true);
            } else Rbutton.setChecked(false);

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //get reference to the row
            View view = super.getView(position, convertView, parent);
           /* Context context = getActivity();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view =inflater.inflate(R.layout.stb_list_item, parent ,false);
            RadioButton radioButton= (RadioButton) view.findViewById(R.id.radioBtn);
*/


            //check for odd or even to set alternate colors to the row background
            if (position % 2 == 0) {
                view.setBackgroundColor(Color.rgb(238, 233, 233));
            } else {
                view.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            return view;
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
            int stbId = (int) checked1;
            int assigned = custId;

            if (dbHendler.getSTBID(custId) > 0) {
                Toast.makeText(getActivity(), "Already Assigned STB", Toast.LENGTH_LONG).show();
            } else {
                dbHendler.assignSTB(new STB(stbId, assigned));
                dbHendler.SetStbID(custId, stbId);
                Toast.makeText(this.getActivity(), "Now Assigned STB", Toast.LENGTH_LONG).show();
                swapRefreshCursor();
                ok.setEnabled(false);
                ViewAll activity = (ViewAll) getActivity();
                activity.refreshListView();
            }
        }
        if (v.getId() == R.id.unassign_button)
            try {
                String stbSN = dbHendler.getAssignedSN(getActivity(), custId);
                dbHendler.unAssignSTB(stbSN); //from stb table
                dbHendler.unSetId(custId);    //From cust table
                Toast.makeText(getActivity(), "Unassigned cust: " + custId + " STB SN: " + stbSN, Toast.LENGTH_SHORT).show();
                swapRefreshCursor();
                stbcount();
                ViewAll activity = (ViewAll) getActivity();
                activity.refreshListView();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    public void swapRefreshCursor() {
        try {
            mCursor = dbHendler.getUnAssignedSTBs();
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
