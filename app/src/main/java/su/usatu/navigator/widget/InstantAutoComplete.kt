package su.usatu.navigator.widget

import android.content.Context
import android.util.AttributeSet

class InstantAutoComplete : androidx.appcompat.widget.AppCompatAutoCompleteTextView {
    constructor(context: Context) : super(context)
    constructor(arg0: Context, arg1: AttributeSet?) : super(arg0, arg1)
    constructor(arg0: Context, arg1: AttributeSet?, arg2: Int) : super(arg0, arg1, arg2)

    override fun enoughToFilter(): Boolean {
        return true
    }
}