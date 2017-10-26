package com.easy.kotlin.picturecrawler.job

import com.easy.kotlin.picturecrawler.service.CrawImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*


@Component
class ImageCrawlerJob {

    @Autowired lateinit var crawImagesService: CrawImageService

    @Scheduled(cron = "0 0 */1 * * ?")
    fun doBaiduImageCrawJob() {
        println("开始执行定时任务 doBaiduImageCrawJob： ${Date()}")
        crawImagesService.doBaiduImageCrawJob()
    }

    @Scheduled(cron = "0 0 9 */1 * ?")
    fun doGankImageCrawJob() {
        println("开始执行定时任务 doGankImageCrawJob： ${Date()}")
        crawImagesService.doGankImageCrawJob()
    }

}


