package com.easy.kotlin.picturecrawler.controller

import com.easy.kotlin.picturecrawler.service.CrawImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Created by jack on 2017/7/22.
 */

@Controller
class CrawController {

    @Autowired lateinit var crawImageService: CrawImageService

    @RequestMapping(value = "doCrawJob", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun doCrawJob(): String {
        crawImageService.doCrawJob()
        return "JOB Started"
    }

}
