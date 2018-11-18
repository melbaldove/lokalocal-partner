package st.teamcataly.lokalocalpartner.root.loggedin.orders

import android.view.View

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link OrdersBuilder.OrdersScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class OrdersRouter(
    view: OrdersView,
    interactor: OrdersInteractor,
    component: OrdersBuilder.Component) : ViewRouter<OrdersView, OrdersInteractor, OrdersBuilder.Component>(view, interactor, component)
