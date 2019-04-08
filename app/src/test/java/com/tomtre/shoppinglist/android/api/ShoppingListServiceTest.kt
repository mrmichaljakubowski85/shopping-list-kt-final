package com.tomtre.shoppinglist.android.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tomtre.shoppinglist.android.util.LiveDataTestUtil.getValue
import com.tomtre.shoppinglist.android.util.LiveDataCallAdapterFactory
import com.tomtre.shoppinglist.android.vo.Product
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
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
    fun `When call getProduct Then request correct path`() {
        //given
        enqueueResponse("single-product-response.json")

        //when
        service.getProduct(1L)

        //then
        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("products/1"))
    }

    @Test
    fun `When call getProduct Then body response holds correct Product`() {
        //given
        enqueueResponse("single-product-response.json")

        //when
        val apiSuccessResponse = getValue(service.getProduct(1L)) as ApiSuccessResponse

        //then
        val productResponse = apiSuccessResponse.body
        assertThat<Product>(productResponse, notNullValue())
        assertThat(productResponse.title, `is`("product title"))

    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
//      val inputStream = javaClass.classLoader.getResourceAsStream("api-response/$fileName")
        val inputStream = checkNotNull(
            this.javaClass::class.java.getResourceAsStream("api-response/$fileName")
        ) { "File $fileName doesn't exist." }
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