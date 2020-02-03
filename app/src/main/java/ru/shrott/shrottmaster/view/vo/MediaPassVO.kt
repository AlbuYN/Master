package ru.shrott.shrottmaster.view.vo

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.android.databinding.library.baseAdapters.BR
import com.bumptech.glide.Glide
import ru.shrott.shrottmaster.view.adapters.IBaseItemVm
import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.other.Utils.Utils
import java.io.File


@BindingAdapter("imgSignalUrl")
fun imageSignalImage(image: ImageView, uri: String) {
    if (uri.isNotEmpty()) {
        Glide.with(image.context)
            .load(Uri.parse(uri))
            .into(image)
    }
}


class MediaPassVO(var idPass: String, var uri: String, var imgBase64: String): IBaseItemVm {

    var clickElement = MutableLiveData<MediaPassVO>()

    override fun getLayoutId(): Int {
        return R.layout.model_media_preview
    }

    override val brVariableId: Int
        get() = BR.vmMediaSignal


    val listener = View.OnClickListener {
        clickElement.postValue(this)
    }
}