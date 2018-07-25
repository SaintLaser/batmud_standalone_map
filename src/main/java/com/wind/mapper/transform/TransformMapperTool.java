package com.wind.mapper.transform;


import com.alibaba.fastjson.JSON;
import com.glaurung.batMap.io.AreaDataPersister;
import com.glaurung.batMap.vo.AreaSaveObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 将原来的batmap格式文件调整为batmap_json文件
 * 格式由java对象序列话改为 json格式
 */
@Slf4j
public class TransformMapperTool {

    public static final String UTF = "UTF-8";

    /**
     * 将baseDir的文件统一转换为json
     * @param baseDir
     */
    public static void transform(String baseDir) throws IOException, ClassNotFoundException {

        File dir = new File(baseDir);
        if( !dir.isDirectory()){
            throw new RuntimeException("baseDir is not a dir!");
        }

        //开始转换
        for( File areaFile : dir.listFiles()){

            String name = areaFile.getName();


            //不是原始的地图文件，pass 掉
            if( !name.endsWith(AreaDataPersister.SUFFIX)){
                continue;
            }

            //查看对应的新文件是否存在
            File jsonFile = new File(baseDir,name.replaceAll(AreaDataPersister.SUFFIX,AreaDataPersister.JSON_SUFFIX));
            if( !jsonFile.exists()){
                System.out.println("start to gen new json file for " + name );

                //读取
                FileInputStream fileInputStream = new FileInputStream( areaFile );
                ObjectInputStream objectInputStream = new ObjectInputStream( fileInputStream );
                AreaSaveObject saveObject = (AreaSaveObject) objectInputStream.readObject();

                String jsonObject = JSON.toJSONString(saveObject);

                //保存
                jsonFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(jsonFile);
                fileOutputStream.write(jsonObject.getBytes(UTF));
                fileOutputStream.flush();

                fileOutputStream.close();
                fileInputStream.close();
                System.out.println("gen json file ok！");
            }

        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        transform("/home/wild/code/batmaps/");
    }
}
