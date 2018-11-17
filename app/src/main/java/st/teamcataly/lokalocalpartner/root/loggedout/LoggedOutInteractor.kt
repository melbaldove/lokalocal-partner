package st.teamcataly.lokalocalpartner.root.loggedout

import android.util.Log
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import st.teamcataly.lokalocalpartner.addTo
import st.teamcataly.lokalocalpartner.root.LokaLocalApi
import st.teamcataly.lokalocalpartner.root.loggedin.Credentials
import st.teamcataly.lokalocalpartner.root.loggedin.Partner
import st.teamcataly.lokalocalpartner.root.loggedin.Profile
import st.teamcataly.lokalocalpartner.root.loggedin.orders.Order
import st.teamcataly.lokalocalpartner.root.loggedin.orders.OrdersInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [LoggedOutScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class LoggedOutInteractor : Interactor<LoggedOutInteractor.LoggedOutPresenter, LoggedOutRouter>() {

    @Inject lateinit var presenter: LoggedOutPresenter
    @Inject lateinit var listener: Listener
    @Inject lateinit var lokaLocalApi: LokaLocalApi
    private val disposables = CompositeDisposable()

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.login()
                .subscribe({
                    doSignIn(it)
                },{
                    Log.e("WTF", it.message, it)
                    presenter.loginFailed()
                }).addTo(disposables)
    }

    private fun doSignIn(it: Credentials?) {
        lokaLocalApi.login(it!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listener.onLoginSuccess(it)
                }, {
                    Log.e("WTF", it.message, it)
                }).addTo(disposables)
    }

    override fun willResignActive() {
        super.willResignActive()

    }

    interface Listener {
        fun onLoginSuccess(partner: Partner)
    }

    interface LoggedOutPresenter {
        fun login(): Observable<Credentials>
        fun loginFailed()
    }
}
