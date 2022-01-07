package com.example.vkloggingonlinefriends.presentation.ui.friendslist

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.example.vkloggingonlinefriends.R
import com.example.vkloggingonlinefriends.databinding.FragmentFriendslistBinding
import com.example.vkloggingonlinefriends.presentation.adapters.FriendsListAdapter
import com.example.vkloggingonlinefriends.presentation.ui.friendslist.FriendsListEvent.LoadingFriends
import com.example.vkloggingonlinefriends.presentation.ui.friendslist.FriendsListEvent.SearchFriends
import com.example.vkloggingonlinefriends.presentation.ui.friendslist.FriendsListState.*
import com.example.vkloggingonlinefriends.utils.EMPTY_STRING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FriendsListFragment : Fragment() {

    private lateinit var binding: FragmentFriendslistBinding
    private val viewModel: FriendsListViewModel by viewModels()
    private var showAllFriends = true
    private val adapter = FriendsListAdapter(FriendsListAdapter.OnClickListener {
        doNavigateToFriendDetailFragment(it.id)
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendslistBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        observeFriendsState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onTriggerEvent(LoadingFriends(showAllFriends))

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.onTriggerEvent(LoadingFriends(showAllFriends))
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_friends_list, menu)

        if (showAllFriends) menu.findItem(R.id.allFriends).isChecked = true
        else menu.findItem(R.id.onlineFriends).isChecked = true

        val search = menu.findItem(R.id.search_friend)
        val searchView = search.actionView as SearchView
        setSearchViewListener(searchView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.allFriends -> {
                showAllFriends = true
                item.isChecked = showAllFriends
                viewModel.onTriggerEvent(LoadingFriends(showAllFriends))
                return true
            }
            R.id.onlineFriends -> {
                showAllFriends = false
                item.isChecked = !showAllFriends
                viewModel.onTriggerEvent(LoadingFriends(showAllFriends))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeFriendsList() {
        lifecycleScope.launchWhenResumed {
            viewModel.filteredFriends.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    private fun observeFriendsState() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFriends.collectLatest { friendsState ->
                when (friendsState) {
                    LoadingFriendsSuccess -> {
                        showProgressBar(false)
                        observeFriendsList()
                    }
                    is LoadingFriendsFailed -> {
                        showProgressBar(false)
                        displayErrorDialog(friendsState.errorMessage)
                    }
                    LoadingFriendsStarted -> {
                        showProgressBar(true)
                    }
                }
            }
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressBarFriends.isVisible = isVisible
    }

    private fun doNavigateToFriendDetailFragment(friendId: Int) {
        findNavController().navigate(
            FriendsListFragmentDirections
                .actionFriendsListFragmentToFriendDetailFragment(friendId)
        )
    }

    private fun displayErrorDialog(message: String?) {
        context?.let {
            MaterialDialog(it)
                .show {
                    title(R.string.text_error)
                    message(text = message)
                    positiveButton(R.string.text_ok)
                }
        }
    }

    private fun setSearchViewListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.onTriggerEvent(SearchFriends(query, true))
                } else {
                    viewModel.onTriggerEvent(SearchFriends(EMPTY_STRING, false))
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.onTriggerEvent(SearchFriends(query, true))
                } else {
                    viewModel.onTriggerEvent(SearchFriends(EMPTY_STRING, false))
                }
                return false
            }
        })
    }

}