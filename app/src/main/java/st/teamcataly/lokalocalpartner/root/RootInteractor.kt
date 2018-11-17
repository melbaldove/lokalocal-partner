package st.teamcataly.lokalocalpartner.root

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import st.teamcataly.lokalocalpartner.root.loggedin.Partner
import st.teamcataly.lokalocalpartner.root.loggedout.LoggedOutInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [RootScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class RootInteractor : Interactor<RootInteractor.RootPresenter, RootRouter>() {

    @Inject
    lateinit var presenter: RootPresenter
    private var partner: Partner? = null

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        router.attachLoggedOut()
    }

    override fun willResignActive() {
        super.willResignActive()

        // TODO: Perform any required clean up here, or delete this method entirely if not needed.
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RootPresenter

    inner class LoggedOutListener : LoggedOutInteractor.Listener {
        override fun onLoginSuccess(partner: Partner) {
            this@RootInteractor.partner = partner
            router.detachLoggedOut()
            router.attachLoggedIn(partner)
        }
    }
}
