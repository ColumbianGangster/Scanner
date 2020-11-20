package com.spike.scanner.ui.zxingscanner


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.spike.scanner.R
import com.spike.scanner.ui.FragmentsTransactionsManager

class ZXingScannerActivity : AppCompatActivity() {

    val ftm = FragmentsTransactionsManager(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        toScanner()
    }

    private fun toScanner() = goTo(
            fragment = ZXingScannerFragment.newInstance(),
            tag = ZXingScannerFragment.TAG)

    private fun goTo(fragment: Fragment, tag: String) {
        ftm.replaceFragment(fragment, tag, R.id.container_scanner)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> com.spike.scanner.ui.mlkitscanner.consume { onBackPressed() }
        else -> false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun consume(syncFun: () -> Unit): Boolean {
    syncFun.invoke()
    return true
}
