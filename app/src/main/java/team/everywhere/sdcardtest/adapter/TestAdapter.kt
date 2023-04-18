package team.everywhere.sdcardtest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import team.everywhere.sdcardtest.R

class TestAdapter(var context: Context, var itemList: ArrayList<String>, var onItemClicked: OnItemClicked) :
    RecyclerView.Adapter<TestAdapter.ItemViewHolder>() {
    interface OnItemClicked{
        fun itemClicked(p:Int)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(s:String){
            itemView.findViewById<TextView>(R.id.tv).text = s
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false)
        return TestAdapter.ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(itemList[position])
        holder.itemView.setOnClickListener {
            onItemClicked.itemClicked(position)
        }
    }
}