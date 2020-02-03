package ru.shrott.shrottmaster.view.fragments


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import ru.shrott.shrottmaster.view.adapters.VmListAdapter

import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.databinding.FragmentBadgeListBinding
import ru.shrott.shrottmaster.other.App
import ru.shrott.shrottmaster.other.Utils.Permissions
import ru.shrott.shrottmaster.view.activities.ScanBarcodeActivity
import ru.shrott.shrottmaster.view_model.BadgesViewModel
import ru.shrott.shrottmaster.view_model.SharedViewModel
import ru.shrott.shrottmaster.view_model.ViewModelFactory
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class BadgeListFragment : Fragment() {

    @Inject lateinit var permissions: Permissions

    private lateinit var binding: FragmentBadgeListBinding
    private val badgesListAdapter = VmListAdapter()
    private var sharedViewModel: SharedViewModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java) }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getComponent()?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_badge_list,
                container, false)

        val viewModel = ViewModelProviders.of(this,
            ViewModelFactory(arguments?.getString("idMaster")) )
                .get(BadgesViewModel::class.java)


        viewModel.errorMessage.observe(this, Observer {
            errorMessage -> if(errorMessage != null) showError(errorMessage)
        })

        sharedViewModel?.updateBadges?.observe(this, Observer { viewModel.loadBadges() })


        binding.viewModel = viewModel

        binding.refresh.setColorSchemeResources(R.color.colorAccent)
        binding.refresh.setOnRefreshListener{ viewModel.loadBadges() }

        val name = arguments?.getString("name")
        binding.toolbar.title = getString(R.string.master) +": " + name
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.rvBadges.layoutManager = LinearLayoutManager(context)
        binding.rvBadges.adapter = badgesListAdapter

        viewModel.badgesList.observe(this, Observer {badgedList ->
            badgedList?.let {
                binding.tvEmpty.visibility = View.GONE
                badgesListAdapter.replaceItems(it) }
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                badgesListAdapter.clearAll()

                //Отрефакторить перенести фильтрацию во ViewModel и выдавать результат в общий Observable===============

                val badges = binding.viewModel?.filter(newText)
                if (!badges.isNullOrEmpty()) {
                    badgesListAdapter.replaceItems(badges)
                    binding.tvEmpty.visibility = View.GONE
                } else {
                    if (newText.isEmpty()) binding.tvEmpty.visibility = View.GONE
                    else binding.tvEmpty.visibility = View.VISIBLE
                }
                return false
            }
        })

        binding.searchView.setIconifiedByDefault(false)

        binding.fabScanBarcode.setOnClickListener {
            if (permissions.checkPermissionCamera(activity as AppCompatActivity)) { startBarcode() }
            else permissions.camera(this) }

        binding.executePendingBindings()

        lifecycle.addObserver(binding.viewModel!!)

        return binding.root
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Permissions.PERMISSION_CAMERA -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                startBarcode()
            } else {
                showError(getString(R.string.nead_permission_camera))
                this.permissions.setDialogRequestPermissions(false)
            }
            else -> {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ScanBarcodeActivity.REQUEST_SCAN_BARCODE -> {
                    val barcode = data.getStringExtra("barcode")
                    binding.searchView.setQuery(barcode, false)
                }
                else -> {
                }
            }
        }
    }


    private fun startBarcode() {
        val intent = Intent(context, ScanBarcodeActivity::class.java)
        startActivityForResult(intent, ScanBarcodeActivity.REQUEST_SCAN_BARCODE)
    }

    private fun showError(errorMessage: String) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }

}
