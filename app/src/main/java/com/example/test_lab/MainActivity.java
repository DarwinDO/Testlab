package com.example.test_lab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity - Hiển thị danh sách món ăn với Custom ListView
 * Hỗ trợ các thao tác: Thêm, Sửa, Xóa
 */
public class MainActivity extends AppCompatActivity {

    // Views
    private ListView lvFood;
    private Button btnAdd;
    private TextView tvResult;

    // Dữ liệu
    private List<Food> foodList;
    private FoodAdapter adapter;
    private int nextId = 5; // ID tiếp theo cho món ăn mới

    // Activity Result Launcher để nhận kết quả từ AddEditFoodActivity
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ views
        initViews();

        // Khởi tạo dữ liệu mẫu
        initData();

        // Thiết lập ListView
        setupListView();

        // Thiết lập Activity Result Launcher
        setupActivityResultLauncher();

        // Thiết lập sự kiện
        setupEvents();
    }

    private void initViews() {
        lvFood = findViewById(R.id.lvFood);
        btnAdd = findViewById(R.id.btnAdd);
        tvResult = findViewById(R.id.tvResult);
    }

    /**
     * Khởi tạo dữ liệu mẫu cho danh sách món ăn
     */
    private void initData() {
        foodList = new ArrayList<>();

        // Thêm các món ăn mẫu (tương tự Lab 4)
        foodList.add(new Food(1, "Bún Bò Huế", "Bún bò cay đặc trưng xứ Huế", 45000, R.drawable.ic_bun_bo));
        foodList.add(new Food(2, "Phở Hà Nội", "Phở truyền thống Hà Nội", 50000, R.drawable.ic_pho));
        foodList.add(new Food(3, "Mì Quảng", "Mì Quảng đặc sản miền Trung", 40000, R.drawable.ic_mi_quang));
        foodList.add(new Food(4, "Hủ Tiếu Sài Gòn", "Hủ tiếu Nam Vang Sài Gòn", 42000, R.drawable.ic_hu_tieu));
    }

    private void setupListView() {
        adapter = new FoodAdapter(this, foodList);
        lvFood.setAdapter(adapter);
    }

    /**
     * Thiết lập Activity Result Launcher để nhận kết quả từ Activity thứ 2
     * Đây là cách mới thay thế cho startActivityForResult
     */
    private void setupActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();

                            // Nhận dữ liệu trả về từ Intent
                            Food food = (Food) data.getSerializableExtra(AddEditFoodActivity.EXTRA_FOOD);
                            int mode = data.getIntExtra(AddEditFoodActivity.EXTRA_MODE, AddEditFoodActivity.MODE_ADD);
                            int position = data.getIntExtra(AddEditFoodActivity.EXTRA_POSITION, -1);

                            if (food != null) {
                                if (mode == AddEditFoodActivity.MODE_ADD) {
                                    // Thêm món ăn mới
                                    food.setId(nextId++);
                                    foodList.add(food);
                                    tvResult.setText("Đã thêm: " + food.getName());
                                } else if (mode == AddEditFoodActivity.MODE_EDIT && position >= 0) {
                                    // Cập nhật món ăn
                                    foodList.set(position, food);
                                    tvResult.setText("Đã sửa: " + food.getName());
                                }

                                // Cập nhật ListView
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void setupEvents() {
        // Nút Thêm - mở Activity thêm món ăn
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditFoodActivity.class);
            intent.putExtra(AddEditFoodActivity.EXTRA_MODE, AddEditFoodActivity.MODE_ADD);
            activityResultLauncher.launch(intent);
        });

        // Click vào item - mở Activity sửa món ăn
        lvFood.setOnItemClickListener((parent, view, position, id) -> {
            Food food = foodList.get(position);
            Intent intent = new Intent(MainActivity.this, AddEditFoodActivity.class);
            intent.putExtra(AddEditFoodActivity.EXTRA_MODE, AddEditFoodActivity.MODE_EDIT);
            intent.putExtra(AddEditFoodActivity.EXTRA_FOOD, food);
            intent.putExtra(AddEditFoodActivity.EXTRA_POSITION, position);
            activityResultLauncher.launch(intent);
        });

        // Long click vào item - xóa món ăn
        lvFood.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmDialog(position);
            return true;
        });
    }

    /**
     * Hiển thị dialog xác nhận xóa món ăn
     */
    private void showDeleteConfirmDialog(int position) {
        Food food = foodList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa món \"" + food.getName() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Xóa món ăn khỏi danh sách
                    foodList.remove(position);
                    adapter.notifyDataSetChanged();
                    tvResult.setText("Đã xóa: " + food.getName());
                    Toast.makeText(MainActivity.this, "Đã xóa món ăn!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}