<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2018/6/5
 * Time: 17:36
 */

namespace app\index\controller;


use think\Controller;

class Edit extends Controller
{
    public function index(){


        return $this->fetch("edit/index");
    }
}