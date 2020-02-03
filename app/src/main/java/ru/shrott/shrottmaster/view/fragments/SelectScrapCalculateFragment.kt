package ru.shrott.shrottmaster.view.fragments


import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.databinding.FragmentSelectScrapCalkulateBinding
import ru.shrott.shrottmaster.view.vo.WeightPercentageVO
import ru.shrott.shrottmaster.view_model.SharedViewModel


class SelectScrapCalculateFragment : Fragment() {



    private lateinit var binding: FragmentSelectScrapCalkulateBinding
    private var sharedViewModel: SharedViewModel? = null
    private var edit: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_scrap_calkulate,
            container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).title = arguments?.getString("name")


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

        val remainingTotalPercentWeight = 100 - arguments?.getInt("totalPercentWeight", 0)!!

        binding.tvTotal.text = (remainingTotalPercentWeight).toString()

        binding.fabOk.setOnClickListener {

            if (binding.tvTotal.text.toString().toInt() <= remainingTotalPercentWeight) {

                val idNomenclature = arguments?.getString("idNomenclature")
                val idScrapGroup = arguments?.getString("idScrapGroup")
                val name = arguments?.getString("name")

                if (idNomenclature != null && idScrapGroup != null && name != null) {
                    sharedViewModel?.weightPercent?.postValue(
                        WeightPercentageVO(idNomenclature, idScrapGroup, name,
                            binding.tvTotal.text.toString().toInt()))
                    activity?.onBackPressed()
                }
            } else {
                showError( getString(R.string.percentage_is_not_higher_than_the_limit) +
                        " $remainingTotalPercentWeight%")
            }

            }

        binding.fabCancel.setOnClickListener { activity?.onBackPressed() }


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

    private fun showError(errorMessage: String) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }


}
