$(document).ready(function() {

    var canSeeDate = 0;
    var isInteger=true;
    var strategy;

    getCanSeeDayNum();
    getCinemaHalls();
    getRefundStrategy();

    function getCinemaHalls() {
        var halls = [];
        getRequest(
            '/hall/all',
            function (res) {
                halls = res.content;
                renderHall(halls);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    function renderHall(halls){
        $('#hall-card').empty();
        var hallDomStr = "";
        halls.forEach(function (hall) {
            var seat = "";
            for(var i =0;i<hall.row;i++){
                var temp = ""
                for(var j =0;j<hall.column;j++){
                    temp+="<div class='cinema-hall-seat'></div>";
                }
                seat+= "<div>"+temp+"</div>";
            }
            var hallDom =
                "<div class='cinema-hall'>" +
                "<div>" +
                "<span class='cinema-hall-name'>"+ hall.name +"</span>" +
                "<span class='cinema-hall-size'>"+ hall.column +'*'+ hall.row +"</span>" +
                "<div class='movie-operations'>" +
                "<button type='button' class='btn btn-primary modify-btns' id='modify-btn'"+
                " data-hid='"+hall.id+"'"+
                " data-hname='"+hall.name+"'"+
                " data-hrow='"+hall.row+"'"+
                " data-hcolumn='"+hall.column+"'"+
                " onclick='editHall(this)'><span>修改影厅规模</span></button>" +
                "<script>" +
                "function editHall(hall0){"+
                "$('#edit-hall-name-input').val(hall0.getAttribute('data-hname'));"+
                "$('#edit-hall-row-input').val(hall0.getAttribute('data-hrow'));"+
                "$('#edit-hall-column-input').val(hall0.getAttribute('data-hcolumn'));"+
                "$('#editHallModal').modal('show');"+
                "$('#edit-hall-form-btn').click(function () {"+
                "var hall={"+
                "id:hall0.getAttribute('data-hid'),"+
                "name:$('#edit-hall-name-input').val(),"+
                "column:$('#edit-hall-column-input').val(),"+
                "row:$('#edit-hall-row-input').val()"+
                "};"+

                "postRequest("+
                "'/hall/update',"+
                "hall,"+
                "function (res) {"+
                "if(res.success){"+
                "window.location.reload();"+
                "$('#editHallModal').modal('hide');"+
                "}else{"+
                "alert(res.message);"+
                "}"+
                "},"+
                "function (error) {"+
                "alert(JSON.stringify(error));"+
                "}"+
                ")"+
                "});"+
                "}"+
                "</script>"+
                "<button type='button' class='btn btn-danger' id='delete-btn-"+hall.id+"' onclick='deleteHall()'><span>删除影厅</span></button>" +
                "<script>" +
                "function deleteHall() {"+
                " var r=confirm(\"确认要删除该影厅信息吗\");"+
                "if (r) {"+
                "deleteRequest("+
                "'/hall/delete',"+
                hall.id+","+
                "function (res) {"+
                "if(res.success){"+
                "window.location.reload();"+
                "} else{"+
                "alert(res.message);"+
                "}"+
                "},"+
                "function (error) {"+
                "alert(JSON.stringify(error));"+
                "}"+
                ");"+
                "}"+
                "}"+

                "</script>"+
                "</div>"+
                "</div>" +
                "<div class='cinema-seat'>" + seat +
                "</div>" +
                "</div>";
            hallDomStr+=hallDom;
        });
        $('#hall-card').append(hallDomStr);
    }

    //TODO:点击新增影厅按钮
    $("#addHall").click(function () {
        $('#hall-name-input').val("");
        $('#hall-column-input').val("");
        $('#hall-row-input').val("");
        $("#hallModal").modal('show');
    });
    //需要post方法，在数据库中增加一条影厅信息
    $("#hall-form-btn").click(function () {

        var hall={
            name: $('#hall-name-input').val(),
            column: $('#hall-column-input').val(),
            row: $('#hall-row-input').val()
        }
        postRequest(
            '/hall/add',
            hall,
            function (res) {
                if(res.success){
                    getCinemaHalls();
                    $("#hallModal").modal("hide");
                }else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )
    });
    var hallId;
    //todo: 编辑影厅信息

    function getCanSeeDayNum() {
        getRequest(
            '/schedule/view',
            function (res) {
                canSeeDate = res.content;
                $("#can-see-num").text(canSeeDate);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    $('#canview-modify-btn').click(function () {
        $("#canview-modify-btn").hide();
        $("#canview-set-input").val(canSeeDate);
        $("#canview-set-input").show();
        $("#canview-confirm-btn").show();
    });

    $('#canview-confirm-btn').click(function () {
        var dayNum = $("#canview-set-input").val();
        // 验证一下是否为数字
        postRequest(
            '/schedule/view/set',
            {day:dayNum},
            function (res) {
                if(res.success){
                    getCanSeeDayNum();
                    canSeeDate = dayNum;
                    $("#canview-modify-btn").show();
                    $("#canview-set-input").hide();
                    $("#canview-confirm-btn").hide();
                } else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    })

    //TODO:验证输入的天数是否是数字

    function getRefundStrategy() {
        getRequest(
            '/ticket/get/refundStrategy',
            function (res) {
                if(res.content===null){
                    $("#unsetStrategy").css('display','');
                    $("#refundStrategy").css('display','none');
                }else {
                    $("#unsetStrategy").css('display','none');
                    $("#refundStrategy").css('display','');
                    strategy = res.content;
                    $("#can-refund-num").text(res.content.time);
                    $("#refund-percent").text(strategy.percent*100);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }
    var refundStrategy;
    $("#strategy-modify-btn").click(function () {
        $("#edit-time-input").val(strategy.time);
        $("#edit-percent-input").val(strategy.percent);
        $("#editStrategyModal").modal('show');
    });
    $("#confirm-strategy-btn").click(function () {
        refundStrategy={
            id:strategy.id,
            time:$("#edit-time-input").val(),
            percent:$("#edit-percent-input").val()
        };
        postRequest('/ticket/refundStrategy/set',
            refundStrategy,
            function (res) {
                if(res.success) {
                    //发送消息
                    var message = "退票策略修改通知：电影开场前"+refundStrategy.time+"分钟内恕不退票。退票额度为+"+refundStrategy.percent*100+"%";
                    postRequest(
                        '/message/add',
                        {description:message},
                        function (res) {
                            if(!res.success){
                                alert(res.message);
                            }
                        },
                        function (error) {
                            alert(error)
                        }
                    )
                    getRefundStrategy();
                    $("#editStrategyModal").modal('hide')
                }else{
                    alert(res.messages);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    });

    //增加策略
    $("#add-strategy-btn").click(function () {
        $('#addStrategyModal').modal('show');
    });
    $("#add-confirm-strategy-btn").click(function () {

        var dataform={
            time:$("#add-time-input").val(),
            percent: $("#add-percent-input").val()
        }
        postRequest('/ticket/refundStrategy/set',
            dataform,
            function (res) {
                if(res.success) {
                    //发送消息
                    var message = "退票策略修改通知：电影开场前"+refundStrategy.time+"分钟内恕不退票。退票额度为"+refundStrategy.percent*100+"%";
                    postRequest(
                        '/message/add',
                        {description:message},
                        function (res) {
                            if(!res.success){
                                alert(res.message);
                            }
                        },
                        function (error) {
                            alert(error)
                        }
                    )
                    getRefundStrategy();
                    $("#editStrategyModal").modal('hide');
                    window.location.reload();
                }else{
                    alert(res.messages);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    });

});