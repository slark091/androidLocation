<?php
namespace app\index\controller;

use app\index\tools\SendSMS;

use think\Controller;
use think\View;

class Index extends Controller
{
    public function index()
    {


        $view = new View();
        return $view->fetch('index');

    }
    public  function  test(){
        $data = $_POST;





        $insert = array("time"=>"test" , "latitude" => "test" , "longitude" => "test");
        \think\Db::name("test")->insert($insert);

        $login = \think\Db::name("username")->where(array("phone"=>$data["phone"]))->find();
        if($login == array()){
            return "noAccount";
        }
        if($login["code"] == $data["code"]){
            return "rightCod";
        }else{
            return "wrongCode";
        }
    }
    public function getMsg(){
        $phone = $_POST["phone"];
        $code = rand(1000, 9999);
        $send = new SendSMS($code , $phone);
        $res = $send->send();
    }

    public function sign(){



    }
    public function edit(){





        $fence = \think\Db::name("fence")->select();

        $this->assign("fence" , $fence);



        return $this->fetch();

    }

    public function edit_(){

        $update = null;
        $add = null;
        $delete = null;
        $arr = $_POST["arr"];
        foreach ($arr as $item){
            if($item[0]["operation"] == "update" ){
                $update[] = $item[0];
                unset($item[0]["operation"]);

                $login = \think\Db::name("fence")->update($item[0]);
                continue;

            }

            if($item[0]["operation"] == "delete" ){
                $delete[] = $item[0];
                unset($item[0]["operation"]);

                $login = \think\Db::name("fence")->delete($item[0]);
                continue;

            }
            if($item[0]["operation"] == "add" ){

                $add[] = $item[0];
                unset($item[0]["operation"]);
                $login = \think\Db::name("fence")->insert($item[0]);


                continue;

            }
        }

        return "test";
    }

}
