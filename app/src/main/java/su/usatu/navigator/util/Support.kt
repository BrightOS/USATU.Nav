package su.usatu.navigator.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import java.lang.reflect.Field


@ColorInt
fun getColorFromAttributes(context: Context?, res: Int): Int {
    val typedValue = TypedValue()
    val theme: Resources.Theme = context?.theme!!
    theme.resolveAttribute(res, typedValue, true)
    return typedValue.data
}

fun setMarqueeSpeed(tv: TextView?, speed: Float) {
    if (tv != null) {
        try {
            val f = if (tv is AppCompatTextView)
                tv.javaClass.superclass.getDeclaredField("mMarquee")
            else
                tv.javaClass.getDeclaredField("mMarquee")

            f.isAccessible = true
            val marquee: Any = f.get(tv)
            val scrollSpeedFieldName = "mPixelsPerSecond"
            val mf: Field = marquee.javaClass.getDeclaredField(scrollSpeedFieldName)
            mf.isAccessible = true
            mf.setFloat(marquee, speed)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun getStatusBarHeight(resources: Resources): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun getNavigationBarHeight(resources: Resources): Int {
    var result = 0
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}