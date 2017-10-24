package com.easy.kotlin.picturecrawler.service

import com.easy.kotlin.picturecrawler.api.ImageSearchApiBuilder
import com.easy.kotlin.picturecrawler.dao.ImageRepository
import com.easy.kotlin.picturecrawler.entity.Image
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File

@Service
class CrawImageService {
    val logger = LoggerFactory.getLogger(CrawImageService::class.java)
    @Autowired lateinit var imageRepository: ImageRepository
    fun doCrawJob() {
        var 搜索关键词列表 = File("搜索关键词列表.data").readLines()

        for (i in 1..10) {
            搜索关键词列表.forEach {
                val api = ImageSearchApiBuilder.build(it, i)
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
    }
}
