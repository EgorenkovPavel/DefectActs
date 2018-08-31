package ru.a7flowers.pegorenkov.defectacts.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.a7flowers.pegorenkov.defectacts.R;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryHolder>{

    private List<Delivery> items;
    private List<Delivery> selectedItems;
    private OnDeliveryClickListener listener;

    public interface OnDeliveryClickListener{
        void onDeliveryClick(Delivery delivery);
    }

    @NonNull
    @Override
    public DeliveryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_delivery, viewGroup, false);

        return new DeliveryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryHolder viewHolder, int position) {

        Delivery delivery = items.get(position);

        viewHolder.tvNumber.setText(delivery.getNumber());
        viewHolder.tvDate.setText(String.format(Locale.getDefault(), "%tD", delivery.getDate()));
        viewHolder.ivActExist.setVisibility(delivery.isActExist() ? View.VISIBLE : View.INVISIBLE);
        viewHolder.cbSelected.setChecked(selectedItems != null && selectedItems.contains(delivery));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<Delivery> deliveries){
        items = deliveries;
        notifyDataSetChanged();
    }

    public void setSelectedItems(List<Delivery> selectedItems) {
        this.selectedItems = new ArrayList<>(selectedItems);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnDeliveryClickListener listener){
        this.listener = listener;
    }

    class DeliveryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNumber;
        TextView tvDate;
        ImageView ivActExist;
        CheckBox cbSelected;

        public DeliveryHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivActExist = itemView.findViewById(R.id.ivActExist);
            cbSelected = itemView.findViewById(R.id.cbSelected);

            cbSelected.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onDeliveryClick(items.get(getAdapterPosition()));
        }
    }

}
