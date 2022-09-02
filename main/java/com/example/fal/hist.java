package com.example.fal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import androidx.fragment.app.Fragment;
public class hist extends ListActivity {
    ArrayAdapter<String> adapter;
    private DBhand db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hist);
        db=new DBhand(hist.this);
        ArrayList a = db.upd();
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item,
                a);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}