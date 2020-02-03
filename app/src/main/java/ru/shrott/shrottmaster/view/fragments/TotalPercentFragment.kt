package ru.shrott.shrottmaster.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.shrott.shrottmaster.databinding.FragmentTotalProcentBinding
import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.view.activities.MainActivityCallback
import ru.shrott.shrottmaster.view_model.SharedViewModel
import java.lang.ClassCastException

class TotalPercentFragment : Fragment() {

    private var sharedViewModel: SharedViewModel? = null
    private lateinit var binding: FragmentTotalProcentBinding
    private var edit: Boolean = false


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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_total_procent,
            container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).title = getString(R.string.arm)



        val totalPercentPollution = arguments?.getString("totalPercentPollution")
        binding.tvTotal.text = totalPercentPollution

        //totalPercentPollution.let { binding.etTotal.text }

        binding.tv0.setOnClickListener (onClickNumber("0"))
        binding.tv1.setOnClickListener (onClickNumber("1"))
        binding.tv2.setOnClickListener (onClickNumber("2"))
        binding.tv3.setOnClickListener (onClickNumber("3"))
        binding.tv4.setOnClickListener (onClickNumber("4"))
        binding.tv5.setOnClickListener (onClickNumber("5"))
        binding.tv6.setOnClickListener (onClickNumber("6"))
        binding.tv7.setOnClickListener (onClickNumber("7"))
        binding.tv8.setOnClickListener (onClickNumber("8"))
        binding.tv9.setOnClickListener (onClickNumber("9"))

        binding.imgBtnRefresh.setOnClickListener { binding.tvTotal.text = "" }

        binding.fabOk.setOnClickListener {
            sharedViewModel?.totalPercentPollution?.postValue(binding.tvTotal.text.toString().trim().toInt())
            activity?.onBackPressed()
        }

        binding.fabCancel.setOnClickListener {
            activity?.onBackPressed() }


        return binding.root
    }


    private fun onClickNumber(number: String): View.OnClickListener {
        return View.OnClickListener {
            if (!edit) binding.tvTotal.text = ""
            val text = binding.tvTotal.text.toString() + number
            binding.tvTotal.text = text
            edit = true
        }
    }


}
