package com.easy.kotlin.picturecrawler.controller

import com.easy.kotlin.picturecrawler.dao.SearchKeyWordRepository
import com.easy.kotlin.picturecrawler.entity.SearchKeyWord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

/**
 * Created by jack on 2017/7/22.
 */

@Controller
class SearchKeyWordController {

    @Autowired lateinit var searchKeyWordRepository: SearchKeyWordRepository

    @RequestMapping(value = "search_keyword_view", method = arrayOf(RequestMethod.GET))
    fun sotuView(model: Model, request: HttpServletRequest): ModelAndView {
        model.addAttribute("requestURI", request.requestURI)
        return ModelAndView("search_keyword_view")
    }


    @RequestMapping(value = "searchKeyWordJson", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun sotuSearchJson(@RequestParam(value = "page", defaultValue = "0") page: Int, @RequestParam(value = "size", defaultValue = "10") size: Int, @RequestParam(value = "searchText", defaultValue = "") searchText: String): Page<SearchKeyWord> {
        return getPageResult(page, size, searchText)
    }

    private fun getPageResult(page: Int, size: Int, searchText: String): Page<SearchKeyWord> {
        val sort = Sort(Sort.Direction.DESC, "id")
        // 注意：PageRequest.of(page,size,sort) page 默认是从0开始
        val pageable = PageRequest.of(page, size, sort)
        if (searchText == "") {
            return searchKeyWordRepository.findAll(pageable)
        } else {
            return searchKeyWordRepository.search(searchText, pageable)
        }
    }


    @RequestMapping(value = "save_keyword", method = arrayOf(RequestMethod.GET,RequestMethod.POST))
    @ResponseBody
    fun save(@RequestParam(value = "keyWord")keyWord:String): String {
        searchKeyWordRepository.saveOnNoDuplicateKey(keyWord)
        return "1"
    }
}
