var code='';
$(document).ready(function() {
    getClerkList();
    getVerificationCode();
});
function getClerkList() {
    var url="/clerk/get";

    getRequest(//获得他们的名字
        url,
        function (res) {
            clerks = res.content;
            renderClerk(clerks);
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
}
function getVerificationCode() {
    var url="/clerk/get/code";

    getRequest(//获得验证码信息
        url,
        function (res) {
            if (res.content===null){
                $('#clerk-veri-code-none').css("display", "");
            }
            else{
                 code= res.content;
                 $('#code-content').text(code);
                $('#clerk-veri-code').css("display", "");
            }
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
}
function renderClerk(list) {
    var str="";
    for (i=0;i<list.length;i++){
        var item=list[i];
        var clerkId=item.id;
        var rank=0;
        getSyncRequest(
            '/clerk/get/rank/'+clerkId,
            function (res) {
                rank=res.content;
            },
            function (error) {
                alert(error);
            }
        )
        str+="<tr>";
        str+="<td>"+(i+1)+"</td>";
        str+="<td>"+item.username+"</td>";
        str+="<td>"+rank+"</td>";
        str+="<td><button class='btn btn-light' value='"+item.id+"' style='width: 100%' onclick='updateClerkRank(this)'>修改</button></td>";
        str+="<td><button class='btn btn-primary' value='"+item.id+"' style='width: 100%' onclick='deleteClerk(this)'>删除</button></td>";
        str+="</tr>";
    }
    $(".clerk-list").html(str);
}
function deleteClerk(w) {
    var isConfirmed=confirm('确认注销此员工账号吗？');
    var clerkId=parseInt(w.getAttribute('value'));
    var url="/clerk/delete/"+clerkId;
    if(isConfirmed){
        deleteRequest(
            url,
            null,
            function (res) {
                if (res.success){
                    window.location.reload();
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )
    }
}

function addCode() {
    $('#clerk-veri-code-none').css("display", "none");
    $('#clerk-veri-code-edit-add').css("display", "");
}
function updateCode() {
    $('#clerk-veri-code').css("display", "none");
    $('#clerk-veri-code-edit-update').css("display", "");
}
function addCodeConfirm() {
    postRequest(
        '/clerk/add/code',
        $('#code-edit-add-input').val(),
        function (res) {
            window.location.reload();
        },
        function (error) {
            alert(error);
        }
    )
}
function updateCodeConfirm() {
    postRequest(
        '/clerk/update/code',
        $('#code-edit-update-input').val(),
        function (res) {
            window.location.reload();
        },
        function (error) {
            alert(error);
        }
    )
}

function updateClerkRank(w) {
    var clerkId=parseInt(w.getAttribute('value'));
    sessionStorage.setItem('clerkId',clerkId);
    $('#clerk-update').modal('show');
}
function updateClerkRankConfirm() {
    var clerkId=sessionStorage.getItem('clerkId');
    var rank=$('#rank-select  option:selected').val();
    postRequest(
        '/clerk/update/rank',
        {"clerkId":clerkId,"rank":rank},
        function (res) {
            window.location.reload();
        },
        function (error) {
            alert(error);
        }
        )
}