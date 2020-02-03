package ru.shrott.shrottmaster.view.fragments


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.shrott.shrottmaster.view.adapters.VmListAdapter


import ru.shrott.shrottmaster.databinding.FragmentPassBinding
import ru.shrott.shrottmaster.view.vo.NomenclatureVO
import ru.shrott.shrottmaster.view.vo.ScrapGroupVO
import ru.shrott.shrottmaster.view_model.PassViewModel

import android.support.annotation.NonNull
import ru.shrott.shrottmaster.view.adapters.IBaseItemVm
import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.other.App
import ru.shrott.shrottmaster.other.Utils.Permissions
import ru.shrott.shrottmaster.other.Utils.Utils
import ru.shrott.shrottmaster.view.activities.MainActivityCallback
import ru.shrott.shrottmaster.view.vo.WeightPercentageVO
import ru.shrott.shrottmaster.view_model.SharedViewModel
import ru.shrott.shrottmaster.view_model.ViewModelFactory
import java.lang.ClassCastException
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class PassFragment : Fragment() {

    @Inject
    lateinit var permissions: Permissions

    @Inject
    lateinit var utils: Utils

    companion object {
        private const val CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100
    }

    private lateinit var binding: FragmentPassBinding
    val scrapGroupListAdapter = VmListAdapter()
    val nomenclatureVOListAdapter = VmListAdapter()
    val nomenclaturePercentAdapter = VmListAdapter()
    val photoAdapter = VmListAdapter()
    private var sharedViewModel: SharedViewModel? = null
    private var barcode: String? = ""
    private var idMaster: String? = ""
    private var mainActivityCallBack: MainActivityCallback? = null



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
        App.getComponent()?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java) }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pass,
            container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)


        idMaster = arguments?.getString("idMaster")
        barcode = arguments?.getString("barcode")

        val viewModel = ViewModelProviders.of(this, ViewModelFactory(barcode))
            .get(PassViewModel::class.java)

        binding.viewModel = viewModel
        binding.passFragment = this


        hideFabForBottomSheetBehaviorSwipe()

        viewModel.scrapGroupVOList.observe(this, observerScrapGroup())
        viewModel.nomenclatureVOList.observe(this, observerNomenclature())
        viewModel.weightPercentageVOList.observe(this, observerWeightPercent())
        viewModel.errorMessage.observe(this, Observer { it?.let { it1 -> showError(it1) } })
        viewModel.photoList.observe(this, Observer { mediaPassList -> mediaPassList
            ?.let { photoAdapter.replaceItems(it) } })
        viewModel.result.observe(this, observerUpdateBadges())
        sharedViewModel?.weightPercent?.observe(this, observerAddWeightPercent())
        sharedViewModel?.totalPercentPollution?.observe(this, Observer { it?.let { percent ->
            barcode?.let { it1 -> viewModel.addTotalPercentPollution(it1, percent) }
        } })


        binding.fabTotal.setOnClickListener { BottomSheetBehavior.from(binding.frBottomSheet).state =
            BottomSheetBehavior.STATE_EXPANDED }
        binding.iBtnAddPhoto.setOnClickListener { startCamera() }

        binding.iBtnAddPercent.setOnClickListener {
            if (viewModel.totalPercentWeight.value == 100) {
                val args = Bundle()
                args.putString("totalPercentPollution", viewModel.totalPercentPollution.get().toString())
                mainActivityCallBack?.openTotalPercentFragment(args)
            } else showError(getString(R.string.weight_required))
        }
        binding.iBtnSend.setOnClickListener {
            if (viewModel.totalPercentWeight.value == 100) {
                if (idMaster != null) {
                    viewModel.sendPass(idMaster!!)
                }
            } else showError(getString(R.string.weight_required))
        }


        binding.rvNomenclature.layoutManager = createGridLayoutManager()
        binding.rvPhotosMini.layoutManager = createGridLayoutManager()

        binding.executePendingBindings()

        lifecycle.addObserver(binding.viewModel!!)

        return binding.root
    }

    private fun createGridLayoutManager(): GridLayoutManager {
        return GridLayoutManager(activity, ((utils.getDisplayWithWithDP().toInt() - 80) / 128))
    }


    private fun <T> ArrayList<T>.addPhoto(element: T) {
        this.add(element)
        photoAdapter.add(element as IBaseItemVm)
    }


    private fun observerWeightPercent(): Observer<List<WeightPercentageVO>> {
        return Observer { weightPercentList ->
            weightPercentList?.let { nomenclaturePercentAdapter.replaceItems(it) }
            scrapGroupListAdapter.notifyDataSetChanged()
            nomenclatureVOListAdapter.notifyDataSetChanged()
        }
    }

    private fun observerScrapGroup(): Observer<List<ScrapGroupVO>> {
        return Observer { scrapGroup -> scrapGroup?.let { scrapGroupListAdapter.replaceItems(it) }
        }
    }

    private fun observerNomenclature(): Observer<List<NomenclatureVO>> {
        return Observer { nomenclatureVOList ->
            nomenclatureVOList?.let { nomenclatureVOListAdapter.replaceItems(it) }
            scrapGroupListAdapter.notifyDataSetChanged()
        }
    }

    private fun observerAddWeightPercent(): Observer<WeightPercentageVO> {
        return Observer { nomenclature ->
            nomenclature?.let { binding.viewModel?.addWeightPercent(arguments, it) }
        }
    }

    private fun observerUpdateBadges(): Observer<Boolean> {
        return Observer { if (it!!) {
            activity?.onBackPressed()
            sharedViewModel?.updateBadges?.postValue(true)
        } }
    }

    private fun hideFabForBottomSheetBehaviorSwipe() {
        BottomSheetBehavior.from(binding.frBottomSheet).peekHeight = 1
        BottomSheetBehavior.from(binding.frBottomSheet).setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {}

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                binding.fabTotal.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start()
            }
        })
    }


    private fun startCamera() {

        if (activity != null) {

            if (permissions.checkPermissionCamera(activity as AppCompatActivity)) {
                if (permissions.storageMediaFiles(activity!!)) {
                    // start the image capture Intent
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val fileUri = binding.viewModel?.createFileUri()
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                    intent.putExtra("file_uri", fileUri)
                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
                }
            } else permissions.camera(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Permissions.PERMISSIONS_MEDIA_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else showError(getString(R.string.error_access_storage_card))
            }
            Permissions.PERMISSION_CAMERA -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                showError(getString(R.string.nead_permission_camera))
                this.permissions.setDialogRequestPermissions(false)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_CAPTURE_IMAGE_REQUEST_CODE -> {
                    barcode?.let { binding.viewModel?.addPhoto(it) }
                }
                else -> {
                }
            }
        }

    }

    private fun showError(errorMessage: String) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }


}
