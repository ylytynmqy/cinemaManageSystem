var isVIP = false;
var useVIP = false;
var orderId=-1;
$(document).ready(function () {
    getOrderList();

});
function getOrderList() {
    getSyncRequest(
        '/order/all/'+sessionStorage.getItem('id'),
        function (res) {
            renderOrderList(res.content);
        },
        function (error) {
            alert(error);
        });
}
function renderOrderList(list){
    var str="";
    var stateStr="";
    list=list.reverse();
    for (i=0;i<list.length;i++) {
        var orderState="";
        if (list[i].state===0){
            orderState="未完成";
            stateStr=            "                <span class='label label-warning'>" +
                orderState+
                "                </span>";
        }
        else if(list[i].state===1){
            orderState="已完成";
            stateStr=            "                <span class='label label-success'>" +
                orderState+
                "                </span>";
        }
        else if (list[i].state===2){
            orderState="已失效";
            stateStr=            "                <span class='label label-default'>" +
                orderState+
                "                </span>";
        }

        var description="";
        if (list[i].type===0){
            var sum=list[i].description.split(',');
            description="电影票"+"×"+sum.length+"张";
        }
        else if (list[i].type===1){
            description="购买会员卡";
        }
        else if (list[i].type===2){
            description="充值会员卡";
        }
        str+="<div class='card'>" +
            "            <div class='card-header form-inline'>" +
            "                <span class='subtitle'>" +
            description+
            "                </span>";

        str+=stateStr;
        str+= "            </div>" +
            "            <div class='card-body order-content'>" +
            "                <bold>"+
            list[i].total+"<span style='font-size: 18px'>元</span>"+
            "</bold>" +
            "                <div class='dark-text'>" +
            "<span style='font-size: 18px'>下单时间：</span>"+
            list[i].join_time.substring(0,10)+" "+list[i].join_time.substring(11,19)+
            "</div>" ;
        // if (orderState===1){
        //     str+="<div class='dark-text'>" +
        //         "<span style='font-size: 18px'>结单时间：</span>"+
        //     list[i].end_time.substring(0,10)+" "+list[i].end_time.substring(11,19) +
        //     "</div>" ;
        // }
        // else if (orderState===2){
        //     str+="<div class='dark-text'>" +
        //         "<span style='font-size: 18px'>失效时间：</span>"+
        //         list[i].end_time.substring(0,10)+" "+list[i].end_time.substring(11,19) +
        //         "</div>" ;
        // }
        var refund="<button class='btn btn-danger order-ed-ticket-refund' value='"+list[i].id+"' onclick='ticketRefund(this)'>退票</button>";
        var cancel="<button class='btn btn-light order-ing-cancel' value='"+list[i].id+"' onclick='orderCancel(this)'>取消订单</button>";
        var finish="<button class='btn btn-primary order-ing-finish' value='"+list[i].id+"' onclick='orderFinish(this)'>立即支付</button>";
        if (list[i].state===0){//未完成的订单
            str+="<div class='card-footer'>"+cancel+finish+"</div>";
        }
        else if (list[i].state===1 && list[i].type===0){//已购买可以退票
            str+="<div class='card-footer justify-content-end'>"+refund+"</div>";
        }
        str+="</div>" + "</div>";
    }
    $('#order-list').html(str);
}
function ticketRefund(w) {
    var r = confirm("确认要退票吗？");
    if(r) {
        var oId = parseInt(w.getAttribute('value'));
        postRequest(
            "/order/refund/" + oId,
            null,
            function (res) {
                if (res.success) {
                    $('.list-view').css("display", "none");
                    $('.user-order-refund-state').css("display", "");

                    function jump(count) {
                        window.setTimeout(function () {
                            count--;
                            if (count > 0) {
                                jump(count);
                            } else {
                                window.location.reload();
                                $('.list-view').css("display", "");
                                $('.user-order-refund-state').css("display", "none");

                            }
                        }, 1000);
                    }

                    jump(2);
                } else {
                    alert(res.message);
                    $(w).removeClass('btn btn-danger');
                    $(w).addClass('btn btn-default');
                    $(w).html('不能退票');
                    $(w).attr("disabled", "true");
                    $(w).attr("onclick", "");
                }
            },
            function (error) {
                alert(JSON.stringify(error))
            }
        )
    }
}
function orderCancel(w) {
    var r = confirm("确认要取消该订单吗？");
    if(r) {
        var oId = parseInt(w.getAttribute('value'));
        postRequest(
            "/order/cancel/" + oId,
            null,
            function (res) {
                if (res.success) {
                    $('.list-view').css("display", "none");
                    $('.user-order-cancel-state').css("display", "");

                    function jump(count) {
                        window.setTimeout(function () {
                            count--;
                            if (count > 0) {
                                jump(count);
                            } else {
                                window.location.reload();
                                $('.list-view').css("display", "");
                                $('.user-order-cancel-state').css("display", "none");

                            }
                        }, 1000);
                    }

                    jump(2);
                }
            },
            function (error) {
                alert(JSON.stringify(error))
            }
        )
    }
}

