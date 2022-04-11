package su.usatu.navigator.epoxy

import android.annotation.SuppressLint
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import su.usatu.navigator.R
import su.usatu.navigator.models.QueryModel
import su.usatu.navigator.ui.main.OnHistoryClickListener
import su.usatu.navigator.util.KotlinHolder

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.item_history)
abstract class HistoryEpoxyModel : EpoxyModelWithHolder<HistoryEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var onHistoryClickListener: OnHistoryClickListener

    @EpoxyAttribute
    lateinit var queryModel: QueryModel

    @EpoxyAttribute
    lateinit var from: String

    @EpoxyAttribute
    lateinit var to: String

    override fun bind(holder: Holder) {
        holder.fromText.text = from
        holder.toText.text = to
        holder.root.setOnClickListener {
            onHistoryClickListener.onHistoryClick(queryModel, id())
        }
    }

    inner class Holder : KotlinHolder() {
        val root: MaterialCardView by bind(R.id.root)
        val fromText: TextView by bind(R.id.from_text)
        val toText: TextView by bind(R.id.to_text)
    }
}