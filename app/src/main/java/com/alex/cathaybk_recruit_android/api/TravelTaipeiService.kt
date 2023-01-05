package com.alex.cathaybk_recruit_android.api

import com.alex.cathaybk_recruit_android.utilities.TRAVEL_TAIPEI_SERVICE_BASE_URL
import com.alex.cathaybk_recruit_android.vo.Attraction
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API communication setup
 */
interface TravelTaipeiService {

    /**
     * https://www.travel.taipei/open-api/zh-tw/Attractions/All?page=1
     */
    @Headers("accept: application/json")
    @GET("{lang}/Attractions/All")
    suspend fun getAttractions(
        @Path("lang") lang: String,
        @Query("categoryIds") categoryIds: String? = null,
        @Query("nlat") nlat: Double? = null,
        @Query("elong") elong: String? = null,
        @Query("page") page: Int? = null,
    ): AttractionsResponse

    class AttractionsResponse(
        val total: Int,
        val data: List<Attraction>
    )

    companion object {
        private const val BASE_URL = TRAVEL_TAIPEI_SERVICE_BASE_URL

        fun create(): TravelTaipeiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TravelTaipeiService::class.java)
        }
    }
}
