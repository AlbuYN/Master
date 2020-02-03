package ru.shrott.shrottmaster.view.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import ru.shrott.shrottmaster.view.adapters.VmListAdapter
import ru.shrott.shrottmaster.databinding.FragmentMastersBinding

import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.view.activities.MainActivityCallback
import ru.shrott.shrottmaster.view_model.MastersViewModel
import ru.shrott.shrottmaster.view_model.SharedViewModel
import java.lang.ClassCastException


/**
 * A simple [Fragment] subclass.
 *
 */
class MastersFragment : Fragment() {

    private lateinit var binding: FragmentMastersBinding
    private val mastersListAdapter = VmListAdapter()
    private var mainActivityCallBack: MainActivityCallback? = null
    private var sharedViewModel: SharedViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java) }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_masters,
            container, false)

        binding.toolbar.title = getString(R.string.shift_master)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val rootView = binding.root

        val viewModel = ViewModelProviders.of(this)
            .get(MastersViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
                errorMessage -> if(errorMessage != null) showError(errorMessage)
        })

        binding.viewModel = viewModel

        sharedViewModel?.updateMasters?.observe(this, Observer { if (it == true) {
            mastersListAdapter.clearAll()
            viewModel.loadMastersList()
            mainActivityCallBack?.loadNomenclature()
        }})

        binding.refresh.setColorSchemeResources(R.color.colorAccent)
        binding.refresh.setOnRefreshListener{ viewModel.loadMastersList() }

        binding.rvMasters.layoutManager = LinearLayoutManager(context)
        binding.rvMasters.adapter = mastersListAdapter

        viewModel.mastersList.observe(this, Observer { masters ->
            masters?.let { mastersListAdapter.replaceItems(it) }
        })

        binding.executePendingBindings()

        lifecycle.addObserver(binding.viewModel!!)

        return rootView
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_main, menu)
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                mainActivityCallBack?.openSettings()
                return true
            }
        }

        return false
    }

    private fun showError(errorMessage: String) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }


}
