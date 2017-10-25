package com.easy.kotlin.picturecrawler.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(indexes = arrayOf(Index(name = "idx_key_word", columnList = "keyWord", unique = true)))
class SearchKeyWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1
    @Column(name = "keyWord", length = 50, nullable = false, unique = true)
    var keyWord: String = ""

    var gmtCreated: Date = Date()
    var gmtModified: Date = Date()
    var isDeleted: Int = 0  //1 Yes 0 No
    var deletedDate: Date = Date()
}
