package com.martilius.smarthome.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.MainViewModel
import com.martilius.smarthome.R
import com.martilius.smarthome.ui.viewmodels.LoginViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.login_fragment.view.*
import java.security.PrivateKey
import javax.inject.Inject

class LoginFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<LoginViewModel> { factory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false).apply {
            enterTransition = MaterialFadeThrough().setDuration(500L)
            exitTransition = MaterialFadeThrough().setDuration(500L)
            val sharedPreferences = activity?.getSharedPreferences("userInfo",Context.MODE_PRIVATE)
            btLogin.setOnClickListener {
                if(!etLogin.text.isNullOrEmpty() && !etPassword.text.isNullOrEmpty()){
                    viewModel.login(etLogin.text.toString(), etPassword.text.toString())
                }else if(etLogin.text.isNullOrEmpty() && !etPassword.text.isNullOrEmpty()){
                    loginTextField.error = "Login cannot be empty"
                }else if(!etLogin.text.isNullOrEmpty() && etPassword.text.isNullOrEmpty()){
                    passwordTextField.error = "Password cannot be empty"
                }else if(etLogin.text.isNullOrEmpty() && etPassword.text.isNullOrEmpty()){
                    loginTextField.error = "Login cannot be empty"
                    passwordTextField.error = "Password cannot be empty"
                }

                //findNavController().navigate(R.id.action_loginFragment_to_nav_home)
                //loginTextField.error = "bla error"
                //passwordTextField.error = "blaa error"
            }

            btRegister.setOnClickListener {
                if(!etLogin.text.isNullOrEmpty() && !etPassword.text.isNullOrEmpty()){
                    viewModel.register(etLogin.text.toString(), etPassword.text.toString())
                }else if(etLogin.text.isNullOrEmpty() && !etPassword.text.isNullOrEmpty()){
                    loginTextField.error = "Login cannot be empty"
                }else if(!etLogin.text.isNullOrEmpty() && etPassword.text.isNullOrEmpty()){
                    passwordTextField.error = "Password cannot be empty"
                }else if(etLogin.text.isNullOrEmpty() && etPassword.text.isNullOrEmpty()){
                    loginTextField.error = "Login cannot be empty"
                    passwordTextField.error = "Password cannot be empty"
                }
            }
            etLogin.addTextChangedListener {
                loginTextField.error = null

            }
            etPassword.addTextChangedListener {
                passwordTextField.error = null
            }

            skip.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_nav_home)
            }

            val mainViewModel =
                activity?.let { ViewModelProvider(it).get(MainViewModel::class.java) }!!

            with(viewModel){
                testpost.observe(viewLifecycleOwner, Observer {
                    Toast.makeText(context,it.toString(),Toast.LENGTH_LONG).show()
                })
                loginRespond.observe(viewLifecycleOwner, Observer {
                    if(it.respond.equals("user doesnt exist")){
                        loginTextField.error = it.respond
                    }else if(it.respond.equals("wrong password")){
                        passwordTextField.error = it.respond
                    }else if(it.respond.equals("logged")){
                        sharedPreferences?.edit()?.putBoolean("logged",true)?.apply()
                        sharedPreferences?.edit()?.putString("login",etLogin.text.toString())?.apply()
                        if (it.admin.equals(true)){
                            sharedPreferences?.edit()?.putBoolean("admin", true)?.apply()
                        }else{
                            sharedPreferences?.edit()?.putBoolean("admin", false)?.apply()
                        }
                        mainViewModel.init()
                        findNavController().navigate(R.id.action_loginFragment_to_nav_home)
                    }
                    //Toast.makeText(context, it.respond, Toast.LENGTH_LONG).show()
                })
                registerRespond.observe(viewLifecycleOwner, Observer {
                    if(it.respond.equals("registered")){
                        findNavController().navigate(R.id.action_loginFragment_to_nav_home)
                    }else{
                        loginTextField.error = it.respond
                    }
                })
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}