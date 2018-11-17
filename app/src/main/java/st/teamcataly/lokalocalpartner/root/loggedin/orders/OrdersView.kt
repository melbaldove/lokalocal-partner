package st.teamcataly.lokalocalpartner.root.loggedin.orders

import android.content.Context
import android.util.AttributeSet
import com.airbnb.epoxy.Carousel
import kotlinx.android.synthetic.main.orders_rib.view.*
import st.teamcataly.lokalocalpartner.*
import android.support.v7.widget.GridLayoutManager
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import st.teamcataly.lokalocalpartner.root.loggedin.Profile


/**
 * Top level view for {@link OrdersBuilder.OrdersScope}.
 */
class OrdersView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), OrdersInteractor.OrdersPresenter {

    private val itemOrderQuantity = mutableMapOf<String, Int>()
    private val addOrderClickObservable = PublishSubject.create<String>()
    private val ordersUpdateObservable = PublishSubject.create<Map<String, Pair<Profile, Order>>>()
    private var profileIdOrderMap: Map<String, Pair<Profile, Order>> = mapOf()
    private var selectedProfileId = "-1"
    private var lastItemId = "-1"
    private var valueChange = 0

    val menu = mutableListOf<CoffeeItem>()

    override fun onFinishInflate() {
        super.onFinishInflate()


        loadRecyclerView()
    }

    private fun loadRecyclerView() {
        Carousel.setDefaultGlobalSnapHelperFactory(null)

        orders_epoxy_rv.layoutManager = GridLayoutManager(context, 2)
        orders_epoxy_rv.withModels {

            val list = profileIdOrderMap.map { it.value.first }

            profileIdOrderMap[selectedProfileId]?.second?.items?.forEach {
                itemOrderQuantity[it.itemId] = it.quantity
            }

            header {
                id("orders")
                title("Orders")
                spanSizeOverride { totalSpanCount, position, itemCount -> totalSpanCount }
            }

            val profiles = mutableListOf<Profile>()
            profiles.add(Profile("-1", "Unregistered", ""))
            profiles.addAll(list)

            carousel {
                id("ordersCarousel")
                initialPrefetchItemCount(3)
                var isFirstTime = true
                spanSizeOverride { totalSpanCount, position, itemCount -> totalSpanCount }
                withModelsFrom(profiles) { profile ->
                    if (isFirstTime) {
                        isFirstTime = false
                        return@withModelsFrom CardAddBindingModel_()
                                .id("addOrder")
                                .isSelected(false)
                                .name("")
                                .status("")
                                .onAddOrderClicked { _ ->
                                    addOrderClickObservable.onNext("")
                                }
                    } else {
                        return@withModelsFrom CardBindingModel_()
                                .id(profile.id)
                                .isSelected(profile.id == selectedProfileId)
                                .name(profile.firstName + " " + profile.lastName)
                                .status(if (profile.id == selectedProfileId) "Selected" else "Draft")
                                .onSelectListener { _ ->
                                    if (profile.id != selectedProfileId) {
                                        itemOrderQuantity.clear()
                                    }
                                    selectedProfileId = profile.id!!
                                    updateAllAndRebuild()
                                }
                    }
                }

            }

            if (selectedProfileId == "-1") return@withModels

            header {
                id("coffee")
                title("Coffee")
                spanSizeOverride { totalSpanCount, position, itemCount -> totalSpanCount }
            }

            menu.forEach { coffee ->

                coffee {
                    id(coffee.id)
                    name(coffee.itemName)
                    image(coffee.itemPath)
                    price("${coffee.price} Credits")
                    quantity(itemOrderQuantity[coffee.id]?.toString() ?: "0")
                    spanSizeOverride { totalSpanCount, position, itemCount -> 1 }

                    onIncrement { _ ->
                        lastItemId = coffee.id
                        val itemQuantity = itemOrderQuantity[coffee.id]
                        if (itemQuantity == null) {
                            itemOrderQuantity[coffee.id] = 1
                            valueChange = 0
                        } else {
                            itemOrderQuantity[coffee.id] = itemQuantity + 1
                            valueChange = 1
                        }

                        updateAllAndRebuild()
                    }

                    onDecrement { _ ->
                        lastItemId = coffee.id
                        val itemQuantity = itemOrderQuantity[coffee.id]
                        if (itemQuantity == null || itemQuantity == 0) {
                            itemOrderQuantity[coffee.id] = 0
                            valueChange = 0
                        } else {
                            itemOrderQuantity[coffee.id] = itemQuantity - 1
                            valueChange = -1
                        }

                        updateAllAndRebuild()
                    }
                }


            }
        }

        updateAllAndRebuild()
    }

    override fun loadMenu(coffeeItems: List<CoffeeItem>) {
        menu.addAll(coffeeItems)
        updateAllAndRebuild()
    }

    private fun updateNumberOfOrder() {
        val list = profileIdOrderMap.map { it.value.first }
        if (list.isNotEmpty()) {
            selectedProfileId = if (selectedProfileId == "-1") {
                list.first().id!!
            } else {
                selectedProfileId
            }
            profileIdOrderMap[selectedProfileId]?.second?.items?.forEach {
                itemOrderQuantity[it.itemId] = it.quantity + if (lastItemId == it.itemId) {
                    valueChange
                } else {
                    0
                }

                if (lastItemId == it.itemId) {
                    valueChange = 0
                }
            }
        }
        number_of_orders.text = itemOrderQuantity.map { it.value }.sum().toString()
        total_price.text = itemOrderQuantity.map { item ->
            (menu.find { coffeeItem -> coffeeItem.id == item.key }?.price ?: 0) * item.value
        }.sum().toString() + " Credits"
    }

    private fun updateAllAndRebuild() {
        orders_epoxy_rv.requestModelBuild()
        updateNumberOfOrder()

        if (selectedProfileId != "-1") {
            val updatedOrders = profileIdOrderMap.toMutableMap()
            updatedOrders[selectedProfileId] = updatedOrders[selectedProfileId]!!.first to Order(selectedProfileId, itemOrderQuantity.map {
                Item(it.key, itemOrderQuantity[it.key] ?: 0)
            })
            ordersUpdateObservable.onNext(updatedOrders)
        }
    }

    override fun newOrder() = addOrderClickObservable.hide().map { }!!

    override fun setOrders(profileIdOrderMap: Map<String, Pair<Profile, Order>>) {
        this@OrdersView.profileIdOrderMap = profileIdOrderMap
        orders_epoxy_rv.requestModelBuild()
    }

    override fun ordersUpdated(): Observable<Map<String, Pair<Profile, Order>>> = ordersUpdateObservable

    override fun buyOrder() = RxView.clicks(orders_checkout).map<Order>{
        return@map profileIdOrderMap[selectedProfileId]?.second
    }

    override fun reload() {
        selectedProfileId = "-1"
        updateAllAndRebuild()
    }
}
