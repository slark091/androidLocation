<?php
namespace app\index\controller;

class Index
{
    public function index()
    {

        $test = \think\Db::query('select * from map');

        return $test[0]["id"];
    }
    public  function  test(){
        return "test";
    }
}