//触发弹窗按钮
function orderFinish(w) {
    var oId=parseInt(w.getAttribute('value'));
    orderId=oId;//参数是用来为后面点击做铺垫的
    var orderType=-1;
    var orderTotal=-0.1;
    getRequest(
        '/order/get/'+oId,
        function (res) {
            orderType=res.content.type;
            orderTotal=res.content.total;
            if (orderType===0){
                //买票

                //得到vip信息
                getRequest(
                    '/vip/' + sessionStorage.getItem('id') + '/get',
                    function (res) {
                        isVIP = res.success;
                        useVIP = res.success;

                        if (isVIP) {
                            $('#vip-pay-body-vipcard-balance').html("<div><b>会员卡余额：</b>" + res.content.balance.toFixed(2) + "元</div>");//交给switch
                        } else {
                            //navi-tab只显示银行卡并启用
                            $("#vip-pay-navi-switch").css("display", "none");
                            $("#bank-pay-navi-switch").addClass("active");

                            //只显示主体输入框
                            $("#vip-pay-body").css("display", "none");
                            $("#bank-pay-body").css("display", "");
                        }

                        $('#pay-total').html("<div><b>金额：</b>" + orderTotal + "元</div>");
                        $('#user-order-pay-modal').modal('show');

                    },
                    function (error) {alert(error);}
                );

            }
            else if (orderType===1) {
                //买卡
                //navi-tab只显示银行卡并启用
                $("#vip-pay-navi-switch").css("display", "none");
                $("#bank-pay-navi-switch").addClass("active");

                //只显示主体输入框
                $("#vip-pay-body").css("display", "none");
                $("#bank-pay-body").css("display", "");

                $('#pay-total').html("<div><b>金额：</b>" + orderTotal + "元</div>");
                $('#user-order-pay-modal').modal('show');
            }
            else if(orderType===2){
                //充卡
                //navi-tab只显示银行卡并启用
                $("#vip-pay-navi-switch").css("display", "none");
                $("#bank-pay-navi-switch").addClass("active");

                //只显示主体输入框
                $("#vip-pay-body").css("display", "none");
                $("#bank-pay-body").css("display", "");
                $('#pay-total').html("<div><b>金额：</b>" + orderTotal + "元</div>");
                $('#user-order-pay-modal').modal('show');
            }
        },
        function (error) {
            alert('getOrderById request failed');
            alert(error);
        });
}

function switchPay(type) {
    useVIP = (type === 0);
    if (type === 0) {
        $("#vip-pay-navi-switch").addClass("active");
        $("#bank-pay-navi-switch").removeClass("active");

        $("#vip-pay-body").css("display", "");
        $("#bank-pay-body").css("display", "none");
    } else if (type===1) {
        $("#vip-pay-navi-switch").removeClass("active");
        $("#bank-pay-navi-switch").addClass("active");

        $("#vip-pay-body").css("display", "none");
        $("#bank-pay-body").css("display", "");
    }
}

function payConfirmClick() {
    if (useVIP) {
        postRequest(
            '/order/pay/vipcard/'+orderId,//vip卡付款
            null,
            function (res) {
                if (res.success){
                    $('.list-view').css("display", "none");
                    $('.user-order-success-state').css("display", "");
                    $('#user-order-pay-modal').modal('hide');

                    function jump(count) {
                        window.setTimeout(function(){
                            count--;
                            if(count > 0) {
                                jump(count);
                            } else {
                                window.location.reload();
                                $('.list-view').css("display", "");
                                $('.user-order-success-state').css("display", "none");

                            }
                        }, 1000);
                    }
                    jump(2);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }

        );
    } else {

        if (validateForm()) {
            if ($('#bank-pay-body-card-num').val() === "123123123" && $('#bank-pay-body-card-password').val() === "123123") {
                postRequest(
                    '/order/pay/bankcard/'+orderId,//普通卡付款
                    null,
                    function (res) {
                        if (res.success){
                            $('.list-view').css("display", "none");
                            $('.user-order-success-state').css("display", "");
                            $('#user-order-pay-modal').modal('hide');

                            function jump(count) {
                                window.setTimeout(function(){
                                    count--;
                                    if(count > 0) {
                                        jump(count);
                                    } else {
                                        window.location.reload();
                                        $('.list-view').css("display", "");
                                        $('.user-order-success-state').css("display", "none");

                                    }
                                }, 1000);
                            }
                            jump(2);
                        }
                    },
                    function (error) {
                        alert(JSON.stringify(error));
                    }
                );
            } else {
                alert("银行卡号或密码错误");
            }
        }
    }
}
function validateForm() {
    var isValidate = true;
    if (!$('#bank-pay-body-card-num').val()) {
        isValidate = false;
        $('#bank-pay-body-card-num').parent('.form-group').addClass('has-error');
        $('#bank-pay-body-card-num-error').css("visibility", "visible");
    }
    if (!$('#bank-pay-body-card-password').val()) {
        isValidate = false;
        $('#bank-pay-body-card-password').parent('.form-group').addClass('has-error');
        $('#bank-pay-body-card-password-error').css("visibility", "visible");
    }
    return isValidate;
}