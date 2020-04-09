//获取了二级评论就放在这里面
var collapseCommentList = {}
// //判断这个一级评论下是否已经存在回复框
// var collapseComments = {}
//判断这个一级评论下已经存在按键
var collapseButton = {}

$(function () {
    editormd.markdownToHTML("question-view",{

    })
    pageTurning(1);

});

function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    if (content.trim()) {
        $.ajax({
            type: "POST",
            url: "/comment",
            contentType: "application/json",
            data: JSON.stringify({
                "parentId": questionId,
                "content": content,
                "type": 1
            }),
            success: function (response) {
                if (response.code == 200) {
                    window.location.reload();
                } else {
                    if (response.code == 2003) {
                        var isAccepted = confirm(response.message);
                        if (isAccepted) {
                            window.open("https://github.com/login/oauth/authorize?client_id=4c749de267870ab6295c&" +
                                "redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                            window.localStorage.setItem("closable", "true");
                        }
                    }
                }
            },
            dataType: "json"
        });
    } else {
        alert("评论不能为空哦！")
    }
}

function pageTurning(page,parentId) {
    console.log(page);
    var id = $("#question_id").val();
    var now = $("#now").val();
    $("#div1").empty();
    var communityPage;
    if (typeof page == "number") {
        communityPage = page;
    } else {
        communityPage = page.innerText;
    }
    //判断是否为二级评论的分页
    if(parentId){
        collapseCommentShow(communityPage,parentId);
        return;
    }
    $.ajax({
        type: "GET",
        url: "/questionPage",
        contentType: "application/json",
        data: {
            "id": id,
            "page": communityPage
        },
        success: function (response) {
            showCommunity(response, "communityUser", "communityRemove", 1);
            showPage(response);
        },
        dataType: "json"
    });
}

//展示评论方法
function showCommunity(response, communityUser1, communityRemove, type, parentId) {
    communityUser = $("#" + communityUser1 + "");
    communityUser.empty();
    var questions = response.questions;
    if (questions != null) {
        for (var i = 0; i < questions.length; i++) {
            var collapseCommentCount = questions[i].collapseCommentCount;
            var good = "";
            var reply = "";
            var collapse = "";
            var replyInput = "";
            var collapsePage = "";
            if (type === 1) {
                good = "            <span class='glyphicon glyphicon-thumbs-up icon'></span>" +
                    "            <span style='margin-right: 0px;' class='glyphicon glyphicon-comment icon' id='collapseCount_" + questions[i].id + "' onclick='collapseComment(this," + type + "," + collapseCommentCount + "," + parentId + ")'" +
                    " data-parent='" + communityUser1 + "' data-parentId='" + parentId + "' data-id='" + questions[i].id + "'><span style='    display: inline-block;\n" +
                    "    font-size: 18px;\n" +
                    "    padding-left: 3px;'> " + collapseCommentCount + "</span></span>";
                collapse = "<div class = 'col-lg-12 col-md-12 col-sm-12 col-xs-12 collapse' id='" + "comment-" + questions[i].id + "'>" +
                    "   <div class='well' style='background: #f5f5f5;'>" +
                    "    <div  style='display: inline-block;width: 100%'>" +
                    "        <div id='communityUserCollapse_" + questions[i].id + "'>" +
                    "        </div>" +
                    "    </div>" +
                    "   </div>" +
                    "</div>";
            } else {
                reply = "<span class='pull-right reply' >回复</span>";
                if (i === questions.length - 1) {
                    replyInput = "<input  id='replyInput_" + parentId + "' onclick='submitReply(this," + communityUser1 + "," + parentId + ")' " +
                        "class='form-control comment' style='width: 98%;margin-left: 10px;'>";
                    collapsePage =
                        "<nav id='questionPages_"+parentId+"' aria-label='Page navigation'>" +
                        "    <ul id='pageUL_"+ parentId +"' class='pagination  pagination-lg' style='float: right;'>" +
                        "    </ul>" +
                        "</nav>";
                }
            }

            if (!collapseCommentCount) {
                collapseCommentCount = "";
            }
            communityUser.append(
                "<div id='communityUser' class='col-lg-12 col-md-12 col-sm-12 col-xs-12 comments'>" +
                "    <div class='media-left'>" +
                "        <a>" +
                "            <img style='height: 35px; width: 35px;' class='media-object img-rounded'" +
                "                 src='" + questions[i].user.avatarUrl + "'>" +
                "        </a>" +
                "    </div>" +
                "    <div class='media-body'>" +
                "        <p style='font-size: 17px;margin-bottom: 1px'" +
                "           >" + questions[i].user.name + "</p>" +
                "        <div >" + questions[i].content + "</div>" +
                "        <div class='menu'>" +
                good +
                "            <span class='pull-right'> " + timeToString(questions[i].gmtCreate) + "</span>" +
                reply +
                "          </div>" +
                collapse +
                "     </div>" +
                "</div>" +
                collapsePage+
                replyInput
            )
        }
    } else if(type !== 1){
        communityUser.append("<input placeholder='评论一下...' id='replyInput_" + parentId + "' onclick='submitReply(this," + communityUser1 + "," + parentId + ")' " +
            "class='form-control comment' style='width: 98%;margin-left: 10px;'>");
    } else{
        communityUser.append("<h4 style='margin-left: 20px'>快点来评论吧</h4>")
    }

}


