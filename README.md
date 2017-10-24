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

上面我们已经看到了使用Kotlin 集成 SpringBoot开发的基本步骤。本节我们给出一个使用MySQL数据库、 Mybatis ORM框架、Freemarker模板引擎的完整Web项目的实例。

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

再次启动应用，启动完毕后我们可以看到数据库中已经自动创建了 image 表 


![image 表结构](http://upload-images.jianshu.io/upload_images/1233356-5a233ebc57298261.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


为了更高的性能，我们建立类别 category 字段和 url 索引。其中 url 是唯一索引

```sql
ALTER TABLE `sotu`.`image`
  ADD INDEX `idx_category` (`category` ASC),
  ADD UNIQUE INDEX `uk_url` (`url` ASC);
```



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



### 13.2.9  展示层模板引擎代码



### 13.2.10  前端交互页面开发



### 13.2.11  前后端分页实现



### 13.2.12  收藏、删除功能



### 13.2.13  运行效果测试



## 13.3 SpringBoot 日志配置



## 13.4 SpringBoot 集成 JPA 开发









##  本章小结

















