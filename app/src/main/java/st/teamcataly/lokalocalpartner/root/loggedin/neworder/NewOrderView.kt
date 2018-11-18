package st.teamcataly.lokalocalpartner.root.loggedin.neworder

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.new_order_rib.view.*

/**
 * Top level view for {@link NewOrderBuilder.NewOrderScope}.
 */
class NewOrderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle), NewOrderInteractor.NewOrderPresenter {

    private val qrScanObservable = PublishSubject.create<String>()

    override fun onFinishInflate() {
        super.onFinishInflate()

        val codeScanner = CodeScanner(context, new_order_scanner_view)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera card
        codeScanner.formats = listOf(BarcodeFormat.QR_CODE) // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            post {
                Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                qrScanObservable.onNext(it.text)
            }
        }

        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            post {
                Toast.makeText(context, "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        new_order_scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }

        codeScanner.startPreview()
    }

    override fun qrScan() = qrScanObservable.hide()!!
    override fun back() = RxView.clicks(new_order_back).map {  }!!
}
