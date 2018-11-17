package st.teamcataly.lokalocalpartner.root

import com.uber.rib.core.ViewRouter
import st.teamcataly.lokalocalpartner.root.loggedin.LoggedInBuilder
import st.teamcataly.lokalocalpartner.root.loggedin.LoggedInRouter
import st.teamcataly.lokalocalpartner.root.loggedin.Partner
import st.teamcataly.lokalocalpartner.root.loggedout.LoggedOutBuilder
import st.teamcataly.lokalocalpartner.root.loggedout.LoggedOutRouter



/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class RootRouter(
        view: RootView,
        interactor: RootInteractor,
        component: RootBuilder.Component,
        private val loggedOutBuilder: LoggedOutBuilder,
        private val loggedInBuilder: LoggedInBuilder) : ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component) {

    private var loggedInRouter: LoggedInRouter? = null
    private var loggedOutRouter: LoggedOutRouter? = null

    fun attachLoggedOut() {
        loggedOutRouter = loggedOutBuilder.build(view)
        attachChild(loggedOutRouter)
        view.addView(loggedOutRouter?.view)
    }

    fun detachLoggedOut() {
        loggedOutRouter?.let {
            detachChild(it)
            view.removeView(it.view)
            loggedOutRouter = null
        }
    }

    fun attachLoggedIn(partner: Partner) {
        attachChild(loggedInBuilder.build(partner))
    }

    fun detachLoggedIn() {
    }
}
