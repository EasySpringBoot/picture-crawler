# 第13章 Kotlin 集成 SpringBoot 服务端开发

本章介绍Kotlin服务端开发的相关内容。首先，我们简单介绍一下Spring Boot服务端开发框架，快速给出一个 Restful Hello World的示例。然后，我们讲下 Kotlin 集成 Spring Boot 进行服务端开发的步骤，最后给出一个完整的 Web 应用开发实例。

## 13.1 SpringBoot 快速开始 Restful Hello World

Spring Boot 大大简化了使用 Spring 框架过程中的各种繁琐的配置, 另外可以更加方便的整合常用的工具链 (比如 Redis, Email, kafka, ElasticSearch, MyBatis, JPA) 等, 而缺点是集成度较高（事物都是两面性的），使用过程中不太容易了解底层，遇到问题了解决曲线比较陡峭。本节我们介绍怎样快速开始SpringBoot服务端开发。


### 13.1.1 Spring Initializr

工欲善其事必先利其器。我们使用 https://start.spring.io/ 可以直接自动生成 SpringBoot项目脚手架。如下图


![start.spring.io](http://upload-images.jianshu.io/upload_images/1233356-84524d5e4bfb1c21.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


点击“Switch to the full version ” ， 可以看到脚手架支持的工具链。

如果 https://start.spring.io/ 网络连不上，我们也可以自己搭建本地的 Spring Initializr服务。步骤如下
1. Git clone 源码到本机 https://github.com/spring-io/initializr
2. 源码根目录执行 $ ./mvnw clean install
3. 到initializr-service子项目目录下 cd initializr-service， 执行 ../mvnw spring-boot:run

即可看到启动日志
```
......
s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
i.s.i.service.InitializrService          : Started InitializrService in 15.192 seconds (JVM running for 15.882)
```
此时，我们本机浏览器访问 http://127.0.0.1:8080/ ，即可看到脚手架initializr页面。


### 13.1.2 创建SpringBoot项目

我们使用本地搭建的脚手架initializr， 页面上表单选项如下


![使用spring initializr创建SpringBoot项目](http://upload-images.jianshu.io/upload_images/1233356-afc659b67e3a702d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

首先 ，我们选择生成的是一个使用Gradle 构建的Kotlin项目，SpringBoot的版本号我们选择2.0.0(SNAPSHOT) 。

在 Spring Boot Starters 和 dependencies 选项中，我们选择 Web starter， 这个启动器里面包含了基本够用的Spring Web开发需要的东西：Tomcat 和 Spring MVC。

其余的项目元数据（Project Metadata）的配置（Bill Of Materials），我们可以从上面的图中看到。然后，点击“Generate Project” ，会自动下载一个项目的zip压缩包。解压导入IDEA中


![导入IDEA](http://upload-images.jianshu.io/upload_images/1233356-461ea8d6c80af877.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

因为我们使用的是Gradle构建项目，所以需要配置一下Gradle环境，这里我们使用的是Local gradle distribution , 选择对应的本地的 gradle 软件包目录。


#### 工程文件目录树

我们将得到如下一个样板工程，工程文件目录树如下
```
kotlin-with-springboot$ tree
.
├── build
│   └── kotlin-build
│       └── version.txt
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── kotlin-with-springboot.iml
└── src
    ├── main
    │   ├── java
    │   ├── kotlin
    │   │   └── com
    │   │       └── easy
    │   │           └── kotlin
    │   │               └── kotlinwithspringboot
    │   │                   └── KotlinWithSpringbootApplication.kt
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        ├── java
        ├── kotlin
        │   └── com
        │       └── easy
        │           └── kotlin
        │               └── kotlinwithspringboot
        │                   └── KotlinWithSpringbootApplicationTests.kt
        └── resources

23 directories, 10 files

```

其中，src/main/kotlin 是Kotlin源码放置目录。src/main/resources目录下面放置工程资源文件。application.properties 是工程全局的配置文件，static文件夹下面放置静态资源文件，templates目录下面放置视图模板文件。

#### build.gradle 配置文件

我们使用 Gradle 来构建项目。其中 build.gradle 配置文件类似 Maven中的pom.xml 配置文件。我们使用 Spring Initializr 自动生成的样板项目的默认配置如下

```groovy
buildscript {
	ext {
		kotlinVersion = '1.1.51'
		springBootVersion = '2.0.0.BUILD-SNAPSHOT'
	}
	repositories {
		mavenCentral()
		maven { url "https://repo.spring.io/snapshot" }
		maven { url "https://repo.spring.io/milestone" }
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
		classpath("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
	}
}

apply plugin: 'kotlin'
apply plugin: 'kotlin-spring'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = 'com.easy.kotlin'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8
compileKotlin {
	kotlinOptions.jvmTarget = "1.8"
}
compileTestKotlin {
	kotlinOptions.jvmTarget = "1.8"
}

repositories {
	mavenCentral()
	maven { url "https://repo.spring.io/snapshot" }
	maven { url "https://repo.spring.io/milestone" }
}


dependencies {
	compile('org.springframework.boot:spring-boot-starter-web')
	compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:${kotlinVersion}")
	compile("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
	testCompile('org.springframework.boot:spring-boot-starter-test')
}

```
其中，

spring-boot-gradle-plugin 是SpringBoot 集成 Gradle 的插件；
kotlin-gradle-plugin 是 Kotlin 集成Gradle的插件；
kotlin-allopen 是 Kotlin 集成 Spring 框架，把类全部设置为 open 的插件。因为Kotlin 的所有类及其成员默认情况下都是 final 的，也就是说你想要继承一个类，就要不断得写各种 open。而使用Java写的 Spring 框架中大量使用了继承和覆写，这个时候使用 kotlin-allopen 插件结合 kotlin-spring 插件，可以自动把 Spring 相关的所有注解的类设置为 open 。

spring-boot-starter-web 就是SpringBoot中提供的使用Spring框架进行Web应用开发的启动器。

kotlin-stdlib-jre8 是Kotlin使用Java 8 的库，kotlin-reflect 是 Kotlin 的反射库。

项目的整体依赖如下图所示


![项目的整体依赖](http://upload-images.jianshu.io/upload_images/1233356-c4ec51dda25fadaf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们可以看出，spring-boot-starter-web 中已经引入了我们所需要的 json 、tomcat 、validator 、webmvc （其中引入了Spring框架的核心web、context、aop、beans、expressions、core）等框架。


#### SpringBoot项目的入口类 KotlinWithSpringbootApplication

自动生成的 SpringBoot项目的入口类 KotlinWithSpringbootApplication如下

```kotlin
package com.easy.kotlin.kotlinwithspringboot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KotlinWithSpringbootApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinWithSpringbootApplication::class.java, *args)
}
```
其中，@SpringBootApplication注解是3个注解的组合，分别是@SpringBootConfiguration (背后使用的又是 @Configuration ),@EnableAutoConfiguration,@ComponentScan。由于这些注解一般都是一起使用，Spring Boot提供了这个@SpringBootApplication 统一的注解。这个注解的定义源码如下
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
    ...
}
```

main 函数中的 KotlinWithSpringbootApplication::class.java 是一个使用反射获取KotlinWithSpringbootApplication类的Java Class引用。这也正是我们在依赖中引入 kotlin-reflect 包的用途所在。

#### 写 Hello World 控制器

下面我们来实现一个简单的Hello World 控制器 。 首先新建 HelloWorldController Kotlin 类，代码实现如下

```kotlin
package com.easy.kotlin.kotlinwithspringboot

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class HelloWorldController {

    @RequestMapping("/")
    @ResponseBody
    fun home(): String {
        return "Hello World!"
    }

}

```


#### 启动运行

系统默认端口号是8080，我们在application.properties 中添加一行服务端口号的配置
```
server.port=8000
```
然后，直接启动入口类 KotlinWithSpringbootApplication , 可以看到启动日志
```
...o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8000 (http)
.e.k.k.KotlinWithSpringbootApplicationKt : Started KotlinWithSpringbootApplicationKt in 7.944 seconds (JVM running for 9.049)

```

也可以点击IDEA的Gradle工具栏里面的 Tasks - application - bootRun 执行


![Gradle工具栏 Tasks - application - bootRun ](http://upload-images.jianshu.io/upload_images/1233356-bfeb853ee7476bef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


启动完毕后，我们直接在浏览器中打开 http://127.0.0.1:8000/ ， 可以看到输出了 Hello World! 


![Hello World! ](http://upload-images.jianshu.io/upload_images/1233356-3f618c909a1872ef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

本节项目源码：https://github.com/EasySpringBoot/kotlin-with-springboot

## 13.2  综合实战：一个图片爬虫的Web应用实例

上面我们已经看到了使用Kotlin 集成 SpringBoot开发的基本步骤。本节我们给出一个使用MySQL数据库、 Spring Data JPA ORM框架、Freemarker模板引擎的完整Web项目的实例。

### 13.2.1 系统技术栈

本节介绍使用Kotlin 集成 SpringBoot 开发一个完整的图片爬虫Web应用，基本功能如下

- 定时抓取图片搜索API的根据关键字搜索返回的图片json信息，解析入库
- Web页面分页展示图片列表，支持收藏、删除等功能
- 列表支持根据图片分类进行模糊搜索


涉及的主要技术栈如下

- 编程语言：Kotlin
- 数据库层： MySQL、mysql-jdbc-driver 、JPA
- 企业级开发框架：Spring Boot、 Spring MVC
- 视图层模板引擎： Freemarker
- 前端框架： jQuery 、 Bootstrap 、Bootstrap-table
- 工程构建工具：Gradle


### 13.2.2  准备工作

#### 使用 Spring Initializr 创建项目

如下图配置项目基本信息和依赖

![使用 Spring Initializr 创建项目](http://upload-images.jianshu.io/upload_images/1233356-63149fe894d6cb21.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



自动生成项目源码工程，导入IDEA中，等待构建完毕，我们将得到下面的工程目录

```
picture-crawler$ tree
.
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── picture-crawler.iml
└── src
    ├── main
    │   ├── java
    │   ├── kotlin
    │   │   └── com
    │   │       └── easy
    │   │           └── kotlin
    │   │               └── picturecrawler
    │   │                   └── PictureCrawlerApplication.kt
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        ├── java
        ├── kotlin
        │   └── com
        │       └── easy
        │           └── kotlin
        │               └── picturecrawler
        │                   └── PictureCrawlerApplicationTests.kt
        └── resources

21 directories, 9 files
```

自动生成的  build.gradle 文件如下
```groovy
buildscript {
    ext {
        kotlinVersion = '1.1.51'
        springBootVersion = '2.0.0.BUILD-SNAPSHOT'
    }
    repositories {
        mavenCentral()
        maven { url "https://repo.spring.io/snapshot" }
        maven { url "https://repo.spring.io/milestone" }
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
    }
}

apply plugin: 'kotlin'
apply plugin: 'kotlin-spring'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = 'com.easy.kotlin'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8
compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    mavenCentral()
    maven { url "https://repo.spring.io/snapshot" }
    maven { url "https://repo.spring.io/milestone" }
}


dependencies {
    compile('org.springframework.boot:spring-boot-starter-freemarker')
    compile('org.springframework.boot:spring-boot-starter-data-jpa')
    compile('org.springframework.boot:spring-boot-starter-quartz')
    compile('org.springframework.boot:spring-boot-starter-web')
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:${kotlinVersion}")
    compile("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    runtime('mysql:mysql-connector-java')
    testCompile('org.springframework.boot:spring-boot-starter-test')
}

```


我们可以看到在 build.gradle 中新增了spring-boot-starter-freemarker 、 mybatis-spring-boot-starter 、 spring-boot-starter-quartz 、mysql-connector-java 等依赖。在这些starter中已经封装了这个工具链所需要的依赖库。整个项目的依赖如下图所示


![整个项目的依赖](http://upload-images.jianshu.io/upload_images/1233356-0359bb59c74ac9ec.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


目前我们的工程已经具备了连接MySQL数据库、解析Freemarker 的 .ftl 模板文件等的能力了。但是，此时如果启动会报错
```
BeanCreationException: Error creating bean with name 'dataSource' defined in class path resource [org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class]
```
创建 dataSource Bean失败。因为，我们还没有配置任何数据库连接信息。下面我们来配置数据源 dataSource 。

###  13.2.3  配置数据源

Spring Boot 的数据源配置在 application.properties 中是以 spring.datasource 为前缀。例如，新建一个 wotu 库

```
CREATE SCHEMA `wotu` DEFAULT CHARACTER SET utf8 ;
```

我们配置数据库的连接url 、用户名 、 密码信息如下

```
spring.datasource.url=jdbc:mysql://localhost:3306/wotu?zeroDateTimeBehavior=convertToNull&characterEncoding=utf8&characterSetResults=utf8&useSSL=false
spring.datasource.username=root
spring.datasource.password=root

spring.datasource.testWhileIdle=true
spring.datasource.validationQuery=SELECT 1
```

然后，再次启动应用，我们可以发现启动成功。


### 13.2.4  数据库表结构设计

下面我们从数据库层开始构建我们的应用。首先我们先设计数据库的表结构如下

```sql
CREATE TABLE `picture` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `deleted_date` datetime DEFAULT NULL,
  `gmt_created` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `is_deleted` int(11) NOT NULL,
  `url` varchar(500) NOT NULL,
  `version` int(11) NOT NULL,
  `is_favorite` int(11) NOT NULL,
  PRIMARY KEY (`id`,`url`),
  KEY `url` (`id`,`url`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
因为我们使用的是 JPA，只需要写好实体类代码，启动应用即可自动创建表结构到 MySQL 数据库中。实体类代码如下

```kotlin
package com.easy.kotlin.picturecrawler.entity

import java.util.*
import javax.persistence.*

@Entity
class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1
    @Version
    var version: Int = 0
    var category: String = ""
    var isFavorite: Int = 0
    var url: String = ""
    var gmtCreated: Date = Date()
    var gmtModified: Date = Date()
    var isDeleted: Int = 0  //1 Yes 0 No
    var deletedDate: Date = Date()

    override fun toString(): String {
        return "Image(id=$id, version=$version, category='$category', isFavorite=$isFavorite, url='$url', gmtCreated=$gmtCreated, gmtModified=$gmtModified, isDeleted=$isDeleted, deletedDate=$deletedDate)"
    }
}

```

#### ddl-auto 配置

我们再配置一下 JPA 的一些行为

```
spring.jpa.database=MYSQL
spring.jpa.show-sql=true
# Hibernate ddl auto (create, create-drop, update)
spring.jpa.hibernate.ddl-auto=update
# Naming strategy
spring.jpa.hibernate.naming-strategy=org.hibernate.cfg.ImprovedNamingStrategy
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
```
其中 spring.jpa.hibernate.ddl-auto 的值有：create、create-drop、update、validate、none，如下表分别作简单说面

|值| 说明 |
|---|---|
|create|每次加载hibernate会自动创建表，以后启动会覆盖之前的表，所以这个值基本不用，严重会导致的数据的丢失。|
|create-drop| 每次加载hibernate时根据model类生成表，但是sessionFactory一关闭，表就自动删除，下一次启动会重新创建。|
|update|加载hibernate时根据实体类model创建数据库表，这是表名的依据是@Entity注解的值或者@Table注解的值，sessionFactory关闭表不会删除，且下一次启动会根据实体model更新结构或者有新的实体类会创建新的表。|
|validate|启动时验证表的结构，不会创建表|
|none|启动时不做任何操作|

所以，在开发项目的过程中，我们通常会选用 update 选项。



再次启动应用，启动完毕后我们可以看到数据库中已经自动创建了 image 表


![image 表结构](http://upload-images.jianshu.io/upload_images/1233356-5a233ebc57298261.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 标注索引

为了更高的性能，我们建立类别 category 字段和 url 索引。其中 url 是唯一索引

```sql
ALTER TABLE `sotu`.`image`
  ADD INDEX `idx_category` (`category` ASC),
  ADD UNIQUE INDEX `uk_url` (`url` ASC);
```

而实际上，我们不需要去手工写上面的 SQL 然后再去数据库中执行。我们只需要写下面的实体类
```kotlin
package com.easy.kotlin.picturecrawler.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(indexes = arrayOf(
        Index(name = "idx_url", unique = true, columnList = "url"),
        Index(name = "idx_category", unique = false, columnList = "category")))
class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1
    @Version
    var version: Int = 0

    @Column(length = 255, unique = true, nullable = false)
    var category: String = ""
    var isFavorite: Int = 0

    @Column(length = 255, unique = true, nullable = false)
    var url: String = ""

    var gmtCreated: Date = Date()
    var gmtModified: Date = Date()
    var isDeleted: Int = 0  //1 Yes 0 No
    var deletedDate: Date = Date()

    override fun toString(): String {
        return "Image(id=$id, version=$version, category='$category', isFavorite=$isFavorite, url='$url', gmtCreated=$gmtCreated, gmtModified=$gmtModified, isDeleted=$isDeleted, deletedDate=$deletedDate)"
    }
}
```
我们在@Table 注解里指定为 url、category 建立索引， 以及设定 url 唯一性约束 unique = true

```
@Table(indexes = arrayOf(
        Index(name = "idx_url", unique = true, columnList = "url"),
        Index(name = "idx_category", unique = false, columnList = "category")))
```


启动应用的时候，JPA 会去解析我们的注解生成对应的 SQL，并且自动去执行相应的 SQL。例如字段url 的唯一索引约束，我们可以在启动日志中看到如下的输出

```
Hibernate: alter table image drop index idx_url
Hibernate: alter table image add constraint idx_url unique (url)
```

其中，Index 是@Index 注解，当做参数使用的时候不需要加@ 。



我们再举个例子。实体类代码如下
```kotlin
package com.easy.kotlin.picturecrawler.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(indexes = arrayOf(Index(name = "idx_key_word", columnList = "keyWord", unique = true)))
class SearchKeyWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1
    @Column(length = 50, unique = true, nullable = false)
    var keyWord: String = ""
    var gmtCreated: Date = Date()
    var gmtModified: Date = Date()
    var isDeleted: Int = 0  //1 Yes 0 No
    var deletedDate: Date = Date()
}

```

重启应用，我们可以看到Hibernate 日志
```
Hibernate: create table search_key_word (id bigint not null auto_increment, deleted_date datetime, gmt_created datetime, gmt_modified datetime, is_deleted integer not null, key_word varchar(50) not null, primary key (id)) engine=MyISAM
Hibernate: alter table search_key_word drop index UK_lvmjkr0dkesio7a33ejre5c26
Hibernate: alter table search_key_word add constraint UK_lvmjkr0dkesio7a33ejre5c26 unique (key_word)
```

自动生成的表结构如下



![自动生成的 search_key_word 表结构](http://upload-images.jianshu.io/upload_images/1233356-8fec2dc7923a1bd3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)




其中，@Column(length = 50, unique = true, nullable = false) 这一句指定了keyWord 字段的长度是50，有唯一约束，不可空。对应生成的数据库表字段 key_word 信息：Type 是 varchar(50) , Null 是 NO, Key 是唯一键 UNI 。



#### 主键自动生成策略
我们使用@Id 注解来标注主键字段
```
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
var id: Long = -1
```
其中的 @GeneratedValue(strategy = GenerationType.IDENTITY) 注解，我们重点介绍一下。这里的GenerationType是主键 ID 的生成规则。JPA提供的四种标准用法为 TABLE、SEQUENCE、IDENTITY、AUTO

|GenerationType | 说明 |
|---|----|
|TABLE|使用一个特定的数据库表格来保存主键。 |
|SEQUENCE|根据底层数据库的序列来生成主键，条件是数据库支持序列。 |
|IDENTITY|主键由数据库自动生成（主要是自动增长型） |
|AUTO|主键由程序控制。|







我们设计源码目录如下
```
├── src
│   ├── main
│   │   ├── java
│   │   ├── kotlin
│   │   │   └── com
│   │   │       └── easy
│   │   │           └── kotlin
│   │   │               └── picturecrawler
│   │   │                   ├── PictureCrawlerApplication.kt
│   │   │                   ├── controller
│   │   │                   ├── dao
│   │   │                   ├── entity
│   │   │                   ├── job
│   │   │                   └── service
...
```

其中，controller 放置 Controller 控制器代码；
entity 放置对应到数据库表的实体类代码；
dao 层放置数据访问层逻辑代码；
service 层放置业务逻辑实现代码；
job 层放置定时任务代码。




###  13.2.5 JSON 数据解析

我们的图片搜索 API 返回的数据结构是 JSON 格式的，内容示例如下
```json
{
  "queryEnc": "%E7%BE%8E%E5%A5%B3",
  "queryExt": "美女",
  "listNum": 3900,
  "displayNum": 415337,
  "gsm": "5a",
  "bdFmtDispNum": "约415,000",
  "bdSearchTime": "",
  "isNeedAsyncRequest": 1,
  "bdIsClustered": "1",
  "data": [
    {
      "adType": "0",
      "hasAspData": "0",
      "thumbURL": "http://img5.imgtn.bdimg.com/it/u=2817128514,340025963&fm=27&gp=0.jpg",
      "middleURL": "http://img5.imgtn.bdimg.com/it/u=2817128514,340025963&fm=27&gp=0.jpg",
      "largeTnImageUrl": "",
      "hasLarge": 0,
       ...
      "currentIndex": "",
      "width": 800,
      "height": 958,
      "type": "jpg",
      "is_gif": 0,
       ...
      "bdImgnewsDate": "1970-01-01 08:00",
      "fromPageTitle": "",
      "fromPageTitleEnc": "性感美女",
   ...
}
```

我们只需要取出其中的thumbURL 和 fromPageTitleEnc 两个字段的值。我们使用 fastjson 来解析这个 json 字符串

```kotlin
try {
    val obj = JSON.parse(jsonstr) as Map<*, *>
    val dataArray = obj.get("data") as JSONArray
    dataArray.forEach {
        val category = (it as Map<*, *>).get("fromPageTitleEnc") as String
        val url = it.get("thumbURL") as String
        if (passFilter(url)) {
            val imageResult = ImageCategoryAndUrl(category = category, url = url)
            imageResultList.add(imageResult)
        }
    }

} catch (ex: Exception) {

}

fun passFilter(imgUrl: String): Boolean {
    return imgUrl.endsWith(".jpg")
            && !imgUrl.contains("baidu.com/")
            && !imgUrl.contains("126.net")
            && !imgUrl.contains("pconline.com")
            && !imgUrl.contains("nipic.com")
            && !imgUrl.contains("zol.com")
}
```

其中的ImageCategoryAndUrl 对象是我们定义的数据转换对象
```kotlin
data class ImageCategoryAndUrl(val category: String, val url: String)
```

搜索图片的 Rest API Builder 类如下

```kotlin
object ImageSearchApiBuilder {
    fun build(word: String, page: Int): String {
        return "http://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&fp=result&word=${word}&pn=${30 * page}&rn=30"
    }
}
```

我们来写个单元测试
```kotlin
package com.easy.kotlin.picturecrawler

import com.easy.kotlin.picturecrawler.api.ImageSearchApiBuilder
import com.easy.kotlin.picturecrawler.service.JsonResultProcessor
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class JsonResultProcessorTest {
    @Test
    fun testJsonResultProcessor() {
        val list = JsonResultProcessor.getImageCategoryAndUrlList(ImageSearchApiBuilder.build("美女", 1))
        println(list)
    }
}

```

输出

```
[ImageCategoryAndUrl(category=性感美女写真集, url=http://img1.imgtn.bdimg.com/it/u=3772875022,724775083&fm=27&gp=0.jpg), ImageCategoryAndUrl(category=美女写真 性感美女_美女写真 性感美女_美女写真 性感美女, url=http://img0.imgtn.bdimg.com/it/u=3312193685,1215837845&fm=11&gp=0.jpg), ImageCategoryAndUrl(category=...

```


### 13.2.6  数据入库逻辑实现

现在我们已经有了数据的表结构，实体类代码； 同时也已经有个业务源数据了。现在我们要做的是把爬到的图片信息存储到数据库中。同时，重复的 url 信息我们不去重复存储。

新建一个实现 PagingAndSortingRepository<Image, Long> 的 ImageRepository 接口

```kotlin
interface ImageRepository : PagingAndSortingRepository<Image, Long>
```

只要上面的一行代码，我们就可以直接使用ImageRepository 的 CRUD 方法了。因为 JPA 框架会帮我们自动生成这些方法。这个PagingAndSortingRepository 是带分页功能的。它继承了CrudRepository 接口

```java
@NoRepositoryBean
public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {
	Iterable<T> findAll(Sort sort);
	Page<T> findAll(Pageable pageable);
}
```

而在接口 CrudRepository 中定义了我们能够直接使用的 CRUD 方法
```java
@NoRepositoryBean
public interface CrudRepository<T, ID> extends Repository<T, ID> {
	<S extends T> S save(S entity);
	<S extends T> Iterable<S> saveAll(Iterable<S> entities);
	Optional<T> findById(ID id);
	boolean existsById(ID id);
	Iterable<T> findAll();
	Iterable<T> findAllById(Iterable<ID> ids);
	long count();
	void deleteById(ID id);
	void delete(T entity);
	void deleteAll(Iterable<? extends T> entities);
	void deleteAll();
}
```
我们入库就直接使用save(S entity) 方法。但是为了保证重复的 url 不保存，需要写个函数来判断当前 url 是否在数据库中存在。我们直接使用 select count(*) 语句来判断即可， 当且仅当 select count(*)  出来的值等于 0 （表明数据库中不存在此 url ），才进行入库动作。在ImageRepository 接口中直接声明函数即可，代码如下

```kotlin
    @Query("select count(*) from #{#entityName} a where a.url = :url")
    fun countByUrl(@Param("url") url: String): Int
```

入库逻辑代码如下
```kotlin
if (imageRepository.countByUrl(url) == 0) {
    val Image = Image()
    Image.category = category
    Image.url = url
    imageRepository.save(Image)
}
```


### 13.2.7  定时调度任务执行

为了简单起见，我们直接使用 Spring 自带的scheduling 包下面的@Schedules 注解来实现任务的定时执行。需要注意的是，要在 SpringBoot 的启动类上面添加注解

```kotlin
@SpringBootApplication
@EnableScheduling
class PictureCrawlerApplication
```

我们的定时任务代码如下

```kotlin
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
```

其中，@Scheduled(cron = "0 */5 * * * ?") 表示每隔5分钟执行一次图片的抓取。

然后，我们重新启动应用，就会看到每隔5分钟，我们的定时任务会去跑一次。

到目前为止，我们的原始数据已经入库。下面，我们将要进行控制器层代码和视图展示层模板引擎的代码的开发。最后是前端页面展示部分的开发。


### 13.2.8  控制器层开发

下面我们实现一个分页查询接口 http://127.0.0.1:8000/sotuJson?page=10&size=3 ，返回的数据是 json 格式
```json
{
  "content": [
    {
      "id": 5981,
      "version": 0,
      "category": "南非,动物世界,非洲地区旅游景点,风景名胜",
      "url": "http://img0.imgtn.bdimg.com/it/u=2871771810,3599000038&fm=27&gp=0.jpg",
      "gmtCreated": 1508858697000,
      "gmtModified": 1508858697000,
      "deletedDate": 1508858697000
    },
   ...
    {
      "id": 5979,
      "version": 0,
      "category": "亚洲,蓝色,明亮,商务,特写,地球,环境,地球形,光,地图,材料",
      "url": "http://img3.imgtn.bdimg.com/it/u=241353052,3712599419&fm=200&gp=0.jpg",
      "gmtCreated": 1508858696000,
      "gmtModified": 1508858696000,
      "deletedDate": 1508858696000
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "offset": 30,
    "pageSize": 3,
    "pageNumber": 10,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 2004,
  "totalElements": 6011,
  "size": 3,
  "numberOfElements": 3,
  "sort": {
    "sorted": true,
    "unsorted": false
  },
  "number": 10,
  "first": false
}
```

#### 实现 findAll 函数


在 Spring Data JPA 中提供了基本的CRUD操作、分页查询、排序等。我们先来实现 ImageRepository 接口中的 findAll 函数

```kotlin
@Query("SELECT a from #{#entityName} a where a.isDeleted=0 order by a.id desc")
override fun findAll(pageable: Pageable): Page<Image>
```

#### @Query 注解与 #{#entityName}

其中 @Query 是JPA中的查询注解。JPA中可以执行两种方式的查询，一种是使用JPQL，一种是使用Native SQL。其中JPQL是基于 Entity 对象（@Entity 注解标注的对象）的查询，可以消除不同数据库SQL语句的差异；本地SQL是基于传统的SQL查询，是对JPQL查询的补充。

这里的 JPQL 我们使用#{#entityName} 代替本来实体的名称，而Spring Data JPA 会自动根据 Image 实体上对应的 @Entity(name = "Image") 或者是默认的@Entity，来自动将实体名称填入HQL 语句中。

实体类 Image 使用@Entity注解后，Spring Data JPA 的 EntityManager 会将实体类 Image 纳入管理。默认的 #{#entityName} 的值就是 Image ，如果指定其中的@Entity(name = "Image")   name 的值，那么 #{#entityName} 就是指定的值。

在 JPQL 语句中

```
SELECT a from #{#entityName} a where a.isDeleted=0 order by a.id desc
```

我们就可以像访问Kotlin 类属性一样来访问字段值。注意到，我们这里的a.isDeleted 是属性名称。

#### Pageable 参数

SpringData JPA 的 PagingAndSortingRepository接口已经提供了对分页的支持，查询的时候我们只需要传入一个 Pageable 类型的实现类。这个Pageable接口定义如下

```
package org.springframework.data.domain;
import java.util.Optional;
import org.springframework.util.Assert;

public interface Pageable {
	static Pageable unpaged() {
		return Unpaged.INSTANCE;
	}

	default boolean isPaged() {
		return true;
	}

	default boolean isUnpaged() {
		return !isPaged();
	}

	int getPageNumber();

	int getPageSize();

	long getOffset();

	Sort getSort();

	default Sort getSortOr(Sort sort) {
		Assert.notNull(sort, "Fallback Sort must not be null!");
		return getSort().isSorted() ? getSort() : sort;
	}

	Pageable next();

	Pageable previousOrFirst();

	Pageable first();

	boolean hasPrevious();

	default Optional<Pageable> toOptional() {
		return isUnpaged() ? Optional.empty() : Optional.of(this);
	}
}
```

springData包中的 PageRequest类已经实现了Pageable接口，我们可以像下面这样直接使用
```kotlin
val sort = Sort(Sort.Direction.DESC, "id")
val pageable = PageRequest.of(page, size, sort)
```
其中，Direction 是 Sort 类中定义的注解
```
public static enum Direction {
		ASC, DESC;
}
```
Sort 类的构造函数签名是
```
public Sort(Direction direction, String... properties) {
	this(direction, properties == null ? new ArrayList<>() : Arrays.asList(properties));
}
```
我们这里Sort(Sort.Direction.DESC, "id")传入的是根据 id 进行降序排序。


#### Page<T> 返回类型

findAll 函数的返回类型是 Page<Image> ， 这里的 Page 类型是 Spring Data JPA 的分页结果的返回对象，Page 继承了 Slice 。这两个接口的定义如下
```java
public interface Page<T> extends Slice<T> {
	static <T> Page<T> empty() {
		return empty(Pageable.unpaged());
	}
	static <T> Page<T> empty(Pageable pageable) {
		return new PageImpl<>(Collections.emptyList(), pageable, 0);
	}
	int getTotalPages();
	long getTotalElements();
	<U> Page<U> map(Function<? super T, ? extends U> converter);
}

public interface Slice<T> extends Streamable<T> {
	int getNumber();
	int getSize();
	int getNumberOfElements();
	List<T> getContent();
	boolean hasContent();
	Sort getSort();
	boolean isFirst();
	boolean isLast();
	boolean hasNext();
	boolean hasPrevious();
	default Pageable getPageable() {
		return PageRequest.of(getNumber(), getSize(), getSort());
	}
	Pageable nextPageable();
	Pageable previousPageable();
	<U> Slice<U> map(Function<? super T, ? extends U> converter);
}
```

这个分页对象的数据结构信息足够我们在前端实现分页的交互页面时使用。

　　　　
我们来实现分页查询所有 image 表记录的REST API 接口。在 controller 包路径下面新建 ImageController 类， 类上使用 @Controller注解。
```kotlin
package com.easy.kotlin.picturecrawler.controller

import com.easy.kotlin.picturecrawler.dao.ImageRepository
import com.easy.kotlin.picturecrawler.entity.Image
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

@Controller
class ImageController {
    @Autowired
    lateinit var imageRepository: ImageRepository

    @RequestMapping(value = "sotuJson", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun sotuJson(@RequestParam(value = "page", defaultValue = "0") page: Int,
                 @RequestParam(value = "size", defaultValue = "10") size: Int): Page<Image> {
        return getPageResult(page, size)
    }
    private fun getPageResult(page: Int, size: Int): Page<Image> {
        val sort = Sort(Sort.Direction.DESC, "id")
        val pageable = PageRequest.of(page, size, sort)
        return imageRepository.findAll(pageable)
    }
    ...
}
```

其中
```
@Autowired
lateinit var imageRepository: ImageRepository
```

这里使用 lateinit 关键字来修饰我们需要装配的 Bean ，表示 imageRepository 延迟初始化。

 从上面的代码可以看出，Kotlin 使用Spring MVC非常自然，跟使用原生 Java 代码几乎一样顺畅。有个稍微明显的区别是 method = arrayOf(RequestMethod.GET) ， 这里Kotlin 数组声明的语法是使用 arrayOf() , 而这个在 Java 中只需要使用花括号 { } 括起来即可。

重新运行应用，浏览器访问 http://127.0.0.1:8000/sotuJson?page=10&size=3 ，我们将看到分页对象 Page 的 JSON 字符串格式的输出结果。

#### 模糊搜索分页接口实现
下面我们来实现根据 category 字段的值进行模糊搜索的接口，并同时支持分页。代码如下

```kotlin
@Query("SELECT a from #{#entityName} a where a.isDeleted=0 and a.category like %:searchText% order by a.id desc")
fun search(@Param("searchText") searchText: String, pageable: Pageable): Page<Image>
```

其中 @Param("searchText") searchText: String 是搜索关键字参数 @Param 注解指定了JPQL 中的参数名 searchText  ，对应到 JPQL 中的参数占位符写作 :searchText ，我们注意到这里的模糊查询的语法是
```
like %:searchText%
```
对应的 Controller 中的方法是
```kotlin
@RequestMapping(value = "sotuSearchJson", method = arrayOf(RequestMethod.GET))
@ResponseBody
fun sotuSearchJson(@RequestParam(value = "page", defaultValue = "0") page: Int, @RequestParam(value = "size", defaultValue = "10") size: Int, @RequestParam(value = "searchText", defaultValue = "") searchText: String): Page<Image> {
    return getPageResult(page, size, searchText)
}

private fun getPageResult(page: Int, size: Int, searchText: String): Page<Image> {
    val sort = Sort(Sort.Direction.DESC, "id")
    val pageable = PageRequest.of(page, size, sort)
    if (searchText == "") {
        return imageRepository.findAll(pageable)
    } else {
        return imageRepository.search(searchText, pageable)
    }
}
```
这里需要注意的是 PageRequest.of(page,size,sort) page 取值默认是从0开始 。

重新运行应用，浏览器访问 `http://127.0.0.1:8000/sotuSearchJson?page=10&size=3&searchText=秋天`  ，我们可以看到输出
```json
{
  "content": [
    {
      "id": 17443,
      "version": 0,
      "category": "初秋岱庙",
      "url": "http://img0.imgtn.bdimg.com/it/u=64076324,3274882882&fm=27&gp=0.jpg",
      "gmtCreated": 1508924545000,
      "gmtModified": 1508924545000,
      "deletedDate": 1508924545000
    },
    {
      "id": 17280,
      "version": 0,
      "category": "初秋落叶信纸.doc",
      "url": "http://img5.imgtn.bdimg.com/it/u=256290403,1153099708&fm=27&gp=0.jpg",
      "gmtCreated": 1508924528000,
      "gmtModified": 1508924528000,
      "deletedDate": 1508924528000
    },
    {
      "id": 17130,
      "version": 0,
      "category": "初秋的小花图片 12张  (天堂图片网)",
      "url": "http://img3.imgtn.bdimg.com/it/u=1333940222,533390017&fm=11&gp=0.jpg",
      "gmtCreated": 1508924510000,
      "gmtModified": 1508924510000,
      "deletedDate": 1508924510000
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "offset": 30,
    "pageSize": 3,
    "pageNumber": 10,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalElements": 148,
  "totalPages": 50,
  "size": 3,
  "number": 10,
  "numberOfElements": 3,
  "sort": {
    "sorted": true,
    "unsorted": false
  },
  "first": false
}
```

### 13.2.9  展示层模板引擎代码

后端的数据接口我们已经开发完毕，下面我们来把这些数据展示到前端页面上。

我们使用的视图层模板引擎是 Freemarker ， 在 SpringBoot 中使用Freemarker，只需要加入 spring-boot-starter-freemarker 。其中，使用默认的配置目录 src/main/resources/templates , 模板文件以 .ftl 为后缀。

我们将前端依赖的外部库静态资源文件全部放到 src/main/resources/static/bower_components 文件夹下 。我们主要使用的是jquery.js 、bootstrap.js ，另外使用后端的分页接口实现前端分页的功能我们使用 bootstrap-table.js 库来实现。前端模板文件以及 js 源码文件的目录结构如下图所示


![前端模板文件以及 js 源码文件的目录结构](http://upload-images.jianshu.io/upload_images/1233356-531337ca55dd7e74.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### head.ftl

head.ftl 文件是公共文件头部分，代码如下
```html
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv=content-type content=text/html;charset=utf-8>
    <meta http-equiv=X-UA-Compatible content=IE=Edge>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>搜图</title>
    <link href="bower_components/bootstrap/dist/css/bootstrap-theme.css" rel="stylesheet">
    <link href="bower_components/bootstrap-table/src/bootstrap-table.css" rel="stylesheet">
    <link href="bower_components/bootstrap/dist/css/bootstrap.css" rel="stylesheet">
    <link href="bower_components/pnotify/src/pnotify.css" rel="stylesheet">
    <link href="app.css" rel="stylesheet">
</head>
<body>
```

#### foot.ftl
foot.ftl 是页面公共底部。代码如下
```html
<script src="bower_components/jquery/dist/jquery.js"></script>
<script src="bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="bower_components/bootstrap-table/src/bootstrap-table.js"></script>
<script src="bower_components/bootstrap-table/src/locale/bootstrap-table-zh-CN.js"></script>
<script src="bower_components/pnotify/src/pnotify.js"></script>
<script src="app.js"></script>
</body>
</html>
```

#### nav.ftl
nav.ftl 是导航栏部分的代码，使用标准的 Bootstrap 样式库来实现

```html
<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">搜图</a>
        </div>
        <div>
            <ul class="nav navbar-nav">

                <li class='<#if requestURI=="/sotu_view">active</#if>'><a href="sotu_view">美图列表</a></li>
                <li class='<#if requestURI=="/sotu_favorite_view">active</#if>'><a href="sotu_favorite_view">精选收藏</a>
                <li class='<#if requestURI=="/search_keyword_view">active</#if>'><a href="search_keyword_view">搜索关键字</a>
                </li>

                <li class=""><a href="doCrawJob" target="_blank">执行抓取</a></li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        Kotlin <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="http://www.jianshu.com/nb/12976878" target="_blank">Kotlin 极简教程</a></li>
                        <li><a href="http://www.jianshu.com/nb/17117730" target="_blank">Kotlin 项目实战开发</a></li>
                        <li><a href="#">SpringBoot</a></li>
                        <li><a href="#">Java</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Scala</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Groovy</a></li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">关于</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
```

其中这地方的代码是实现 Tab 切换的时候，active 状态跟随的交互

```
                <li class='<#if requestURI=="/sotu_view">active</#if>'><a href="sotu_view">美图列表</a></li>
                <li class='<#if requestURI=="/sotu_favorite_view">active</#if>'><a href="sotu_favorite_view">精选收藏</a>
                <li class='<#if requestURI=="/search_keyword_view">active</#if>'><a href="search_keyword_view">搜索关键字</a>
                </li>

```
requestURI 是后端的 Controller 获取当前请求传给前端页面的
```kotlin
@RequestMapping(value = *arrayOf("/", "sotu_view"), method = arrayOf(RequestMethod.GET))
fun sotuView(model: Model, request: HttpServletRequest): ModelAndView {
    model.addAttribute("requestURI", request.requestURI)
    return ModelAndView("sotu_view")
}
```


#### 图片列表页面

新建 sotu_view.ftl 为图片列表页面
```html
<#include 'common/head.ftl'>
<#include 'common/nav.ftl'>
<table id="sotu_table"></table>
<#include 'common/foot.ftl'>
<script src="sotu_table.js"></script>

```

 对应的 ModelAndView 控制器代码是
```kotlin
@RequestMapping(value = *arrayOf("/", "sotu_view"), method = arrayOf(RequestMethod.GET))
fun sotuView(model: Model, request: HttpServletRequest): ModelAndView {
    model.addAttribute("requestURI", request.requestURI)
    return ModelAndView("sotu_view")
}
```

#### 表格后端分页实现

新建 sotu_table.js ， 我们在这里写表格后端分页实现的前端 js 代码。主要是使用 bootstrap-table.js 中的 bootstrapTable 函数来完成

```js
$.fn.bootstrapTable = function (option)
```

sotu_table.js 的代码如下
```js
$(function () {
    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['zh-CN'])

    var searchText = $('.search').find('input').val()

    var columns = []
    columns.push({
        title: '分类',
        field: 'category',
        align: 'center',
        valign: 'middle',
        width: '5%',
        formatter: function (value, row, index) {
            return value
        }
    }, {
        title: '美图',
        field: 'url',
        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index) {
            return "![](" + value + ")"
        }
    }, {
        title: ' 操作',
        field: 'id',
        align: 'center',
        width: '5%',
        formatter: function (value, row, index) {
            var html = ""
            html += "<div onclick='addFavorite(" + value + ")' name='addFavorite' id='addFavorite" + value + "' class='btn btn-default'>收藏</div><p>"
            html += "<div onclick='deleteById(" + value + ")' name='delete' id='delete" + value + "' class='btn btn-default'>删除</div>"
            return html
        }
    })

    $('#sotu_table').bootstrapTable({
        url: 'sotuSearchJson',
        sidePagination: "server",
        queryParamsType: 'page,size',
        contentType: "application/x-www-form-urlencoded",
        method: 'get',
        striped: false,     //是否显示行间隔色
        buttonsAlign: 'right',
        smartDisplay: true,
        cache: false,      //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,  //是否显示分页（*）
        paginationLoop: true,
        paginationHAlign: 'right', //right, left
        paginationVAlign: 'bottom', //bottom, top, both
        paginationDetailHAlign: 'left', //right, left
        paginationPreText: ' 上一页',
        paginationNextText: '下一页',
        search: true,
        searchText: searchText,
        searchTimeOut: 500,
        searchAlign: 'right',
        searchOnEnterKey: false,
        trimOnSearch: true,
        sortable: true,    //是否启用排序
        sortOrder: "desc",   //排序方式
        sortName: "id",
        pageNumber: 1,     //初始化加载第一页，默认第一页
        pageSize: 10,      //每页的记录行数（*）
        pageList: [8, 16, 32, 64, 128], // 可选的每页数据
        totalField: 'totalElements', // 所有记录 count
        dataField: 'content', //后端 json 对应的表格List数据的 key
        columns: columns,
        queryParams: function (params) {
            return {
                size: params.pageSize,
                page: params.pageNumber - 1,
                sortName: params.sortName,
                sortOrder: params.sortOrder,
                searchText: params.searchText
            }
        },
        classes: 'table table-responsive full-width',
    })


    $(document).on('keydown', function (event) {
        // 键盘翻页事件
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if (e && e.keyCode == 38 || e && e.keyCode == 37) {//上,左
            // 上一页
            $('.page-pre').click()
        }
        if (e && e.keyCode == 40 || e && e.keyCode == 39) {//下,右
            // 下一页
            $('.page-next').click()
        }

    })


    var keyWord = getKeyWord()
    $('.search').find('input').val(keyWord)

})

function getKeyWord() {
    var url = decodeURI(location.href)
    var indexOfKeyWord = url.indexOf('?keyWord=')
    if (indexOfKeyWord != -1) {
        var start = indexOfKeyWord + '?keyWord='.length
        return url.substring(start)
    } else {
        return ""
    }
}
```

其中 url: 'sotuSearchJson' 后端的查询接口实现代码是
```kotlin
@RequestMapping(value = "sotuSearchJson", method = arrayOf(RequestMethod.GET))
@ResponseBody
fun sotuSearchJson(@RequestParam(value = "page", defaultValue = "0") page: Int, @RequestParam(value = "size", defaultValue = "10") size: Int, @RequestParam(value = "searchText", defaultValue = "") searchText: String): Page<Image> {
    return getPageResult(page, size, searchText)
}

private fun getPageResult(page: Int, size: Int, searchText: String): Page<Image> {
    val sort = Sort(Sort.Direction.DESC, "id")
    // 注意：PageRequest.of(page,size,sort) page 默认是从0开始
    val pageable = PageRequest.of(page, size, sort)
    if (searchText == "") {
        return imageRepository.findAll(pageable)
    } else {
        return imageRepository.search(searchText, pageable)
    }
}
```

其中，
dataField: 'content' 对应的是 Page<Image> 中的 content 属性的值；

 totalField: 'totalElements' 对应的是 Page<Image>  中 totalElements属性的值。

columns: columns 是对应到 content List 中的每个元素的对象属性。例如
```js
var columns = []
    columns.push({
        title: '分类',
        field: 'category',
        align: 'center',
        valign: 'middle',
        width: '5%',
        formatter: function (value, row, index) {
            return value
        },
        ...
}
```
里面的field: 'category' 对应的就是Image 实体类的category 属性名称。然后，我们在formatter: function (value, row, index)  函数中处理改单元格显示的样式 html 。

重新启动运行应用，我们将看到分页以及模糊搜索的效果



![模糊搜索的效果](http://upload-images.jianshu.io/upload_images/1233356-b6673a8c2306d786.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![分页的效果](http://upload-images.jianshu.io/upload_images/1233356-97547ab83ecb981e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)















提示：Bootstrap-table完整的配置项在 bootstrap-table.js 源码 (https://github.com/wenzhixin/bootstrap-table）中的BootstrapTable.DEFAULTS 这行代码中

```js
BootstrapTable.DEFAULTS = {
        classes: 'table table-hover',
        sortClass: undefined,
        locale: undefined,
        height: undefined,
        undefinedText: '-',
        sortName: undefined,
        sortOrder: 'asc',
        sortStable: false,
        rememberOrder: false,
        striped: false,
        columns: [[]],
        data: [],
        totalField: 'total',
        dataField: 'rows',
        method: 'get',
        url: undefined,
        ajax: undefined,
        cache: true,
        contentType: 'application/json',
        dataType: 'json',
        ajaxOptions: {},
        queryParams: function (params) {
            return params;
        },
        queryParamsType: 'limit', // undefined
        responseHandler: function (res) {
            return res;
        },
        pagination: false,
        onlyInfoPagination: false,
        paginationLoop: true,
        sidePagination: 'client', // client or server
        totalRows: 0, // server side need to set
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 25, 50, 100],
        paginationHAlign: 'right', //right, left
        paginationVAlign: 'bottom', //bottom, top, both
        paginationDetailHAlign: 'left', //right, left
        paginationPreText: '‹',
        paginationNextText: '›',
        search: false,
        searchOnEnterKey: false,
        strictSearch: false,
        searchAlign: 'right',
        selectItemName: 'btSelectItem',
        showHeader: true,
        ...
}
```

#### 收藏、删除功能


下面我们来实现收藏图片和删除图片功能。后端接口实现逻辑如下
```kotlin
@Modifying
@Transactional
@Query("update #{#entityName} a set a.isFavorite=1,a.gmtModified=now() where a.id=:id")
fun addFavorite(@Param("id") id: Long)

@Modifying
@Transactional
@Query("update #{#entityName} a set a.isDeleted=1 where a.id=:id")
fun delete(@Param("id") id: Long)

```
我们用 isFavorite=1来表示该图片是被收藏的，isDeleted=1 表示该图片被删除。需要注意的是 JPA 中 update、delete 操作需要在对应的函数上面添加@Modifying 和 @Transactional 注解。

控制层的 http 接口代码如下
```kotlin
@RequestMapping(value = "addFavorite", method = arrayOf(RequestMethod.POST))
@ResponseBody
fun addFavorite(@RequestParam(value = "id") id: Long): Boolean {
    imageRepository.addFavorite(id)
    return true
}

@RequestMapping(value = "delete", method = arrayOf(RequestMethod.POST))
@ResponseBody
fun delete(@RequestParam(value = "id") id: Long): Boolean {
    imageRepository.delete(id)
    return true
}
```
 前端 js 代码如下
```js
function addFavorite(id) {
    $.ajax({
        url: 'addFavorite',
        data: {id: id},
        dataType: 'json',
        type: 'post',
        success: function (resp) {
            // alert(JSON.stringify(resp))
            new PNotify({
                title: '收藏操作',
                styling: 'bootstrap3',
                text: JSON.stringify(resp),
                type: 'success',
                delay: 500,
            });
        },
        error: function (msg) {
            // alert(JSON.stringify(msg))
            new PNotify({
                title: '收藏操作',
                styling: 'bootstrap3',
                text: JSON.stringify(msg),
                type: 'error',
                delay: 500,
            });
        }
    })
}

function deleteById(id) {
    $.ajax({
        url: 'delete',
        data: {id: id},
        dataType: 'json',
        type: 'post',
        success: function (resp) {
            // alert(JSON.stringify(resp))
            $('#sotu_favorite_table').bootstrapTable('refresh')
            $('#sotu_table').bootstrapTable('refresh')
            new PNotify({
                title: '删除操作',
                styling: 'bootstrap3',
                text: JSON.stringify(resp),
                type: 'info',
                delay: 500,
            });

        },
        error: function (msg) {
            // alert(JSON.stringify(msg))
            new PNotify({
                title: '删除操作',
                styling: 'bootstrap3',
                text: JSON.stringify(msg),
                type: 'error',
                delay: 500,
            });
        }
    })
}
```
对应的表格中的前端按钮组件代码在 sotu_table.js 中，关键片段如下
```js
{
        title: ' 操作',
        field: 'id',
        align: 'center',
        width: '5%',
        formatter: function (value, row, index) {
            var html = ""
            html += "<div onclick='addFavorite(" + value + ")' name='addFavorite' id='addFavorite" + value + "' class='btn btn-default'>收藏</div><p>"
            html += "<div onclick='deleteById(" + value + ")' name='delete' id='delete" + value + "' class='btn btn-default'>删除</div>"
            return html
        }
    }
```

#### 点击图片下载功能

在 sotu_table.js 中，我们实现点击图片自动触发下载图片到本地的功能。代码如下
```
{
        title: '美图',
        field: 'url',
        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index) {
            return "![](" + value + ")"
        }
    }
```
其中，downloadImage 函数实现如下
```js
function downloadImage(src) {
    var $a = $("<a></a>").attr("href", src).attr("download", "sotu.png");
    $a[0].click();
}
```

### 13.2.10 搜索关键字管理

本节我们开发爬虫爬取的关键字管理的功能。


#### 数据库实体类

首先，新建实体类SearchKeyWord 如下
```kotlin
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
    @Column(nullable = true)
    var totalImage: Int? = 0
    var gmtCreated: Date = Date()
    var gmtModified: Date = Date()
    var isDeleted: Int = 0  //1 Yes 0 No
    var deletedDate: Date = Date()
}
```
其中，keyWord 是搜索关键字，有唯一性约束，同时我们给它建立了索引。

#### dao 层接口

我们来实现插入数据的 dao 层接口
```kotlin
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO `search_key_word` (`deleted_date`, `gmt_created`, `gmt_modified`, `is_deleted`, `key_word`) VALUES (now(), now(), now(), '0', :keyWord) ON DUPLICATE KEY UPDATE `gmt_modified` = now()", nativeQuery = true)
    fun saveOnNoDuplicateKey(@Param("keyWord") keyWord: String): Int
```
其中，ON DUPLICATE KEY UPDATE 这句表明当遇到重复的键值的时候，执行更新 gmt_modified = now() 的操作。这里nativeQuery = true ，表示使用的是原生 SQL 查询。


####  系统启动初始化动作

我们在应用启动类PictureCrawlerApplication 中添加初始化动作

```kotlin
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

```

Spring Boot应用程序在启动后会去遍历 CommandLineRunner 接口的实例并运行它们的run方法。使用@Order注解来指定 CommandLineRunner 实例的运行顺序。

#### 搜索查询接口

查询所有关键字记录接口如下
```kotlin
@Query("SELECT a from #{#entityName} a where a.isDeleted=0 order by a.id desc")
override fun findAll(pageable: Pageable): Page<SearchKeyWord>
```
模糊搜索关键字接口如下
```kotlin
@Query("SELECT a from #{#entityName} a where a.isDeleted=0 and a.keyWord like %:searchText% order by a.id desc")
fun search(@Param("searchText") searchText: String, pageable: Pageable): Page<SearchKeyWord>
```

#### 模糊搜索 http 接口实现

跟搜索图片分类的逻辑类似，模糊搜索关键字的接口如下
```
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
```

#### 前端列表页面代码

search_keyword_view.ftl 模板页面代码如下
```html
<#include 'common/head.ftl'>
<#include 'common/nav.ftl'>

<form id="add_key_word_form">
    <div class="col-lg-3">
        <div class="input-group">
            <input name="keyWord"
                   id="add_key_word_form_keyWord"
                   type="text"
                   class="form-control"
                   placeholder="输入爬虫抓取关键字">
            <span class="input-group-btn">
						<button id="add_key_word_form_save_button"
                                class="btn btn-default"
                                type="button">
							 保存
						</button>
			</span>
        </div><!-- /input-group -->
    </div><!-- /.col-lg-3 -->
</form>
<table id="search_keyword_table"></table>
<#include 'common/foot.ftl'>
<script src="search_keyword_table.js"></script>

```
search_keyword_table.js 代码如下
```js
$(function () {
    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['zh-CN'])
    var searchText = $('.search').find('input').val()

    var columns = []

    columns.push(
        {
            title: 'ID',
            field: 'id',
            align: 'center',
            valign: 'middle',
            width: '10%',
            formatter: function (value, row, index) {
                return value
            }
        },
        {
            title: '关键字',
            field: 'keyWord',
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                var html = "<a href='sotu_view?keyWord=" + value + "' target='_blank'>" + value + "</a>"
                return html
            }
        },
        {
            title: '图片总数',
            field: 'totalImage',
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                var html = "<a href='sotu_view?keyWord=" + row.keyWord + "' target='_blank'>" + row.totalImage + "</a>"
                return html
            }
        })

    $('#search_keyword_table').bootstrapTable({
        url: 'searchKeyWordJson',
        sidePagination: "server",
        queryParamsType: 'page,size',
        contentType: "application/x-www-form-urlencoded",
        method: 'get',
        striped: false,     //是否显示行间隔色
        cache: false,      //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,  //是否显示分页（*）
        paginationLoop: true,
        paginationHAlign: 'right', //right, left
        paginationVAlign: 'bottom', //bottom, top, both
        paginationDetailHAlign: 'left', //right, left
        paginationPreText: ' 上一页',
        paginationNextText: '下一页',
        search: true,
        searchText: searchText,
        searchTimeOut: 500,
        searchAlign: 'right',
        searchOnEnterKey: false,
        trimOnSearch: true,
        sortable: true,    //是否启用排序
        sortOrder: "desc",   //排序方式
        sortName: "id",
        pageNumber: 1,     //初始化加载第一页，默认第一页
        pageSize: 10,      //每页的记录行数（*）
        pageList: [8, 16, 32, 64, 128], // 可选的每页数据
        totalField: 'totalElements', // 所有记录 count
        dataField: 'content', //后端 json 对应的表格List数据的 key
        columns: columns,
        queryParams: function (params) {
            return {
                size: params.pageSize,
                page: params.pageNumber - 1,
                sortName: params.sortName,
                sortOrder: params.sortOrder,
                searchText: params.searchText
            }
        },
        classes: 'table table-responsive full-width',
    })


    $(document).on('keydown', function (event) {
        // 键盘翻页事件
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if (e && e.keyCode == 38 || e && e.keyCode == 37) {//上,左
            // 上一页
            $('.page-pre').click()
        }
        if (e && e.keyCode == 40 || e && e.keyCode == 39) {//下,右
            // 下一页
            $('.page-next').click()
        }

    })

    $('#add_key_word_form_save_button').on('click', function () {
        var keyWord = $('#add_key_word_form_keyWord').val()
        $.ajax({
            url: 'save_keyword',
            type: 'get',
            data: {keyWord: keyWord},
            success: function (response) {
                if (response == "1") {
                    alert("保存成功")
                } else {
                    alert("保存失败")
                }

            },
            error: function (error) {
                alert(JSON.stringify(error))
            }
        })
    })

})

```

#### 添加爬取关键字

添加爬取关键字 http 接口代码如下

``` kotlin
@RequestMapping(value = "save_keyword", method = arrayOf(RequestMethod.GET,RequestMethod.POST))
@ResponseBody
fun save(@RequestParam(value = "keyWord")keyWord:String): String {
    if(keyWord==""){
        return "0"
    }else{
        searchKeyWordRepository.saveOnNoDuplicateKey(keyWord)
        return "1"
    }
}
```

前端输入框表单代码

```html
<form id="add_key_word_form">
    <div class="col-lg-3">
        <div class="input-group">
            <input name="keyWord"
                   id="add_key_word_form_keyWord"
                   type="text"
                   class="form-control"
                   placeholder="输入爬虫抓取关键字">
            <span class="input-group-btn">
						<button id="add_key_word_form_save_button"
                                class="btn btn-default"
                                type="button">
							 保存
						</button>
			</span>
        </div><!-- /input-group -->
    </div><!-- /.col-lg-3 -->
</form>
```


对应的 js 代码如下
```js
$('#add_key_word_form_save_button').on('click', function () {
    var keyWord = $('#add_key_word_form_keyWord').val()
    $.ajax({
        url: 'save_keyword',
        type: 'get',
        data: {keyWord: keyWord},
        success: function (response) {
            if (response == "1") {
                alert("保存成功")
                $('#search_keyword_table').bootstrapTable('refresh')
            } else {
                alert("数据不能为空")
            }

        },
        error: function (error) {
            alert(JSON.stringify(error))
        }
    })
})
```

其中， $('#search_keyword_table').bootstrapTable('refresh') 是当保存成功后，刷新表格内容。

#### 定时更新该关键字的图片总数任务


最终的效果如下


![爬取关键字管理页面](http://upload-images.jianshu.io/upload_images/1233356-26622fda49b7cb45.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



![模糊搜索“秋”](http://upload-images.jianshu.io/upload_images/1233356-e0bc514f04a6ba46.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


更新 search_key_word 表 total_image 字段的 SQL 逻辑如下

```kotlin
@Modifying
@Transactional
@Query("update search_key_word a set a.total_image = (select count(*) from image i where i.is_deleted=0 and i.category like concat('%',a.key_word,'%'))", nativeQuery = true)
fun batchUpdateTotalImage()
```

表示该对应关键字包含的图片总数。

然后，我们用一个定时任务去执行它
```kotlin
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
        println("开始执行定时任务 batchUpdateTotalImage： ${Date()}")
        searchKeyWordRepository.batchUpdateTotalImage()
    }
}
```

### 13.2.11  使用协程实现异步爬虫任务

上面我们的定时任务都是同步的。当我们想用 http 接口去触发任务执行的时候，可能并不想一直等待，这个时候可以使用异步的方式。这里我们使用 Kotlin 提供的轻量级线程——协程来实现。在常用的并发模型中，多进程、多线程、分布式是最普遍的，不过近些年来逐渐有一些语言以first-class或者library的形式提供对基于协程的并发模型的支持。其中比较典型的有Scheme、Lua、Python、Perl、Go等以first-class的方式提供对协程的支持。同样地，Kotlin也支持协程。（关于协程的更多介绍，可参考《Kotlin 极简教程》第9章 轻量级线程：协程 ）

我们在 build.gradle 中添加kotlinx-coroutines-core 依赖

```
compile group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-core', version: '0.19.2'
```

然后把我们的定时任务代码改写为

```kotlin

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
```

同样的爬虫抓取图片的任务也可以改写成

```kotlin
fun doCrawJob() = runBlocking {
    val list = searchKeyWordRepository.findAll()
    for (i in 1..1000) {
        list.forEach {
            launch(CommonPool) {
                saveImage(it.keyWord, i)
            }
        }
    }
}
```

其中，launch函数会以非阻塞（non-blocking）当前线程的方式，启动一个新的协程后台任务，并返回一个Job类型的对象作为当前协程的引用。我们把真正要执行的代码逻辑放到 launch(CommonPool) { } 中。这样我们就可以手动启动任务异步执行了。


本节完整的项目源码：https://github.com/EasySpringBoot/picture-crawler


##  本章小结

在Spring Framework 5.0中已经添加了对 Kotlin 的支持。使用 Kotlin 集成 SpringBoot 开发非常流畅自然，几乎不需要任何迁移成本。所以，Kotlin 在未来的 Java 服务端领域也必将受到越来越多的程序员的关注。

