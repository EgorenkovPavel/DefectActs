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
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DeliveriesViewModel;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder>{

    private List<User> items;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_user, viewGroup, false);

        return new UserHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder viewHolder, int position) {
        User user = items.get(position);
        viewHolder.tvName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<User> users){
        items = users;
        notifyDataSetChanged();
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }

    class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            User user = items.get(getAdapterPosition());
            if(listener != null) listener.onUserClick(user);
        }
    }

}
