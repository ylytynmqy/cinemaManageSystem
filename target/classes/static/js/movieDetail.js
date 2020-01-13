$(document).ready(function(){

    var movieId = parseInt(window.location.href.split('?')[1].split('&')[0].split('=')[1]);
    var userId = sessionStorage.getItem('id');
    var isLike = false;

    getMovie();
    if(sessionStorage.getItem('role') === "clerk") {
        getMovieLikeChart();

    }

    function getMovieLikeChart() {
       getRequest(
           '/movie/' + movieId + '/like/date',
           function(res){
               var data = res.content,
                    dateArray = [],
                    numberArray = [];
               data.forEach(function (item) {
                   dateArray.push(item.likeTime);
                   numberArray.push(item.likeNum);
               });

               var myChart = echarts.init($("#like-date-chart")[0]);

               // 指定图表的配置项和数据
               var option = {
                   title: {
                       text: '想看人数变化表'
                   },
                   xAxis: {
                       type: 'category',
                       data: dateArray
                   },
                   yAxis: {
                       type: 'value'
                   },
                   series: [{
                       data: numberArray,
                       type: 'line'
                   }]
               };

               // 使用刚指定的配置项和数据显示图表。
               myChart.setOption(option);
           },
           function (error) {
               alert(error);
           }
       );
    }

    function getMovie() {
        getRequest(
            '/movie/'+movieId + '/' + userId,
            function(res){
                var data = res.content;
                isLike = data.islike;
                repaintMovieDetail(data);
            },
            function (error) {
                alert(error);
            }
        );
    }

    function repaintMovieDetail(movie) {
        // isLike ? $('#heart').addClass('error-text') : $('#heart').removeClass('error-text');
        // 设置HTML值
        // $('#like-btn').find("span").eq(1).text(isLike ? ' 已想看' : ' 想 看');
        var button ="";
        if (isLike) {
            $('.nolike').hide();
            $('.like').show();
        } else {
            $('.like').hide();
            $('.nolike').show();
        }
        $('#movie-img').attr('src',movie.posterUrl);
        $('#movie-name').text(movie.name);
        $('#order-movie-name').text(movie.name);
        $('#movie-description').text(movie.description);
        $('#movie-startDate').text(new Date(movie.startDate).toLocaleDateString());
        $('#movie-type').text(movie.type);
        $('#movie-country').text(movie.country);
        $('#movie-language').text(movie.language);
        $('#movie-director').text(movie.director);
        $('#movie-starring').text(movie.starring);
        $('#movie-writer').text(movie.screenWriter);
        $('#movie-length').text(movie.length);

        //已下架电影的修改和删除两个按钮消失
        if(movie.status==1){
            $(".movie-operations").css("display","none");
        }
    }

    // user界面才有
    $('#like-btn').click(function () {
        var url = isLike ?'/movie/'+ movieId +'/unlike?userId='+ userId :'/movie/'+ movieId +'/like?userId='+ userId;
        postRequest(
             url,
            null,
            function (res) {
                 isLike = !isLike;
                getMovie();
            },
            function (error) {
                alert(error);
            });
    });

    // admin界面才有
    $("#modify-btn").click(function () {
       //alert('交给你们啦，修改时需要在对应html文件添加表单然后获取用户输入，提交给后端，别忘记对用户输入进行验证。（可参照添加电影&添加排片&修改排片）');
     /*  var movieform0 = {
            id: movieId,
            name: $('#movie-name').val(),
            startDate: $('#movie-startDate').val(),
            posterUrl: $('#movie-img').val(),
            description: $('#movie-description').val(),
            type: $('#movie-type').val(),
            length: $('#movie-length').val(),
            country: $('#movie-country').val(),
            starring: $('#movie-starring').val(),
            director: $('#movie-director').val(),
            screenWriter: $('#movie-writer').val(),
            language: $('#movie-language').val()
        };
    /*    var movie={};
        getRequest(
            '/movie/'+movieId + '/' + userId,
            function(res){
                var movie = res.content;
            },
            function (error) {
                alert(error);
            }
        );*/
     //   var movie = JSON.parse(this.target.dataset.movie);
       // var movie;
        //var movieform;
        getRequest(
            '/movie/'+movieId + '/' + userId,
            function (res) {
                var movieform=res.content;
                $('#movie-edit-name-input').val(movieform.name.slice(0,16));
                //$('#movie-edit-date-input').val(movieform.startDate.slice(0,16));
                $('#movie-edit-img-input').val(movieform.posterUrl);
                $('#movie-edit-description-input').val(movieform.description);
                $('#movie-edit-type-input').val(movieform.type);
                $('#movie-edit-length-input').val(movieform.length);
                $('#movie-edit-country-input').val(movieform.country);
                $('#movie-edit-star-input').val(movieform.starring);
                $('#movie-edit-director-input').val(movieform.director);
                $('#movie-edit-writer-input').val(movieform.screenWriter);
                $('#movie-edit-language-input').val(movieform.language);
            }
        )

        getSyncRequest(
            '/movie/'+movieId + '/' + userId,
            function (res) {
                var movieform=res.content;
                $('#movie-edit-date-input').val(movieform.startDate.slice(0,16));

            }
        )
        $('#movieEditModal').modal('show');
    });

    $("#movie-form-btn").click(function () {
        var formData = {
            id: movieId,
            name: $('#movie-edit-name-input').val(),
            startDate: $('#movie-edit-date-input').val(),
            posterUrl: $('#movie-edit-img-input').val(),
            description: $('#movie-edit-description-input').val(),
            type: $('#movie-edit-type-input').val(),
            length: $('#movie-edit-length-input').val(),
            country: $('#movie-edit-country-input').val(),
            starring: $('#movie-edit-star-input').val(),
            director: $('#movie-edit-director-input').val(),
            screenWriter: $('#movie-edit-writer-input').val(),
            language: $('#movie-edit-language-input').val()
        };
        //  if(!validateMovieForm(formData)) {
        //    return;
        //  }
        //提交表单
        postRequest(
            '/movie/update',
            formData,
            function (res) {
                if(res.success){
                    getMovie();
                    $("#movieEditModal").modal('hide');
                }
                else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            });

    })
    $("#delete-btn").click(function () {
        //alert('交给你们啦，下架别忘记需要一个确认提示框，也别忘记下架之后要对用户有所提示哦');
   /*     var movieform = {
            id: movieId,
            name: $('#movie-name').val(),
            startDate: $('#movie-startDate').val(),
            posterUrl: $('#movie-img').val(),
            description: $('#movie-description').val(),
            type: $('#movie-type').val(),
            length: $('#movie-length').val(),
            country: $('#movie-country').val(),
            starring: $('#movie-starring').val(),
            director: $('#movie-director').val(),
            screenWriter: $('#movie-writer').val(),
            language: $('#movie-language').val(),
            status:0
        }
     //   var movieform = movieList[movieId];
      //  var movieform = JSON.parse(e.target.dataset.movie);
     /*   var formData = {
            id: movieId,
            name: $('#movie-edit-name-input').val(),
            startDate: $('#movie-edit-date-input').val(),
            posterUrl: $('#movie-edit-img-input').val(),
            description: $('#movie-edit-description-input').val(),
            type: $('#movie-edit-type-input').val(),
            length: $('#movie-edit-length-input').val(),
            country: $('#movie-edit-country-input').val(),
            starring: $('#movie-edit-star-input').val(),
            director: $('#movie-edit-director-input').val(),
            screenWriter: $('#movie-edit-writer-input').val(),
            language: $('#movie-edit-language-input').val(),
            //status:movieList[movieId].status
        };*/

        var r=confirm("确认要下架该电影吗")
        if (r) {
            //alert('开始post');
            var idList = [movieId];
            var movieform={
                movieIdList:idList
            };
   /*         getRequest(
                '/movie/'+movieId + '/' + userId,
                function (res) {
                    var movie=res.content;
                    movie.status=1;
                    alert('status=1');
                    movieform=movie;
                },
                function (error) {
                    alert(JSON.stringify(error));
                }
            );*/

            postRequest(
                '/movie/off/batch',
                movieform,
                function (res) {
                    if(res.success){
                        //getMovie();
                        alert('下架成功');
                    }
                    else{
                        alert(res.message);
                    }
                },
                function (error) {
                    alert(JSON.stringify(error));
                });

        };
    });

    //修改和删除两个按钮，下架之后消失

});