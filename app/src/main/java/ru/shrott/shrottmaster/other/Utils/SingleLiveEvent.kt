package ru.shrott.shrottmaster.other.Utils

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T>: MutableLiveData<T>() {

    private val TAG = "SingleLiveEvent"

    private val mPending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {

        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }

        super.observe(owner, Observer { t: T? -> if (mPending.compareAndSet(true, false)) {
            observer.onChanged(t)
        } })
    }

    override fun setValue(value: T) {
        mPending.set(true)
        super.setValue(value)
    }



}