package com.example.fypintelidea.core.views.textview;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

public interface CustomTextView {
    public enum TextViewStyle {
        NORMAL(Typeface.NORMAL),
        BOLD(Typeface.BOLD),
        ITALIC(Typeface.ITALIC),
        BOLD_ITALIC(Typeface.BOLD_ITALIC);

        private final int value;

        private TextViewStyle(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    public enum TextViewPosition {
        BOTTOM, TOP
    }

    TTextView.Builder setTextColor(int value);

    TTextView.Builder setBackgroundColor(int value);

    TTextView.Builder setBackground(Drawable background);

    TTextView.Builder setStyle(TextViewStyle style);

    TTextView.Builder setPosition(TextViewPosition position);

    TTextView.Builder setTypeface(Typeface typeface);

    TTextView.Builder setText(String text);

    TTextView.Builder setText(String text, TextView.BufferType type);

    TTextView.Builder setTag(Object tag);

    TTextView.Builder setId(int id);

    TTextView.Builder setMinWidth(int minPixels);

    TTextView.Builder setPadding(int left, int top, int right, int bottom);
}