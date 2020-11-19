package com.spike.scanner.ui.scanner


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spike.scanner.R
import kotlinx.android.synthetic.main.bottom_error_sheet.*
import org.greenrobot.eventbus.EventBus


class ScannerBottomSheetFragment : BottomSheetDialogFragment() {


    fun newInstance(): ScannerBottomSheetFragment = ScannerBottomSheetFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.bottom_error_sheet, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_scan_again.setOnClickListener {
            EventBus.getDefault().post(ScannerStatusChanged(Scanner.RELOAD))
            this.dismiss()
        }
    }
}