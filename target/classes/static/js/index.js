$(document).ready(function () {

    $("#login-btn").click(function () {
        var formData = getLoginForm();
        if (!validateLoginForm(formData)) {
            return;
        }
        postRequest(
            '/login',
            formData,
            function (res) {
                if (res.success) {
                    sessionStorage.setItem('role',formData.role);
                    sessionStorage.setItem('username', formData.username);
                    sessionStorage.setItem('id', res.content.id);
                    if (formData.role=="admin") {
                        window.location.href = "/admin/promotion/manage"
                    }
                    else if (formData.role=="clerk"){
                        window.location.href="/clerk/movie/manage"
                    }
                    else if (formData.role=="user") {
                        window.location.href = "/user/home"
                        checkMessage();
                    }
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(error);
            });
    });

    function getLoginForm() {
        var info=$("#login-role input[name=role]:checked").val();
        var role="";
        if (info==="管理员"){
            role="admin";
        }
        else if (info==="员工"){
            role="clerk";
        }
        else if (info==="用户"){
            role="user";
        }
        return {
            role:role,
            username: $('#index-name').val(),
            password: $('#index-password').val()
        };
    }

    function validateLoginForm(data) {
        var isValidate = true;
        if (!data.username) {
            isValidate = false;
            $('#index-name').parent('.input-group').addClass('has-error');
            $('#index-name-error').css("visibility", "visible");
        }
        if (!data.password) {
            isValidate = false;
            $('#index-password').parent('.input-group').addClass('has-error');
            $('#index-password-error').css("visibility", "visible");
        }
        if (!data.role) {
            isValidate = false;
            $('#index-role').addClass('has-error');
            $('#index-role-error').css("visibility", "visible");
        }
        return isValidate;
    }
    
});
