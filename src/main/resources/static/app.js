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


function downloadImage(src) {
    var $a = $("<a></a>").attr("href", src).attr("download", "sotu.png");
    $a[0].click();
}
