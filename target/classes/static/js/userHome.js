$(document).ready(function () {
    getMessages();
    getTopMovies();

    function getMessages() {
        getRequest(
            '/message/get/all',
            function (res) {
                if(res.success){
                    var messageList = res.content;
                    messageList=messageList.reverse();
                    var allRead = true;
                    if(messageList.length==0){
                        allRead=true;
                    }else{
                        if(messageList[0].beenRead==0){
                            allRead=false; //有未读
                        }else{
                            allRead=true;
                        }
                    }
                    if(allRead){
                        $(".alert").css("display","none");
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

    function getTopMovies() {
        getRequest(
            '/statistics/boxOffice/total',
            function (res) {
                if(res.success){
                    var boxOfficeList = res.content;
                    //默认影片数大于3
                    // alert("fffffff");
                    renderTop(res.content);
                }else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(error);
            }
        )
    }
    function renderTop(movieList) {
        for(i=0;i<3;i++) {
            var movieId = movieList[i].movieId;
            var posterId;
            var nameId;
            var accId;
            var hrefValue="";
            //获得电影名和海报
            getSyncRequest(
                '/movie/'+movieId+'/'+1,
                function (res) {
                    //     alert("rrrrrrrrr")
                    if(res.success){
                        posterId="poster"+i;
                        nameId = "name"+i;
                        accId = "acc"+i;
                        hrefValue +="/user/movieDetail?id="+movieId;
                        // alert(nameId)
                        ///alert(res.content.posterUrl);
                        $("#"+posterId).prop("src",res.content.posterUrl);
                        $("#"+accId).attr("href",hrefValue);
                        $("#"+nameId).text(res.content.name);
                    }else{
                        alert(res.message);
                    }
                },
                function (error) {
                    alert(error);
                }
            )
        };
    }
});
