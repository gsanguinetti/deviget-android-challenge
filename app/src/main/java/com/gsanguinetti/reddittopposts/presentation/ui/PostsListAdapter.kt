package com.gsanguinetti.reddittopposts.presentation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import kotlinx.android.synthetic.main.view_post_item.view.*

class PostsListAdapter : PagedListAdapter<RedditPost, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<RedditPost>() {
        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean =
            oldItem == newItem
    }
) {
    companion object {
        private const val POST_ITEM = 1
        private const val LOADING_ITEM = 2
    }

    private var _loadingNextPage: Boolean = false

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && _loadingNextPage) LOADING_ITEM else POST_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == POST_ITEM) RedditPostViewHolder.create(parent)
        else LoadingViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            POST_ITEM -> (holder as RedditPostViewHolder).bind(getItem(position))
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (_loadingNextPage) 1 else 0
    }

    override fun onCurrentListChanged(
        previousList: PagedList<RedditPost>?,
        currentList: PagedList<RedditPost>?
    ) {
        super.onCurrentListChanged(previousList, currentList)
        if (_loadingNextPage) setLoadingNextPage(false)
    }

    fun onLoadingNextPage() {
        setLoadingNextPage(true)
    }

    private fun setLoadingNextPage(loadingNextPage: Boolean) {
        val previousState = _loadingNextPage
        _loadingNextPage = loadingNextPage
        if (previousState != _loadingNextPage) {
            if (_loadingNextPage) notifyItemInserted(super.getItemCount())
            else notifyItemRemoved(super.getItemCount())
        }
    }

}

class RedditPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(redditPost: RedditPost?) {
        itemView.postTitleTextView?.text = redditPost?.title
    }

    companion object {
        fun create(parent: ViewGroup): RedditPostViewHolder {
            return RedditPostViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_post_item, parent, false)
            )
        }
    }
}

class LoadingViewHolder {
    companion object {
        fun create(parent: ViewGroup): RedditPostViewHolder {
            return RedditPostViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_loading_next, parent, false)
            )
        }
    }
}