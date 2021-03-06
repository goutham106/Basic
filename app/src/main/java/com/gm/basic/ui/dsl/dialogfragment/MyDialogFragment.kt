package com.gm.basic.ui.dsl.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.gm.basic.utils.TAG
import java.lang.Exception

class MyDialogFragment : DialogFragment() {

    var layoutId: Int? = null

    lateinit var callback: (View, DialogFragment) -> Unit

    companion object {

        val TAG = MyDialogFragment::TAG.toString()

        fun newInstance(layoutId: Int?): MyDialogFragment {

            val frag = MyDialogFragment()
            frag.arguments = Bundle().apply {
                putInt("layoutId", layoutId!!)
            }
            return frag
        }
    }

    fun setCustomView(customView: (View, DialogFragment) -> Unit) {
        callback = customView
    }

    override fun onStart() {
        super.onStart()
        val deviceWidth = context!!.resources.displayMetrics.widthPixels
        dialog?.window?.setLayout(deviceWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutId = arguments?.getInt("layoutId")
        if (layoutId == null) throw Exception("layoutId can't be null")
        return inflater.inflate(layoutId!!, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback.invoke(view, this)
    }

}