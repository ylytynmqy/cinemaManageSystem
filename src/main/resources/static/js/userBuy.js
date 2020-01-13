var start;
var end;
var sid;
var movieName;
var hallId;
var isVIP = false;
var useVIP = true;
$(document).ready(function () {
    getMovieList();

    function getMovieList() {
        getRequest(
            '/ticket/get/' + sessionStorage.getItem('id')
            ,
            function (res) {
                renderTicketList(res.content);
            },
            function (error) {
                alert(error);
            });
    }
    // TODO:填空
    function renderTicketList(list) {
        var bodyContent="";
        list=list.reverse();
        for (var i = 0; i<list.length; i++) {
            sid=list[i].scheduleId;
            getSyncRequest(
                '/schedule/'+ sid,
                function (res) {
                    schedule=res.content;
                    start=schedule.startTime.toString();
                    end=schedule.endTime.toString();
                    start=start.substring(0,10)+" "+start.substring(11,19);
                    end=end.substring(0,10)+" "+end.substring(11,19);
                    movieName=schedule.movieName;
                    hallId=schedule.hallId;
                },
                function (error) {
                    alert(error);
                });
            //state
            state=list[i].state;
            stateStr='';
            if (state===0)
                stateStr+='未付款';
            else if (state===1)
                stateStr+='已付款';
            else if (state===2)
                stateStr+='已失效';
            else if (state===3)
                stateStr+='已观影';

            // 拼接
            bodyContent += "<tr>" +
                "<td>" + (movieName)+ "</td>" +
                "<td>" + (hallId) + "号厅</td>" +
                "<td>" + (list[i].columnIndex+1) + '排' + (list[i].rowIndex+1) + '座' + "</td>"+
                "<td>" + start+ "</td>" +
                "<td>"+ end +"</td>" +
                "<td>" + (stateStr) + "</td>";
            bodyContent+="</tr>";
        }

        $('#ticket').html(bodyContent);
    }

});


