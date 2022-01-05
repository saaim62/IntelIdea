package com.example.fypintelidea.core.views;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class DynamicViews {

    Context context;

    public DynamicViews(Context context) {
        this.context = context;
    }

    public ImageView imageView(int resId, String tag, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageView = new ImageView(this.context);
        imageView.setImageResource(resId);
        imageView.setLayoutParams(layoutParams);
//        imageView.setId(i);
        imageView.setTag(tag);
        imageView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        return imageView;
    }

    public CheckBox checkBoxWithoutSpace(String value, String tag, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        CheckBox checkBox = new CheckBox(this.context);
        checkBox.setLayoutParams(layoutParams);
//        checkBox.setOnCheckedChangeListener(this.context);
        if (value != null)
            checkBox.setText(value);
        checkBox.setTextSize(16);
//        checkBox.setId(i);
        checkBox.setTag(tag);
        checkBox.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        return checkBox;
    }

    public CheckBox checkBoxWithSpace(String value, String tag, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        CheckBox checkBox = new CheckBox(this.context);
        checkBox.setLayoutParams(layoutParams);
//        checkBox.setOnCheckedChangeListener(this.context);
        if (value != null) {
            String space = " ";
            checkBox.setText(space.concat(" ").concat(value.concat(" ")));
        }
        checkBox.setTextSize(16);
//        checkBox.setId(i);
        checkBox.setTag(tag);
        checkBox.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        return checkBox;
    }

    public TextView textView(String value, String tag, boolean border, int minWidth) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this.context);
        textView.setLayoutParams(layoutParams);
        textView.setText(value);
        textView.setTextSize(14);
        textView.setMinWidth(minWidth);
        textView.setMaxWidth(500);
        textView.setTag(tag);
        if (border)
            textView.setBackground(context.getResources().getDrawable(android.R.drawable.editbox_background));

        return textView;
    }

    public TextView textView(String value, String tag, boolean border, int minWidth, int color, int fontSize, boolean isBold, boolean isItalic) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this.context);
        textView.setLayoutParams(layoutParams);
        textView.setText(value);
        textView.setMinWidth(minWidth);
        textView.setTag(tag);
        if (border)
            textView.setBackground(context.getResources().getDrawable(android.R.drawable.editbox_background));
        textView.setTextColor(color);
        textView.setTextSize(fontSize);
        if (isBold)
            textView.setTypeface(null, Typeface.BOLD);
        if (isItalic)
            textView.setTypeface(null, Typeface.ITALIC);
        return textView;
    }

    public TextView textView(String value, String tag, boolean border, int minWidth, int color, int fontSize, boolean isBold, boolean isItalic, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this.context);
        textView.setLayoutParams(layoutParams);
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        textView.setText(value);
        textView.setMinWidth(minWidth);
        textView.setTag(tag);
        if (border)
            textView.setBackground(context.getResources().getDrawable(android.R.drawable.editbox_background));
        textView.setTextColor(color);
        textView.setTextSize(fontSize);
        if (isBold)
            textView.setTypeface(null, Typeface.BOLD);
        if (isItalic)
            textView.setTypeface(null, Typeface.ITALIC);
        return textView;
    }

    public EditText editText(String val, String tag, int type, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText editText = new EditText(this.context);
//        editText.setMinimumWidth(300);
        editText.setTag(tag);
        editText.setText(val);
        editText.setTextSize(15);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(true);
        editText.setInputType(type);
        editText.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        return editText;
    }

    public Spinner spinner(SpinnerAdapter spinnerAdapter, String tag, int position) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = 48;
        Spinner spinner = new Spinner(this.context);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(position);
//        spinner.setBackgroundResource(R.drawable.spinner_background);
        spinner.setMinimumWidth(800);
        spinner.setTag(tag);
        spinner.setLayoutParams(layoutParams);
        spinner.setGravity(Gravity.CENTER_VERTICAL);
        return spinner;
    }

}
