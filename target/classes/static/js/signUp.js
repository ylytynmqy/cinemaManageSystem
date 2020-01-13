var veriCode='';
var veriCodeTrue='111';
$(document).ready(function () {
   // getCode();

    $("#signUp-btn").click(function () {
        var formData = getSignUpForm();
        if (!validateSignUpForm(formData)) {
            return;
        }

        postRequest(
            '/register',
            formData,
            function (res) {
                if (res.success) {
                    alert("注册成功");
                    window.location.href = "/index";
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(error);
            });
    });

    /**
     * role:
     * 管理员 admin
     * 员工 clerk
     * 用户 user
     */
    var url='/get/refundStrategy';
    getRequest(
        url,
        function (res) {
           // alert(res.content+"hhhhhh")
            if(res.success){
                alert("get true");
                veriCodeTrue=res.content;
                alert("code true= "+veriCodeTrue)
            }else{
                alert("============="+res.message);
            }

        },
        function(error){
            alert(error);
        });
    $('#signUp-role').change(function() {
        var type=$("#signUp-role input[name=role]:checked").val();
        if (type==='员工'){
            // getSyncRequest(
            //     '/clerk/get/code',
            //     function (res) {
            //         alert("get true");
            //         veriCodeTrue=res.content;
            //         alert("code true= "+veriCodeTrue)
            //     },
            //     function(error){
            //         alert(error);
            //     });

           // getCode();
           //  postRequest(
           //      '/clerk/post/code',
           //      0,
           //      function (res) {
           //          if(res.success){
           //              veriCodeTrue=res.content;
           //          }else{
           //              alert(res.message);
           //          }
           //      },
           //      function (error) {
           //          alert(error);
           //      }
           //  )
            $('#signUp-veri-code-input').css('display','');
        }
        else if (type==='用户'){
            $('#signUp-veri-code-input').css('display','none');
        }
    });

    function getSignUpForm() {
        var info=$("#signUp-role input[name=role]:checked").val();
        var role;
        if (info==="用户"){
            role="user";
        }
        else if (info==="员工") {
            role="clerk";
            veriCode=$('#signUp-veri-code').val();
        }
        return {
            role:role,
            username: $('#signUp-name').val(),
            password: $('#signUp-password').val(),
            secondPassword: $('#signUp-second-password').val()
        };
    }

    function validateSignUpForm(data) {
        var isValidate = true;
        if (!data.username || data.username.length < 4 || data.username.length > 10) {
            isValidate = false;
            $('#signUp-name').parent('.input-group').addClass('has-error');
            $('#signUp-name-error').css("visibility", "visible");
        }
        if (!data.password || data.password.length < 6 || data.password.length > 12) {
            isValidate = false;
            $('#signUp-password').parent('.input-group').addClass('has-error');
            $('#signUp-password-error').css("visibility", "visible");
        }

        if (!data.secondPassword) {
            isValidate = false;
            $('#signUp-second-password').parent('.input-group').addClass('has-error');
            $('#signUp-second-password-error').css("visibility", "visible");
            $('#signUp-second-password-error').text("请再次输入密码");
        } else if (data.secondPassword != data.password) {
            isValidate = false;
            $('#signUp-second-password').parent('.input-group').addClass('has-error');
            $('#signUp-second-password-error').css("visibility", "visible");
            $('#signUp-second-password-error').text("两次输入密码不一致");
        }
        if (!data.role) {
            isValidate = false;
            $('#signUp-role').addClass('has-error');
            $('#signUp-role-error').css("visibility", "visible");
        }

        if (data.role==='clerk') {
            if (veriCodeTrue===null){
                alert('无法注册员工');
                isValidate = false;
            }
            else if (veriCodeTrue!==veriCode){
                // getRequest(
                //     '/clerk/get/code',
                //     function (res) {
                //         if(res.success){
                //             alert("get true");
                //             veriCodeTrue=res.content;
                //             alert("code true= "+veriCodeTrue)
                //         }else{
                //             alert(res.message);
                //         }
                //
                //     },
                //     function(error){
                //         alert(error);
                //     });
                alert('true='+veriCodeTrue);
                alert('verinput='+veriCode);
                isValidate=false;
                $('#signUp-veri-code').addClass('has-error');
                $('#signUp-veri-code-error').css("visibility", "visible");
            }
        }

        return isValidate;
    }
    
});
// function getCode() {
//     getSyncRequest(
//         '/get/refundStrategy',
//         function (res) {
//             if(res.success){
//                 alert("get true");
//                 veriCodeTrue=res.content;
//                 alert("code true= "+veriCodeTrue)
//             }else{
//                 alert("============="+res.message);
//             }
//
//         },
//         function(error){
//             alert(error);
//         });
// }