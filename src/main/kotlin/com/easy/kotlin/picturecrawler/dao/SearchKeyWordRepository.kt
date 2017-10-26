package com.easy.kotlin.picturecrawler.dao

import com.easy.kotlin.picturecrawler.entity.SearchKeyWord
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional


/**
 * Created by jack on 2017/7/17.
 */

interface SearchKeyWordRepository : PagingAndSortingRepository<SearchKeyWord, Long> {
@Modifying
@Transactional
@Query(value = "INSERT INTO `search_key_word` (`deleted_date`, `gmt_created`, `gmt_modified`, `is_deleted`, `key_word`) VALUES (now(), now(), now(), '0', :keyWord) ON DUPLICATE KEY UPDATE `gmt_modified` = now()", nativeQuery = true)
fun saveOnNoDuplicateKey(@Param("keyWord") keyWord: String): Int

@Query("SELECT a from #{#entityName} a where a.isDeleted=0 order by a.id desc")
override fun findAll(pageable: Pageable): Page<SearchKeyWord>

@Query("SELECT a from #{#entityName} a where a.isDeleted=0 and a.keyWord like %:searchText% order by a.id desc")
fun search(@Param("searchText") searchText: String, pageable: Pageable): Page<SearchKeyWord>


@Modifying
@Transactional
@Query("update search_key_word a set a.total_image = (select count(*) from image i where i.is_deleted=0 and i.category like concat('%',a.key_word,'%'))", nativeQuery = true)
fun batchUpdateTotalImage()

}
