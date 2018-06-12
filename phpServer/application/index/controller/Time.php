<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2018/6/10
 * Time: 21:10
 */

namespace app\index\controller;


use think\Controller;

class Time extends Controller
{

    public function getTime(){



    }

    public function getCalendar(){

        error_log(print_r(($_POST), 1 ) , 3 , "./log.txt");



        $user = \think\Db::name("username")->where($_POST)->find();
        $where["uid"] = $user["id"];
        $calendar = \think\Db::name("calendar")->where($where)->select();

        error_log(print_r(($calendar), 1 ) , 3 , "./log.txt");




        return json_encode($calendar);

    }
    public function setCalendar(){

        $user = \think\Db::name("username")->where(array("phone"=>$_POST["phone"]))->find();

    }

}