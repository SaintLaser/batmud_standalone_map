$(function () {
    console.log('start realm map......')

    var WebSocketUrl = "ws://localhost:10001/location"

    //define the char div size. 基本delta的多少倍？
    var multiple = 3; //

    //init objects
    var $char = $("#char");  //charname div

    var $x = $("#x");
    var $y = $("#y");

    // get env vars
    var bodyWidth = document.body.scrollWidth;
    var bodyHeight = document.body.scrollHeight;

    var windowWidth = $(window).width();
    var windowHeight = $(window).height();

    // listen window resize event
    $(window).resize(function() {
        windowWidth = $(window).width();
        windowHeight = $(window).height();
    });


    // 计算起始位置和单个字符的px增量
    var $placeholder_00 = $("#helploc00");
    var $placeholder_11 = $("#helploc11");

    var startx = $placeholder_00.offset().left;
    var starty = $placeholder_00.offset().top;
    var startx1 = $placeholder_11.offset().left;
    var starty1 = $placeholder_11.offset().top;
    var deltax = startx1 - startx;
    var deltay = starty1 - starty;

    //初始化char div大小
    var charWidth = multiple * deltax;
    var charHeight = multiple * deltay;
    $char.css('width', charWidth + 'px');
    $char.css('height', charHeight + 'px');

    //计算每次对准中心的偏移量
    var dx = (1.0-multiple)*deltax/2;
    var dy = (1.0-multiple)*deltay/2;

    $char.show();

    //移动查内姆到指定的行列
    function move2ColRow(col,row) {
        console.log('start to move ',col,row)
        //计算位置
        var x = startx + (col-1)*deltax + dx;
        var y = starty + (row-1)*deltay + dy;

        //move char to location
        $char.animate({
            left: x + 'px',
            top: y + 'px'
        });

        //scroll 屏幕及边界校验
        var movex = parseInt(x) - windowWidth/2;

        if( movex < 0 )
            movex = 0;

        if( movex > bodyWidth-windowWidth)
            movex = bodyWidth-windowWidth;

        var movey = parseInt(y) - windowHeight/2;

        if( movey < 0 )
            movey = 0;

        if( movey > bodyHeight-windowHeight)
            movey = bodyHeight-windowHeight;

        $("html,body").animate({scrollLeft: movex+"px",scrollTop: movey + "px"}, 500);
    };

    $("#cc").on("click",function () {

        var col =  parseInt($x.val());
        var row =  parseInt($y.val());
        move2ColRow(col,row);
    });

    //luce mapfile 15,397
    var ws = new WebSocket(WebSocketUrl);

    ws.onopen = function () {
        console.log('ws onopen');
        ws.send('i am client ');
    };

    ws.onmessage = function (e) {
        console.log('receive location: ' + e.data);

        //数据格式 Lucentium,397,15
        // 需要转移到 15行，397列
        var datas = e.data.split(",")

        //is this map fit?
        if( datas[0] != map_name ){
            console.log('not this map, jump to' , map_pages[datas[0]])
            if( map_pages[datas[0]] != null){
                window.location = map_pages[datas[0]];
            }
        }

        move2ColRow(parseInt(datas[1]), parseInt(datas[2]));
    };

})