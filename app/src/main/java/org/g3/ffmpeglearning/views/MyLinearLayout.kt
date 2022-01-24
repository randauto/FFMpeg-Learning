package org.g3.ffmpeglearning.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class MyLinearLayout : LinearLayout {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {

    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {}
}