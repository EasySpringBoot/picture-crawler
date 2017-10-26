package com.easy.kotlin.picturecrawler.job

import com.easy.kotlin.picturecrawler.dao.SearchKeyWordRepository
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
class BatchUpdateJob {

    @Autowired lateinit var searchKeyWordRepository: SearchKeyWordRepository

    @Scheduled(cron = "0 */5 * * * ?")
    fun job() {
        doBatchUpdate()
    }

    fun doBatchUpdate() = runBlocking {
        launch(CommonPool) {
            println("开始执行定时任务 batchUpdateTotalImage： ${Date()}")
            searchKeyWordRepository.batchUpdateTotalImage()
        }
    }
}