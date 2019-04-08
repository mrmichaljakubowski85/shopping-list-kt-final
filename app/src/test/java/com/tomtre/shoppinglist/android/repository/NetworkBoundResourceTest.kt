package com.tomtre.shoppinglist.android.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tomtre.shoppinglist.android.api.ApiResponse
import com.tomtre.shoppinglist.android.util.ApiUtil
import com.tomtre.shoppinglist.android.util.CountingAppExecutors
import com.tomtre.shoppinglist.android.util.InstantAppExecutors
import com.tomtre.shoppinglist.android.util.mock
import com.tomtre.shoppinglist.android.vo.Resource
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.then
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.never
import retrofit2.Response
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@RunWith(Parameterized::class)
class NetworkBoundResourceTest(private val useRealExecutors: Boolean) {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var handleSaveCallResult: (Foo) -> Unit

    private lateinit var handleShouldMatch: (Foo?) -> Boolean

    private lateinit var handleCreateCall: () -> LiveData<ApiResponse<Foo>>

    private val dbData = MutableLiveData<Foo>()

    private lateinit var networkBoundResource: NetworkBoundResource<Foo, Foo>

    private val fetchedOnce = AtomicBoolean(false)
    private lateinit var countingAppExecutors: CountingAppExecutors

    init {
        if (useRealExecutors) {
            countingAppExecutors = CountingAppExecutors()
        }
    }

    @Before
    fun init() {
        val appExecutors =
            if (useRealExecutors)
                countingAppExecutors.appExecutors
            else
                InstantAppExecutors()

        networkBoundResource = object : NetworkBoundResource<Foo, Foo>(appExecutors) {
            override fun saveCallResult(item: Foo) {
                handleSaveCallResult(item)
            }

            override fun shouldFetch(data: Foo?): Boolean {
                // since test methods don't handle repetitive fetching, call it only once
                return handleShouldMatch(data) && fetchedOnce.compareAndSet(false, true)
            }

            override fun loadFromDb(): LiveData<Foo> {
                return dbData
            }

            override fun createCall(): LiveData<ApiResponse<Foo>> {
                return handleCreateCall()
            }

        }
    }

    @Test
    fun `When success network call Then return success resource`() {
        //given
        val saved = AtomicReference<Foo>()
        handleShouldMatch = { it == null }
        val fetchedDbValue = Foo(1)
        handleSaveCallResult = { foo ->
            saved.set(foo)
            dbData.value = fetchedDbValue
        }
        val networkResult = Foo(1)
        handleCreateCall = { ApiUtil.createCall(Response.success(networkResult))}

        //when
        val observer = mock<Observer<Resource<Foo>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        drain()
        dbData.value = null
        drain()

        //then
        val inOrder = inOrder(observer)
        then(observer).should(inOrder).onChanged(Resource.loading(null))
        assertThat(saved.get(), `is`(networkResult))
        then(observer).should(inOrder).onChanged(Resource.success(fetchedDbValue))
        then(observer).should(inOrder, never()).onChanged(any())
    }

    @Test
    fun `When failure network call Then return error resource`() {
        //given
        val saved = AtomicBoolean(false)
        handleShouldMatch = { it == null }
        handleSaveCallResult = { foo ->
            saved.set(true)
        }
        val body = ResponseBody.create(MediaType.parse("text/html"), "error message")
        handleCreateCall = { ApiUtil.createCall(Response.error<Foo>(500, body))}

        //when
        val observer = mock<Observer<Resource<Foo>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        drain()
        dbData.value = null
        drain()

        //then
        val inOrder = inOrder(observer)
        then(observer).should(inOrder).onChanged(Resource.loading(null))
        assertThat(saved.get(), `is`(false))
        then(observer).should(inOrder).onChanged(Resource.error("error message", null))
        then(observer).should(inOrder, never()).onChanged(any())
    }

    private fun drain() {
        if (!useRealExecutors) {
            return
        }
        try {
            countingAppExecutors.drainTasks(1, TimeUnit.SECONDS)
        } catch (t: Throwable) {
            throw AssertionError(t)
        }
    }

    private data class Foo(var value: Int)

    companion object {
        @Parameterized.Parameters
        @JvmStatic
        fun param(): List<Boolean> {
            return arrayListOf(true, false)
        }
    }
}