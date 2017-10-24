package com.easy.kotlin.picturecrawler

import com.easy.kotlin.picturecrawler.api.ImageSearchApiBuilder
import com.easy.kotlin.picturecrawler.service.JsonResultProcessor
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class JsonResultProcessorTest {
    @Test
    fun testJsonResultProcessor() {
        val list = JsonResultProcessor.getImageCategoryAndUrlList(ImageSearchApiBuilder.build("美女", 1))
        println(list)
    }
}
