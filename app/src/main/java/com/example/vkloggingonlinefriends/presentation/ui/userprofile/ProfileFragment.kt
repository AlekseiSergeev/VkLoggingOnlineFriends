package com.example.vkloggingonlinefriends.presentation.ui.userprofile


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vkloggingonlinefriends.R
import com.example.vkloggingonlinefriends.databinding.FragmentProfileBinding
import com.example.vkloggingonlinefriends.data.cache.datastore.AppDataStore
import com.example.vkloggingonlinefriends.domain.model.User
import com.example.vkloggingonlinefriends.domain.service.FriendsLoggingService
import com.example.vkloggingonlinefriends.presentation.MaterialDialogsCallback
import com.example.vkloggingonlinefriends.presentation.ui.userprofile.ProfileEvent.*
import com.example.vkloggingonlinefriends.presentation.ui.userprofile.ProfileState.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var dataStore: AppDataStore
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private var serviceIsRunning = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        observeProfileState()
        observeServiceState()
        viewModel.onTriggerEvent(LoadingUser)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFriends.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFriendsListFragment())
        }
        binding.buttonLogout.setOnClickListener {
            val areYouSureCallback = object : MaterialDialogsCallback {
                override fun proceed() {
                    viewModel.onTriggerEvent(Logout)
                }

                override fun cancel() {
                    viewModel.onTriggerEvent(ReturnToInitialProfileState)
                }
            }
            displayAreYouSureDialog(getString(R.string.you_really_want_logout), areYouSureCallback)
        }
    }

    private fun observeProfileState() {
        lifecycleScope.launchWhenStarted {
            viewModel.profileState.collectLatest { state ->
                when (state) {
                    InitialProfileState -> {
                        showProgressBar(false)
                    }
                    is LoadingUserSuccess -> {
                        showProgressBar(false)
                        showUserInfo(state.user)
                    }
                    is LoadingUserFailed -> {
                        showProgressBar(false)
                        displayErrorDialog(state.errorMessage)
                    }
                    LoadingUserStarted -> {
                        showProgressBar(true)
                    }
                    UnloggedUser -> {
                        stopService()
                        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
                        viewModel.onTriggerEvent(ReturnToInitialProfileState)
                    }
                }
            }
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressBarProfile.isVisible = isVisible
    }

    private fun showUserInfo(user: User) {
        binding.textViewUserName.text = "${user.firstName} ${user.lastName}"
        Glide.with(this@ProfileFragment)
            .load(user.photo)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.imageUserAvatar)
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

    private fun displayAreYouSureDialog(message: String?, callback: MaterialDialogsCallback) {
        context?.let {
            MaterialDialog(it)
                .show {
                    title(R.string.are_you_sure)
                    message(text = message)
                    negativeButton(R.string.text_cancel) {
                        callback.cancel()
                    }
                    positiveButton(R.string.text_yes) {
                        callback.proceed()
                    }
                }
        }
    }

    private fun observeServiceState() {
        lifecycleScope.launchWhenResumed {
            dataStore.isServiceRunning.collect { isRunning ->
                serviceIsRunning = isRunning
            }
        }
    }

    private fun stopService() {
        if (serviceIsRunning) {
            val serviceIntent = Intent(activity, FriendsLoggingService::class.java)
            activity?.stopService(serviceIntent)
            Toast.makeText(context, getString(R.string.service_stopped), Toast.LENGTH_SHORT).show()
        }
    }

}
