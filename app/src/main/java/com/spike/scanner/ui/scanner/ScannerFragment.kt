package com.spike.scanner.ui.scanner

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.spike.scanner.R
import kotlinx.android.synthetic.main.fragment_scan.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.google.zxing.Result

import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class ScannerFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var scannerBottomSheetFragment: ScannerBottomSheetFragment


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = container?.inflate(R.layout.fragment_scan)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_retry.setOnClickListener {
            checkForPermissionsAndLoadCamera()
        }

        checkForPermissionsAndLoadCamera()
    }

    @AfterPermissionGranted(CAMERA_PERMISSION_REQUEST_CODE)
    private fun checkForPermissionsAndLoadCamera() {
        if (btn_retry != null) {
            btn_retry.visibility = View.GONE
            context?.let {
                if (EasyPermissions.hasPermissions(it, PERMISSION_CAMERA)) {
                    loadCamera()
                } else {
                    EasyPermissions.requestPermissions(this, getString(R.string.permission_camera_rationale),
                            CAMERA_PERMISSION_REQUEST_CODE, PERMISSION_CAMERA
                    )
                }
            }
        }
    }

    private fun loadCamera() {
        with(scanner_view) {
            visibility = View.VISIBLE
            setFormats(listOf(
                    BarcodeFormat.EAN_8,
                    BarcodeFormat.EAN_13,
                    BarcodeFormat.AZTEC,
                    BarcodeFormat.CODABAR,
                    BarcodeFormat.CODE_93,
                    BarcodeFormat.CODE_128,
                    BarcodeFormat.PDF_417,
                    BarcodeFormat.ITF,
                    BarcodeFormat.UPC_A,
                    BarcodeFormat.UPC_E,
                    BarcodeFormat.UPC_EAN_EXTENSION,
                    BarcodeFormat.RSS_14,
                    BarcodeFormat.RSS_EXPANDED,
                    BarcodeFormat.DATA_MATRIX,
                    BarcodeFormat.MAXICODE))
            setAutoFocus(true)
            setSquareViewFinder(true)
            setBorderColor(Color.WHITE)
            setAspectTolerance(0.5f)
            setResultHandler { rawResult: Result? -> rawResult?.let { handleResult(it) } }
            startCamera()
        }

        showSnackBar(false)

    }

    private fun handleResult(rawResult: Result) {
        //scannerViewModel.getBarcodeData(rawResult.text)
        showSnackBar(true)
    }

    private fun showSnackBar(isScanned: Boolean) {
        val snackBarText: String
        val color: Int?
        val drawableScan: Int

        if (isScanned) {
            snackBarText = getString(R.string.scanned)
            color = context?.let { ContextCompat.getColor(it, R.color.green) }
            drawableScan = R.drawable.ic_launcher_background
        } else {
            snackBarText = getString(R.string.scanning)
            color = context?.let { ContextCompat.getColor(it, R.color.dark_green) }
            drawableScan = R.drawable.ic_launcher_foreground

        }
        val snackBar = view?.let { Snackbar.make(it, snackBarText, Snackbar.LENGTH_INDEFINITE) }
        val textView: TextView? = view?.findViewById(com.google.android.material.R.id.snackbar_text)
        textView?.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableScan, 0)

        textView?.gravity = Gravity.CENTER_HORIZONTAL
        textView?.textAlignment = View.TEXT_ALIGNMENT_CENTER

        color?.let { snackBar?.view?.setBackgroundColor(it) }
        snackBar?.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

   /* private fun content(result: UiSearchNavigation?) {
        if (result?.resultsets?.default?.results.isNullOrEmpty()) {
            showBottomFragment()
        } else {
            val id = result?.resultsets?.default?.results?.get(0)?.id.toString()
            view?.postDelayed({
                navigator.toProductDetailsWithId(id, UiProductListing(isEmpty = true))
            }, 2000)

        }
    }*/

    private fun showBottomFragment() {
        scannerBottomSheetFragment = ScannerBottomSheetFragment().newInstance()
        scannerBottomSheetFragment.isCancelable = false
        fragmentManager?.let { scannerBottomSheetFragment.show(it, TAG) }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, listOf(PERMISSION_CAMERA))) {
            btn_retry.visibility = View.GONE
            tv_scan_text.text = getString(R.string.camera_permission_denied_copy)
            scanner_view.visibility = View.GONE
        } else {
            btn_retry.visibility = View.VISIBLE
            scanner_view.visibility = View.GONE
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //No-OP
    }

    companion object {
        const val TAG = "Scanner Fragment"
        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
        fun newInstance() = ScannerFragment()
    }

    private fun stopCamera() {
        scanner_view.stopCamera()
    }

    override fun onStart() {
        super.onStart()
        checkForPermissionsAndLoadCamera()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        stopCamera()
        EventBus.getDefault().unregister(this)
    }

   /* override fun onErrorPositiveAction(errSource: ErrorSource, uiError: UiError) {
        checkForPermissionsAndLoadCamera()
    }

    override fun onErrorNegativeAction(errorSource: ErrorSource, uiError: UiError) {
        // noop
    }*/

    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    fun reloadScanner(scannerStatusChanged: ScannerStatusChanged) {
        if (scannerStatusChanged.origin == Scanner.RELOAD) {
            if (::scannerBottomSheetFragment.isInitialized) scannerBottomSheetFragment.dismiss()
            this.checkForPermissionsAndLoadCamera()
        }
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    fun searchScannerStatusChanged(scannerStatusChanged: ScannerStatusChanged) {
        if (scannerStatusChanged.origin == Scanner.SEARCH) {
           // navigator.toSearch()
        }
    }
}
