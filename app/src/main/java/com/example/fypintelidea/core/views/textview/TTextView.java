package com.example.fypintelidea.core.views.textview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TTextView {
    public static class Builder implements CustomTextView {
        protected String text = "";
        private TextView.BufferType bufferType = TextView.BufferType.NORMAL;
        protected float textSize;
        protected int textColor = Color.BLACK;
        protected int backgroundColor = Color.WHITE;
        OnClickListener action = null;
        protected TextViewStyle style = TextViewStyle.NORMAL;
        protected TextViewPosition position = TextViewPosition.BOTTOM;
        protected Typeface typeface = null;
        protected int id;
        protected Object tag;
        TextView textView;
        private int minPixels;
        private int left;
        private int top;
        private int right;
        private int bottom;
        private Drawable background = null;

        public Builder(Context context) {
            textView = new TextView(context);
        }

        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        @Override
        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        @Override
        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        @Override
        public Builder setBackground(Drawable background) {
            this.background = background;
            return this;
        }

        @Override
        public Builder setStyle(TextViewStyle style) {
            this.style = style;
            return this;
        }

        @Override
        public Builder setPosition(TextViewPosition position) {
            this.position = position;
            return this;
        }

        @Override
        public Builder setTypeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        @Override
        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        @Override
        public Builder setText(String text, TextView.BufferType type) {
            this.text = text;
            this.bufferType = type;
            return this;
        }

        @Override
        public Builder setTag(Object tag) {
            this.tag = tag;
            return this;
        }

        @Override
        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder setMinWidth(int minPixels) {
            this.minPixels = minPixels;
            return this;
        }

        @Override
        public Builder setPadding(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            return this;
        }

        public void setAction(OnClickListener action) {
            this.action = action;
        }

        public TextView build() {
            textView.setText(text);
            styleTextView(textView);
            return textView;
        }

        protected TextView styleTextView(TextView textView) {
            if (textView == null)
                throw new IllegalStateException("TextView can never be null! Review your Builder implementation");

            textView.setText(text, bufferType);
            textView.setBackgroundColor(backgroundColor);
            textView.setBackground(background);
            textView.setTextColor(textColor);
            textView.setTypeface(typeface, style.value());
            textView.setOnClickListener(action);
            textView.setMinWidth(minPixels);
            textView.setTag(tag);
            textView.setId(id);
            textView.setPadding(left, top, right, bottom);
            textView.setOnClickListener(action);

            return textView;
        }
    }
}