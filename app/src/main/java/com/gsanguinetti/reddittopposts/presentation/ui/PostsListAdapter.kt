package com.gsanguinetti.reddittopposts.presentation.ui

import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.presentation.viewmodel.PostListViewModel
import kotlinx.android.synthetic.main.view_loading_next.view.*
import kotlinx.android.synthetic.main.view_post_item.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class PostsListAdapter(private val listViewModel: PostListViewModel) :
    PagedListAdapter<RedditPost, RecyclerView.ViewHolder>(
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
        private const val EMPTY_ITEM = 3
    }

    private var _loadingNextPage: Boolean = false
    private var _loadingInitial: Boolean = true
    private var empty: Boolean = false

    override fun getItemViewType(position: Int): Int {
        return when {
            (position == itemCount - 1 && _loadingNextPage) -> LOADING_ITEM
            empty && !_loadingNextPage -> EMPTY_ITEM
            else -> POST_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            POST_ITEM -> RedditPostViewHolder.create(parent, listViewModel)
            EMPTY_ITEM -> EmptyViewHolder.create(parent)
            LOADING_ITEM -> LoadingViewHolder.create(parent)
            else -> throw IllegalAccessException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!empty) {
            when (getItemViewType(position)) {
                POST_ITEM -> (holder as RedditPostViewHolder).bind(getItem(position))
                LOADING_ITEM -> (holder as LoadingViewHolder).bind(_loadingNextPage)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (empty && !_loadingNextPage) 1
        else super.getItemCount() + if (_loadingNextPage) 1 else 0
    }

    override fun onCurrentListChanged(
        previousList: PagedList<RedditPost>?,
        currentList: PagedList<RedditPost>?
    ) {
        super.onCurrentListChanged(previousList, currentList)

        //Delayed removal of the bottom progress bar, in order to have a smoothly interaction with the
        //new items being inserted
        Handler(Looper.getMainLooper()).postDelayed(
            { if (_loadingNextPage) setLoadingNextPage(false) },
            20
        )

        if (!_loadingNextPage && !_loadingInitial && currentList.isNullOrEmpty()) {
            empty = true
            notifyDataSetChanged()
        }

        if (!_loadingNextPage && !currentList.isNullOrEmpty() && empty) {
            empty = false
            notifyItemRangeChanged(0, itemCount)
        }
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

    fun setLoadingInitial(loading: Boolean) {
        _loadingInitial = loading
    }
}

class RedditPostViewHolder(
    itemView: View,
    private val viewModel: PostListViewModel
) : RecyclerView.ViewHolder(itemView) {

    val timeFormatter = PrettyTime(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            itemView.context.resources.configuration.locales.get(0)
        else itemView.context.resources.configuration.locale
    )

    fun bind(redditPost: RedditPost?) {
        if (redditPost != null) {
            itemView.run {

                postLayout.setOnClickListener { viewModel.onPostSelected(redditPost) }

                postTitleTextView?.run {
                    text = redditPost.title
                    isEnabled = !redditPost.read
                }

                subredditNameTextView?.text = redditPost.subredditName
                postAttrsTextView?.text = context.getString(
                    R.string.post_attrs_format,
                    redditPost.author, timeFormatter.format(Date(redditPost.createdAt))
                )

                thumbnailImageView.visibility =
                    if (Uri.parse(redditPost.thumbnail).host == null)
                        View.GONE else View.VISIBLE

                viewModel.isThumbnailClickable(redditPost).apply {
                    thumbnailImageView.isClickable = this
                    if (this) thumbnailImageView.setOnClickListener {
                        viewModel.onThumbnailClicked(
                            redditPost
                        )
                    }
                }

                Glide.with(subredditIconImageView.context)
                    .load(redditPost.subredditIcon)
                    .error(R.drawable.ic_avatar_placeholder)
                    .transforms(CenterCrop(), CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(subredditIconImageView)

                Glide.with(thumbnailImageView.context)
                    .load(redditPost.thumbnail)
                    .transforms(
                        CenterCrop(),
                        RoundedCorners(
                            thumbnailImageView.context.resources
                                .getDimensionPixelOffset(R.dimen.thumbnail_rounded_corners)
                        )
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(thumbnailImageView)

                dismissPostImageView.setOnClickListener {
                    viewModel.onDismissPost(redditPost)
                }

                commentsAmountTextView?.setText(redditPost.commentsCount.toString())
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewModel: PostListViewModel): RedditPostViewHolder {
            return RedditPostViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_post_item, parent, false),
                viewModel
            )
        }
    }
}

class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(progressBarEnabled: Boolean) {
        itemView.loadingMoreProgressBar.visibility =
            if (progressBarEnabled) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            return LoadingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_loading_next, parent, false)
            )
        }
    }
}

class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            return LoadingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_empty_item, parent, false)
            )
        }
    }
}