package com.wind.mapper;

import com.wind.mapper.config.MapperConfig;
import com.wind.mapper.transform.TransformMapperTool;

import javax.swing.*;
import java.io.IOException;

public class Main {
    /**
     * @param args
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        MapperConfig.load();

        //转换已有的地图
        TransformMapperTool.transform(MapperConfig.baseDir);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setTitle("standalone mapper");
                frame.setLayout(null);
                frame.setContentPane(new Windows());
                frame.setSize(MapperConfig.mainWidth, MapperConfig.mainHeight);
                frame.setVisible(true);
                // new TabComponentsDemo().runTest();
            }
        });

    }
}
