package com.main;

import com.view.Window2;

import javax.swing.*;
import java.awt.*;

/**
 * 2048界面
 *
 * 
 */

public class GameMain2 {
    public static void main(String[] args) {
        Window2 win = new Window2();
        win.initView();
        win.setTitle("2048下落模式");
        win.getContentPane().setPreferredSize(new Dimension(395, 625));
        //JFrame直接调用setBackground设置背景色不生效
        win.getContentPane().setBackground(new Color(0xfaf8ef));
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setResizable(false); //去掉最大化按钮
        win.pack(); //获得最佳大小
        win.setVisible(true);
    }
}
