package ru.a7flowers.pegorenkov.defectacts.adapters;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ru.a7flowers.pegorenkov.defectacts.R;
import ru.a7flowers.pegorenkov.defectacts.data.Mode;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DeliveriesViewModel;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryHolder>{

    private DeliveriesViewModel mViewModel;
    private List<Delivery> items;
    private Mode mMode;

    public void setViewModel(DeliveriesViewModel viewModel) {
        mViewModel = viewModel;

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

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        viewHolder.tvNumber.setText(delivery.getNumber());
        viewHolder.tvDate.setText(format.format(delivery.getDate()));

        if (mMode.equals(Mode.DEFECTS) && delivery.isDefectActExist()){
            viewHolder.ivActExist.setVisibility(View.VISIBLE);
            viewHolder.ivActExist.setImageResource(R.drawable.bug);
        }else if(mMode.equals(Mode.DIFFERENCIES) && delivery.isDifferenceActExist()){
            viewHolder.ivActExist.setVisibility(View.VISIBLE);
            viewHolder.ivActExist.setImageResource(R.drawable.swap);
        }else{
            viewHolder.ivActExist.setVisibility(View.INVISIBLE);
        }

        viewHolder.cbSelected.setChecked(mViewModel.isDeliverySelected(delivery));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<Delivery> deliveries){
        items = deliveries;
        notifyDataSetChanged();
    }

    public void setMode(Mode mode){
        mMode = mode;
        notifyDataSetChanged();
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
            if (!view.equals(cbSelected))
            cbSelected.setChecked(!cbSelected.isChecked());

            Delivery delivery = items.get(getAdapterPosition());
            if(cbSelected.isChecked()){
                mViewModel.addSelectedDelivery(delivery);
            }else{
                mViewModel.removeSelectedDelivery(delivery);
            }
        }
    }

}
