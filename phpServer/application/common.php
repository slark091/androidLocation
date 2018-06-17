<?php
// +----------------------------------------------------------------------
// | ThinkPHP [ WE CAN DO IT JUST THINK ]
// +----------------------------------------------------------------------
// | Copyright (c) 2006-2016 http://thinkphp.cn All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.apache.org/licenses/LICENSE-2.0 )
// +----------------------------------------------------------------------
// | Author: 流年 <liu21st@gmail.com>
// +----------------------------------------------------------------------

// 应用公共文件
function loginJudge($proof){
    $where["phone"] = $proof["phone"];

    $res = \think\Db::name("username")->where($where)->find();

    if($res == array()){
        return "loginJudge/findError";
    }
    if(timeJudge($res["last_login_time"] , 3*24*60*60)){
        return "loginJudge/timeOut";
    }
    if($res["code"]!=$proof["code"]){
        return "loginJudge/codeError";
    }
    if($res["mac"]!=$proof["mac"] ){
        return "loginJudge/macError";
    }
    if($res["status"]=="notOnline"){
        return "loginJudge/statusError";
    }


    return true;
}
function timeJudge($time , $second){
//    $getTime = $temp['time_stamp'];
    $getTime = $time;
    $getTime = strtotime($getTime);
    $currentTime = date("Y-m-d H:i:s",time());
    $currentTime = strtotime($currentTime);
    if($currentTime - $getTime > $second){
        return true;
    }else{
        return false;
    }
}