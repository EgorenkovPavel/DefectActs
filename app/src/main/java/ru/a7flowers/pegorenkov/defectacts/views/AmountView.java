package ru.a7flowers.pegorenkov.defectacts.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.a7flowers.pegorenkov.defectacts.R;

public class AmountView extends ConstraintLayout {

    private TextView tvTitle;
    private EditText etAmount;
    private ImageButton btnDec;
    private ImageButton btnInc;

    private int value;
    private ValueChangeListener listener;

    public AmountView(Context context) {
        super(context);
        init(context);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AmountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.edit_text_inc_dec, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvTitle = findViewById(R.id.lblTitle);
        etAmount = findViewById(R.id.etAmount);
        btnDec = findViewById(R.id.btnDec);
        btnInc = findViewById(R.id.btnInc);

        btnDec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                value--;
                refreshText();
                if(listener != null) listener.onValueChange(value);
            }
        });

        btnInc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                value++;
                refreshText();
                if(listener != null) listener.onValueChange(value);
            }
        });

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = String.valueOf(etAmount.getText());
                int newValue = text.isEmpty() ? 0 : Integer.valueOf(text);

                if(listener != null && newValue != value) {
                    value = newValue;
                    listener.onValueChange(value);
                };
            }
        });
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setValue(int value){
        this.value = value;
        refreshText();
    }

    public void setListener(ValueChangeListener listener) {
        this.listener = listener;
    }

    private void refreshText(){
        String text = value == 0 ? "" : String.valueOf(value);

        etAmount.setText(text);
        etAmount.setSelection(etAmount.getText().length());
    }

    public interface ValueChangeListener{
        void onValueChange(int value);
    }
}
