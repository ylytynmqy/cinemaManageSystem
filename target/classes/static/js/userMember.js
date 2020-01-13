$(document).ready(function () {
    // $(".username-on-nav").text(sessionStorage.getItem('username'));
    getVIP();
    getCoupon();
});

var isBuyState = true;
var vipCardId;
var myCard;
var myType; //订单种类
var myState; //订单状态
var cardKindBuy; //买的什么卡
var cardPriceBuy; //卡的价格
var cardChargeAmount; //充卡金额
function getVIP() {
    getRequest(
        '/vip/' + sessionStorage.getItem('id') + '/get',
        function (res) {
            if (res.success) {
                // 是会员
                $("#member-card").css("visibility", "visible");
                $("#member-card").css("display", "");
                $("#nonmember-card").css("display", "none");

                vipCardId = res.content.id;
                var balance = res.content.balance.toFixed(2);
                var joinDate = res.content.joinDate.substring(0, 10);
                //var percent=res.content.percent;
                getRequest(
                    '/vip/card/get/'+res.content.type,
                    function (res) {
                        $("#member-id").text(vipCardId);
                        $("#member-balance").text("¥" + balance);
                        $("#member-joinDate").text(joinDate);
                        $("#member-description").text("满"+res.content.targetAmount+"送"+res.content.bonusAmount);
                        $("#member-price").text(res.content.percent);
                    }
                )
            } else {
                // 非会员
                $("#member-card").css("display", "none");
                $("#nonmember-card").css("display", "");
            }
        },
        function (error) {
            alert(error);
        });

    //得到所有种类的vip卡的信息，t添加到页面上
    getRequest(
        '/vip/card/all',
        function (res) {
            if(res.success){
                renderVIPCards(res.content);
            }else{
                alert(res.content);
            }
        },
        function (error) {
            alert(error);
        }
    )
}
function renderVIPCards(cards) {
    $("#notMember").empty();
    var cardDOMStr="";
    cards.forEach(function (card) {
        cardDOMStr+=
            "<div class='info'>" +
            "<div class='price'>"+card.price+"元/张</div>"+
            "<div clas='description'>"+card.description+"。充值优惠：满"+card.targetAmount+"送"+card.bonusAmount+"。</div>"+
            "<button class='btn-primary btn-block' data-type='"+card.id+"' data-price='"+card.price+"' onclick='buyClick(this)'>立即购买</button> "+
            "</div>"
    });
    $("#notMember").append(cardDOMStr);
}

function buyClick(card0) {
    cardKindBuy = card0.getAttribute('data-type');
    cardPriceBuy=card0.getAttribute('data-price');
    clearForm();
    $('#buyModal').modal('show');
    $("#userMember-amount-group").css("display", "none");
    isBuyState = true;
}

function chargeClick() {
    clearForm();
    $('#buyModal').modal('show')
    $("#userMember-amount-group").css("display", "");
    isBuyState = false;
}

function clearForm() {
    $('#userMember-form input').val("");
    $('#userMember-form .form-group').removeClass("has-error");
    $('#userMember-form p').css("visibility", "hidden");
}

