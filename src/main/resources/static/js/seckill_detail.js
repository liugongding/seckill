//js模块化
//seckill.detail.init(param)
var seckill = {
    //封装秒杀相关ajax的url
    url: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' +md5 + '/execute';
        }
    },
    validatePhone: function (phone) {
        //phone是否为空
        //为空就是undefine
        //inNaN是非数字就为true
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }

    },
    detail: {
        //详情页初始化
        init: function (params) {
            //用户手机验证、计时交互
            var killPhone = $.cookie('killPhone');
            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //没有登录，立即绑定手机号
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true, //显示弹出层
                    backdrop: 'static', //禁止位置关闭弹出层
                    keyboard: false //禁止通过键盘关闭弹出层
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    //填写手机号之后再次判断
                    if (seckill.validatePhone(inputPhone)) {
                        //验证手机号通过
                        //电话写入cookie
                        $.cookie("killPhone", inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                    }
                });
            }
            //已经登录、计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckilId = params['seckillId'];
            $.get(seckill.url.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    //计时交互
                    seckill.countDown(seckilId, nowTime, startTime, endTime);
                } else {
                    console.log('result: ' + result);
                }
            });

        }
    },
    handleSeckill: function (seckillId, node) {
        console.log(node);
        //var node = $('#seckill-Box')
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.url.exposer(seckillId), {}, function (result) {
            //在回调函数中、执行交互流程
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀、获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.url.execution(seckillId, md5);
                    //绑定一次点击事件
                    $('#killBtn').one('click', function () {
                     //执行秒杀请求
                        //1、禁用按钮
                        $(this).addClass('disabled');
                        //2、发送秒杀请求
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');

                            }
                        });
                    });
                    node.show();
                } else {
                    //未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckillId.countDown(seckillId, now, start, end);
                }
            } else {

            }
        })
    },
    countDown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        //时间判断
        if (nowTime > endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            //秒杀未开启、开始计时
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                //格式化日期
                var format = event.strftime('秒杀计时: %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                //时间完成后回调时间
            }).on('finish.countdown', function () {
                //获取秒杀地址、控制现实逻辑、执行秒杀
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            //秒杀开始
            seckill.handleSeckill(seckillId, seckillBox);
        }
    }
}