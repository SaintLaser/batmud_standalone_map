#### usage

对mapper做了一层封装。

1. install jdk 8 or higher.

2. set the 'baseDir'/'port' to a proper value below.

3. run

        java -cp .;batUtils-1.4-SNAPSHOT-full.jar com.wind.mapper.Main

4. for test, pls read 'test_data.txt'

#### pp

目前实现了socket接收方式，接收的单行字符串格式

 示例
    
        arelium;;$apr1$dF!!_X#W$dG7RGdqpERshxk5XnrLDN/;;west;;1;;east@@shortdesc@@longdesc
        
        crimson guild;;$apr1$dF!!_X#W$kXXkOct/6BkbBsksle6aS/;;north;;1;;south,west,east@@shortdesc@@longdesc
        
 规范
 
    [areaName];;[roomUID];;[exitUsed];;[indoors];;[exits1,exits2]@@[shortDesc]@@[longDesc]
    
 说明: '@@[shortDesc]@@[longDesc]'这个串之前的是proxy可以直接返回的内容。shortDesc，需要截取房间简称。longDesc需要截取room的多行描述。
 
 todo:
 
    在mudlet中截图房屋short和long描述，然后通过socket（或者其他协议，需要调整本程序）发送，则实现mudlet的batmapper。
    
    