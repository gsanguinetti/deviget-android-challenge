package com.gsanguinetti.reddittopposts.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.presentation.viewmodel.PostListViewModel
import kotlinx.android.synthetic.main.fragment_post_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostListFragment : Fragment() {

    private val postListViewModel: PostListViewModel by sharedViewModel()
    private var postListAdapter: PostsListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postRecyclerView.layoutManager = LinearLayoutManager(context)
        postRecyclerView.setHasFixedSize(true)

        postListAdapter = PostsListAdapter().apply { postRecyclerView.adapter = this }
        swipeRefreshLayout.setOnRefreshListener { postListViewModel.onRefreshData() }

        postListViewModel.postList.observe(this) {
            postListAdapter?.submitList(it)
        }
        postListViewModel.loadingInitial.observe(this) {
            swipeRefreshLayout.isRefreshing = it
        }
        postListViewModel.loadingNext.observe(this) {
            if (it) postListAdapter?.onLoadingNextPage()
        }
    }
}