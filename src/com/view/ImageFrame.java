package com.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.*;

public class ImageFrame extends JFrame implements Runnable {
    int[] x = new int[100];
    int[] y = new int[100];

    public ImageFrame() {
        this.setSize(640, 480);
        this.setLocation(200, 100);
        this.setTitle("下雪了");

        for(int i = 0; i < 100; ++i) {
            this.x[i] = (int)(Math.random() * 640.0D);
            this.y[i] = (int)(Math.random() * 480.0D);
        }

    }

    public void run() {
        while(true) {
            for(int i = 0; i < 100; ++i) {
                ++this.y[i];
                if (this.y[i] >= 480) {
                    this.y[i] = 0;
                    this.x[i] = (int)(Math.random() * 640.0D);
                }
            }

            try {
                Thread.sleep(20L);
            } catch (Exception var2) {
                ;
            }

            this.repaint();
        }
    }

    public void paint(Graphics g) {
        Image i1 = Toolkit.getDefaultToolkit().getImage("../J2048/src/resource/img/5.png");
        g.drawImage(i1, 0, 0, 640, 480, this);
        //g.setColor(Color.WHITE);
        Font f = new Font("", 1, 25);
        g.setFont(f);

        for(int i = 0; i < 100; ++i) {
            g.drawString("*", this.x[i], this.y[i]);
        }

    }


}
