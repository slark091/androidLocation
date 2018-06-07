<?php if (!defined('THINK_PATH')) exit(); /*a:1:{s:76:"C:\wamp\www\test\phpServer\public/../application/index\view\index\index.html";i:1528281619;}*/ ?>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>浏览器定位</title>
    <link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=b5713c7bcc8fe27fad9a40e32a29768d"></script>
    <script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
<body>
<div id='container'></div>
<div id="tip"></div>

<script src="http://cdn.static.runoob.com/libs/jquery/1.10.2/jquery.min.js">

</script>


<script type="text/javascript">
    var map, geolocation;
    //加载地图，调用浏览器定位服务
    map = new AMap.Map('container', {
        resizeEnable: true
    });
    map.plugin('AMap.Geolocation', function() {
        geolocation = new AMap.Geolocation({
            enableHighAccuracy: true,//是否使用高精度定位，默认:true
            timeout: 10000,          //超过10秒后停止定位，默认：无穷大
            buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
            zoomToAccuracy: true,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
            buttonPosition:'RB',
            useNative:true
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
        str.push('精度：' + data.accuracy + ' 米');
        str.push('是否经过偏移：' + (data.isConverted ? '是' : '否'));
        document.getElementById('tip').innerHTML = str.join('<br>');
    }
    //解析定位错误信息
    function onError(data) {
        document.getElementById('tip').innerHTML = '定位失败';
    }
</script>



<script>
    function addBeiJing(city) {
        //加载行政区划插件
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
                var polygons = [];
                bounds = [{"M":31.451368308665725,"I":104.54671467578123,"lng":104.546715,"lat":31.451368},{"M":31.338833798081726,"I":104.59065998828123,"lng":104.59066,"lat":31.338834},{"M":31.35759891514965,"I":104.70052326953129,"lng":104.700523,"lat":31.357599},{"M":31.329449835779005,"I":104.85433186328129,"lng":104.854332,"lat":31.32945},{"M":31.376360287779555,"I":104.89827717578123,"lng":104.898277,"lat":31.37636},{"M":31.43262192640987,"I":104.89827717578123,"lng":104.898277,"lat":31.432622},{"M":31.48884981970347,"I":104.81038655078135,"lng":104.810387,"lat":31.48885},{"M":31.51226814095469,"I":104.67855061328129,"lng":104.678551,"lat":31.512268},{"M":31.530998573910583,"I":104.56868733203123,"lng":104.568687,"lat":31.530999},{"M":31.49821785207472,"I":104.48079670703135,"lng":104.480797,"lat":31.498218}];


                var polygonArr = new Array();//多边形覆盖物节点坐标数组

                $.each(bounds , function (name , value) {
//                        polygonArr.push([ value.I ,  value.M]);
                    polygonArr.push([ value.lng ,  value.lat]);

                });

                if (bounds) {
                    console.log("bounds" , bounds);
                    // for (var i = 0, l = bounds.length; i < l; i++)
                    {
                        //生成行政区划polygon
                        var polygon = new AMap.Polygon({
                            map: map,
                            strokeWeight: 122,
                            path: polygonArr,
                            fillOpacity: 0,
                            fillColor: '#ff8677',
                            strokeColor: '#CC66CC'
                        });
                        polygons.push(polygon);
                        polygon.set(map);
                        var res = polygon.contains(map.getCenter());
                    }

                }
            });
        });

    }

    addBeiJing('绵阳');

</script>
</body>
</html>