package com.example.test_lab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Custom Adapter để hiển thị danh sách món ăn trong ListView
 * Mỗi item bao gồm: Hình ảnh, Tên món, Mô tả, Đơn giá
 */
public class FoodAdapter extends BaseAdapter {
    private Context context;
    private List<Food> foodList;
    private LayoutInflater inflater;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return foodList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // Inflate layout cho item
            convertView = inflater.inflate(R.layout.item_food, parent, false);

            // Tạo ViewHolder để tối ưu hiệu suất
            holder = new ViewHolder();
            holder.imgFood = convertView.findViewById(R.id.imgFood);
            holder.tvName = convertView.findViewById(R.id.tvFoodName);
            holder.tvDescription = convertView.findViewById(R.id.tvFoodDescription);
            holder.tvPrice = convertView.findViewById(R.id.tvFoodPrice);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy dữ liệu món ăn tại vị trí position
        Food food = foodList.get(position);

        // Gán dữ liệu vào các view
        holder.imgFood.setImageResource(food.getImageResId());
        holder.tvName.setText(food.getName());
        holder.tvDescription.setText(food.getDescription());
        holder.tvPrice.setText(String.format("%,d VNĐ", food.getPrice()));

        return convertView;
    }

    /**
     * ViewHolder pattern để tối ưu hiệu suất ListView
     */
    private static class ViewHolder {
        ImageView imgFood;
        TextView tvName;
        TextView tvDescription;
        TextView tvPrice;
    }

    /**
     * Cập nhật lại danh sách và refresh ListView
     */
    public void updateList(List<Food> newList) {
        this.foodList = newList;
        notifyDataSetChanged();
    }
}
