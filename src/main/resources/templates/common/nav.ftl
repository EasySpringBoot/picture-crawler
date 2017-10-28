<nav class="navbar navbar-inverse" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">搜图</a>
        </div>
        <div>
            <ul class="nav navbar-nav">

                <li class='<#if requestURI=="/sotu_view">active</#if>'><a href="sotu_view">图片列表</a></li>
                <li class='<#if requestURI=="/sotu_gank_view">active</#if>'><a href="sotu_gank_view">干货福利</a></li>
                <li class='<#if requestURI=="/sotu_favorite_view">active</#if>'><a href="sotu_favorite_view">精选收藏</a>
                <li class='<#if requestURI=="/search_keyword_view">active</#if>'><a href="search_keyword_view">搜索关键字</a>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        系统定时任务 <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="doBaiduImageCrawJob" target="_blank">抓取百度图片</a></li>
                        <li><a href="doGankImageCrawJob" target="_blank">抓取干货福利图</a></li>
                        <li><a href="doBatchUpdateJob" target="_blank">更新图片总数</a></li>
                    </ul>
                </li>


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


