package ru.a7flowers.pegorenkov.defectacts.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.R;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;

public class GoodsSearchAdapter extends ArrayAdapter<Good> {

    private String suffix;
    private String preffix;

    private List<Good> goods;
    private List<Good> suggestions = new ArrayList<>();
    private GoodFoundCallback callback;
    private Filter goodsFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Good)resultValue).getSeries();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                String text = constraint.toString();
                text = clearBarcode(text);

                suggestions.clear();
                for (Good good:goods){
                    if (good.getSeries().startsWith(text)){
                        suggestions.add(good);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if((filterResults == null) || (filterResults.count == 0)) return;
            ArrayList<Good> filteredList = (ArrayList<Good>)filterResults.values;
            clear();

            String text = charSequence.toString();
            text = clearBarcode(text);

            if(text.length() == 13
                    && filteredList.size() == 1
                    && callback != null){
                callback.onGoodFounded(filteredList.get(0));
            }else{
                addAll(filteredList);
            }
        }
    };

    public interface GoodFoundCallback{
        void onGoodFounded(Good good);
    }

    public GoodsSearchAdapter(@NonNull Context context) {
        super(context, R.layout.item_good);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        suffix = prefs.getString(context.getString(R.string.pref_sufix), "");
        preffix = prefs.getString(context.getString(R.string.pref_prefix), "");
    }

    public void setCallback(GoodFoundCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return inflateView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return inflateView(position, convertView, parent);
    }

    private View inflateView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_good, parent, false);
        Good good = getItem(position);

        TextView tvSeries = v.findViewById(R.id.tvSeries);
        TextView tvGood = v.findViewById(R.id.tvGood);
        TextView tvSuplier = v.findViewById(R.id.tvSuplier);
        TextView tvCounty = v.findViewById(R.id.tvCountry);
        TextView tvAmount = v.findViewById(R.id.tvQuantity);

        tvSeries.setText(good.getSeries());
        tvGood.setText(good.getGood());
        tvSuplier.setText(good.getSuplier());
        tvCounty.setText(good.getCountry());
        tvAmount.setText(String.valueOf(good.getDeliveryQuantity()));

        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return goodsFilter;
    }

    public void setItems(List<Good> items) {
        goods = items;
    }

    private String clearBarcode(String text){
        if(!suffix.isEmpty() && text.endsWith(suffix))
            text = text.substring(0, text.length() - suffix.length());

        if(!preffix.isEmpty() && text.startsWith(preffix))
            text = text.substring(preffix.length());

        return text;
    }
}
