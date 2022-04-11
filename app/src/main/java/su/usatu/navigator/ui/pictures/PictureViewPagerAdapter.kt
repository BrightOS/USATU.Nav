package su.usatu.navigator.ui.pictures

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import ru.followy.util.followy_extensions.api.APIConfig
import su.usatu.navigator.R
import java.util.*


class PictureViewPagerAdapter(
    context: Context?,
    private val imagesList: List<String>
) : PagerAdapter() {

    private val layoutInflater =
        context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int =
        imagesList.size

    override fun isViewFromObject(view: View, `object`: Any) =
        view == (`object` as ImageView)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = layoutInflater.inflate(R.layout.item_picture, container, false)

        val imageView = itemView.findViewById<View>(R.id.picture) as ImageView

        val url = "${APIConfig.urlImage}${imagesList[position]}"
        Glide.with(imageView)
            .load(url)
            .into(imageView)

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }
}