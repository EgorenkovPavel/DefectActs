package ru.a7flowers.pegorenkov.defectacts.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.R;
import ru.a7flowers.pegorenkov.defectacts.objects.Defect;
import ru.a7flowers.pegorenkov.defectacts.objects.Reason;

public class ReasonsAdapter extends RecyclerView.Adapter<ReasonsAdapter.DefectHolder> {

    private List<Reason> mReasons;
    private List<Reason> mSelectedReasons;

    private OnReasonClickListener listener;

    public interface OnReasonClickListener {
        void onReasonClick(Reason reason);
    }

    @NonNull
    @Override
    public DefectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reason, parent, false);
        return new DefectHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DefectHolder holder, int position) {

        Reason reason = mReasons.get(position);

        holder.cbReason.setText(reason.getTitle());
        holder.cbReason.setChecked(mSelectedReasons == null ? false : mSelectedReasons.contains(reason));
    }

    @Override
    public int getItemCount() {
        return mReasons == null ? 0 : mReasons.size();
    }

    public void setItems(List<Reason> reasons){
        mReasons = reasons;
        notifyDataSetChanged();
    }

    public void setSelectedItems(List<Reason> reasons){
        mSelectedReasons = reasons;
        notifyDataSetChanged();
    }

    public void setOnReasonClickListener(OnReasonClickListener listener){
        this.listener = listener;
    }

    class DefectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox cbReason;

        public DefectHolder(View itemView) {
            super(itemView);

            cbReason = itemView.findViewById(R.id.cbReason);

            itemView.setOnClickListener(this);
            cbReason.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onReasonClick(mReasons.get(getAdapterPosition()));
        }
    }

}
