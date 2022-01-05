package com.example.fypintelidea.core.views.snackbar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.fypintelidea.R;
import com.google.android.material.snackbar.Snackbar;

public class SSnackbar {
    public static class Builder implements CustomSnackbar {
        protected String text = "";
        protected int duration = Snackbar.LENGTH_SHORT;
        protected float textSize;
        protected int actionTextColor = 0;
        protected int textColor = Color.WHITE;
        protected int backgroundColor = Color.BLACK;
        OnClickListener action = null;
        protected SnackbarStyle style = SnackbarStyle.NORMAL;
        protected SnackbarPosition position = SnackbarPosition.BOTTOM;
        protected Typeface typeface = null;
        protected View rootView;
        protected Snackbar snackBar;

        public Builder(View rootView) {
            this.rootView = rootView;
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
        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        @Override
        public Builder setStyle(SnackbarStyle style) {
            this.style = style;
            return this;
        }

        @Override
        public Builder setPosition(SnackbarPosition position) {
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

        public Builder setActionTextColor(int actionTextColor) {
            this.actionTextColor = actionTextColor;
            return this;
        }

        public void setAction(OnClickListener action) {
            this.action = action;
        }

        public Snackbar build() {
            snackBar = Snackbar.make(rootView, text, duration);
            styleSnackbar(snackBar);
            return snackBar;
        }

        protected Snackbar styleSnackbar(Snackbar snackbar) {
            if (snackbar == null)
                throw new IllegalStateException("Snackbar can never be null! Review your Builder implementation");

            View snackBarView = snackbar.getView();
            TextView snackBarTextView = snackBarView.findViewById(R.id.snackbar_text);
            snackBarView.setBackgroundColor(backgroundColor);
            snackBarTextView.setTextColor(textColor);
            snackBarTextView.setTypeface(typeface, style.value());
            if (actionTextColor != 0)
                snackbar.setActionTextColor(actionTextColor);
            snackbar.setAction(text, action);

            return snackbar;
        }
    }
}