package com.example.vkloggingonlinefriends.presentation.ui.login


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vkloggingonlinefriends.databinding.FragmentLoginBinding
import com.example.vkloggingonlinefriends.datastore.AppDataStore
import com.example.vkloggingonlinefriends.presentation.ui.login.LoginEvent.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@InternalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var dataStore: AppDataStore
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collectLatest {
                observeLoginState(it)
            }
        }
        viewModel.onTriggerEvent(ReturnToInitialState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vkActivityLauncher = registerForActivityResult(StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                viewModel.onTriggerEvent(LoginSuccess)
            }
            if (it.resultCode == Activity.RESULT_CANCELED) {
                val errorMessage = it.data?.getStringExtra("error")
                viewModel.onTriggerEvent(LoginFailed(errorMessage))
            }
        }

        binding.signInButton.setOnClickListener {
            viewModel.onTriggerEvent(LoginStarted)
            val intent = Intent(activity, LoginVkActivity::class.java)
            vkActivityLauncher.launch(intent)
        }
    }

    private fun observeLoginState(loginState: LoginState) {
        binding.progressBarLogin.visibility = if (loginState.showProgressBar) {
            View.VISIBLE
        } else {
            View.GONE
        }
        if (loginState.isLoggedIn) {
            Toast.makeText(activity, "You successfully login!", Toast.LENGTH_SHORT).show()
            viewModel.onTriggerEvent(ReturnToInitialState)
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToProfileFragment())
        }
        if (loginState.loginError != null) {
            Toast.makeText(activity, "Login error: ${loginState.loginError}", Toast.LENGTH_SHORT)
                .show()
        }
    }

}