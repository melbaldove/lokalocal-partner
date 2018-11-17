package st.teamcataly.lokalocalpartner.root.loggedin.orders

import android.content.Context
import android.util.AttributeSet
import com.airbnb.epoxy.Carousel
import kotlinx.android.synthetic.main.orders_rib.view.*
import st.teamcataly.lokalocalpartner.*
import android.support.v7.widget.GridLayoutManager
import android.widget.LinearLayout


/**
 * Top level view for {@link OrdersBuilder.OrdersScope}.
 */
class OrdersView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), OrdersInteractor.OrdersPresenter {

    private val itemOrderQuantity = mutableMapOf<String, Int>()

    override fun onFinishInflate() {
        super.onFinishInflate()

        val list = listOf<String>("", "A-Ar Andrew Concepcion", "Zyb Jared Valdez", "Melbourne Baldove")

        val menu = listOf<CoffeeItem>(
                CoffeeItem(id = "1", itemName = "Cafe Latte Ala Pobre", itemImage = ""),
                CoffeeItem(id = "2", itemName = "Cafe Latte Ala Pobre", itemImage = ""),
                CoffeeItem(id = "3", itemName = "Cafe Latte Ala Pobre", itemImage = ""),
                CoffeeItem(id = "4", itemName = "Cafe Latte Ala Pobre", itemImage = ""),
                CoffeeItem(id = "5", itemName = "Cafe Latte Ala Pobre", itemImage = ""),
                CoffeeItem(id = "6", itemName = "Cafe Latte Ala Pobre", itemImage = ""),
                CoffeeItem(id = "7", itemName = "Cafe Latte Ala Pobre", itemImage = "")
        )


        Carousel.setDefaultGlobalSnapHelperFactory(null)

        orders_epoxy_rv.layoutManager = GridLayoutManager(context, 2)
        orders_epoxy_rv.withModels {

            header {
                id("orders")
                title("Orders")
                spanSizeOverride { totalSpanCount, position, itemCount -> totalSpanCount }
            }

            carousel {
                id("ordersCarousel")
                initialPrefetchItemCount(3)
                var isFirstTime = true
                spanSizeOverride { totalSpanCount, position, itemCount -> totalSpanCount }
                withModelsFrom(list) {
                    if (isFirstTime) {
                        isFirstTime = false
                        return@withModelsFrom CardAddBindingModel_()
                                .id("addOrder")
                                .isSelected(false)
                                .name("")
                                .status("")
                    } else {
                        return@withModelsFrom CardBindingModel_()
                                .id(it)
                                .isSelected(false)
                                .name(it)
                                .status("Draft")
                    }
                }

            }

            header {
                id("coffee")
                title("Coffee")
                spanSizeOverride { totalSpanCount, position, itemCount -> totalSpanCount }
            }

            menu.forEach { coffee ->
                coffee {
                    id(coffee.id)
                    name(coffee.itemName)
                    image(coffee.itemImage)
                    quantity(itemOrderQuantity[coffee.id]?.toString() ?: "0")
                    spanSizeOverride { totalSpanCount, position, itemCount -> 1 }

                    onIncrement { _ ->
                        val itemQuantity = itemOrderQuantity[coffee.id]
                        if (itemQuantity == null) {
                            itemOrderQuantity[coffee.id] = 1
                        } else {
                            itemOrderQuantity[coffee.id] = itemQuantity + 1
                        }

                        updateNumberOfOrder()

                    }

                    onDecrement { _ ->
                        val itemQuantity = itemOrderQuantity[coffee.id]
                        if (itemQuantity == null || itemQuantity == 0) {
                            itemOrderQuantity[coffee.id] = 0
                        } else {
                            itemOrderQuantity[coffee.id] = itemQuantity - 1
                        }

                        updateNumberOfOrder()
                    }
                }
            }
        }
    }

    private fun updateNumberOfOrder() {
        orders_epoxy_rv.requestModelBuild()
        number_of_orders.text = itemOrderQuantity.map { it.value }.sum().toString()

    }

//    private fun setData(locations: List<Orders>) {
//
//    }
}
