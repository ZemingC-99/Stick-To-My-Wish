package com.example.StickToMyWish.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.StickToMyWish.R
import com.example.StickToMyWish.adpater.TabsAdapter.TabsViewHolder
import com.example.StickToMyWish.entity.Tab
import java.util.function.Consumer

class TabsAdapter : RecyclerView.Adapter<TabsViewHolder>() {

    var tabList: List<Tab>? = ArrayList()

    var itemClickListener : Consumer<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabsViewHolder {
        return TabsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_tabs, null, false)
        )
    }

    override fun onBindViewHolder(holder: TabsViewHolder, position: Int) {
        if (position == 0) {
            holder.tab.text = "All "
            holder.itemView.setOnClickListener{
                itemClickListener?.accept("")
            }
        } else {
            holder.itemView.setOnClickListener{
                itemClickListener?.accept(tabList!![position - 1].name)
            }
            holder.tab.text = tabList!![position - 1].name
        }

    }

    override fun getItemCount(): Int {
        return tabList!!.size + 1
    }

    inner class TabsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tab: TextView
        init {
            tab = itemView.findViewById(R.id.item_tab_text)
        }
    }
}