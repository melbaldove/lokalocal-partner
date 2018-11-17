package st.teamcataly.lokalocalpartner.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import st.teamcataly.lokalocalpartner.root.LokaLocalApi
import st.teamcataly.lokalocalpartner.root.RootBuilder

@Module
abstract class NetworkModule {

    @Module
    companion object {
        @RootBuilder.RootScope
        @JvmStatic
        @Provides
        fun provideHttpClient(
                httpLoggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient {
            return OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build()
        }

        @RootBuilder.RootScope
        @JvmStatic
        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor { }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        @RootBuilder.RootScope
        @JvmStatic
        @Provides
        fun gson() = Gson()

        @RootBuilder.RootScope
        @JvmStatic
        @Provides
        fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
                .client(httpClient)
                .baseUrl("http://68.183.226.73:3000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

        @RootBuilder.RootScope
        @JvmStatic
        @Provides
        fun provideApi(retrofit: Retrofit): LokaLocalApi {
            return retrofit.create(LokaLocalApi::class.java)
        }
    }

}