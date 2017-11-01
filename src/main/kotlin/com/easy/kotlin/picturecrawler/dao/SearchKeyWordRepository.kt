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


/**

事物传播行为介绍:

　　@Transactional(propagation=Propagation.REQUIRED) ：如果有事务, 那么加入事务, 没有的话新建一个(默认情况下)
　　@Transactional(propagation=Propagation.NOT_SUPPORTED) ：容器不为这个方法开启事务
　　@Transactional(propagation=Propagation.REQUIRES_NEW) ：不管是否存在事务,都创建一个新的事务,原来的挂起,新的执行完毕,继续执行老的事务
　　@Transactional(propagation=Propagation.MANDATORY) ：必须在一个已有的事务中执行,否则抛出异常
　　@Transactional(propagation=Propagation.NEVER) ：必须在一个没有的事务中执行,否则抛出异常(与Propagation.MANDATORY相反)
　　@Transactional(propagation=Propagation.SUPPORTS) ：如果其他bean调用这个方法,在其他bean中声明事务,那就用事务.如果其他bean没有声明事务,那就不用事务.



事物超时设置:

　　@Transactional(timeout=30) //默认是30秒



事务隔离级别:

　　@Transactional(isolation = Isolation.READ_UNCOMMITTED)：读取未提交数据(会出现脏读, 不可重复读) 基本不使用
　　@Transactional(isolation = Isolation.READ_COMMITTED)：读取已提交数据(会出现不可重复读和幻读)
　　@Transactional(isolation = Isolation.REPEATABLE_READ)：可重复读(会出现幻读)
　　@Transactional(isolation = Isolation.SERIALIZABLE)：串行化

　　MYSQL: 默认为REPEATABLE_READ级别
　　SQLSERVER: 默认为READ_COMMITTED

 */
