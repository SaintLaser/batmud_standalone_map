#### usage

1. install jdk 8 or higher, install python.

2. 修改配置文件config.properties中的baseDir为合适的值，比如client的地图目录。

3. 执行 

        1.mapper.bat

    执行成功后应该弹出一个窗口程序。此时程序监听10000端口，在proxy中已经配置（bcprotocol首部）。

4. 执行
    
        2.proxy.bat
        
    此时proxy程序链接到10000端口，同时监听本地的9999端口。   

5. 使用mud客户端链接localhost:9999。

    
说明

1. 目前mapper仅在切换到新area的时候才保存地图
2. 不能删除单个房间
