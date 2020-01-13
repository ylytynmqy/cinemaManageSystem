$(document).ready(function () {

    getAllMessages();

    function getAllMessages() {
        getRequest(
            '/message/get/all',
            function (res) {
                if(res.success){
                    renderMessages(res.content);
                    //更新所有消息状态为已读
                    postRequest(
                        '/message/set/read',
                        null,
                        function (res) {
                            if(!res.success){
                                alert(res.message);
                            }
                        },
                        function (error) {
                            alert(error);
                        }
                    )
                }else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(error);
            }
        )
    }

    function renderMessages(messageList) {
        messageList=messageList.reverse();
        var messageDOMs = "";
        messageList.forEach(function (message0) {
            messageDOMs+=
                "<div class='card'>"+
                "<div class='card-body'>"+
                "<div class='card-title'>"+message0.time.toString().substring(0,10)+" "+message0.time.toString().substring(11,19)+"</div>"+
                "<div class='card-text'>"+message0.description+"</div>"+
                "</div>"+
                "</div>";
        })
        $("#message-list").html(messageDOMs);
    }

});