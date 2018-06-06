<?php
namespace app\index\controller;

use app\index\tools\SendSMS;

use think\Controller;
use think\View;

class Index extends Controller
{
    public function index()
    {


//        return $test[0]["id"];
        $view = new View();
        return $view->fetch('index');

    }
    public  function  test(){
        $data = $_POST;





        $insert = array("time"=>"test" , "latitude" => "test" , "longitude" => "test");
        \think\Db::name("test")->insert($insert);
//        error_log(print_r($data , 1 ) , 3 , "D:log.txt");

        $login = \think\Db::name("username")->where(array("phone"=>$data["phone"]))->find();
        if($login == array()){
            error_log(print_r("empty" , 1 ) , 3 , "D:log.txt");
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
//        $phone = 15281140795;
        $send = new SendSMS($code , $phone);
        $res = $send->send();
//        $res["emptyTSASDFQWEXXX"] = 0;
//        error_log(print_r($res , 1 ) , 3 , "D:log.txt");
    }

    public function sign(){

        error_log(print_r($_POST , 1 ) , 3 , "D:log.txt");


    }
    public function edit(){





        $fence = \think\Db::name("fence")->select();

        $this->assign("fence" , $fence);

//        error_log(print_r($fence , 1 ) , 3 , "D:log.txt");


        return $this->fetch();
//
//
//        $view = new View();
//        return $view->fetch("edit");

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
                error_log(print_r("\n================================ITEM=======================\n", 1 ) , 3 , "D:log.txt");
                error_log(print_r($item, 1 ) , 3 , "D:log.txt");
                error_log(print_r("\n=========================================================\n", 1 ) , 3 , "D:log.txt");

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
//        error_log(print_r("\n================================ARR=======================\n", 1 ) , 3 , "D:log.txt");
//        error_log(print_r($arr, 1 ) , 3 , "D:log.txt");
//        error_log(print_r("\n=========================================================\n", 1 ) , 3 , "D:log.txt");
//
//
//
//        error_log(print_r("\n================================UPDATE=======================\n", 1 ) , 3 , "D:log.txt");
//        error_log(print_r($update, 1 ) , 3 , "D:log.txt");
//        error_log(print_r("\n=========================================================\n", 1 ) , 3 , "D:log.txt");
//
//        error_log(print_r("\n==========================DELETE=============================\n", 1 ) , 3 , "D:log.txt");
//        error_log(print_r($delete, 1 ) , 3 , "D:log.txt");
//        error_log(print_r("\n=========================================================\n", 1 ) , 3 , "D:log.txt");
//
//        error_log(print_r("\n=======================ADD================================\n", 1 ) , 3 , "D:log.txt");
//        error_log(print_r($add, 1 ) , 3 , "D:log.txt");
//        error_log(print_r("\n=========================================================\n", 1 ) , 3 , "D:log.txt");


//        if($update!=null){
//
//        }
//        if($delete!=null){
//            $login = \think\Db::name("fence")->delete($delete);
//
//        }
//        if($add!=null){
//            $login = \think\Db::name("fence")->insert($add);
//
//        }

        return "test";
    }

}
