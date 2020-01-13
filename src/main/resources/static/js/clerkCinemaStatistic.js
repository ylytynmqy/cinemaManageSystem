$(document).ready(function() {


    getScheduleRate();
    
    getBoxOffice();

    getAudiencePrice();

    getPlacingRate();

    getPolularMovie();

    function getScheduleRate() {
        var date=formatDate(new Date());
        $('#schedule-rate-form-btn').click(function () {
            var date=$("#schedule-rate-date-input").val();

        getRequest(
            '/statistics/scheduleRate?date='+date.replace(/-/g,'/'),
            function (res) {
                var data = res.content||[];
                var tableData = data.map(function (item) {
                   return {
                       value: item.time,
                       name: item.name
                   };
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title : {
                        text: '排片率',
                        subtext: date,
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        x : 'center',
                        y : 'bottom',
                        data:nameList
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true},
                            dataView : {show: true, readOnly: false},
                            magicType : {
                                show: true,
                                type: ['pie', 'funnel']
                            },
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    series : [
                        {
                            name:'面积模式',
                            type:'pie',
                            radius : [30, 110],
                            center : ['50%', '50%'],
                            roseType : 'area',
                            data:tableData
                        }
                    ]
                };
                var scheduleRateChart = echarts.init($("#schedule-rate-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    })}

    function getBoxOffice() {

        getRequest(
            '/statistics/boxOffice/total',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.boxOffice;
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title : {
                        text: '所有电影票房',
                        subtext: '截止至'+new Date().toLocaleDateString(),
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var scheduleRateChart = echarts.init($("#box-office-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }

    function getAudiencePrice() {
        getRequest(
            '/statistics/audience/price',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.price;
                });
                var nameList = data.map(function (item) {
                    return formatDate(new Date(item.date));
                });
                var option = {
                    title : {
                        text: '每日客单价',
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'line'
                    }]
                };
                var scheduleRateChart = echarts.init($("#audience-price-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }

    function getPlacingRate() {
        var date=formatDate(new Date());
        $('#place-rate-form-btn').click(function () {
            var date=$("#place-rate-date-input").val();
            getRequest(
                '/statistics/PlacingRate?date='+date.replace(/-/g,'/'),
                function (res) {
                    var data = res.content||[];
                    var tableData = data.map(function (item) {
                        return {
                            value: item.placingRate,

                        };
                    });
                    var nameList = data.map(function (item) {
                        return item.name;
                    });
                    var option = {
                        title : {
                            text: '上座率',
                            subtext: date,
                            x:'center'
                        },
                        xAxis: {
                            type: 'category',
                            data: nameList
                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [{
                            data: tableData,
                            type: 'bar'
                        }
                        ]
                    };
                    var scheduleRateChart = echarts.init($("#place-rate-container")[0]);
                    scheduleRateChart.setOption(option);
                },
                function (error) {
                    alert(JSON.stringify(error));
                }
            );
        })



        // todo
    }

    function getPolularMovie() {
        var form={
            days:7,
            movieNum:2
        }
        $('#popular-movie-form-btn').click(function () {
            var form = {
                days: $("#popular-movie-days-input").val(),
                movieNum: $("#popular-movie-movieNum-input").val(),
            };


        getRequest(

        '/statistics/popular/movie?days='+form.days+'&movieNum='+form.movieNum,
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.boxoffice;
                });
                var name = data.map(function (item) {
                    return item.movieName;
                });

                var option = {
                    title : {
                        text: '最受欢迎电影',
                        subtext: '截止至'+new Date().toLocaleDateString(),
                        x:'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: name
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var scheduleRateChart = echarts.init($("#popular-movie-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            })



        })


        // todo


};})