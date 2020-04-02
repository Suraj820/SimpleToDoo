package com.scts.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView lv;
    FloatingActionButton fb;
    List<String> items;
    itemAdapter itm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.lv);
        fb = findViewById(R.id.fab);
        loadItems();

        itemAdapter.OnLongItemClickListner onLongItemClickListner =   new itemAdapter.OnLongItemClickListner(){
            @Override
            public void ItemLongClickListner(int position) {
                items.remove(position);
                itm.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), " Task removed" , Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };
        itm =  new itemAdapter(items,onLongItemClickListner);
       lv.setAdapter(itm);
       lv.setLayoutManager(new LinearLayoutManager(this));
       fb.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
               builder.setTitle("Add Your Task");

               final EditText input = new EditText(MainActivity.this);
               LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
               input.setLayoutParams(lp);
               builder.setView(input);
               builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       items.add(input.getText().toString());
                       itm.notifyItemInserted(items.size() -1);
                       Toast.makeText(getApplicationContext(), " Task Added" + input.getText().toString(), Toast.LENGTH_SHORT).show();
                       saveItems();
                   }
               });
               builder.setNegativeButton(" Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });
               builder.show();


           }
       });
    }
    private File getDatafile()
    {
        return  new File(getFilesDir(),"data.txt");
    }
    private void loadItems()
    {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDatafile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error while reading lines " );
            items = new ArrayList<>();
        }
    }

    private void saveItems(){
        try {
            FileUtils.writeLines(getDatafile(),items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error while write lines " );
        }
    }
}
