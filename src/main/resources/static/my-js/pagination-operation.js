//总的function,用来调用各个细分的function
function generateEventPagination(){
    //获取Event分页数据
    var resultEntity=getPageInfoEvent();

    //填充Event表格
    fillEventCardDeck(resultEntity.data);

    //调用分页导航栏生成函数
    generateNavigationBarEvent(resultEntity);
}

//获取pageInfo函数
function getPageInfoEvent(){
    var res;

    $.ajax({
        url: "/event/paginated",
        type: "post",
        data: {
            "pageNum": window.eventPageNum,
            "pageSize": window.eventPagesize,
            // "keyword": window.keyword
        },
        dataType: "json",
        async: false,
        success: function(response){
            res=response;
        },
        error: function(response){
            res=response;
         }
    });

    return res;
}

//生成表格函数
function fillEventCardDeck(list){
    //清除tbody中原有数据
    $("#event-deck").empty();

    //清空分页导航条
    $("#Pagination-event").empty();

    if(list==null || list.length==0){
        $("#event-deck").append("<tr><td colspan='4'>No data found for you</td></tr>");
        return;
    }

    for(var i=0;i<list.length;i++){
        var base64Encoded=list[i].base64Encoded;
        var eventName=list[i].eventName;
        var description=list[i].description;

        //Image tag
        var img="<img src='data:image/jpeg;base64,"+base64Encoded+"' class='card-img-top' alt='Event Image' style='width:100%; height: 65%;'>";

        //Card Body
        var eventNameH5="<h5 class='card-title'>"+eventName+"</h5>";
        var discriptionP="<p class='card-text'>"+description+"</p>";
        var findDetailsA="<a href=\"#\" class=\"btn btn-primary\">Find Details</a>"

        var cardBody="<div class=\"card-body\">"+eventNameH5+discriptionP+findDetailsA+"</div>";

        //Card
        var card="<div class=\"card\" style='width:320px; height:430px; max-width: 33.33%;'>"+img+cardBody+"</div>";

        $("#event-deck").append(card);
    }
}

//分页导航栏生成函数
function generateNavigationBarEvent(resultEntity){
    //获得role记录的总数
    var totalRecord=resultEntity.totalRecords;

    //声明一个属性设置pagination函数所需的属性
    var properties={
        num_edge_entries: 2,        //边缘页数量
        num_display_entries: 4,     //主体页数量
        callback: pageSelectCallbackEvent,       //用户点击页码时，调用这个函数进行跳转
        items_per_page: window.eventPagesize,
        current_page: window.eventPageNum-1,   //Pagination内部使用pageIndex(从0开始），而pageNum从1开始
        prev_text: "Previous",
        next_text: "Next"
    };

    $("#Pagination-event").pagination(totalRecord, properties);
}

//用户点击页码时，调用这个函数进行跳转
function pageSelectCallbackEvent(pageIndex, jQuery){
    //计算pageNum
    window.eventPageNum=pageIndex+1;

    //调用分页函数
    generateEventPagination();

    //由于每一个页码按钮都是超链接，它默认跳转到上面的地址，但是那是不正确的，因为没有加host和contextPath。
    //我们要取消超链接的默认行为
    return false;
}









//总的function,用来调用各个细分的function
function generateGroupPagination(){
    //获取Event分页数据
    var resultEntity=getPageInfoGroup();

    //填充Event表格
    fillGroupCardDeck(resultEntity.data);

    //调用分页导航栏生成函数
    generateNavigationBarGroup(resultEntity);
}

//获取pageInfo函数
function getPageInfoGroup(){
    var res;

    $.ajax({
        url: "/group/paginated",
        type: "post",
        data: {
            "pageNum": window.groupPageNum,
            "pageSize": window.groupPagesize,
            // "keyword": window.keyword
        },
        dataType: "json",
        async: false,
        success: function(response){
            res=response;
        },
        error: function(response){
            res=response;
        }
    });

    return res;
}

//生成表格函数
function fillGroupCardDeck(list){
    //清除tbody中原有数据
    $("#group-deck").empty();

    //清空分页导航条
    $("#Pagination-group").empty();

    if(list==null || list.length==0){
        $("#group-deck").append("<tr><td colspan='4'>No data found for you</td></tr>");
        return;
    }

    for(var i=0;i<list.length;i++){
        var base64Encoded=list[i].base64Encoded;
        var name=list[i].name;
        var description=list[i].description;

        //Image tag
        var img="<img src='data:image/jpeg;base64,"+base64Encoded+"' class='card-img-top' alt='Event Image' style='width:100%; height: 65%;'>";

        //Card Body
        var nameH5="<h5 class='card-title'>"+name+"</h5>";
        var descriptionP="<p class='card-text'>"+description+"</p>";
        var findDetailsA="<a href=\"#\" class=\"btn btn-primary\">Find Details</a>"

        var cardBody="<div class=\"card-body\">"+nameH5+descriptionP+findDetailsA+"</div>";

        //Card
        var card="<div class=\"card\" style='width:320px; height:430px; max-width: 33.33%;'>"+img+cardBody+"</div>";

        $("#group-deck").append(card);
    }
}

//分页导航栏生成函数
function generateNavigationBarGroup(resultEntity){
    //获得role记录的总数
    var totalRecord=resultEntity.totalRecords;

    //声明一个属性设置pagination函数所需的属性
    var properties={
        num_edge_entries: 2,        //边缘页数量
        num_display_entries: 4,     //主体页数量
        callback: pageSelectCallbackGroup,       //用户点击页码时，调用这个函数进行跳转
        items_per_page: window.groupPagesize,
        current_page: window.groupPageNum-1,   //Pagination内部使用pageIndex(从0开始），而pageNum从1开始
        prev_text: "Previous",
        next_text: "Next"
    };

    $("#Pagination-group").pagination(totalRecord, properties);
}

//用户点击页码时，调用这个函数进行跳转
function pageSelectCallbackGroup(pageIndex, jQuery){
    //计算pageNum
    window.groupPageNum=pageIndex+1;

    //调用分页函数
    generateGroupPagination();

    //由于每一个页码按钮都是超链接，它默认跳转到上面的地址，但是那是不正确的，因为没有加host和contextPath。
    //我们要取消超链接的默认行为
    return false;
}