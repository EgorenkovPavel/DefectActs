package ru.a7flowers.pegorenkov.defectacts.adapters;

import android.support.annotation.NonNull;
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
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DeliveriesViewModel;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryHolder>{

    private DeliveriesViewModel mViewModel;
    private List<Delivery> items;
    private TakePhotoListener listener;

    public void setViewModel(DeliveriesViewModel viewModel) {
        mViewModel = viewModel;
    }

    public interface TakePhotoListener{
        void takePhoto(Delivery delivery);
    }

    public void setTakePhotoListener(TakePhotoListener listener) {
        this.listener = listener;
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
        viewHolder.ivDefectActExist.setVisibility(delivery.isDefectActExist() ? View.VISIBLE : View.INVISIBLE);
        viewHolder.ivDiffActExist.setVisibility(delivery.isDifferenceActExist() ? View.VISIBLE : View.INVISIBLE);
        viewHolder.cbSelected.setChecked(mViewModel.isDeliverySelected(delivery));
        viewHolder.tvPhotoCount.setText(String.valueOf(delivery.getPhotoCount()));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<Delivery> deliveries){
        items = deliveries;
        notifyDataSetChanged();
    }

    class DeliveryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNumber;
        TextView tvDate;
        ImageView ivDefectActExist;
        ImageView ivDiffActExist;
        ImageView ivPhoto;
        TextView tvPhotoCount;
        CheckBox cbSelected;
        View vTap;

        public DeliveryHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivDefectActExist = itemView.findViewById(R.id.ivDefectActExist);
            ivDiffActExist = itemView.findViewById(R.id.ivDiffActExist);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            tvPhotoCount = itemView.findViewById(R.id.tvNewPhotoCount);
            cbSelected = itemView.findViewById(R.id.cbSelected);
            vTap = itemView.findViewById(R.id.vTap);

            cbSelected.setOnClickListener(this);
            itemView.setOnClickListener(this);

            vTap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Delivery delivery = items.get(getAdapterPosition());
                    if (listener != null) listener.takePhoto(delivery);
                }
            });
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
