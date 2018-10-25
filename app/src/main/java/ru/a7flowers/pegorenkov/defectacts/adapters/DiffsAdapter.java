package ru.a7flowers.pegorenkov.defectacts.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.R;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;

public class DiffsAdapter extends RecyclerView.Adapter<DiffsAdapter.DiffHolder> {

    private List<Diff> mDiffs;
    private OnDiffClickListener listener;

    public interface OnDiffClickListener {
        void onDiffClick(Diff diff);
    }

    @NonNull
    @Override
    public DiffHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diff, parent, false);
        return new DiffHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiffHolder holder, int position) {

        Diff diff = mDiffs.get(position);

        holder.tvSeries.setText(diff.getSeries());
        holder.tvGood.setText(diff.getTitle());
        holder.tvSuplier.setText(diff.getSuplier());
        holder.tvCountry.setText(diff.getCountry());
        holder.tvQuantity.setText(String.valueOf(diff.getQuantity()));
        holder.tvPhotoQuantity.setText(String.valueOf(diff.getPhotoQuantity()));

    }

    @Override
    public int getItemCount() {
        return mDiffs == null ? 0 : mDiffs.size();
    }

    public void setItems(List<Diff> diffs){
        mDiffs = diffs;
        notifyDataSetChanged();
    }

    public void setOnDiffClickListener(OnDiffClickListener listener){
        this.listener = listener;
    }

    class DiffHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvSeries;
        TextView tvGood;
        TextView tvSuplier;
        TextView tvCountry;
        TextView tvQuantity;
        TextView tvPhotoQuantity;

        public DiffHolder(View itemView) {
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
            listener.onDiffClick(mDiffs.get(getAdapterPosition()));
        }
    }

}
