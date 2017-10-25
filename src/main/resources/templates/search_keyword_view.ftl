<#include 'common/head.ftl'>
<#include 'common/nav.ftl'>

<form id="add_key_word_form">
    <input name="keyWord" id="add_key_word_form_keyWord" placeholder="输入爬虫抓取关键字">
    <button type="submit" id="add_key_word_form_save_button">保存</button>
</form>

<table id="search_keyword_table"></table>
<#include 'common/foot.ftl'>
<script src="search_keyword_table.js"></script>
