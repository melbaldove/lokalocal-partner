package st.teamcataly.lokalocalpartner.root.loggedin

import com.google.gson.Gson
import com.uber.rib.core.Bundle
import com.uber.rib.core.EmptyPresenter
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import st.teamcataly.lokalocalpartner.addTo
import st.teamcataly.lokalocalpartner.root.LokaLocalApi
import st.teamcataly.lokalocalpartner.root.loggedin.neworder.Credit
import st.teamcataly.lokalocalpartner.root.loggedin.neworder.NewOrderInteractor
import st.teamcataly.lokalocalpartner.root.loggedin.orders.Order
import st.teamcataly.lokalocalpartner.root.loggedin.orders.OrdersInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [LoggedInScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class LoggedInInteractor : Interactor<EmptyPresenter, LoggedInRouter>() {

    @Inject lateinit var lokaLocalApi: LokaLocalApi

    private var partner: Partner? = null
    val profiles = mutableListOf<Profile>()
    val profileIdOrderMap = mutableMapOf<String, Pair<Profile, Order>>()
    val disposables = CompositeDisposable()

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        router.attachOrders(profileIdOrderMap, partner)
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
            router.attachOrders(profileIdOrderMap, partner)
        }

        override fun onQRScanned(data: String) {
            processQr(data)
        }
    }

    fun processQr(qr: String) {
        // {"creditId":"7666a7b9-66a5-450f-ba89-b4e19215d9c1"}
        val credit = Gson().fromJson(qr, Credit::class.java)
        val qrId = credit.creditId


        lokaLocalApi.getByQrId(qrId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val profile = it.copy(id = qrId)
                    if (profiles.firstOrNull { it.id == qrId } == null) {
                        profiles.add(profile)
                    }

                    val profileIdOrder = profileIdOrderMap[qrId]
                    if (profileIdOrder == null) {
                        profileIdOrderMap[qrId] = profile to Order(qrId, listOf())
                    }

                    // do something
                    router.detachNewOrder()
                    router.attachOrders(profileIdOrderMap, partner)
                }, {
                    var profile = Profile(id = qrId, lastName = "Unregistered", firstName = "")
                    if (profiles.firstOrNull { it.id == qrId } == null) {
                        profiles.add(profile)
                    }

                    val profileIdOrder = profileIdOrderMap[qrId]
                    if (profileIdOrder == null) {
                        profileIdOrderMap[qrId] = profile to Order(qrId, listOf())
                    }

                    // do something
                    router.detachNewOrder()
                    router.attachOrders(profileIdOrderMap, partner)
                })
                .addTo(disposables)



    }

    fun setPartner(partner: Partner) {
        this@LoggedInInteractor.partner = partner
    }
}
