package su.usatu.navigator.ui.main

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import su.usatu.navigator.models.PointModel


class PointsAdapter(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    private val items: List<PointModel>
) : ArrayAdapter<PointModel>(context, resource, textViewResourceId, items) {
    private val tempList = ArrayList<PointModel>(items)
    private val suggestions = arrayListOf<PointModel>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (convertView == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.simple_list_item_1, parent, false)
        }
        val point = items.get(position)
        val lblName = view?.findViewById(R.id.text1) as TextView
        lblName.text = point.title
        return view
    }

    override fun getFilter(): Filter {
        return titleFilter
    }

    val titleFilter = object : Filter() {
        override fun convertResultToString(resultValue: Any?) =
            (resultValue as PointModel).title

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null) {
                suggestions.clear()
                tempList.forEach {
                    if (it.title.contains(constraint.toString(), false) || constraint.length < 1)
                        suggestions.add(it)
                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else {
                FilterResults()
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null && results.count > 0) {
                clear()
                (results.values as ArrayList<*>).forEach {
                    add(it as PointModel)
                }
                notifyDataSetChanged()
            }
        }

    }
}