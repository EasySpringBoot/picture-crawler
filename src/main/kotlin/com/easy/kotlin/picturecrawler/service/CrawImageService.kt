package com.easy.kotlin.picturecrawler.service

import com.easy.kotlin.picturecrawler.api.ImageSearchApiBuilder
import com.easy.kotlin.picturecrawler.dao.ImageRepository
import com.easy.kotlin.picturecrawler.dao.SearchKeyWordRepository
import com.easy.kotlin.picturecrawler.entity.Image
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CrawImageService {
    val logger = LoggerFactory.getLogger(CrawImageService::class.java)

    @Autowired lateinit var imageRepository: ImageRepository
    @Autowired lateinit var searchKeyWordRepository: SearchKeyWordRepository

    fun doCrawJob() = runBlocking {

        val list = searchKeyWordRepository.findAll()

        for (i in 1..100) {
            list.forEach {
                launch(CommonPool) {
                    saveImage(it.keyWord, i)
                }
            }
        }
    }

    private fun saveImage(word: String, i: Int) {
        val api = ImageSearchApiBuilder.build(word, i)
        JsonResultProcessor.getImageCategoryAndUrlList(api).forEach {
            val category = it.category
            val url = it.url
            if (imageRepository.countByUrl(url) == 0) {
                val Image = Image()
                Image.category = category
                Image.url = url
                imageRepository.save(Image)
            }
        }
    }
}
