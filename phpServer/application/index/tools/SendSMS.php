<?php
namespace app\index\tools;
/**
 * Created by PhpStorm.
 * User: MAC
 * Date: 17/4/1
 * Time: 下午11:25
 */

use Flc\Alidayu\Client;
use Flc\Alidayu\App;
use Flc\Alidayu\Requests\AlibabaAliqinFcSmsNumSend;
class SendSMS
{
    private $code, $phone;
    public function __construct($code, $phone) {
        $this->code = $code;
        $this->phone  = $phone;
    }
    public function  send(){
        // 配置信息
        $config = [
            'app_key'    => '23390099',
            'app_secret' => '5852ee68910825650b2eba11d83b73f9',
        ];

        $client = new Client(new App($config));
        $req    = new AlibabaAliqinFcSmsNumSend;

        $req->setRecNum($this->phone)
            ->setSmsParam([
                //'code' => rand(100000, 999999)
                'code' => $this->code
            ])
            ->setSmsFreeSignName('本地租车')
            ->setSmsTemplateCode('SMS_59940383');
        $log = $client->execute($req);
        return $log;
//        print_r($log);
//        error_log(print_r($log , 1 ) ,  3   , "/home/wwwroot/log.txt");

    }
    public function showMsg() {
    }
}