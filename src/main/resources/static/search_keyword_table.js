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

})
