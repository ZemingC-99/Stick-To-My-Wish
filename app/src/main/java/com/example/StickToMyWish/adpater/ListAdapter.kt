package com.example.StickToMyWish.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.StickToMyWish.MyApplication
import com.example.StickToMyWish.MyApplication.Companion.projectIndex
import com.example.StickToMyWish.R
import com.example.StickToMyWish.entity.Project

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    var projectList: List<Project>? = ArrayList()
    lateinit var itemClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.title.text = projectList!![position].title
        holder.tab.text = projectList!![position].tab
        holder.content.text = projectList!![position].content
        holder.day.text = "(${projectList!![position].start}) — (${projectList!![position].end})"

        holder.itemView.setOnClickListener {
            MyApplication.projectList = projectList as List<Project>
            projectIndex = position
            itemClickListener.onClick(it)
        }
    }

    override fun getItemCount(): Int {
        return projectList!!.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tab: TextView
        var title: TextView
        var content: TextView
        var day: TextView

        init {
            tab = itemView.findViewById(R.id.item_list_tab)
            title = itemView.findViewById(R.id.item_list_title)
            content = itemView.findViewById(R.id.item_list_content)
            day = itemView.findViewById(R.id.item_list_day)
        }
    }
}