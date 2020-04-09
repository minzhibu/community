var keydown = "";
$(function () {
    $("#selectTag").hide();
    keydown = $("#tag").val();
    var editor = editormd("question-editor", {
        width: "100%",
        height: 600,
        path: "/js/lib/",
        delay: 0,
        watch: false,
        placeholder: "请输入问题描述",
        emoji: true,
        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL: "/file/upload"
    });
})

//添加标签方法
function selectTag(data) {
    var tag = $("#tag")
    var tagVal = tag.val()
    var strings = tagVal.split(",");
    if (strings.length === 7) {
        alert("最多选中7个标签")
        return;
    }
    for (var i = 0; i < strings.length; i++) {
        if (data === strings[i]) {
            return;
        }
    }
    if (tagVal === "") {
        tag.val(data);
    } else {
        tag.val(tagVal + "," + data)
    }
    keydown = tag.val();
}

//获取焦点的时候展示
function showSelectTag() {
    $("#selectTag").show();
}

//失去焦点的时候消失
function hideSelectTag() {
    console.log("????")
    $("#selectTag").hide();
}

function textKeyup(e) {
    var tag = $("#tag")
    if (e.keyCode !== 8) {
        $("#errorSpan").text("本论坛暂时不支持自定义标签！")
        document.getElementById("error").style.display = ""
    } else {
        var theKeydown = tag.val()
        if (theKeydown === "") {
            keydown = theKeydown;
        }
        var n = -1
        var is
        for (var i = 0; i < keydown.length; i++) {
            if (i >= theKeydown.length) {
                break;
            }
            if (keydown.charAt(i) !== ',' && keydown.charAt(i) !== theKeydown.charAt(i)) {
                is = true;
                var first = keydown.substring(0, n);
                var last = keydown.substring(n + 1, keydown.length)
                if (last.indexOf(',') === -1) {
                    last = "";
                } else {
                    last = last.substring(last.indexOf(',') + 1, last.length)
                }
                if (first === "" || last === "") {
                    keydown = first + last
                } else {
                    keydown = first + ',' + last
                }
                break;
            }
            if (keydown.charAt(i) === ',') {
                if (keydown.charAt(i) !== theKeydown.charAt(i)) {
                    is = true;
                    break
                }
                n = i
            }
        }
        if (!is) {
            keydown = keydown.substring(0, keydown.lastIndexOf(","))
        }
    }
    tag.val(keydown)
}