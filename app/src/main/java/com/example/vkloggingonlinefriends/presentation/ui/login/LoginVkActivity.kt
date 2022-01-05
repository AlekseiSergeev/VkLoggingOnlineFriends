package com.example.vkloggingonlinefriends.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vkloggingonlinefriends.R
import com.example.vkloggingonlinefriends.datastore.AppDataStore
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKAuthException
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginVkActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStore: AppDataStore
    private var vkToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginvk)

        loginVk()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                // User passed authorization
                vkToken = token.accessToken
                dataStore.setNewToken(vkToken)

                setResult(RESULT_OK)
                finish()
            }

            override fun onLoginFailed(authException: VKAuthException) {
                // User didn't pass authorization
                val intentFailed = Intent()
                intentFailed.putExtra("error", authException.authError)
                setResult(RESULT_CANCELED, intentFailed)
                finish()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun loginVk() {
        VK.login(this, arrayListOf(VKScope.FRIENDS, VKScope.WALL, VKScope.OFFLINE))
    }
}