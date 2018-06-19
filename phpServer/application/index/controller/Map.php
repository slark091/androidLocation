<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2018/6/17
 * Time: 16:19
 */

namespace app\index\controller;


class map
{
    function getPolygons(){
        $res = \think\Db::name("fence")->select();

        if($res == array()){
            return "empty";
        }else{
            return json_encode($res);
        }
    }
}