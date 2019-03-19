# 项目描述
本项目实现了多人抢购一个商品的秒杀系统！  
# 技术栈
前端：html+css+jquery  
后端：springboot+mybatis  
数据库：mysql+redis  
IDE：ieda
# 高并发优化
## 第一层（浏览器）
1、将http请求拦截在上游、分担数据层的流量。这里采用一次点击按钮、立即禁用的策略。  
2、我们可以采用分时请求、比如每隔多少秒只能透过一次请求（这里没有实现） 
## 第二层（站点）
1、将大量的静态资源交给cdn去解析  
2、采用负载均衡：将大量请求平均分摊到各个应用服务器（这里没有实现）
## 第三层（服务层拦截）  
1、对于写请求，做请求队列，每次只透有限的写请求去数据层，比如有1w部手机，只透1w个下单请求到db，如果均成功再放下一批，
如果库存不够则队列里的写请求全部返回“已售完”（这里使用请求队列、项目中没有实现）  
2、对于秒杀项目，用户肯定每时每刻都在进行查询还有多少库存、不可能大量的读请求都放到db上承载、这样db的压力太大了。
我们可以采用缓存的策略、项目中采用redis做为缓存服务器。进行查询的时候、从缓存中读取数据。官方给出的是可以承载10w的并发量。  
这里我们就产生了一个问题：如何保证redis和mysql的数据一致性。（我只能给出自己的一些理解、能力有限、听说成熟的解决方案是采用请求队列）  
:memo:我们将缓存设置操作时间、当超时后、redis就没法读取数据了、这是就会触发程序读取db、然后更新缓存，这样就能在读取过程中按一定的时间间隔刷新数据了。
```
这里我给出伪代码
    //尝试从Redis中读取数据
    DataObject data = getRedis(key);
    if (data != null) {
        //从db中读取数据
       data = getFromDataBase();
       //重新写入db
       writeRedis(key, data);
       //设置超时时间
       setRedisExpire(key, 5);
    }
```
:memo:写操作要考虑的数据一致性问题、尤其是那些重要的业务、所以首先应该从db中读取最新数据、然后对数据进行操作、最后在把数据写入Redis缓存中。
```
这里我给出伪代码  
  //从数据库中读取最新数据
    DataObject data = getFromDataBase(params);
    //执行业务逻辑
    ExecuteLogic(data);
    //更新db
    updateDataBase();
    //刷新缓存
    updateRedis(data);
```
## 第四层（数据库层）
这里我们采用乐观锁控制事务、避免脏读。
```
发送update语句去更新数据：版本号修改递增1，判断条件中的版本号必须是刚刚查询出来的版本号
update person set name = 'java', version = version + 1 where id = 1 and version = "刚刚查询出来的版本号"。
判断update语句执行之后受影响的行数，若rows>0,则提交事务，否则回滚事务。  
如果需要处理乐观锁、应该在service层编写update方法  
int rows = dao.update(person p) ;  
if (rows == 0) {
    throw new RuntimeException(....);
}
```
# 项目展示
![](https://github.com/liugongding/seckill/blob/master/description/%E9%A6%96%E9%A1%B5.png)  

![](https://github.com/liugongding/seckill/blob/master/description/seckill.png)
![](https://github.com/liugongding/seckill/blob/master/description/seckillTrue.png)

