package com.wind.server;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class TestScroll extends JFrame {

    JMenuBar jb;
    JTextArea ja;
    JScrollPane jsp;

    public void setImage() {
        jb = new JMenuBar();
        this.setJMenuBar(jb);
        ja = new JTextArea();
        jsp = new JScrollPane(ja);

        ja.setBounds( 0, 0, 280, 200 );
        ja.setEditable( false );
        ja.setColumns( 25 );
        ja.setBorder( new LineBorder( Color.BLACK ) );
        ja.setAlignmentY( Component.BOTTOM_ALIGNMENT );
        ja.setLineWrap( true );
        ja.setText("Two well-traveled roads cross paths here. One road leads north to a great black castle on a hill. To the west, the road leads to a mountain ridge. Taking the east path will lead you through a forest, and the south path will lead to more familiar areas.Two well-traveled roads cross paths here. One road leads north to a great black castle on a hill. To the west, the road leads to a mountain ridge. Taking the east path will lead you through a forest, and the south path will lead to more familiar areas.Two well-traveled roads cross paths here. One road leads north to a great black castle on a hill. To the west, the road leads to a mountain ridge. Taking the east path will lead you through a forest, and the south path will lead to more familiar areas.Two well-traveled roads cross paths here. One road leads north to a great black castle on a hill. To the west, the road leads to a mountain ridge. Taking the east path will lead you through a forest, and the south path will lead to more familiar areas.Two well-traveled roads cross paths here. One road leads north to a great black castle on a hill. To the west, the road leads to a mountain ridge. Taking the east path will lead you through a forest, and the south path will lead to more familiar areas.Two well-traveled roads cross paths here. One road leads north to a great black castle on a hill. To the west, the road leads to a mountain ridge. Taking the east path will lead you through a forest, and the south path will lead to more familiar areas.Two well-traveled roads cross paths here. One road leads north to a great black castle on a hill. To the west, the road leads to a mountain ridge. Taking the east path will lead you through a forest, and the south path will lead to more familiar areas.");

        this.setSize(500, 200);
        this.setLayout(new BorderLayout());
        this.add(jsp);
        this.setVisible(true);
    }


    public static void main(String[] args) {
        TestScroll a = new TestScroll();
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.setImage();

    }

}
