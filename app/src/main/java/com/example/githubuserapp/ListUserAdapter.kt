package com.example.githubuserapp

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListUserAdapter(private val listUser: ArrayList<DataUser>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_user, viewGroup, false)
        return ListViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        val id: Int = holder.itemView.resources.getIdentifier("com.example.githubuserapp:drawable/${user.avatar}", null, null)
        holder.imgPhoto.setImageResource(id)
        holder.tvUsername.text = user.name
        holder.tvLocation.text = user.location
        holder.tvFollowers.text = user.followers + " Followers"
        holder.tvFollowing.text = user.following + " Following"

        val userGithub = DataUser(
            user.username,
            user.name,
            user.location,
            user.repository,
            user.company,
            user.followers,
            user.following,
            user.avatar
        )

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, MovieDetail::class.java)
            intent.putExtra("userGithub", userGithub)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        var tvLocation: TextView = itemView.findViewById(R.id.tv_location)
        var tvFollowers: TextView = itemView.findViewById(R.id.tv_followers)
        var tvFollowing: TextView = itemView.findViewById(R.id.tv_following)
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
    }
}