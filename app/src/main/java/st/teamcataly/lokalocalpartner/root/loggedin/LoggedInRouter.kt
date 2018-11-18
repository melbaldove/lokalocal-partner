package st.teamcataly.lokalocalpartner.root.loggedin

import com.uber.rib.core.Router
import st.teamcataly.lokalocalpartner.root.RootView
import st.teamcataly.lokalocalpartner.root.loggedin.neworder.NewOrderBuilder
import st.teamcataly.lokalocalpartner.root.loggedin.neworder.NewOrderRouter
import st.teamcataly.lokalocalpartner.root.loggedin.orders.Order
import st.teamcataly.lokalocalpartner.root.loggedin.orders.OrdersBuilder
import st.teamcataly.lokalocalpartner.root.loggedin.orders.OrdersRouter

/**
 * Adds and removes children of {@link LoggedInBuilder.LoggedInScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class LoggedInRouter(
    interactor: LoggedInInteractor,
    component: LoggedInBuilder.Component,
    private val rootView: RootView,
    private val ordersBuilder: OrdersBuilder,
    private val newOrderBuilder: NewOrderBuilder) : Router<LoggedInInteractor, LoggedInBuilder.Component>(interactor, component) {

    private var ordersRouter: OrdersRouter? = null
    private var newOrderRouter: NewOrderRouter? = null

    fun attachOrders(profileIdOrderMap: MutableMap<String, Pair<Profile, Order>>, partner: Partner?) {
        ordersRouter = ordersBuilder.build(rootView, profileIdOrderMap, partner)
        attachChild(ordersRouter)
        rootView.addView(ordersRouter?.view)
    }

    fun detachOrders() {
        ordersRouter ?: return
        detachChild(ordersRouter)
        rootView.removeView(ordersRouter?.view)
        ordersRouter = null
    }

    fun attachNewOrder() {
        newOrderRouter = newOrderBuilder.build(rootView)
        attachChild(newOrderRouter)
        rootView.addView(newOrderRouter?.view)
    }

    fun detachNewOrder() {
        newOrderRouter ?: return
        detachChild(newOrderRouter)
        rootView.removeView(newOrderRouter?.view)
        newOrderRouter = null
    }
}
