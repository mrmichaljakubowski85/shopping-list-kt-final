package com.tomtre.shoppinglist.android.api

import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class ApiResponseTest {

    @Test
    fun `When create ApiErrorResponse from Exception Then ApiErrorResponse holds correct error message`() {
        //given
        val exception = Exception("error message")

        //when
        val apiErrorResponse = ApiResponse.create<String>(exception) as ApiErrorResponse<String>

        //then
        assertThat(apiErrorResponse.errorMessage, `is`("error message"))
    }

    @Test
    fun `When create ApiSuccessResponse Then ApiSuccessResponse holds correct body value`() {
        //given
        val successResponse = Response.success("body value")

        //when
        val apiSuccessResponse = ApiResponse.create(successResponse) as ApiSuccessResponse

        //then
        assertThat(apiSuccessResponse.body, `is`("body value"))
    }

    @Test
    fun `When create ApiErrorResponse from error response Then ApiErrorResponse holds correct error message`() {
        //given
        val errorResponse = Response.error<String>(400,
            ResponseBody.create(MediaType.parse("application/txt"), "error message"))

        //when
        val apiErrorResponse = ApiResponse.create<String>(errorResponse) as ApiErrorResponse

        //then
        assertThat(apiErrorResponse.errorMessage, `is`("error message"))
    }
}