/*
判断是否存在二级评论存在则展开不存在则展开输入框来进行回复
 */
function collapseComment(e, type, collapseCommentCount) {
    var id = e.getAttribute("data-id");
    var parent = e.getAttribute("data-parent");
    var parentId = e.getAttribute("data-parentId")
    if (type === 1) {
        var collapse = e.getAttribute("data-collapse");
        var key = "communityUserCollapse_" + id;
        var comment = $("#comment-" + id);
        if (!collapse) {
            e.setAttribute("data-collapse", "in");
            comment.addClass("in");
            if (collapseCommentList[key] == null) {
                collapseCommentShow(1,id);

            } else {
                showCommunity(collapseCommentList[key], key, null, 2, id);
                showPage(collapseCommentList[key],id);
            }
            e.classList.add("active");
        } else {
            e.removeAttribute("data-collapse");
            comment.removeClass("in");
            e.classList.remove("active");
            $("#communityUserCollapse_" + id).empty();
            collapseButton["communityUserCollapse_" + id] = null;
            // collapseComments["replyInput_" + id] = 0;
        }
    }
}

/**
 * 请求获取当前这个评论的二级评论
 * @param currentTime
 * @returns {string}
 */
function collapseCommentShow(page,id) {
    console.log("二级评论的一级评论id:" + id);
    console.log("二级评论的页数:" + page);
    $.ajax({
        type: "GET",
        url: "/questionPage",
        contentType: "application/json",
        data: {
            "id": id,
            "type": 2,
            "page": page
        },
        success: function (response) {
            var key = "communityUserCollapse_" + id;
            collapseCommentList[key] = response;
            $("#collapseCount_" + id).text(response.totalCount);
            showCommunity(response, "communityUserCollapse_" + id, null, 2, id);
            showPage(response,id);
        },
        dataType: "json"
    });
}

function submitReply(e, collapseCommentCount, parentId) {
    var attribute = collapseCommentCount.getAttribute("id");
    if (collapseButton[attribute] !== 1) {
        $("#" + attribute).append("<button type='button' style='float: right;margin-right: 13px;' onclick='discuss(" + parentId + "," + attribute + ")'" +
            " class='btn btn-primary'>评论</button>")
        collapseButton[attribute] = 1;
    }
}

