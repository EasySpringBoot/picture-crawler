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


function downBase64Image(url) {
    var blob = base64Img2Blob(url);
    url = window.URL.createObjectURL(blob);
    var $a = $("<a></a>").attr("href", url).attr("download", "sotu.png");
    $a[0].click();
}


function base64Img2Blob(code) {
    var parts = code.split(';base64,');
    var contentType = parts[0].split(':')[1];
    var raw = window.atob(parts[1]);
    var rawLength = raw.length;
    var uInt8Array = new Uint8Array(rawLength);
    for (var i = 0; i < rawLength; ++i) {
        uInt8Array[i] = raw.charCodeAt(i);
    }
    return new Blob([uInt8Array], {type: contentType});
}

function downloadFile(fileName, content) {
    var aLink = document.createElement('a');
    var blob = base64Img2Blob(content); //new Blob([content]);
    var evt = document.createEvent("HTMLEvents");
    evt.initEvent("click", false, false);//initEvent 不加后两个参数在FF下会报错
    aLink.download = fileName;
    aLink.href = URL.createObjectURL(blob);
    aLink.dispatchEvent(evt);
}

function slideShow(index) {
    var urlArray = []
    var size = $('img').length
    for (var i = 0; i < size; i++) {
        urlArray.push($($('img')[i]).attr('data-original'))
    }

    var nextLoop = setInterval(function () {
        $('#picNext').click()
    }, 5000)

    fnSlideShow(urlArray, index, nextLoop);
}

$(function () {
    $(document).on('keydown', function (event) {
        // 键盘翻页事件
        var e = event || window.event || arguments.callee.caller.arguments[0];

        if (e && e.keyCode == 38) {//上
            // 上一张图片
            $('#picPrev').click()
        }

        if (e && e.keyCode == 40) {//下
            // 下一张图片
            $('#picNext').click()

        }

        if (e && e.keyCode == 37) {//左
            // 上一页
            $('.page-pre').click()
        }

        if (e && e.keyCode == 39) {//右
            // 下一页
            $('.page-next').click()

        }
    })
})