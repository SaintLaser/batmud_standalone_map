#### usage

1. install jdk 8 or higher, install python.

2. change the profile : config.properties

notice:  'baseDir' points the directory saving indoor rooms; you could set it equals to the batclient's configure.
    'addr', 'port' is for the bat proxy


3. python 配置

    ## 安装pip
    pip install flask
    pip install flask_sockets
    
3. run

        1.proxy.bat

    执行成功后应该弹出一个窗口程序。此时proxy主程序监听9999,mapper监听10000端口，realm map监听10001。
    you should see a command window. the proxy program is listened at 9999, mapper 1000 and realm map 10001.

4. run
    
        2.mapper.bat
        
    此时启动mapper,连接proxy程序中的mapper端口,开启小地图窗口。
    now you open a java client for batmapper.

5. 打开realmmap目录下的laenor.html地图文件。 open the file 'laenor.html' in realmmap directory.

6. 使用mud客户端链接localhost:9999。 using you favorite mud client to connect : localhost:9999

    
说明

1. 目前mapper仅在切换到新area的时候才保存地图
