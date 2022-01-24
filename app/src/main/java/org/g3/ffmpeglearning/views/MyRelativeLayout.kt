package org.g3.ffmpeglearning.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout

class MyRelativeLayout : RelativeLayout {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {

    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {}

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        Log.d("TTTT","MyRelativeLayout onLayout changed = $changed left = $l, top=$t, right=$r, bottom=$b")
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        Log.d("TTTT","MyRelativeLayout onViewAdded")
    }
}