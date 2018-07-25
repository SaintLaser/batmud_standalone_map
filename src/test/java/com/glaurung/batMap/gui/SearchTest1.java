package com.glaurung.batMap.gui;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import com.glaurung.batMap.controller.SearchEngine;
import com.glaurung.batMap.gui.search.SearchPanel;
import com.glaurung.batMap.vo.json.Exit;
import com.glaurung.batMap.vo.json.Room;

import edu.uci.ics.jung.graph.SparseMultigraph;

public class SearchTest1 {

    public static void main( String[] args ) {

        JFrame frame = new JFrame( "" );
        frame.setLayout( new FlowLayout() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        SparseMultigraph<Room, Exit> graph = new SparseMultigraph<Room, Exit>();


        SearchEngine engine = new SearchEngine( graph );
        //真正的文件存储位置在这个目录下的 conf\batMapAreas 内。
        engine.setBaseDir( "C:\\Users\\wind\\batclient\\" );
        frame.getContentPane().add( new SearchPanel( engine ) );
        frame.setSize( 1200, 800 );
        frame.setVisible( true );

    }
}
