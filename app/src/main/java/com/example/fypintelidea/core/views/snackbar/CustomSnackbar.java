package com.example.fypintelidea.core.views.snackbar;

import android.graphics.Typeface;


public interface CustomSnackbar {
    public enum SnackbarStyle {
        NORMAL(Typeface.NORMAL),
        BOLD(Typeface.BOLD),
        ITALIC(Typeface.ITALIC),
        BOLD_ITALIC(Typeface.BOLD_ITALIC);

        private final int value;

        private SnackbarStyle(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    public enum SnackbarPosition {
        BOTTOM, TOP
    }

    SSnackbar.Builder setTextColor(int value);
    SSnackbar.Builder setBackgroundColor(int value);
    SSnackbar.Builder setStyle(SnackbarStyle style);
    SSnackbar.Builder setPosition(SnackbarPosition position);
    SSnackbar.Builder setTypeface(Typeface typeface);
    SSnackbar.Builder setText(String text);
    SSnackbar.Builder setDuration(int duration);
}