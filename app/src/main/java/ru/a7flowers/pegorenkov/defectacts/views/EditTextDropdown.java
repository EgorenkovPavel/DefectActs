package ru.a7flowers.pegorenkov.defectacts.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.R;

public class EditTextDropdown<T extends Number> extends ConstraintLayout{

    private TextView tvTitle;
    private AutoCompleteTextView acText;
    private ImageButton btnDropdown;
    private TextChangeListener listener;
    private String value = "";
    private DropDownAdapter adapter;

    public EditTextDropdown(Context context) {
        super(context);
        init(context);
    }

    public EditTextDropdown(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditTextDropdown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.edittext_dropdown, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvTitle = findViewById(R.id.tvLabel);
        acText = findViewById(R.id.acText);
        btnDropdown = findViewById(R.id.btnDropdown);

        acText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String val = getValueFromText();
                if(!value.equals(val)){
                    if (listener != null) listener.onTextChanged(val);
                    value = val;
                }
            }
        });

        adapter = new DropDownAdapter(getContext());
        acText.setAdapter(adapter);
        acText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                value = adapter.getItem(i).toString();
                acText.setText(String.valueOf(value));
                if (listener != null) listener.onTextChanged(value);
            }
        });

        btnDropdown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                acText.showDropDown();
            }
        });
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(acText.getWindowToken(), 0);
    }

    private String getValueFromText(){
        String text = String.valueOf(acText.getText());
        if (text.isEmpty()){
            return "0";
        }else {
            return text;
        }
    }

    public void setTextChangeListener(TextChangeListener listener){
        this.listener = listener;
    }

    public void setTitle(int res){
        tvTitle.setText(res);
    }

    public void setValue(T value){
        if (value instanceof Integer)
            acText.setInputType(InputType.TYPE_CLASS_NUMBER);
        else
            acText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        if (value == null){
            this.value = "";
        }else if(value.equals(0) || value.equals(0f)){
            this.value = "";
        }else{
            this.value = value.toString();
        }

        acText.setText(this.value);
    }

    public void setList(List<T> list){
        adapter.clear();
        adapter.addAll(list);
    }

    public interface TextChangeListener{
        void onTextChanged(String value);
    }

    class DropDownAdapter extends ArrayAdapter<T>{

        public DropDownAdapter(@NonNull Context context) {
            super(context, android.R.layout.simple_dropdown_item_1line);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    return null;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                }
            };
        }
    }
}
