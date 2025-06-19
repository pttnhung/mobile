package com.example.listview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.listview.R;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvCount;
    private FloatingActionButton btnAdd;
    private MyViewModel model;
    private ListView lvcount;
    private ArrayList<Integer> arrayList;
    private ArrayAdapter<Integer> arrayAdapter;
    private static final int REQUEST_CODE_DETAILS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new ViewModelProvider(this).get(MyViewModel.class);
        tvCount = findViewById(R.id.tv_count);
        btnAdd = findViewById(R.id.btn_add);

        lvcount = findViewById(R.id.lv_count);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        lvcount.setAdapter(arrayAdapter);

        model.getNumberList().observe(this, new androidx.lifecycle.Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                arrayList.clear();
                if (integers != null) arrayList.addAll(integers);
                arrayAdapter.notifyDataSetChanged();

                // Hiển thị tổng số phần tử
                tvCount.setText(String.valueOf(arrayList.size()));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.increaseNumber();
            }
        });
        lvcount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("number", arrayList.get(position));
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUEST_CODE_DETAILS);
            }
        });
        lvcount.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Tạo list mới từ list hiện tại
                ArrayList<Integer> newList = new ArrayList<>(model.getNumberList().getValue());
                // Xóa item trên list mới
                newList.remove(position);
                // Cập nhật lại ViewModel bằng list mới
                model.setNumberList(newList);
                return true; // trả về true để báo đã xử lý sự kiện
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra("position", -1);
            int newValue = data.getIntExtra("new_number", -1);
            if (position != -1 && newValue != -1) {
                model.updateNumber(position, newValue);
            }
        }
    }
}
