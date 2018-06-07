<?php if (!defined('THINK_PATH')) exit(); /*a:1:{s:75:"C:\wamp\www\test\phpServer\public/../application/index\view\index\edit.html";i:1528363202;}*/ ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

</head>
<style>
    input[name="radio"]{
        vertical-align: center;
    }
</style>
<body style="background: #232323">


<div style="width: 100%">
    <div id="radioDiv" style=" float: left ;; background: #4288ce ; height: auto ; width: 30%;"  >

        <?php if(is_array($fence) || $fence instanceof \think\Collection || $fence instanceof \think\Paginator): $i = 0; $__LIST__ = $fence;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$data): $mod = ($i % 2 );++$i;?>
        <div style="float:top;   " id="div<?php echo $data['id']; ?>" >
            <button style="size: A3 ; width: auto  " value="<?php echo $data['id']; ?>" class="deleteBtn" > 删除这个愚蠢的radio </button>
            <input type="radio" name="fence" value=<?php echo $data['fence_array']; ?> id=<?php echo $data['id']; ?> operation="origin"  >fence<?php echo $data['id']; ?></input>
        </div>

        <?php endforeach; endif; else: echo "" ;endif; ?>



    </div>
    <div style="float: right;width: 70%;min-width: 350px; height: 100% ;overflow: hidden;position: relative">
        <div style="width:100%;height: 500px;overflow: hidden;margin:0;"  id="container">

        </div>

        <div class="button-group" style="width: 100%">
            <input type="button" class="button btn" value="鼠标绘制面" id="polygon" style="width: 120px;margin-left:30px;position: relative;top: -60px"/>
        </div>

        <div style="float: left" >
            <div>
                <button style="size: A3 ; width: auto  " id="submit" > 确认修改 </button>
                <button style="size: A3 ; width: auto ; left: 10% " id="addRadio" > 增加radio </button>

            </div>

        </div>

        <div style="float: right" >
            <div id="tip" style="color: #4288ce;" ></div>

        </div>

    </div>
</div>


</body>
<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js">
</script>
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=cab3c2ad396c9b5a4598f2104750ff48&plugin=AMap.DistrictSearch,AMap.MouseTool"></script>

<script  record = "detail" >


    function writeObj(obj){
        var description = "";
        for(var i in obj){
            var property=obj[i];
            description+=i+" = "+property+"\n";
        }
        alert(description);
    }
</script>
<script>
    var recordPolygons = [];
    var map = new AMap.Map("container", {

        resizeEnable: true,

    });


    map.plugin('AMap.Geolocation', function() {
        geolocation = new AMap.Geolocation({
            enableHighAccuracy: true,//是否使用高精度定位，默认:true
            timeout: 10000,          //超过10秒后停止定位，默认：无穷大
            buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
            zoomToAccuracy: true,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
            buttonPosition:'RB'
        });
        map.addControl(geolocation);
        geolocation.getCurrentPosition();
        AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
        AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
    });
    //解析定位结果
    function onComplete(data) {
        var str=['定位成功'];
        str.push('经度：' + data.position.getLng());
        str.push('纬度：' + data.position.getLat());
        if(data.accuracy){
            str.push('精度：' + data.accuracy + ' 米');
        }//如为IP精确定位结果则没有精度信息
        str.push('是否经过偏移：' + (data.isConverted ? '是' : '否'));
        document.getElementById('tip').innerHTML = str.join('<br>');
    }
    //解析定位错误信息
    function onError(data) {
        document.getElementById('tip').innerHTML = '定位失败';
    }



   //在地图中添加MouseTool插件
    var mouseTool = new AMap.MouseTool(map);
    AMap.event.addDomListener(document.getElementById('polygon'), 'click', function(e) {
        map.clearMap();
        let checked = $("input[name=fence]:checked")[0];

        if(checked === undefined ) {
            alert("please choose radio")
            return;
        }
        mouseTool.polygon();
    }, false);
    AMap.event.addListener( mouseTool,'draw',function(e){ //添加事件
//        alert((e.obj.getPath()));//获取路径/范围
        let checked = $("input[name=fence]:checked")[0];

        var path = e.obj.getPath();
        recordPolygons = JSON.stringify(path);
        console.log(recordPolygons);
        if(checked.value !== JSON.stringify(path)  ){
            checked.value = JSON.stringify(path);
            if(checked.getAttribute("operation")==="origin"){
                checked.setAttribute("operation" , "update")

            }
        }

    });
    var city = "绵阳"






    setTimeout(function () {
        if($("input[name=fence]").get(0)!==undefined){
            $("input[name=fence]").get(0).click();

        }

    } , 30)



