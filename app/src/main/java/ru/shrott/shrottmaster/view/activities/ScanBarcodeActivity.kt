package ru.shrott.shrottmaster.view.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import ru.shrott.shrottmaster.R

class ScanBarcodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {


    companion object {
        const val REQUEST_SCAN_BARCODE = 2
    }

    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barcode)

        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        mScannerView?.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView?.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);

        val intent = Intent()
        intent.putExtra("barcode", rawResult.text)

        setResult(Activity.RESULT_OK, intent)
        onBackPressed()
    }
}
