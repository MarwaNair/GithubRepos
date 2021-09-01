package com.sriyank.javatokotlindemo.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.sriyank.javatokotlindemo.adapters.DisplayAdapter.MyViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sriyank.javatokotlindemo.R
import android.content.Intent
import android.net.Uri
import android.view.View
import com.sriyank.javatokotlindemo.extensions.toast
import com.sriyank.javatokotlindemo.models.Repository
import io.realm.Realm
import kotlinx.android.synthetic.main.list_item.view.*

class DisplayAdapter(context: Context, items: List<Repository>) : RecyclerView.Adapter<MyViewHolder>() {
    private var repositoriesList: List<Repository>
    private val context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = repositoriesList[position]
        holder.setData(current, position)
    }

    override fun getItemCount(): Int = repositoriesList.size


    fun swap(dataList: List<Repository>) {
        if (dataList.isEmpty()) context.toast("No Items Found")
        repositoriesList = dataList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var pos = 0
        private var current: Repository? = null
        fun setData(current: Repository? , position: Int) {
            current?.let {
                itemView.txvName.text = current.name
                itemView.txvLanguage.text = current.language
                itemView.txvForks.text = current.forks.toString()
                itemView.txvWatchers.text = current.watchers.toString()
                itemView.txvStars.text = current.stars.toString()

            }
            this.pos = position
            this.current = current
        }

        private fun bookmarkRepository(current: Repository?) {

            current?.let {
                val realm = Realm.getDefaultInstance()
                realm.executeTransactionAsync(
                        { realm -> realm.copyToRealmOrUpdate(current) },
                        { context.toast("Bookmarked Successfully") },
                        { context.toast("Error Occurred") }
                )
            }




        }

        init {

            itemView.imgBookmark.setOnClickListener { bookmarkRepository(current) }
            itemView.setOnClickListener {
                current?.let {
                    val url = current!!.htmlUrl
                    val webpage = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }

            }
        }
    }

    companion object {
        private val TAG = DisplayAdapter::class.java.simpleName
    }

    init {
        repositoriesList = items
        this.context = context
    }
}