package com.zenora.elemento.common.network

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.zenora.elemento.BaseApplication
import com.zenora.elemento.BuildConfig
import com.zenora.elemento.common.SharedPreferenceHelper
import com.zenora.elemento.common.constants.AppConstants
import com.zenora.elemento.common.constants.PreferenceConstants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Class to handle Retrofit client builder.
 */
object AppAPIClient : KoinComponent {

    private val headerInterceptor: HeaderInterceptor by inject()
    private val connectivityInterceptor: ConnectivityInterceptor by inject()
    var retryCount = 0

    fun retrofitBuilder(okHttpClient: OkHttpClient, url: String): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(url)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    fun getUserOkhttpClientWithHeader(isHeaderNeedToPass: Boolean = true): OkHttpClient {

        val builder = OkHttpClient().newBuilder()
        builder.addInterceptor(connectivityInterceptor)
        if (isHeaderNeedToPass) {
            builder.addInterceptor(headerInterceptor)
            builder.readTimeout(300, TimeUnit.SECONDS)
            builder.writeTimeout(300, TimeUnit.SECONDS)
            builder.connectTimeout(300, TimeUnit.SECONDS)

            //todo This will accept all the certificate. Basically not  safe :)
            val trustAllCerts = getTrustAllCerts()
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            /*-----------------End of the certificate---------------------*/

            builder.authenticator(object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    val header = response.headers
                    if (!response.isSuccessful && response.code == 401 && response.body != null
                        && header["Content-Type"] != null && header["Content-Type"] == "application/json"
                    ) {
                        return null
                    }

                    if (!response.isSuccessful && response.code == 401) {
                        if (retryCount < 6) {
                            retryCount++
                        } else {
                            Log.e("Logout", "Retry completed")
                            retryCount = 0
                            LocalBroadcastManager.getInstance(BaseApplication.applicationContext())
                                .sendBroadcast(Intent(AppConstants.ACTION_LOGOUT))
                            return null
                        }
                    } else {
                        retryCount = 0
                    }

                    val responseData =
                        retrofitBuilder.create(APIInterfaces::class.java).getRefreshToken()
                            .execute()


                    @Suppress("SENSELESS_COMPARISON")
                    if (null != responseData && responseData.isSuccessful && null != responseData.body()) {
                        retryCount = 0
                        SharedPreferenceHelper.saveString(
                            PreferenceConstants.ACCESS_TOKEN,
                            responseData.body()?.token.toString()
                        )
                        SharedPreferenceHelper.saveString(
                            PreferenceConstants.REFRESH_TOKEN,
                            responseData.body()?.refreshToken.toString()
                        )
                        return response.request.newBuilder()
                            .header(
                                "Authorization",
                                "Bearer ${SharedPreferenceHelper.getString(PreferenceConstants.ACCESS_TOKEN)}"
                            )
                            .header(
                                "X-Refresh-Token",
                                SharedPreferenceHelper.getString(PreferenceConstants.REFRESH_TOKEN)
                                    ?: ""
                            ).build()
                    }
                    return null
                }
            })
        }
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }


    val retrofitBuilder by lazy {
        retrofitBuilder(getUserOkhttpClientWithHeader(), "")
    }


    inline fun <reified T> createWebService(url: String): T {
        val retrofit = retrofitBuilder(getUserOkhttpClientWithHeader(), url)
        return retrofit.create(T::class.java)
    }

    /**
     * For accepting all the certificate given
     * */
    private fun getTrustAllCerts(): Array<TrustManager> {
        return arrayOf(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                Log.e("AppAPIClient", authType)
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                Log.e("AppAPIClient", authType)
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
    }

}