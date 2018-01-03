package com.wind.mapper;

import com.wind.mapper.config.MapperConfig;

import javax.swing.*;
import java.io.IOException;

public class Main {
    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {

        MapperConfig.load();

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
