package pl.droidsonroids.bootcamp.yo.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import pl.droidsonroids.bootcamp.yo.model.User;

public class UserItemViewVolder extends RecyclerView.ViewHolder {

    public UserItemViewVolder(View itemView) {
        super(itemView);
    }

    public void bindData(User user) {
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        SpannableString spannableString =
                new SpannableString(user.getName().substring(0, 1).toUpperCase() + " " + user.getName());
        spannableString.setSpan(bss, 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ((TextView) itemView).setText(spannableString);
        itemView.setBackgroundColor(Color.WHITE);
    }

    public void bindDataAndColor(User user) {
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        SpannableString spannableString =
                new SpannableString(user.getName().substring(0, 1).toUpperCase() + " " + user.getName());
        spannableString.setSpan(bss, 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ((TextView) itemView).setText(spannableString);
        itemView.setBackgroundColor(Color.BLUE);
    }
}
