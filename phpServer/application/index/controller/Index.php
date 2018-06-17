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
        $data["phone"] = $_POST["phone"];
        $data["code"] = $_POST["code"];

        $login = \think\Db::name("username")->where($data)->find();

        if($login == array()){
            return "failed";
        }else{

            $login["mac"] = $_POST["mac"];
            $login["last_login_time"] = date("Y-m-d H:i:s",time());
            $login["status"] = "online";
            $res = \think\Db::name("username")->update($login);

        }

        return json_encode($login);
    }
    public function getMsg(){
        $phone = $_POST["phone"];
        $where["phone"] = $phone;
        $count = \think\Db::name("username")->where($where)->count();
//        error_log(print_r(($count), 1 ) , 3 , "./log.txt");

        if($count){
            return "phoneExits";
        }


        $code = rand(1000, 9999);
        $send = new SendSMS($code , $phone);
        $res = $send->send();

        $res =  json_decode( json_encode( $res),true);
//        error_log(print_r(($res), 1 ) , 3 , "./log.txt");
        if(!array_key_exists("sub_code", $res) )
        {
            $insert["verifyCode"] = $code;
            $insert["phone"] = $phone;
            \think\Db::name("verify")->insert($insert);
            return "OK";
        }
        return $res["sub_code"];

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

        if(timeJudge($temp["time_stamp"] , 600)){
            return "timeOut";
        }else{
            return "success";
        }




    }

    public function signUp(){
        $insert["code"] = $_POST["code"];
        $insert["phone"] = $_POST["phone"];
        $insert["mac"] = $_POST["mac"];
        $insert["status"] = "online";
        $res = \think\Db::name("username")->insert($insert);
//        error_log(print_r(($res), 1 ) , 3 , "./log.txt");

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
