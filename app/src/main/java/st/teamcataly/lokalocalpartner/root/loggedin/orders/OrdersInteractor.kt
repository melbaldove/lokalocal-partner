package st.teamcataly.lokalocalpartner.root.loggedin.orders

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import st.teamcataly.lokalocalpartner.addTo
import st.teamcataly.lokalocalpartner.root.LokaLocalApi
import st.teamcataly.lokalocalpartner.root.loggedin.Partner
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
    @Inject lateinit var lokaLocalApi: LokaLocalApi

    private val disposables = CompositeDisposable()

    private var profileIdOrderMap: Map<String, Pair<Profile, Order>> = mapOf()
    private var partner: Partner? = null

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.newOrder().subscribe {
            listener.onNewOrder()
        }.addTo(disposables)

        presenter.ordersUpdated().subscribe {
            listener.onOrdersUpdated(it)
        }.addTo(disposables)
        presenter.setOrders(profileIdOrderMap)
        lokaLocalApi.getMenu(partner!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    presenter.loadMenu(it)
                }, {

                })
                .addTo(disposables)

        presenter.buyOrder().subscribe { order ->

            processPurchase(order)
        }
    }

    private fun processPurchase(order: Order) {
        lokaLocalApi.buy(partner!!.id, order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    profileIdOrderMap = profileIdOrderMap.filter { it.key != order.card }
                    listener.onOrdersUpdated(profileIdOrderMap)
                    presenter.setOrders(profileIdOrderMap)
                    presenter.reload()
                }, {

                })
                .addTo(disposables)
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
        fun loadMenu(coffeeItems: List<CoffeeItem>)
        fun buyOrder(): Observable<Order>
        fun reload()
    }

    fun setProfileIdOrderMap(profileIdOrderMap: Map<String, Pair<Profile, Order>>) {
        this@OrdersInteractor.profileIdOrderMap = profileIdOrderMap
    }

    fun setPartner(partner: Partner?) {
        this@OrdersInteractor.partner = partner
    }
}
