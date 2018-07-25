package com.wind.mapper;

import com.glaurung.batMap.controller.MapperEngine;
import com.glaurung.batMap.controller.SearchEngine;
import com.glaurung.batMap.vo.json.Exit;
import com.glaurung.batMap.vo.json.Room;
import com.wind.mapper.config.MapperConfig;
import com.wind.mapper.handler.IHandler;
import com.wind.mapper.handler.SocketHandler;
import edu.uci.ics.jung.graph.SparseMultigraph;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * 窗口程序
 */
public class Windows extends JPanel {

    public Windows() {
        layoutComponents();

        new Thread() {
            @Override
            public void run() {
                //开始监听事件
                IHandler handler = new SocketHandler();
                handler.setMapperEngine(mapperEngine);
                handler.beginHandle();
            }
        }.start();
    }

    /**
     * init components
     */
    private void layoutComponents() {
        int i = 0;
        JPanel jpanelFirst = initMapperPanel();
        jTabbedpane.addTab(tabNames[i++], null, jpanelFirst, "go go");// 加入第一个页面
        jTabbedpane.setMnemonicAt(0, KeyEvent.VK_0);

        JPanel jpanelSecond = initSearchPanel();
        jTabbedpane.addTab(tabNames[i++], null, jpanelSecond, "search");
        jTabbedpane.setMnemonicAt(1, KeyEvent.VK_1);
        setLayout(new GridLayout(1, 1));
        add(jTabbedpane);
    }

    private JPanel initMapperPanel() {
        SparseMultigraph<Room, Exit> graph = new SparseMultigraph<Room, Exit>();
        MapperEngine engine = new MapperEngine(graph);
        engine.setBaseDir(MapperConfig.baseDir);

        //init mapper
        mapperEngine = engine;


        return engine.getPanel();
    }

    private JPanel initSearchPanel() {
        SparseMultigraph<Room, Exit> graph = new SparseMultigraph<Room, Exit>();

        SearchEngine engine = new SearchEngine(graph);
        engine.setBaseDir(MapperConfig.baseDir);

        searchEngine = engine;

        return engine.getPanel();
    }

    private MapperEngine mapperEngine;
    private SearchEngine searchEngine;
    private JTabbedPane jTabbedpane = new JTabbedPane();
    private String[] tabNames = {"mapper", "search"};
}
