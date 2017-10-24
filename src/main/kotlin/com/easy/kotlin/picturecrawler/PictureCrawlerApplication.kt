package com.easy.kotlin.picturecrawler

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class PictureCrawlerApplication

fun main(args: Array<String>) {
    SpringApplication.run(PictureCrawlerApplication::class.java, *args)
}
