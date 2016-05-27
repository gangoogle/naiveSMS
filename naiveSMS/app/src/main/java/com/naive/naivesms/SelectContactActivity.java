package com.naive.naivesms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SelectContactActivity extends Activity {

    private ListView listView;

    private List<ContactInfo> infoList;
    private List<ContactInfo> mSelectList;
    private Button mBtAll;
    private Button mBtTurn;
    private Button mBtSure;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_contact);
        ctx = this;
        mBtAll = (Button) findViewById(R.id.bt_all);
        mBtTurn = (Button) findViewById(R.id.bt_turn);
        mBtSure = (Button) findViewById(R.id.bt_sure);
        listView = (ListView) findViewById(R.id.lv);
        infoList = ContactInfoProvider.getContactInfos(this);
        Collections.sort(infoList);
        mSelectList = new ArrayList<>();
        adapter = new MyListAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mSelectList.contains(infoList.get(position))) {
                    mSelectList.remove(infoList.get(position));
                } else {
                    mSelectList.add(infoList.get(position));
                }
                adapter.notifyDataSetChanged();

            }
        });
        mBtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectList.clear();
                mSelectList.addAll(infoList);
                adapter.notifyDataSetChanged();
            }
        });
        mBtTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ContactInfo contactInfo : infoList) {
                    if (mSelectList.contains(contactInfo)) {
                        mSelectList.remove(contactInfo);
                    } else {
                        mSelectList.add(contactInfo);
                    }

                }
                adapter.notifyDataSetChanged();
            }
        });
        mBtSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("list", (Serializable) mSelectList);
                setResult(1, intent);
                finish();
            }
        });
    }

    private MyListAdapter adapter;

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(ctx, R.layout.list_item_select_contact, null);
                viewHolder = new ViewHolder();
                viewHolder.TvName = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            ContactInfo contactInfo = infoList.get(position);
            viewHolder.TvName.setText(contactInfo.getName());
            viewHolder.tvNumber.setText(contactInfo.getPhone());
            if (mSelectList.contains(contactInfo)) {
                convertView.setBackgroundColor(ctx.getResources().getColor(R.color.colorAccent));
            } else {
                convertView.setBackgroundColor(ctx.getResources().getColor(R.color.colorWhite));
            }

            return convertView;
        }
    }

    class ViewHolder {
        TextView TvName;
        TextView tvNumber;

    }


}
