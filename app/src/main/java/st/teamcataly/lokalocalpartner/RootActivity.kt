package st.teamcataly.lokalocalpartner

import android.Manifest
import android.os.Bundle
import android.view.ViewGroup
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uber.rib.core.RibActivity
import com.uber.rib.core.ViewRouter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import st.teamcataly.lokalocalpartner.root.RootBuilder


class RootActivity : RibActivity() {

    private lateinit var rxPermissions: RxPermissions
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        rxPermissions = RxPermissions(this)
//        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        requestPermissions()
    }

    private fun requestPermissions() {
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe { if (!it) requestPermissions() }.addTo(disposables)
    }

    override fun createRouter(parentViewGroup: ViewGroup): ViewRouter<*, *, *> {
        return RootBuilder(object : RootBuilder.ParentComponent {}).build(parentViewGroup)
    }

    override fun onBackPressed() {

    }
}