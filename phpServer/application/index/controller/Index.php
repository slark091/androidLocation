<?php
namespace app\index\controller;

use app\index\tools\SendSMS;

use think\Controller;
use think\helper\Arr;
use think\View;

class Index extends Controller
{
    public function index()
    {


        $view = new View();
        return $view->fetch('index');

    }
    public  function  login(){
        $data = $_POST;


        $login = \think\Db::name("username")->where($data)->find();
        error_log(print_r(($login), 1 ) , 3 , "./log.txt");

        if($login == array()){
            return "failed";
        }

        return json_encode($login);
    }
    public function getMsg(){
        $phone = $_POST["phone"];
        $where["phone"] = $phone;
        if(\think\Db::name("username")->where($where)->count()){
            return "phoneExits";
        }


        $code = rand(1000, 9999);
        $send = new SendSMS($code , $phone);
        $res = $send->send();

        $res =  json_decode( json_encode( $res),true);
        error_log(print_r(($res), 1 ) , 3 , "./log.txt");
        if($res["sub_code"] !== null )
        {
            $insert["verifyCode"] = $code;
            $insert["phone"] = $phone;
            \think\Db::name("verify")->insert($insert);
            return $res["sub_code"];
        }


        return "OK";
    }



    public function sign(){
        $phone = $_POST["phone"];
        $code = $_POST["verifyCode"];
        $where["phone"]=$phone;
        $where["verifyCode"]=$code;
        $temp = \think\Db::name("verify")->where($where)->find();

        if($temp == Array()){
            return "verifyCode does not exits";
        }
        $getTime = $temp['time_stamp'];
        $getTime = strtotime($getTime);

        $currentTime = date("Y-m-d H:i:s",time());
        $currentTime = strtotime($currentTime);

        if($currentTime - $getTime > 600 ){
            return "timeOut";
        }else{
            return "success";
        }




    }

    public function signUp(){
        $phone = $_POST["phone"];
        $code = $_POST["code"];
        $insert["code"] = $code;
        $insert["phone"] = $phone;
        $res = \think\Db::name("username")->insert($insert);
        error_log(print_r(($res), 1 ) , 3 , "./log.txt");

        if($res != 0){
            return json_encode($insert);
        }
        return "dataBaseError";
    }



    public function edit(){





        $fence = \think\Db::name("fence")->select();

        $this->assign("fence" , $fence);



        return $this->fetch();

    }

    public function edit_(){


        $arr = $_POST["arr"];
        foreach ($arr as $item){
            if($item[0]["operation"] == "update" ){
                unset($item[0]["operation"]);

                \think\Db::name("fence")->update($item[0]);
                continue;

            }

            if($item[0]["operation"] == "delete" ){
                unset($item[0]["operation"]);

                \think\Db::name("fence")->delete($item[0]);
                continue;

            }
            if($item[0]["operation"] == "add" ){

                unset($item[0]["operation"]);
                unset($item[0]["id"]);
                \think\Db::name("fence")->insert($item[0]);


                continue;

            }
        }

        return "test";
    }

}