</script>

<script>
    function addEffort(){
        $(".deleteBtn").click(function (e) {
            var value = e.target.value;
            var selector = "div" + value;
            document.getElementById(selector).style.display = "none";
            var temp =$("#"+value)
            if(temp[0].getAttribute("operation") === "origin" ){
                temp[0].setAttribute("operation" , "delete");
            }else{
                temp[0].setAttribute("operation" , "useless");

            }

            console.log(temp[0]);
        });
        var polygon
        $("input[name=fence]").each(function(){
            $(this).click(function(e){
                console.log(e.target.value);
                var value = JSON.parse(e.target.value);
                var id = e.target.id;
                AMap.service('AMap.DistrictSearch', function() {
                    var opts = {
                        subdistrict: 1,   //返回下一级行政区
                        extensions: 'all',  //返回行政区边界坐标组等具体信息
                        level: 'city'  //查询行政级别为 市
                    };
                    //实例化DistrictSearch
                    district = new AMap.DistrictSearch(opts);
                    district.setLevel('district');
                    //行政区查询
                    district.search(city, function(status, result) {
                        // var bounds = result.districtList[0].boundaries;
                        var bounds = [];
                        // bounds = [{"M":31.451368308665725,"I":104.54671467578123,"lng":104.546715,"lat":31.451368},{"M":31.338833798081726,"I":104.59065998828123,"lng":104.59066,"lat":31.338834},{"M":31.35759891514965,"I":104.70052326953129,"lng":104.700523,"lat":31.357599},{"M":31.329449835779005,"I":104.85433186328129,"lng":104.854332,"lat":31.32945},{"M":31.376360287779555,"I":104.89827717578123,"lng":104.898277,"lat":31.37636},{"M":31.43262192640987,"I":104.89827717578123,"lng":104.898277,"lat":31.432622},{"M":31.48884981970347,"I":104.81038655078135,"lng":104.810387,"lat":31.48885},{"M":31.51226814095469,"I":104.67855061328129,"lng":104.678551,"lat":31.512268},{"M":31.530998573910583,"I":104.56868733203123,"lng":104.568687,"lat":31.530999},{"M":31.49821785207472,"I":104.48079670703135,"lng":104.480797,"lat":31.498218}];
                        bounds = value;
                        console.log(  bounds);
                        var polygonArr = new Array();//多边形覆盖物节点坐标数组

                        $.each(bounds , function (name , value) {
//                        polygonArr.push([ value.I ,  value.M]);
                            polygonArr.push([ value.lng ,  value.lat]);

                        });

                        if (bounds) {
                            // for (var i = 0, l = bounds.length; i < l; i++)
                            map.clearMap();
                            if(bounds != [] )
                            {
                                //生成行政区划polygon
                                    polygon = new AMap.Polygon({
                                    map: map,
                                    strokeWeight: 11,
                                    path: polygonArr,
                                    fillOpacity: 0.3,
                                    fillColor: '#ff8677',
                                    strokeColor: '#CC66CC'
                                });
                            }else{

                            }

                        }else{
                            polygon.remove()
                            alert()

                        }
                    });
                });


            });
        });


    }

    addEffort()


    var add = 0;
    $("#addRadio").click(function (e) {
        add ++;
        var appendStr = "        <div style=\"float:top;  \" id='" +
            "divadd" + add +
            "' >\n" +
            "            <button style=\"size: A3 ; width: auto  \" value=\"" +
            "add" + add +
            "\" class=\"deleteBtn\" > 删除这个愚蠢的radio </button>\n" +
            "            <input type=\"radio\" name=\"fence\" value='[]' operation='" +
            "add" +
            "' id='" +
            "add" + add +
            "'  >fenceAdd" +
            add +
            "</input>\n" +
            "        </div>"
        $("#radioDiv").append(appendStr)
        addEffort()
    })


    $("#submit").click(function (e) {
        var result = [] , num = 0  ;

        $("input[name=fence]").each(function (e) {
            var temp = this.value;

            console.log( "submit" , this)



            var arr =  [ {
                "fence_array"  : temp ,
                "operation" : this.getAttribute("operation"),
                "id" : this.getAttribute("id"),



            }]
            result[num] = arr;
            num++;
        })
        $.ajax({
            type : "post",
            url: "edit_" ,
            data:{
                "arr" : result
            },
            success:function (e) {
                console.log(e);
                alert("edit successfully")
                location.reload();

            }




        })

    })
</script>

</html>