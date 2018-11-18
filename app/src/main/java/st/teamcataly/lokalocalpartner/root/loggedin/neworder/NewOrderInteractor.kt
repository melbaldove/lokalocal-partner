package st.teamcataly.lokalocalpartner.root.loggedin.neworder

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import st.teamcataly.lokalocalpartner.addTo
import st.teamcataly.lokalocalpartner.root.loggedin.orders.OrdersInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [NewOrderScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class NewOrderInteractor : Interactor<NewOrderInteractor.NewOrderPresenter, NewOrderRouter>() {

    @Inject lateinit var presenter: NewOrderPresenter
    @Inject lateinit var listener: Listener
    private val disposables = CompositeDisposable()

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.qrScan().subscribe {
            listener.onQRScanned(it)
        }.addTo(disposables)

        presenter.back().subscribe {
            listener.onBackClicked()
        }.addTo(disposables)
    }

    override fun willResignActive() {
        super.willResignActive()

        // TODO: Perform any required clean up here, or delete this method entirely if not needed.
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface Listener {
        fun onQRScanned(data: String)
        fun onBackClicked()
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface NewOrderPresenter {
        fun qrScan(): Observable<String>
        fun back(): Observable<Unit>
    }
}
