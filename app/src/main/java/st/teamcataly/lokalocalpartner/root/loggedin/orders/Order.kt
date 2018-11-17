package st.teamcataly.lokalocalpartner.root.loggedin.orders

data class Order(
        val id: String,
        val items: List<Item>
)

data class Item(
        val id: String,
        val quantity: Int
)