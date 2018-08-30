package ru.a7flowers.pegorenkov.defectacts.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.R;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.objects.DefectGood;

public class DefectsAdapter extends RecyclerView.Adapter<DefectsAdapter.DefectHolder> {

    private List<DefectGood> mDefects;
    private OnDefectClickListener listener;

    public interface OnDefectClickListener{
        void onDefectClick(Defect defect);
    }

    @NonNull
    @Override
    public DefectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_defect, parent, false);
        return new DefectHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DefectHolder holder, int position) {

        DefectGood defect = mDefects.get(position);

        holder.tvSeries.setText(defect.getSeries());
        holder.tvGood.setText(defect.getGood());
        holder.tvSuplier.setText(defect.getSuplier());
        holder.tvCountry.setText(defect.getCountry());
        holder.tvQuantity.setText(String.valueOf(defect.getQuantity()));
        holder.tvPhotoQuantity.setText(String.valueOf(defect.getPhotoQuantity()));

    }

    @Override
    public int getItemCount() {
        return mDefects == null ? 0 : mDefects.size();
    }

    public void setItems(List<DefectGood> defects){
        mDefects = defects;
        notifyDataSetChanged();
    }

    public void setOndefectClickListener(OnDefectClickListener listener){
        this.listener = listener;
    }

    class DefectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvSeries;
        TextView tvGood;
        TextView tvSuplier;
        TextView tvCountry;
        TextView tvQuantity;
        TextView tvPhotoQuantity;

        public DefectHolder(View itemView) {
            super(itemView);

            tvSeries = itemView.findViewById(R.id.tvSeries);
            tvGood = itemView.findViewById(R.id.tvGood);
            tvSuplier = itemView.findViewById(R.id.tvSuplier);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPhotoQuantity = itemView.findViewById(R.id.tvPhotoQuantity);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onDefectClick(new Defect(mDefects.get(getAdapterPosition())));
        }
    }

}
