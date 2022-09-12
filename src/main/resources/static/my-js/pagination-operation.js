//总的function,用来调用各个细分的function
function generateEventPagination(){
    //获取分页数据
    var pageInfo=getPageInfo().data;

    //填充表格
    fillTable(pageInfo.list);

    //调用分页导航栏生成函数
    // generateNavigationBar(pageInfo);

    // //给每个角色记录的铅笔按钮(修改按钮）绑定点击响应函数
    // $(".btn-pencil").click(function (){
    //     //获取当前点击的角色记录的roleName
    //     var roleName=$(this).parent().prev().text();
    //
    //     //获取当前点击的角色记录的roleId
    //     window.roleId=this.id;
    //
    //     //回显模态框中的文本框
    //     $("#modal-role-update [id=input-roleName-update]").val(roleName);
    //
    //     //打开模态框
    //     $("#modal-role-update").modal("show");
    // });
    //
    // //給每个角色记录的删除按钮绑定点击响应函数
    // $(".btn-cross").click(function (){
    //     //获取roleName
    //     var roleName=$(this).parent().prev().text();
    //
    //     //创建role对象并传入数组
    //     var roleList=[{
    //         id: this.id,
    //         name: roleName
    //     }];
    //
    //     //调用专门的函数来打开确认删除的模态框
    //     showModalForDeleteConfirmation(roleList);
    // });
    //
    // //给每个角色记录的checkbox绑定效果（如果所有的checkbox都选中了，将总的checkbox-summary也选中）
    // $('.checkbox-role').click(function (){
    //     //获取全部checkbox的数量
    //     var checkboxNum=$(".checkbox-role").length;
    //
    //     //获取当前已选中的checkbox的数量
    //     var checkedNum=$(".checkbox-role:checked").length;
    //
    //     //设置checkbox-summary的值
    //     $("#checkbox-summary").prop("checked", checkboxNum==checkedNum);
    // });
}

//获取pageInfo函数
function getPageInfo(){
    var res;

    $.ajax({
        url: "/event/paginated",
        type: "post",
        data: {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
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
function fillTable(pageInfo){
    //清除tbody中原有数据
    $("#event-deck").empty();

    //清空分页导航条
    $("#Pagination").empty();

    if(pageInfo==null || pageInfo.length==0){
        $("#event-deck").append("<tr><td colspan='4'>No data found for you</td></tr>");
        return;
    }

    for(var i=0;i<pageInfo.length;i++){
        var roleId=pageInfo[i].id;
        var roleName=pageInfo[i].name;

        var numberTd="<td>"+roleId+"</td>";
        var checkboxTd="<td><input class='checkbox-role' type='checkbox'></td>";
        var roleNameTd= "<td>"+roleName+"</td>";

        //Button td
        var checkBtn="<button type=\"button\" class=\"btn btn-success btn-xs\"><i class=\" glyphicon glyphicon-check\"></i></button>";
        var pencilBtn="<button id='"+roleId+"' type=\"button\" class=\"btn btn-primary btn-xs btn-pencil\"><i class=\" glyphicon glyphicon-pencil\"></i></button>";
        var removeBtn="<button id='"+roleId+"' type=\"button\" class=\"btn btn-danger btn-xs btn-cross\"><i class=\" glyphicon glyphicon-remove\"></i></button>";

        var buttonTd="<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+"</td>";

        //tr
        var tr="<tr>"+numberTd+checkboxTd+roleNameTd+buttonTd+"</tr>";

        $("#tbody").append(tr);
    }
}

//分页导航栏生成函数
function generateNavigationBar(pageInfo){
    //获得role记录的总数
    var totalRecord=pageInfo.total;

    //声明一个属性设置pagination函数所需的属性
    var properties={
        num_edge_entries: 3,        //边缘页数量
        num_display_entries: 5,     //主体页数量
        callback: pageSelectCallback,       //用户点击页码时，调用这个函数进行跳转
        items_per_page: pageInfo.pageSize,
        current_page: pageInfo.pageNum-1,   //Pagination内部使用pageIndex(从0开始），而pageNum从1开始
        prev_text: "Previous",
        next_text: "Next"
    };

    $("#Pagination").pagination(totalRecord, properties);
}

//用户点击页码时，调用这个函数进行跳转
function pageSelectCallback(pageIndex, jQuery){
    //计算pageNum
    window.pageNum=pageIndex+1;

    //调用分页函数
    generatePage();

    //由于每一个页码按钮都是超链接，它默认跳转到上面的地址，但是那是不正确的，因为没有加host和contextPath。
    //我们要取消超链接的默认行为
    return false;
}

//在删除角色时，显示模态框用于确认删除行为
function showModalForDeleteConfirmation(roleList){
    //设置全局变量，为以后发送删除角色的Ajax作准备
    window.roleIdList=[];

    //清除模态框
    $("#span-roleName").empty();

    for(var i=0;i<roleList.length;i++){
        var role=roleList[i];
        var roleName=role.name;

        $("#span-roleName").append(roleName+"<br/>");

        //往roleidList传入要删除的roleId
        window.roleIdList.push(role.id);
    }

    //打开模态框
    $("#modal-role-delete-confirmation").modal("show");
}