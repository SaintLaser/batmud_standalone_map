package com.tcp;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class TabDemo  extends JPanel {

    private JTabbedPane jTabbedpane = new JTabbedPane();// 存放选项卡的组件
    private String[] tabNames = { "mapper", "search" };

    public TabDemo() {
        layoutComponents();
    }

    private void layoutComponents() {
        int i = 0;
        // 第一个标签下的JPanel
        JPanel jpanelFirst = new JPanel();
        jTabbedpane.addTab(tabNames[i++], null, jpanelFirst, "first");// 加入第一个页面
        jTabbedpane.setMnemonicAt(0, KeyEvent.VK_0);// 设置第一个位置的快捷键为0

        // 第二个标签下的JPanel
        JPanel jpanelSecond = new JPanel();
        jTabbedpane.addTab(tabNames[i++], null, jpanelSecond, "second");// 加入第一个页面
        jTabbedpane.setMnemonicAt(1, KeyEvent.VK_1);// 设置快捷键为1
        setLayout(new GridLayout(1, 1));
        add(jTabbedpane);

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //JFrame.setDefaultLookAndFeelDecorated(true);// 将组建外观设置为Java外观
                JFrame frame = new JFrame();
                frame.setTitle("standalone mapper");
                frame.setLayout(null);
                frame.setContentPane(new TabDemo());
                frame.setSize(500, 560);
                frame.setVisible(true);
                // new TabComponentsDemo().runTest();
            }
        });
    }

}
