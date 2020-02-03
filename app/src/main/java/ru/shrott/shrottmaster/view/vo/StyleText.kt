package ru.shrott.shrottmaster.view.vo

import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import ru.shrott.shrottmaster.R

@BindingAdapter("textSpannable")
fun setText(textVew: TextView, text: String) {
    textVew.text = setSpannable(textVew, text)
}

fun setSpannable(textVew: TextView, myText: String): SpannableString {
    val spannableContent = SpannableString(myText)
    spannableContent.setSpan(ForegroundColorSpan(ContextCompat.getColor(textVew.context, R.color.colorPrimaryDark)),
        0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannableContent
}