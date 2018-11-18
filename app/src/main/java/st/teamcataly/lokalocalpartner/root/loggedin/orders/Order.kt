package st.teamcataly.lokalocalpartner.root.loggedin.orders

data class Order(
        val card: String,
        val items: List<Item>
)

data class Item(
        val itemId: String,
        val quantity: Int
)