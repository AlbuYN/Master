package ru.shrott.shrottmaster.view.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import ru.shrott.shrottmaster.databinding.FragmentSettingsBinding
import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.view.activities.MainActivityCallback
import ru.shrott.shrottmaster.view_model.SettingsViewModel
import ru.shrott.shrottmaster.view_model.SharedViewModel
import java.lang.ClassCastException

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private var sharedViewModel: SharedViewModel? = null
    private var mainActivityCallBack: MainActivityCallback? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            mainActivityCallBack = context as MainActivityCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context?.javaClass).toString() + " must implements " + MainActivityCallback::class.java
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings,
                container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.settings)

        val viewModel = ViewModelProviders.of(this)
                .get(SettingsViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.saveIpAddress.observe(this, Observer {
            mainActivityCallBack?.hideKeyboard()
            activity?.onBackPressed()
            if (it == true) sharedViewModel?.updateMasters?.postValue(it)
        })

        viewModel.updateNomenclature.observe(this, Observer {
            showMessage(getString(R.string.updated_nomenclature))
        })

        viewModel.errorMessage.observe(this, Observer { it?.let { it1 -> showMessage(it1) } })

        binding.fabSave.setOnClickListener { viewModel.saveIpAddress() }
        binding.tvUpdateNomenclature.setOnClickListener { viewModel.updateNomenclature() }

        binding.executePendingBindings()

        lifecycle.addObserver(binding.viewModel!!)

        return binding.root
    }

    private fun showMessage(errorMessage: String) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }



}
