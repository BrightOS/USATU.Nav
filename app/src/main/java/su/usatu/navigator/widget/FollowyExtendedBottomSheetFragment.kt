package su.usatu.navigator.widget

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import su.usatu.navigator.R
import su.usatu.navigator.util.getColorFromAttributes
import su.usatu.navigator.util.getNavigationBarHeight


open class FollowyExtendedBottomSheetFragment :
    BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.findViewById<View>(com.google.android.material.R.id.container)?.fitsSystemWindows =
            false
        dialog.setOnShowListener { dialogInterface: DialogInterface ->
            val d = dialogInterface as BottomSheetDialog
            val bottomSheet =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            val newMaterialShapeDrawable = createMaterialShapeDrawable(bottomSheet)
            ViewCompat.setBackground(bottomSheet, newMaterialShapeDrawable)
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val newMaterialShapeDrawable = createMaterialShapeDrawable(bottomSheet)
                ViewCompat.setBackground(bottomSheet, newMaterialShapeDrawable)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val newMaterialShapeDrawable = createMaterialShapeDrawable(bottomSheet)
                ViewCompat.setBackground(bottomSheet, newMaterialShapeDrawable)
            }
        })
        return dialog
    }

    override fun onStart() {
        super.onStart()
        println("FUUUUUUUUUUUUUUUUUUUUUUUCK")
        if (dialog != null && dialog!!.window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val window: Window? = dialog!!.window
            println("FUUUUUUUUUUUUUUCK")
            window?.findViewById<View>(com.google.android.material.R.id.container)?.fitsSystemWindows =
                false
            // dark navigation bar icons
            val decorView: View = window!!.decorView
            decorView.systemUiVisibility =
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels - getNavigationBarHeight(resources)
    }

    private fun createMaterialShapeDrawable(bottomSheet: View): MaterialShapeDrawable {
        val shapeAppearanceModel =
            ShapeAppearanceModel.builder(context, 0, R.style.CustomShapeAppearanceBottomSheetDialog)
                .build()
        val currentMaterialShapeDrawable = bottomSheet.background as MaterialShapeDrawable
        val newMaterialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        newMaterialShapeDrawable.initializeElevationOverlay(requireContext())
        newMaterialShapeDrawable.fillColor =
            ColorStateList.valueOf(getColorFromAttributes(context, android.R.attr.colorBackground))
        newMaterialShapeDrawable.setPadding(10, 10, 10, 10)
        newMaterialShapeDrawable.tintList = currentMaterialShapeDrawable.tintList
        newMaterialShapeDrawable.elevation = currentMaterialShapeDrawable.elevation
        newMaterialShapeDrawable.strokeWidth = currentMaterialShapeDrawable.strokeWidth
        newMaterialShapeDrawable.strokeColor = currentMaterialShapeDrawable.strokeColor
        return newMaterialShapeDrawable
    }
}