package com.view;

import com.main.DirectEnum;

import javax.swing.*;
import java.awt.*;

public class Tile {

    final Font[] fonts = {new Font("Helvetica Neue", Font.BOLD, 48)
            , new Font("Helvetica Neue", Font.BOLD, 42)
            , new Font("Helvetica Neue", Font.BOLD, 36)
            , new Font("Helvetica Neue", Font.BOLD, 30)
            , new Font("Helvetica Neue", Font.BOLD, 24)
    };
    public int num[] = { 0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384 };
    public int step; //移动的步数
    public int value;//显示的数字
    public DirectEnum directEnum;//移动的方向
    public boolean ismerge;//是否是合并的

    public Tile() {
        clear();
    }

    public void clear() {
        step = 0;
        value = 0;
        ismerge = false;
        directEnum = DirectEnum.NONE;
    }

    public void swap(Tile tile) {
        this.step = tile.step;
        this.value = tile.value;
        this.ismerge = tile.ismerge;
        this.directEnum = tile.directEnum;
    }

    public Color getForeground() {
        switch (value) {
            case 0:
                return new Color(0xcdc1b4);
            case 2:
            case 4:
                return new Color(0x776e65);
            default:
                return new Color(0xf9f6f2);
        }
    }

    public String[] n2 = {
            "../J2048/src/resource/num02/0.png",
            "../J2048/src/resource/num02/2.png",
            "../J2048/src/resource/num02/4.png",
            "../J2048/src/resource/num02/8.png",
            "../J2048/src/resource/num02/16.png",
            "../J2048/src/resource/num02/32.png",
            "../J2048/src/resource/num02/64.png",
            "../J2048/src/resource/num02/128.png",
            "../J2048/src/resource/num02/256.png",
            "../J2048/src/resource/num02/512.png",
            "../J2048/src/resource/num02/1024.png",
            "../J2048/src/resource/num02/2048.png",
            "../J2048/src/resource/num02/4096.png",
            "../J2048/src/resource/num02/8192.png",
            "../J2048/src/resource/num02/16384.png"
    };

    public String[] n3 = {
            "../J2048/src/resource/num03/0.png",
            "../J2048/src/resource/num03/2.png",
            "../J2048/src/resource/num03/4.png",
            "../J2048/src/resource/num03/8.png",
            "../J2048/src/resource/num03/16.png",
            "../J2048/src/resource/num03/32.png",
            "../J2048/src/resource/num03/64.png",
            "../J2048/src/resource/num03/128.png",
            "../J2048/src/resource/num03/256.png",
            "../J2048/src/resource/num03/512.png",
            "../J2048/src/resource/num03/1024.png",
            "../J2048/src/resource/num03/2048.png",
            "../J2048/src/resource/num03/4096.png",
            "../J2048/src/resource/num03/8192.png",
            "../J2048/src/resource/num03/16384.png"
    };

    public int index(int value) {
        for(int i=0; i<15; i++) {
            if(num[i]==value) return i;
        }
        return 0;
    }
    public Image setImage(String n[]) {
        int i = index(value);
        return new ImageIcon(n[i]).getImage();
    }

    public Color getBackground() {
        switch (value) {
            case 0:
                return new Color(0xcdc1b4);
            case 2:
                return new Color(0xeee4da);
            case 4:
                return new Color(0xede0c8);
            case 8:
                return new Color(0xf2b179);
            case 16:
                return new Color(0xf59563);
            case 32:
                return new Color(0xf67c5f);
            case 64:
                return new Color(0xf65e3b);
            case 128:
                return new Color(0xedcf72);
            case 256:
                return new Color(0xedcc61);
            case 512:
                return new Color(0xedc850);
            case 1024:
                return new Color(0xedc53f);
            case 2048:
                return new Color(0xedc22e);
            case 4096:
                return new Color(0x65da92);
            case 8192:
                return new Color(0x5abc65);
            case 16384:
                return new Color(0x248c51);
            default:
                return new Color(0x248c51);
        }
    }

    public Font getTileFont() {
        int index = value < 100 ? 1 : value < 1000 ? 2 : value < 10000 ? 3 : 4;
        return fonts[index];
    }
}

