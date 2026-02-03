package com.example.test_lab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity để Thêm hoặc Sửa món ăn
 * Sử dụng Intent để nhận dữ liệu và trả về kết quả cho MainActivity
 */
public class AddEditFoodActivity extends AppCompatActivity {

    // Request codes
    public static final String EXTRA_FOOD = "extra_food";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_MODE = "extra_mode";
    public static final int MODE_ADD = 1;
    public static final int MODE_EDIT = 2;

    // Views
    private TextView tvTitle;
    private EditText edtName, edtDescription, edtPrice;
    private RadioGroup radioGroupImage;
    private RadioButton rbBunBo, rbPho, rbMiQuang, rbHuTieu;
    private ImageView imgPreview;
    private Button btnSave, btnCancel;

    // Dữ liệu
    private int mode;
    private int position = -1;
    private Food editFood;
    private int selectedImageResId = R.drawable.ic_bun_bo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_food);

        // Ánh xạ views
        initViews();

        // Nhận dữ liệu từ Intent
        receiveIntentData();

        // Thiết lập sự kiện
        setupEvents();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        edtName = findViewById(R.id.edtFoodName);
        edtDescription = findViewById(R.id.edtFoodDescription);
        edtPrice = findViewById(R.id.edtFoodPrice);
        radioGroupImage = findViewById(R.id.radioGroupImage);
        rbBunBo = findViewById(R.id.rbBunBo);
        rbPho = findViewById(R.id.rbPho);
        rbMiQuang = findViewById(R.id.rbMiQuang);
        rbHuTieu = findViewById(R.id.rbHuTieu);
        imgPreview = findViewById(R.id.imgPreview);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    /**
     * Nhận dữ liệu từ Intent được gửi từ MainActivity
     */
    private void receiveIntentData() {
        Intent intent = getIntent();
        mode = intent.getIntExtra(EXTRA_MODE, MODE_ADD);

        if (mode == MODE_ADD) {
            // Chế độ Thêm mới
            tvTitle.setText("THÊM MÓN ĂN MỚI");
            rbBunBo.setChecked(true);
            selectedImageResId = R.drawable.ic_bun_bo;
            imgPreview.setImageResource(selectedImageResId);
        } else {
            // Chế độ Sửa - nhận dữ liệu món ăn cần sửa
            tvTitle.setText("SỬA THÔNG TIN MÓN ĂN");
            position = intent.getIntExtra(EXTRA_POSITION, -1);
            editFood = (Food) intent.getSerializableExtra(EXTRA_FOOD);

            if (editFood != null) {
                // Hiển thị thông tin món ăn
                edtName.setText(editFood.getName());
                edtDescription.setText(editFood.getDescription());
                edtPrice.setText(String.valueOf(editFood.getPrice()));

                // Chọn radio button tương ứng với hình ảnh
                selectedImageResId = editFood.getImageResId();
                selectRadioButton(selectedImageResId);
                imgPreview.setImageResource(selectedImageResId);
            }
        }
    }

    private void selectRadioButton(int imageResId) {
        if (imageResId == R.drawable.ic_bun_bo) {
            rbBunBo.setChecked(true);
        } else if (imageResId == R.drawable.ic_pho) {
            rbPho.setChecked(true);
        } else if (imageResId == R.drawable.ic_mi_quang) {
            rbMiQuang.setChecked(true);
        } else if (imageResId == R.drawable.ic_hu_tieu) {
            rbHuTieu.setChecked(true);
        }
    }

    private void setupEvents() {
        // Sự kiện khi chọn hình ảnh
        radioGroupImage.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbBunBo) {
                selectedImageResId = R.drawable.ic_bun_bo;
            } else if (checkedId == R.id.rbPho) {
                selectedImageResId = R.drawable.ic_pho;
            } else if (checkedId == R.id.rbMiQuang) {
                selectedImageResId = R.drawable.ic_mi_quang;
            } else if (checkedId == R.id.rbHuTieu) {
                selectedImageResId = R.drawable.ic_hu_tieu;
            }
            imgPreview.setImageResource(selectedImageResId);
        });

        // Nút Lưu
        btnSave.setOnClickListener(v -> saveFood());

        // Nút Hủy
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    /**
     * Lưu thông tin món ăn và trả về kết quả cho MainActivity
     */
    private void saveFood() {
        // Lấy dữ liệu từ form
        String name = edtName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();

        // Validate dữ liệu
        if (name.isEmpty()) {
            edtName.setError("Vui lòng nhập tên món ăn");
            edtName.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            edtDescription.setError("Vui lòng nhập mô tả");
            edtDescription.requestFocus();
            return;
        }

        if (priceStr.isEmpty()) {
            edtPrice.setError("Vui lòng nhập đơn giá");
            edtPrice.requestFocus();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
            if (price <= 0) {
                edtPrice.setError("Đơn giá phải lớn hơn 0");
                edtPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            edtPrice.setError("Đơn giá không hợp lệ");
            edtPrice.requestFocus();
            return;
        }

        // Tạo đối tượng Food
        int id = (mode == MODE_EDIT && editFood != null) ? editFood.getId() : 0;
        Food food = new Food(id, name, description, price, selectedImageResId);

        // Tạo Intent trả về kết quả
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_FOOD, food);
        resultIntent.putExtra(EXTRA_MODE, mode);
        resultIntent.putExtra(EXTRA_POSITION, position);

        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this,
                mode == MODE_ADD ? "Đã thêm món ăn mới!" : "Đã cập nhật món ăn!",
                Toast.LENGTH_SHORT).show();

        finish();
    }
}
