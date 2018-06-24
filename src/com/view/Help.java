package com.view;

import javax.swing.*;
import java.awt.*;

public class Help extends JFrame {

    private Image helpImg;
    private Dimension winSize;
    private int w, h;
    private JPanel panel;

    public Help(String imgPath, int width, int height) {
        helpImg = Toolkit.getDefaultToolkit().createImage(imgPath);
        w = width; h = height;
        winSize = new Dimension(w, h);
        initView();
    }
    public void initView() {
        setLayout(null);
        setTitle("游戏帮助——按Esc键重置游戏");
        getContentPane().setPreferredSize(winSize);
        panel = new setImg();
        panel.setPreferredSize(winSize); //没有则不会出现
        panel.setBounds(0, 0, w, h); //没有则不会出现
        add(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false); //去掉最大化按钮
        pack();    //获得最佳大小，同时会影响界面生成位置
        setLocationRelativeTo(null);
        setVisible(true);
    }

    class setImg extends JPanel {

        setImg() {
            setLayout(null);
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(helpImg,0,0, w, h,this);
        }
    }
}
