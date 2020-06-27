package com.martilius.smarthome.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.R
import com.martilius.smarthome.ui.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.view.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false).apply {
            enterTransition = MaterialFadeThrough().setDuration(500L)
            exitTransition = MaterialFadeThrough().setDuration(500L)
            btLogin.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_nav_home)
                loginTextField.error = "bla error"
                passwordTextField.error = "blaa error"
            }
            etLogin.addTextChangedListener {
                loginTextField.error = null
                passwordTextField.error = null
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}