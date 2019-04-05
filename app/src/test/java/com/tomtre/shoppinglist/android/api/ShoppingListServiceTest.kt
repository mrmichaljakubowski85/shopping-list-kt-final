package com.tomtre.shoppinglist.android.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tomtre.shoppinglist.android.LiveDataTestUtil.getValue
import com.tomtre.shoppinglist.android.util.LiveDataCallAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class ShoppingListServiceTest {
    @Rule
    @JvmField
    val instantExecutionRule = InstantTaskExecutorRule()

    private lateinit var service: ShoppingListService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(ShoppingListService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun `When xxxx`() {
        enqueueResponse("single-product-response.json")
        val apiSuccessResponse = getValue(service.getProduct(1L)) as ApiSuccessResponse
        val bodyResponse = apiSuccessResponse.body

    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
//      val inputStream = javaClass.classLoader.getResourceAsStream("api-response/$fileName")
        val inputStream = checkNotNull(
            this.javaClass::class.java.getResourceAsStream("api-response/$fileName")
        ) { "File doesn't exist." }
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse.setBody(
                source.readString(Charsets.UTF_8)
            )
        )
    }

}