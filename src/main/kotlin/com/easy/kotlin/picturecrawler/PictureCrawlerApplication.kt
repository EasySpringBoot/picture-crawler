package com.easy.kotlin.picturecrawler

import com.easy.kotlin.picturecrawler.dao.SearchKeyWordRepository
import com.easy.kotlin.picturecrawler.entity.SearchKeyWord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component
import java.io.File


@SpringBootApplication
@EnableScheduling
class PictureCrawlerApplication

fun main(args: Array<String>) {
    SpringApplication.run(PictureCrawlerApplication::class.java, *args)
}


@Component
@Order(value = Ordered.LOWEST_PRECEDENCE)
class initSearchKeyWordRunner : CommandLineRunner {
    @Autowired lateinit var searchKeyWordRepository: SearchKeyWordRepository

    override fun run(vararg args: String) {
        var keyWords = File("搜索关键词列表.data").readLines()
        keyWords.forEach {
            val SearchKeyWord = SearchKeyWord()
            SearchKeyWord.keyWord = it
            searchKeyWordRepository.saveOnNoDuplicateKey(it)
        }
    }
}
