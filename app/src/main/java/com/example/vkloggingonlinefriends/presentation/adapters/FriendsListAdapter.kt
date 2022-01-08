package com.example.vkloggingonlinefriends.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vkloggingonlinefriends.R
import com.example.vkloggingonlinefriends.databinding.ItemFriendBinding
import com.example.vkloggingonlinefriends.domain.model.Friend
import com.example.vkloggingonlinefriends.utils.VK_ONLINE

class FriendsListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Friend, FriendsListAdapter.FriendViewHolder>(FriendDiffCallback()) {

    private lateinit var binding: ItemFriendBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = getItem(position)
        holder.firstName.text = friend.firstName
        holder.lastName.text = friend.lastName
        Glide.with(holder.photoFriends)
            .load(friend.photo)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.photoFriends)
        if (friend.online == VK_ONLINE) {
            holder.onlineStatus.setImageResource(R.drawable.online_img)
        } else {
            holder.onlineStatus.setImageResource(R.drawable.offline_img)
        }
        holder.itemView.setOnClickListener {
            onClickListener.onClick(friend)
        }

        if (friend.logging) {
            holder.constraintLayout.setBackgroundResource(R.drawable.rectangle_blue)
        } else {
            holder.constraintLayout.setBackgroundResource(R.drawable.rectangle_gray)
        }
    }

    inner class FriendViewHolder(binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val firstName = binding.textFirstNameItem
        val lastName = binding.textLastNameItem
        val photoFriends = binding.imageViewFriendItem
        val onlineStatus = binding.imageOnlineItem
        val constraintLayout = binding.layoutFriendItem
    }

    class OnClickListener(val clickListener: (friend: Friend) -> Unit) {
        fun onClick(friend: Friend) = clickListener(friend)
    }

}

class FriendDiffCallback : DiffUtil.ItemCallback<Friend>() {

    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean =
        oldItem.id == newItem.id && oldItem.firstName == newItem.firstName
                && oldItem.lastName == newItem.lastName && oldItem.photo == newItem.photo
                && oldItem.online == newItem.online && oldItem.logging == newItem.logging

}