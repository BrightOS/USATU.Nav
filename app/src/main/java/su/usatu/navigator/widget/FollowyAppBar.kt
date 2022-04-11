package su.usatu.navigator.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import su.usatu.navigator.R
import su.usatu.navigator.util.getColorFromAttributes


class FollowyAppBar constructor(
    cont: Context,
    attrs: AttributeSet?
) : AppBarLayout(cont, attrs) {

    fun setStartButtonOnClickListener(p0: (View) -> Unit) {
        findViewById<ImageView>(R.id.start_button).setOnClickListener(p0)
    }

    fun setEndButtonOnClickListener(p0: (View) -> Unit) {
        findViewById<ImageView>(R.id.end_button).setOnClickListener(p0)
    }

    fun hideEndButton() {
        findViewById<ImageView>(R.id.end_button).visibility = View.INVISIBLE
    }

    fun showEndButton() {
        findViewById<ImageView>(R.id.end_button).visibility = View.VISIBLE
    }

    val startButton: ImageView
        get() = findViewById(R.id.start_button)

    val endButton: ImageView
        get() = findViewById(R.id.end_button)

    init {
        View.inflate(context, R.layout.layout_followy_appbar, this)

        elevation = 0f
        background = ColorDrawable(getColorFromAttributes(cont, android.R.attr.colorPrimaryDark))

        addOnOffsetChangedListener(object : OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                this@FollowyAppBar.findViewById<TextView>(R.id.appbar_title)
                    ?.setTextSize(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24f + verticalOffset / (appBarLayout.totalScrollRange.toFloat() / 4)
                    )
            }
        })

        val text = findViewById<View>(R.id.appbar_title) as TextView
        text.isSelected = true

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.FollowyAppBar)

            typedArray.getResourceId(
                R.styleable.FollowyAppBar_startIcon,
                0
            ).let {
                if (it != 0)
                    findViewById<ImageView>(R.id.start_button).setImageResource(it)
            }

            typedArray.getResourceId(
                R.styleable.FollowyAppBar_endIcon,
                0
            ).let {
                if (it != 0)
                    findViewById<ImageView>(R.id.end_button).setImageResource(it)
            }

            findViewById<TextView>(R.id.appbar_title).text = typedArray.getText(
                R.styleable.FollowyAppBar_titleText
            )

            typedArray.recycle()
        }
    }
}