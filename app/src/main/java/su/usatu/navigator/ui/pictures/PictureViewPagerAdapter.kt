package su.usatu.navigator.ui.pictures

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jsibbold.zoomage.ZoomageView
import ru.followy.util.followy_extensions.api.APIConfig
import su.usatu.navigator.R


class PictureViewPagerAdapter(
    context: Context?
) : ListAdapter<String, PictureViewPagerAdapter.Holder>(ImageDiffCallback()) {

    private val layoutInflater =
        context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PictureViewPagerAdapter.Holder {
        val view = layoutInflater.inflate(R.layout.item_picture, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val url = "${APIConfig.urlImage}${currentList[position]}"

        Glide.with(holder.imageView)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(holder.imageView)

        // Objects.requireNonNull(container).addView(itemView)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ZoomageView>(R.id.picture)
    }
}