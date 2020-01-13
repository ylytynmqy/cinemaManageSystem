var selectedSeats = [];
var order = {ticketId: [], couponId: 0};
var coupons = [];
var isVIP = false;
var useVIP = true;
var movieId;
var userId;//int
var scheduleId;//int
var seats=[];//seatsform
var total;
var fare;
var actualTotal;
$(document).ready(function () {
    scheduleId = parseInt(window.location.href.split('?')[1].split('&')[1].split('=')[1]);
    movieId = parseInt(window.location.href.split('?')[1].split('&')[1].split('=')[0]);
    getInfo();
    function getInfo() {
        getRequest(
            '/ticket/get/occupiedSeats?scheduleId=' + scheduleId,
            function (res) {//res:ScheduleWithSeatVO
                if (res.success) {
                    this.seats=res.content.seats;
                    renderSchedule(res.content.scheduleItem, res.content.seats);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }
});

function renderSchedule(schedule, seats) {
    $('#schedule-movie-name').text(schedule.movieName);
    $('#order-movie-name').text(schedule.movieName);
    //影厅
    $('#schedule-hall-name').text(schedule.hallName);
    $('#order-schedule-hall-name').text(schedule.hallName);
    //票价
    this.fare=schedule.fare.toFixed(2);
    $('#schedule-fare').text(schedule.fare.toFixed(2));
    $('#order-schedule-fare').text(schedule.fare.toFixed(2));
    //时间
    $('#schedule-time').text(schedule.startTime.substring(5, 7) + "月" + schedule.startTime.substring(8, 10) + "日 " + schedule.startTime.substring(11, 16) + "场");
    $('#order-schedule-time').text(schedule.startTime.substring(5, 7) + "月" + schedule.startTime.substring(8, 10) + "日 " + schedule.startTime.substring(11, 16) + "场");

    //显示座位
    var hallDomStr = "";
    var seat = "";
    for (var i = 0; i < seats.length; i++) {
        var temp = "";
        for (var j = 0; j < seats[i].length; j++) {
            var id = "seat" + i + j;

            if (seats[i][j] == 0) {
                // 未选
                temp += "<button class='cinema-hall-seat-choose' id='" + id + "' onclick='seatClick(\"" + id + "\"," + i + "," + j + ")'></button>";
            } else {
                // 已选中
                temp += "<button class='cinema-hall-seat-lock'></button>";
            }
        }
        seat += "<div>" + temp + "</div>";
    }
    var hallDom =
        "<div class='cinema-hall'>" +
        "<div>" +
        "<span class='cinema-hall-name'>" + schedule.hallName + "</span>" +
        "<span class='cinema-hall-size'>" + seats.length + '*' + seats[0].length + "</span>" +
        "</div>" +
        "<div class='cinema-seat'>" + seat +
        "</div>" +
        "</div>";
    hallDomStr += hallDom;//这个是座位表

    $('#hall-card').html(hallDomStr);
}

function seatClick(id, i, j) {
    var seat = $('#' + id);
    if (seat.hasClass("cinema-hall-seat-choose")) {
        seat.removeClass("cinema-hall-seat-choose");
        seat.addClass("cinema-hall-seat");

        selectedSeats[selectedSeats.length] = [i, j]
    }
    else {
        seat.removeClass("cinema-hall-seat");
        seat.addClass("cinema-hall-seat-choose");

        selectedSeats = selectedSeats.filter(function (value) {
            return value[0] != i || value[1] != j;
        })
    }

//给他们排序
    selectedSeats.sort(function (x, y) {
        var res = x[0] - y[0];
        return res === 0 ? x[1] - y[1] : res;
    });

    var seatDetailStr = "";
    if (selectedSeats.length == 0) {
        seatDetailStr += "还未选择座位";
        $('#order-confirm-btn').attr("disabled", "disabled")
    } else {
        for (var seatLoc of selectedSeats) {
            seatDetailStr += "<span>" + (seatLoc[0] + 1) + "排" + (seatLoc[1] + 1) + "座</span>";
        }
        $('#order-confirm-btn').removeAttr("disabled");
    }
    $('#seat-detail').html(seatDetailStr);
}

// 这里是1.
function seatConfirmClick() {
    $('#seat-state').css("display", "none");
    $('#order-state').css("display", "");

    //处理addticket
    this.userId=parseInt(sessionStorage.getItem('id'));
    for(let seat of selectedSeats){
        seats[seats.length] = {columnIndex:seat[1],rowIndex:seat[0]}
    }
    let data = {
        userId: userId,
        scheduleId: scheduleId,
        seats: seats
    };
    let url='/ticket/lockSeat';
    postRequest(
        url,
        data,
        function (res) {
            order.ticketId=res.content;
        },
        function (error) {
            alert(JSON.stringify(error));
        });

    //处理total
    let numOfTickets=selectedSeats.length;
    total=numOfTickets * fare;
    total=total.toFixed(2);

    //处理coupon
    getSyncRequest(
        '/coupon/' + sessionStorage.getItem('id') + '/get',
        function (res) {
            for (let one of res.content){
                if(one.targetAmount<=total){
                    coupons.push({
                        "id": one.id,
                        "targetAmount":  one.targetAmount,
                        "discountAmount":  one.discountAmount,
                        "startTime": one.startTime,
                        "endTime": one.endTime
                    });
                }
            }
        },
        function (error) {alert(error);}
    );
    renderOrder();
    //获得vip
    getRequest(
        '/vip/' + sessionStorage.getItem('id') + '/get',
        function (res) {
            isVIP = res.success;
            useVIP = res.success;
            if (isVIP) {
                getRequest(
                    '/vip/card/get/'+res.content.type,
                    function (res) {
                        //  alert(res.content.percent);
                        if(res.success){
                            $("#member-discount").html("<div><b>会员折扣：</b>"+res.content.percent+"</div>");
                        }else{
                            alert(res.message);
                        }
                    },
                    function (error) {
                        alert(error);
                    }
                )
                $('#member-balance').html("<div><b>会员卡余额：</b>" + res.content.balance.toFixed(2) + "元</div>");
            } else {
                $("#member-pay").css("display", "none");
                $("#nonmember-pay").addClass("active");

                $("#modal-body-member").css("display", "none");
                $("#modal-body-nonmember").css("display", "");
            }
        },
        function (error) {alert(error);}
    );

}//锁座
function switchPay(type) {
    useVIP = (type == 0);
    if (type == 0) {
        $("#member-pay").addClass("active");
        $("#nonmember-pay").removeClass("active");

        $("#modal-body-member").css("display", "");
        $("#modal-body-nonmember").css("display", "none");
    } else {
        $("#member-pay").removeClass("active");
        $("#nonmember-pay").addClass("active");

        $("#modal-body-member").css("display", "none");
        $("#modal-body-nonmember").css("display", "");
    }
}

function renderOrder() {
    var ticketStr = "<div>" + selectedSeats.length + "张</div>";

    for (let seat of selectedSeats) {
        ticketStr += "<div>" + (seat[0] + 1) + "排" + (seat[1] + 1) + "座</div>";
    }
    $('#order-tickets').html(ticketStr);
    $('#order-total').text(total);
    $('#order-footer-total').text("总金额： ¥" + total);


    var couponTicketStr = "";
    if (coupons.length === 0) {
        $('#order-discount').text("优惠金额：无");
        $('#order-actual-total').text(" ¥" + total);
        $('#pay-amount').html("<div><b>金额：</b>" + total + "元</div>");
        order.couponId=-1;
    } else {
        for (let coupon of coupons) {
            couponTicketStr += "<option>满" + coupon.targetAmount + "减" + coupon.discountAmount + "</option>"
        }
        $('#order-coupons').html(couponTicketStr);
        changeCoupon(0);
    }
}

//这里是2.确认订单支付，选择优惠劵，序号
function changeCoupon(couponIndex) {
    order.couponId = coupons[couponIndex].id;
    $('#order-discount').text("优惠金额： ¥" + coupons[couponIndex].discountAmount.toFixed(2));
    actualTotal = (parseFloat($('#order-total').text()) - parseFloat(coupons[couponIndex].discountAmount)).toFixed(2);
    $('#order-actual-total').text(" ¥" + actualTotal);
    $('#pay-amount').html("<div><b>金额：</b>" + actualTotal + "元</div>");
}

function cancelClick() {
    postRequest(
        "/ticket/cancel?ticketId="+order.ticketId,
        null,
        function (res) {
            $('#order-state').css("display", "none");//隐藏order页面
            $('#cancel-state').css("display", "");

            function jump(count) {
                window.setTimeout(function(){
                    count--;
                    if(count > 0) {
                        jump(count);
                    } else {
                        window.location.reload();
                        $('#seat-state').css("display", "");//回到选座页面
                        $('#cancel-state').css("display", "none");

                    }
                }, 1000);
            }
            jump(2);
        },
        function (error) {
            alert(JSON.stringify(error));
        })
}

function orderConfirmClick() {
    $("#userBuy-cardNum").val("");
    $("#userBuy-cardPwd").val("");
    $('#buyModal').modal('show');
}

function payConfirmClick() {
    if (useVIP) {
        postPayRequest();
    } else {
        if (validateForm()) {
            if ($('#userBuy-cardNum').val() === "123123123" && $('#userBuy-cardPwd').val() === "123123") {
                postPayRequest();
            } else {
                alert("银行卡号或密码错误");
            }
        }
    }
}

function unpaidClick() {
    var oneticket=order.ticketId[0];
    getRequest('/ticket/get/one/ticket/'+oneticket,
        function (res) {
            var time=res.content.time;
            //
            var payMethod = 0;
            if(useVIP){
                payMethod=1;
            }
            //
            let data = {
                user_id:sessionStorage.getItem('id'),
                total:total,
                state:0,
                type:0,
                description:order.ticketId.join(","),
                join_time:time,
                //新增付款方式
                payMethod: payMethod,
            };
            postRequest(
                '/order/add',
                data,
                function (res) {
                    $('#order-state').css("display", "none");//隐藏order页面
                    $('#unpaid-state').css("display", "");//稍后支付
                    $('#buyModal').modal('hide')
                },
                function (error) {
                    alert(error);
                }
            );
        },
        function (error) {
            alert("unpaidclick"+error);
        }
    )
}

function postPayRequest() {
    if(useVIP){
        postRequest(
            '/ticket/vip/buy?ticketId='+order.ticketId+'&couponId='+order.couponId,
            null,
            function (res) {
                if(res.success){
                    addOrder();
                }else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }

        );

    }
    else{
        postRequest(
            '/ticket/buy?ticketId='+order.ticketId+'&couponId='+order.couponId,
            null,
            function (res) {
                if(res.success) {
                    addOrder();
                }else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }
}
function addOrder() {
    var oneticket = order.ticketId[0];
    getRequest('/ticket/get/one/ticket/' + oneticket,
        function (res) {
            var time = res.content.time;
            var paymethod=0;
            if(useVIP){
                paymethod=1;
            }
            let data = {
                user_id:sessionStorage.getItem('id'),
                total:total,
                state:1,
                type:0,
                description:order.ticketId.join(","),
                join_time:time,
                payMethod:paymethod
            };
            postRequest(
                '/order/add',
                data,
                function (res) {
                    $('#order-state').css("display", "none");//隐藏order页面
                    $('#success-state').css("display", "");//支付成功
                    $('#buyModal').modal('hide')
                },
                function (error) {
                    alert(error);
                }
            );
        },
        function (error) {
            alert(error);
        }
    )
}
function validateForm() {
    var isValidate = true;
    if (!$('#userBuy-cardNum').val()) {
        isValidate = false;
        $('#userBuy-cardNum').parent('.form-group').addClass('has-error');
        $('#userBuy-cardNum-error').css("visibility", "visible");
    }
    if (!$('#userBuy-cardPwd').val()) {
        isValidate = false;
        $('#userBuy-cardPwd').parent('.form-group').addClass('has-error');
        $('#userBuy-cardPwd-error').css("visibility", "visible");
    }
    return isValidate;
}
