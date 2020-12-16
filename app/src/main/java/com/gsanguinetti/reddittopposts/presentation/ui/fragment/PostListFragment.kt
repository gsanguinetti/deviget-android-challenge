package com.gsanguinetti.reddittopposts.presentation.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.base.presentation.DividerSpacingDecoration
import com.gsanguinetti.reddittopposts.base.presentation.showError
import com.gsanguinetti.reddittopposts.presentation.ui.PostsListAdapter
import com.gsanguinetti.reddittopposts.presentation.viewmodel.PostListViewModel
import kotlinx.android.synthetic.main.fragment_post_list.*
import kotlinx.android.synthetic.main.view_empty_item.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostListFragment : Fragment() {

    private val postListViewModel: PostListViewModel by sharedViewModel()
    private var postListAdapter: PostsListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (postListViewModel.canDismissAll.value == true &&
            postListAdapter?.currentList?.isNullOrEmpty() == false
        )
            inflater.inflate(R.menu.post_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.dismiss_all) {
            postListAdapter?.currentList?.let { postListViewModel.onDismissingAll(it) }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postRecyclerView.layoutManager = LinearLayoutManager(context)
        postRecyclerView.setHasFixedSize(true)
        postRecyclerView.addItemDecoration(
            DividerSpacingDecoration(
                context?.resources?.getDimensionPixelOffset(R.dimen.default_padding) ?: 0
            )
        )

        postListAdapter =
            PostsListAdapter(postListViewModel) {
                emptyListLayout.visibility =
                    if (postListViewModel.loadingInitial.value != true) View.VISIBLE
                    else View.GONE
                activity?.invalidateOptionsMenu()
            }.apply { postRecyclerView.adapter = this }
        swipeRefreshLayout.setOnRefreshListener { postListViewModel.onRefreshData() }


        postListViewModel.postList.observe(this) {
            postListAdapter?.submitList(it)
        }
        postListViewModel.loadingInitial.observe(this) {
            swipeRefreshLayout.isRefreshing = it
            if (!it) evaluateEmptyView()
        }
        postListViewModel.loadingNext.observe(this) {
            if (it) postListAdapter?.onLoadingNextPage()
        }
        postListViewModel.errorFetchingData.observe(this) {
            showError(R.string.error_fetching_data)
            emptyListLayout.visibility = View.GONE
        }
        postListViewModel.errorDismissingPost.observe(this) {
            showError(R.string.error_dismissing_posts)
        }
        postListViewModel.errorSelectingPost.observe(this) {
            showError(R.string.error_selecting_post)
        }
        postListViewModel.canDismissAll.observe(this) {
            activity?.invalidateOptionsMenu()
        }
        postListViewModel.openUrlEvent.observe(this) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }
    }

    fun evaluateEmptyView() {
        emptyListLayout.visibility =
            if (postListViewModel.loadingInitial.value != true &&
                postListAdapter?.itemCount == 0
            ) View.VISIBLE else View.GONE
    }
}