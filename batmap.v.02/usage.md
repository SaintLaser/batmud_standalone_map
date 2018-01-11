#### usage

1. install jdk 8 or higher, install python.

2. 修改配置文件config.properties

    baseDir为地图存放目录,可以设置为原client的地图目录。
    addr、port为proxy中监听的mapper信息

3. 执行 

        1.proxy.bat

    执行成功后应该弹出一个窗口程序。此时proxy主程序监听9999,mapper监听10000端口，realm map监听10001。

4. 执行
    
        2.mapper.bat
        
    此时启动mapper,连接proxy程序中的mapper端口,开启小地图窗口。

5. 打开realmmap目录下的laenor.html地图文件。

6. 使用mud客户端链接localhost:9999。

    
说明

1. 目前mapper仅在切换到新area的时候才保存地图
