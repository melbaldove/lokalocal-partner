package st.teamcataly.lokalocalpartner.root

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*
import st.teamcataly.lokalocalpartner.root.loggedin.Credentials
import st.teamcataly.lokalocalpartner.root.loggedin.Partner
import st.teamcataly.lokalocalpartner.root.loggedin.Profile
import st.teamcataly.lokalocalpartner.root.loggedin.orders.CoffeeItem
import st.teamcataly.lokalocalpartner.root.loggedin.orders.Order

/**
 * @author Melby Baldove
 * melbourne.baldove@owtoph.com
 */
interface LokaLocalApi {

    @POST("partner/login")
    fun login(@Body credentials: Credentials): Single<Partner>

    @GET("partner/{card}/menu")
    fun getMenu(@Path("card") id: String): Single<List<CoffeeItem>>

    @POST("partner/{partnerId}/buy")
    fun buy(@Path("partnerId") id: String, @Body request: Order): Completable

    @GET("customer/findOne")
    fun getByQrId(@Query("filter[where][qrId]") cardId: String): Single<Profile>
}