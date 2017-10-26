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