//发送二级评论
function discuss(parentId, attribute) {
    var content = $("#replyInput_" + parentId).val();
    if (content === "") {
        alert("评论不能为空");
    } else {
        $.ajax({
            type: "POST",
            url: "/comment",
            contentType: "application/json",
            data: JSON.stringify({
                "parentId": parentId,
                "content": content,
                "type": 2
            }),
            success: function (response) {
                if (response.code === 200) {
                    collapseCommentList["communityUserCollapse_" + parentId] = null;
                    collapseButton["communityUserCollapse_" + parentId] = {};
                    collapseCommentShow(1,parentId);
                    // window.location.reload();
                } else {
                    if (response.code === 2003) {
                        var isAccepted = confirm(response.message);
                        if (isAccepted) {
                            window.open("https://github.com/login/oauth/authorize?client_id=4c749de267870ab6295c&" +
                                "redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                            window.localStorage.setItem("closable", "true");
                        }
                    }
                }
            },
            dataType: "json"
        });
    }
}


//显示页数
function showPage(response,id) {
    console.log("获取id:" +id);
    var pageUL = "#pageUL";
    var pageId = "";
    if(id){
        pageId = "_" + id;
        pageUL += pageId;
    }
    pageUL = $(pageUL);
    pageUL.empty();
    if (response.totalPage > 1) {
        if (response.showFirstPage) {
            pageUL.append(
                "<li>" +
                "    <a onclick='pageTurning(1,"+id+")' aria-label='Previous'>" +
                "        <span aria-hidden='true'>&laquo;&laquo;</span>" +
                "    </a>" +
                "</li>");
        }
        if (response.showPrevious) {
            pageUL.append(
                "<li>" +
                "    <a onclick='pageTurning("+(response.page - 1)+","+id+",)' aria-label='Previous'>" +
                "        <span aria-hidden='true'>&laquo;</span>" +
                "    </a>" +
                "</li>");
        }
        for (var i = 0; i < response.pages.length; i++) {
            if (response.page === response.pages[i]) {
                pageUL.append(
                    "<li class='active'>" +
                    "    <a onclick='pageTurning(this,"+id+","+response.page+")' >" + response.pages[i] + "</a>" +
                    "</li>");
            } else {
                pageUL.append(
                    "<li>" +
                    "    <a onclick='pageTurning(this,"+id+","+response.page+")' >" + response.pages[i] + "</a>" +
                    "</li> ");
            }
        }
        if (response.showNext) {
            pageUL.append(
                "<li>" +
                "    <a onclick='pageTurning("+(response.page + 1)+","+id+")' aria-label='Previous'>" +
                "        <span aria-hidden='true'>&raquo;</span>" +
                "    </a>" +
                "</li>");
        }
        if (response.showEndPage) {
            pageUL.append(
                "<li>" +
                "    <a onclick='pageTurning("+response.totalPage+","+id+")' aria-label='Previous'>" +
                "        <span aria-hidden='true'>&raquo;&raquo;</span>" +
                "    </a>" +
                "</li>");
        }
    }

}


//毫秒转化为字符串
function timeToString(currentTime) {
    var date1 = new Date(currentTime);
    var date2 = new Date();
    var time2 = date2.getTime();
    var number = (time2 - currentTime) / 1000;
    if ((Math.floor(number / (60 * 60))) === 0) {
        return Math.ceil(number / 60) + "分钟前发布";
    }
    if (Math.floor(number / (60 * 60 * 24)) === 0) {
        return Math.floor(number / (60 * 60)) + "小时前发布";
    }
    if (Math.floor(number / (60 * 60 * 24 * 7)) === 0) {
        return Math.floor(number / (60 * 60 * 24)) + "天前发布";
    }
    if (Math.floor(number / (60 * 60 * 24 * 7 * 4)) === 0) {
        return Math.floor(number / (60 * 60 * 24 * 7)) + "周前发布";
    }
    return date1.getFullYear() + "年-" + (date1.getMonth() + 1) + "月-" + date1.getDate() + "日发布";
}
