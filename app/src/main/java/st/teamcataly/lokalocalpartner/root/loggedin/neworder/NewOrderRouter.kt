package st.teamcataly.lokalocalpartner.root.loggedin.neworder

import android.view.View

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link NewOrderBuilder.NewOrderScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class NewOrderRouter(
    view: NewOrderView,
    interactor: NewOrderInteractor,
    component: NewOrderBuilder.Component) : ViewRouter<NewOrderView, NewOrderInteractor, NewOrderBuilder.Component>(view, interactor, component)
