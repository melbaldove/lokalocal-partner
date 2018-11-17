package st.teamcataly.lokalocalpartner.root.loggedin

import com.uber.rib.core.Router
import st.teamcataly.lokalocalpartner.root.RootView
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
    private val ordersBuilder: OrdersBuilder) : Router<LoggedInInteractor, LoggedInBuilder.Component>(interactor, component) {

    private var ordersRouter: OrdersRouter? = null

    fun attachOrders() {
        ordersRouter = ordersBuilder.build(rootView)
        attachChild(ordersRouter)
        rootView.addView(ordersRouter?.view)
    }

}
