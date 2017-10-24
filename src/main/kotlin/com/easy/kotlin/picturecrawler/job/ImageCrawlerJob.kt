package com.easy.kotlin.picturecrawler.job

import com.easy.kotlin.picturecrawler.service.CrawImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*


@Component
class ImageCrawlerJob {

    @Autowired lateinit var CrawImagesService: CrawImageService
    @Scheduled(cron = "0 */5 * * * ?")
    fun job() {
        println("开始执行定时任务： ${Date()}")
        CrawImagesService.doCrawJob()
    }
}


