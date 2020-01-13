$(document).ready(function() {

    getCards();
    getMemberInfo();
    //formatDatas();
    var vipcard;

    function getCards() {
        getRequest(
            '/vip/card/all',
            function (res) {
                if(res.success){
                    renderVipCards(res.content);
                }else{
                    alert(res.messages);
                }
            }
        )
    }
    function renderVipCards(vipCards) {
        $(".content-vip").empty();
        var vipcardsDomStr = "";

        vipCards.forEach(function (vipcard) {
            vipcardsDomStr+=
                "<div class='vip-container card'>" +
                "    <div class='gray-text'>"+vipcard.description+"</div>" +
                "    <div class='gray-text'>"+vipcard.price+"元/张</div> "+
                "    <div class='gray-text'>满"+vipcard.targetAmount+"赠"+vipcard.bonusAmount+"</div>"+
                "    <div class='gray-text'>付款折扣："+vipcard.percent+"</div>"+
                "    <button class='btn btn-light card-edit-btn'"+
                " data-cid='"+vipcard.id+"'"+
                " data-description='"+vipcard.description+
                "' data-price='"+vipcard.price+"'"+
                " data-targetAmount='"+vipcard.targetAmount+"'"+
                " data-bonusAmount='"+vipcard.bonusAmount+"'"+
                " data-percent='"+vipcard.percent+"'"+
                " onclick='editCard(this)'>修改</button>"+
                "    <script>" +
                "function editCard(card0){" +
                "$(\"#edit-member-card-description-input\").val(card0.getAttribute('data-description'));" +
                "        $(\"#edit-member-card-price-input\").val(card0.getAttribute('data-price'));" +
                "        $(\"#edit-member-card-bound-input\").val(card0.getAttribute('data-targetAmount'));" +
                "        $(\"#edit-member-card-bonus-input\").val(card0.getAttribute('data-bonusAmount'));" +
                "        $(\"#edit-member-card-percent-input\").val(card0.getAttribute('data-percent'));" +
                "        $(\"#vipModalEdit\").modal('show');"+
                "$(\"#edit-member-card-form-btn\").click(function (){"+
                "var dataform = {"+
                "id:card0.getAttribute('data-cid'),"+
                "description: $(\"#edit-member-card-description-input\").val(),"+
                "price:$(\"#edit-member-card-price-input\").val(),"+
                "targetAmount:$(\"#edit-member-card-bound-input\").val(),"+
                "bonusAmount:$(\"#edit-member-card-bonus-input\").val(),"+
                "percent:$(\"#edit-member-card-percent-input\").val()"+
                "};"+
                "var message = dataform.description+'已修改，请及时查看。';"+
                "postRequest("+
                "'/vip/card/update',"+
                "dataform,"+
                "function (res) {"+
                "if(res.success){"+
                "postRequest("+
                " '/message/add',"+
                " {description:message},"+
                "function (res) {"+
                "if(!res.success){"+
                "alert(res.message);"+
                "}"+
                "},"+
                "function (error) {"+
                "alert(error);"+
                "}"+
                ");"+
                "window.location.reload();"+
                "$('#vipModalEdit').modal('hide');"+
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
                "    </script>"+
                "</div>";
        });
        $(".content-vip").append(vipcardsDomStr);
    }

    $("#addCardKind").click(function () {
        $("#member-card-description-input").val("");
        $("#member-card-price-input").val("");
        $("#member-card-bound-input").val("");
        $("#member-card-bonus-input").val("");
        $("#member-card-percent-input").val("");
        $("#vipModal").modal('show');
    });
    //增加，确认
    $("#member-card-form-btn").click(function () {
        var form = {
            description: $("#member-card-description-input").val(),
            price:$("#member-card-price-input").val(),
            targetAmount:$("#member-card-bound-input").val(),
            bonusAmount:$("#member-card-bonus-input").val(),
            percent:$("#member-card-percent-input").val()
        };
        postRequest(
            '/vip/card/publish',
            form,
            function (res) {
                if(res.success){
                    getCards();
                    $("#vipModal").modal('hide');
                    //发消息
                    postRequest(
                        '/message/add',
                        {description:"新的会员卡上线了"},
                        function (res) {
                            if(!res.success){
                                alert(res.message)
                            }
                        },
                        function (error) {
                            alert(error);
                        }
                    )
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );

    });
    /*
        function getAllMemberInfo() {
            getRequest(
                '/vip/memberInfo',
                function (res) {
                    if(res.success){
                        renderMemberInfo(res.content);
                    }else{
                        alert(res.messages);
                    }
                },
                function (error) {
                    alert(error);
                }
            )
        }
        function renderMemberInfo(members) {
            var MemberDOMs = "";
            members.forEach(function (member) {
                MemberDOMs+="<tr>"+
                    "<td>"+member.userId+"</td>"+
                    "<td>"+member.joinTime.substring(0,10)+" "+member.joinTime.substring(11,19)+"</td>"+
                    "<td>"+member.total+"</td>"+
                    "</tr>";
            });
            $("#member").html(MemberDOMs);
        }
     */
    function getMemberInfo(){
        getRequest(
            '/vip/memberInfo',
            function (res) {
                if(res.success){
                    $("#member").bootstrapTable({
                        //method:'GET',
                        //sortable: true,
                        //sortOrder: "asc",
                        uniqueId: "userId",
                        columns:[{
                            field:'userId',
                            title:'会员id'
                        },{
                            field:'joinTime',
                            title:'入会时间',
                            sortable:true

                        },{
                            field:'total',
                            title:'消费总额',
                            sortable:true
                        },{
                            field:'Button',
                            title:'操作',
                            events:operateEvents,
                            formatter: function (value, row, index) {
                                var userId0 = row.userId;
                                //alert(userId0);
                                var btnDOM=""
                                btnDOM="<button id='couponAdd' class='btn btn-light' value='"+userId0+"'>赠送优惠券</button>";
                                // alert(userIdList[point]);
                                // point=point+1;
                                return btnDOM;
                            }
                        }],
                        data:formatDatas(res.content)
                    })
                }else{
                    alert(res.messages);
                }
            },
            function (error) {
                alert(error);
            }
        )
    }
    function formatDatas(datas) {
        var formatData=[];
        datas.forEach(function (data) {
            var form={
                userId:data.userId,
                joinTime:data.joinTime.substring(0,10)+" "+data.joinTime.substring(11,19),
                total:data.total
            };
            formatData.push(form);
        });
        return formatData;
    }
    /*
    function AddFunction(){
        var btnDOM=""
        btnDOM="<button id='couponAdd' class='btn btn-default' value='"+userIdList[point]+"'>赠送优惠券</button>";
       // alert(userIdList[point]);
        point=point+1;
        return btnDOM;
    }*/
    var selectedCouponId=new Set();
    window.operateEvents = {
        "click #couponAdd":function (e, value, row, index) {
            //用户的id
            var userId0 = row.userId;
            //todo:获得所有在有效期内的优惠券
            getRequest(
                '/coupon/get/unexpired',
                function (res) {
                    if(res.success) {
                        alert('get coupon unexpired')
                        var coupons = res.content;
                        var couponsDOMs = "";
                        coupons.forEach(function (coupon) {
                            couponsDOMs += "<label><input name='coupon' type='checkbox' id='" + coupon.id + "' value='" + userId0 + "'/><span>" + coupon.description + "</span></label>";
                        });
                        $("#select-coupon-input").html(couponsDOMs);
                        $("#couponAddModal").modal('show');

                        //
                        $("#select-coupon-input").change(function () {
                            selectedCouponId.clear();
                            $("input:checkbox[name = 'coupon']:checked").each(function(){
                                //  alert($(this).attr('value'));
                                selectedCouponId.add($(this).attr('id'));
                            })
                        });

                        //选择优惠券的确认
                    }else{
                        alert(res.messages);
                    }
                },
                function (error) {
                    alert(error);
                }
            )
            $("#send-coupon-form-btn").click(function () {
                //todo:获得被点击的用户id，优惠券ID（列表）
                selectedCouponId.forEach(function (couponId) {
                    sendCoupon(couponId,userId0);
                })
                $("#couponAddModal").modal('hide');
            });
        }
    }
    /*
    $("#select-coupon-input").change(function () {

    });
    //确认
        $("#send-coupon-form-btn").click(function () {
            //todo:获得被点击的用户id，优惠券ID（列表）

        });
    */

    function sendCoupon(couponId,userId) {
        postRequest(
            '/coupon/send?couponId='+couponId+"&userId="+userId,
            null,
            function (res) {
                if(!res.success){
                    alert(res.messages);
                }else{
                    alert("赠送优惠券成功");
                }
            },
            function (error) {
                alert(error);
            }
        )
    }
});