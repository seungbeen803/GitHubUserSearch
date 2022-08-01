package com.example.githubusersearch

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GitHubAdaptor(val datalist: List<GitHubUserRepo>)
    : RecyclerView.Adapter<GitHubAdaptor.ItemViewHolder>()
{
        class ItemViewHolder(val view: View)
            :RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = datalist[position]
        holder.view.findViewById<TextView>(R.id.name).text = "${data.name}"
        holder.view.findViewById<TextView>(R.id.description).text = "${data.description}"
        holder.view.findViewById<TextView>(R.id.forks_count).text = "${data.forks_count}"
        holder.view.findViewById<TextView>(R.id.watchers_count).text = "${data.watchers_count}"
        holder.view.findViewById<TextView>(R.id.stargazers_count).text = "${data.stargazers_count}"

        // 암시적 인텐트
        holder.view.setOnClickListener {
            var intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(data.html_url)
            )
            holder.view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        // 전달한 리스트의 길이 리턴
        return datalist.size
    }

    override fun getItemViewType(position: Int): Int {
        // 유일하게 구분하게 할 수 있는 숫자 리턴
        // 레이아웃 리소스 식별자를 숫자로 반환
        return R.layout.item_recycler
    }
}