function confirmCommit() {
    if (validateForm()) {
        if ($('#userMember-cardNum').val() === "123123123" && $('#userMember-cardPwd').val() === "123123") {
            myState=1;
            if (isBuyState) {
                postRequest(
                    '/vip/add?userId=' + sessionStorage.getItem('id')+'&cardKind='+cardKindBuy,
                    null,
                    function (res) {
                        $('#buyModal').modal('hide');
                        getVIP();
                    },
                    function (error) {
                        alert(error);
                    });
                myType=1;
                //添加买卡的订单
                makeOrder(cardPriceBuy,myType,myState)
            } else {
                cardChargeAmount = parseInt($('#userMember-amount').val());
                postRequest(
                    '/vip/charge',
                    {vipId: vipCardId, amount: cardChargeAmount},
                    function (res) {
                        $('#buyModal').modal('hide');
                        getVIP();
                    },
                    function (error) {
                        alert(error);
                    });
                myType=2;
                //添加充卡的订单
                makeOrder(cardChargeAmount,myType,myState);
            }
        } else {
            alert("银行卡号或密码错误");
        }
    }
}
//稍后支付
function payLater() {
    if (validateForm()) {
        if ($('#userMember-cardNum').val() === "123123123" && $('#userMember-cardPwd').val() === "123123") {
            myState=0;
            if (isBuyState) {
                myType=1;
                //添加买卡的订单
                makeOrder(cardPriceBuy,myType,myState);
            } else {
                cardChargeAmount = parseInt($('#userMember-amount').val());

                myType=2;
                //添加充卡的订单
                makeOrder(cardChargeAmount,myType,myState);
            }
        } else {
            alert("银行卡号或密码错误");
        }
    }
}
function makeOrder(total,type,state) {
    var orderForm={
        user_id:sessionStorage.getItem('id'),
        total:total,
        state:state,
        type:type,
        //新增付款方式
        payMethod:0,
    };
    if (type===1){ //买卡
        orderForm.description=cardKindBuy+"";
    }
    else if (type===2){ //充卡
        orderForm.description=vipCardId;
    }
    postRequest(
        '/order/add',
        orderForm,
        function (res) {
            if(res.success){
                if(state==1){ //已付款
                    $('.member-body').css("display", "none");
                    $('.user-member-success-state').css("display", "");
                    $('#buyModal').modal('hide');

                    function jump(count) {
                        window.setTimeout(function(){
                            count--;
                            if(count > 0) {
                                jump(count);
                            } else {
                                window.location.reload();
                                $('.member-body').css("display", "");
                                $('.user-member-success-state').css("display", "none");

                            }
                        }, 1000);
                    }
                    jump(2);
                }
                else if (state==0){ //未付款
                    $('.member-body').css("display", "none");
                    $('.user-member-unpaid-state').css("display", "");
                    $('#buyModal').modal('hide');

                    function jump(count) {
                        window.setTimeout(function(){
                            count--;
                            if(count > 0) {
                                jump(count);
                            } else {
                                window.location.reload();
                                $('.member-body').css("display", "");
                                $('.user-member-unpaid-state').css("display", "none");

                            }
                        }, 1000);
                    }
                    jump(2);

                }
            }else{
                alert(res.message);
            }
        },
        function (error) {
            alert(error);
        }
    )
}

function validateForm() {
    var isValidate = true;
    if (!$('#userMember-cardNum').val()) {
        isValidate = false;
        $('#userMember-cardNum').parent('.form-group').addClass('has-error');
        $('#userMember-cardNum-error').css("visibility", "visible");
    }
    if (!$('#userMember-cardPwd').val()) {
        isValidate = false;
        $('#userMember-cardPwd').parent('.form-group').addClass('has-error');
        $('#userMember-cardPwd-error').css("visibility", "visible");
    }
    if (!isBuyState && (!$('#userMember-amount').val() || parseInt($('#userMember-amount').val()) <= 0)) {
        isValidate = false;
        $('#userMember-amount').parent('.form-group').addClass('has-error');
        $('#userMember-amount-error').css("visibility", "visible");
    }
    return isValidate;
}

function getCoupon() {
    getRequest(
        '/coupon/' + sessionStorage.getItem('id') + '/get',
        function (res) {
            if (res.success) {
                var couponList = res.content;
                var couponListContent = '';
                for (let coupon of couponList) {
                    couponListContent += '<div class="col-md-6 coupon-wrapper"><div class="coupon"><div class="content">' +
                        '<div class="col-md-8 left">' +
                        '<div class="name">' +
                        coupon.name +
                        '</div>' +
                        '<div class="description">' +
                        coupon.description +
                        '</div>' +
                        '<div class="price">' +
                        '满' + coupon.targetAmount + '减' + coupon.discountAmount +
                        '</div>' +
                        '</div>' +
                        '<div class="col-md-4 right">' +
                        '<div>有效日期：</div>' +
                        '<div>' + formatDate(coupon.startTime) + ' ~ ' + formatDate(coupon.endTime) + '</div>' +
                        '</div></div></div></div>'
                }
                $('#coupon-list').html(couponListContent);

            }
        },
        function (error) {
            alert(error);
        });
}

function formatDate(date) {
    return date.substring(5, 10).replace("-", ".");
}