package st.teamcataly.lokalocalpartner.root.loggedin

import com.uber.rib.core.Bundle
import com.uber.rib.core.EmptyPresenter
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import st.teamcataly.lokalocalpartner.root.loggedin.neworder.NewOrderInteractor
import st.teamcataly.lokalocalpartner.root.loggedin.orders.Order
import st.teamcataly.lokalocalpartner.root.loggedin.orders.OrdersInteractor
import java.util.*

/**
 * Coordinates Business Logic for [LoggedInScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class LoggedInInteractor : Interactor<EmptyPresenter, LoggedInRouter>() {

    val profiles = mutableListOf<Profile>()
    val profileIdOrderMap = mutableMapOf<String, Pair<Profile, Order>>()
    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        router.attachOrders(profileIdOrderMap)
    }

    override fun willResignActive() {
        super.willResignActive()
    }

    inner class OrdersListener : OrdersInteractor.Listener {
        override fun onOrdersUpdated(profileIdOrderMap: Map<String, Pair<Profile, Order>>) {
            this@LoggedInInteractor.profileIdOrderMap.clear()
            this@LoggedInInteractor.profileIdOrderMap.putAll(profileIdOrderMap)
        }

        override fun onNewOrder() {
            router.detachOrders()
            router.attachNewOrder()
        }
    }

    inner class NewOrderListener : NewOrderInteractor.Listener {
        override fun onBackClicked() {
            router.detachNewOrder()
            router.attachOrders(profileIdOrderMap)
        }

        override fun onQRScanned(data: String) {
            processQr(data)
        }
    }

    fun processQr(qr: String) {
//        val profile: Profile = Gson().fromJson(qr, Profile::class.java)

        val userId = UUID.randomUUID().toString()
        val profile = Profile(id = userId, lastName = "Unregistered", firstName = "")
        if (profiles.firstOrNull { it.id == userId } == null) {
            profiles.add(profile)
        }

        val profileIdOrder = profileIdOrderMap[userId]
        if (profileIdOrder == null) {
            profileIdOrderMap[userId] = profile to Order("", listOf())
        }

        // do something
        router.detachNewOrder()
        router.attachOrders(profileIdOrderMap)

    }
}
