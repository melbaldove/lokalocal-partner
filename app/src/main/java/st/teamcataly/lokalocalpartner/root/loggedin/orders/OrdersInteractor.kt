package st.teamcataly.lokalocalpartner.root.loggedin.orders

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import st.teamcataly.lokalocalpartner.addTo
import st.teamcataly.lokalocalpartner.root.loggedin.Profile
import javax.inject.Inject

/**
 * Coordinates Business Logic for [OrdersScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class OrdersInteractor : Interactor<OrdersInteractor.OrdersPresenter, OrdersRouter>() {

    @Inject lateinit var presenter: OrdersPresenter
    @Inject lateinit var listener: Listener
    private val disposables = CompositeDisposable()
    private var profileIdOrderMap: Map<String, Pair<Profile, Order>> = mapOf()

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.newOrder().subscribe {
            listener.onNewOrder()
        }.addTo(disposables)

        presenter.ordersUpdated().subscribe {
            listener.onOrdersUpdated(it)
        }.addTo(disposables)
        presenter.setOrders(profileIdOrderMap)
    }

    override fun willResignActive() {
        super.willResignActive()

        // TODO: Perform any required clean up here, or delete this method entirely if not needed.
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface Listener {
        fun onNewOrder()
        fun onOrdersUpdated(profileIdOrderMap: Map<String, Pair<Profile, Order>>)
    }

    interface OrdersPresenter {
        fun newOrder(): Observable<Unit>
        fun ordersUpdated(): Observable<Map<String, Pair<Profile, Order>>>
        fun setOrders(profileIdOrderMap: Map<String, Pair<Profile, Order>>)
    }

    fun setProfileIdOrderMap(profileIdOrderMap: Map<String, Pair<Profile, Order>>) {
        this@OrdersInteractor.profileIdOrderMap = profileIdOrderMap
    }
}
