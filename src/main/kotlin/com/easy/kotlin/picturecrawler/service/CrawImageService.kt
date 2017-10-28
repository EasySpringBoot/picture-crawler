package com.easy.kotlin.picturecrawler.service

import com.easy.kotlin.picturecrawler.api.GankApiBuilder
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
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

@Service
class CrawImageService {
    val logger = LoggerFactory.getLogger(CrawImageService::class.java)

    @Autowired lateinit var imageRepository: ImageRepository
    @Autowired lateinit var searchKeyWordRepository: SearchKeyWordRepository

    fun doBaiduImageCrawJob() = runBlocking {
        val list = searchKeyWordRepository.findAll()

        for (i in 1..1000) {
            list.forEach {
                launch(CommonPool) {
                    saveBaiduImage(it.keyWord, i)
                }
            }
        }


    }

    fun doGankImageCrawJob() = runBlocking {
        for (page in 1..6) {
            launch(CommonPool) {
                saveGankImage(page)
            }
        }
    }


    private fun saveGankImage(page: Int) {
        val api = GankApiBuilder.build(page)
        JsonResultProcessor.getGankImageUrls(api).forEach {
            val url = it
            if (imageRepository.countByUrl(url) == 0) {
                val image = Image()
                image.category = "干货集中营福利 ${SimpleDateFormat("yyyy-MM-dd").format(Date())}"
                image.url = url
                image.sourceType = 1
                image.imageBlob = getByteArray(url)
                logger.info("image = ${image}")
                imageRepository.save(image)
            }
        }
    }

    private fun saveBaiduImage(word: String, i: Int) {
        val api = ImageSearchApiBuilder.build(word, i)
        JsonResultProcessor.getBaiduImageCategoryAndUrlList(api).forEach {
            val category = it.category
            val url = it.url
            if (imageRepository.countByUrl(url) == 0) {
                val image = Image()
                image.category = category
                image.url = url
                image.sourceType = 0
                //image.imageBlob = getByteArray(url)
                logger.info("image = ${image}")
                imageRepository.save(image)
            }
        }
    }

    private fun getByteArray(url: String): ByteArray {
        val urlObj = URL(url)
        return urlObj.readBytes()
    }
}
