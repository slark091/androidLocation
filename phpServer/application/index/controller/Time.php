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

        $login = loginJudge($_POST);
        error_log(print_r(($_POST), 1 ) , 3 , "./log.txt");

        if($login!=1){
            return $login;
        }


        $user = \think\Db::name("username")->where($_POST)->find();
        $where["uid"] = $user["id"];
        $calendar = \think\Db::name("calendar")->where($where)->select();





        return json_encode($calendar);

    }
    public function setCalendar(){

        $user = \think\Db::name("username")->where(array("phone"=>$_POST["phone"]))->find();

    }

}