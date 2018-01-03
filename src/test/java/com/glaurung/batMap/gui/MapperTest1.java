package com.glaurung.batMap.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.glaurung.batMap.controller.MapperEngine;
import com.glaurung.batMap.vo.Exit;
import com.glaurung.batMap.vo.Room;

import edu.uci.ics.jung.graph.SparseMultigraph;

public class MapperTest1 implements ActionListener {

    static JButton button;
    static JButton sbutton;
    static JButton lbutton;
    static JTextField field;

    public static void main(String[] args) {
        // 基本图结构
        SparseMultigraph<Room, Exit> graph = new SparseMultigraph<Room, Exit>();


        MapperEngine engine = new MapperEngine(graph);
        engine.setBaseDir("C:\\Users\\wind\\batclient\\");

        JFrame frame = new JFrame("Graph View");
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(engine.getPanel());

        frame.pack();
        frame.setVisible(true);

        // new room coming
        HashSet<String> exits = new HashSet<String>();
        exits.add("ne");
        exits.add("n");
        engine.moveToRoom("testArea", "1", "enter path", false, "areaFirstRoom", "it is a now room.", exits);

        // 2 room
        exits.clear();
        exits.add("ne");
        exits.add("n");
        exits.add("w");
        exits.add("sw");
        engine.moveToRoom("testArea", "2", "n", false, "secondRoom", "jshgfskgyfhsgfjhtsgdfhgsdfsygfsgdyfuysgdfjkhsgdfskuygfsuygfusygfuysd", exits);

        // 3 room
        exits.clear();
        exits.add("ne");
        exits.add("n");
        exits.add("w");
        exits.add("sw");
        engine.moveToRoom("testArea", "3", "e", true, "thirdRoom", "jshgfskgyfhsgfjhtsgdfhgsdfsygfsgdyfuysgdfjkhsgdfskuygfsuygfusygfuysd", exits);

        // 4 room
        exits.clear();
        exits.add("ne");
        exits.add("n");
        exits.add("w");
        exits.add("sw");
        engine.moveToRoom("testArea", "4", "s", true, "fourthRoom", "jshgfskgyfhsgfjhtsgdfhgsdfsygfsgdyfuysgdfjkhsgdfskuygfsuygfusygfuysd", exits);

        // 5 room
        exits.clear();
        exits.add("w");
        exits.add("w");
        exits.add("e");
        exits.add("e");
        engine.moveToRoom("testArea", "5", "w", true, "fifthRoom", "jshgfskgyfhsgfjhtsgdfhgsdfsygfsgdyfuysgdfjkhsgdfskuygfsuygfusygfuysd", exits);

        // 6 room
        exits.clear();
        exits.add("ne");
        exits.add("nw");
        exits.add("sw");
        exits.add("se");
        engine.moveToRoom("testArea", "6", "n", false, "sixthRoom", "jshgfskgyfhsgfjhtsgdfhgsdfsygfsgdyfuysgdfjkhsgdfskuygfsuygfusygfuysd", exits);

        // 1 room
        exits.clear();
        exits.add("ne");
        exits.add("nw");
        exits.add("sw");
        exits.add("se");
        engine.moveToRoom("testArea", "1", "jump to start", false, "areaFirstRoom", "wind is here now", exits);

        // 2 1 room
        exits.clear();
        exits.add("ne");
        exits.add("nw");
        exits.add("sw");
        exits.add("s");
        engine.moveToRoom("testArea2", "2-1", "enter", false, "area2  Room1", "area 2 dddddddddddddddddddddd", exits);

//        // 2 2 room
//        exits.clear();
//        exits.add("n");
//        exits.add("e");
//        engine.moveToRoom("testArea2", "2-2", "s", false, "area 2 2 Room", "area s s s ", exits);
////
//        // 2 3 room
//        exits.clear();
//        exits.add("n");
//        exits.add("w");
//        engine.moveToRoom("testArea2", "2-3", "e", false, "area 2 3 Room", "area s s s ", exits);
//
//        // 2 2 room
//        exits.clear();
//        exits.add("n");
//        exits.add("e");
//        engine.moveToRoom("testArea2", "2-2", "fly", false, "area 2 2 Room", "area s s s ", exits);
//
//
//        // 3 1 room
//        exits.clear();
//        exits.add("n");
//        exits.add("w");
//        engine.moveToRoom("testArea3", "3-1", "boat", false, "area 3 1 Room", "area s s s ", exits);
//
//        // 3 2 room
//        exits.clear();
//        exits.add("n");
//        exits.add("w");
//        engine.moveToRoom("testArea3", "3-2", "n", false, "area 3 2 Room", "area s s s ", exits);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(sbutton)) {

        }

    }
}
