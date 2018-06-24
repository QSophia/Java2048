package com.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Window extends JFrame {

    final Font[] fonts = {new Font("Helvetica Neue", Font.BOLD, 48)
            , new Font("Helvetica Neue", Font.BOLD, 42)
            , new Font("Helvetica Neue", Font.BOLD, 36)
            , new Font("Helvetica Neue", Font.BOLD, 30)
            , new Font("Helvetica Neue", Font.BOLD, 24)
    };
    private String[] f1 = {
            "../J2048/src/resource/img/btn1.png",
            "../J2048/src/resource/img/btn2.png",
            "../J2048/src/resource/img/btn3.png"
    };
    private String[] f2 = {
            "../J2048/src/resource/img/btn01.png",
            "../J2048/src/resource/img/btn02.png",
            "../J2048/src/resource/img/btn03.png"
    };

    private ModeBoard modeBoard;
    private final int WIDTH_WIN = 400;
    private final int HEIGHT_WIN = 500;
    private final int WIDTH_BUT = 240 ;
    private final int HEIGHT_BUT = 70;
    private Dimension frameSize = new Dimension(WIDTH_WIN, HEIGHT_WIN);
    ImageIcon icon = new ImageIcon("../J2048/src/resource/img/btn00.png" );
    private Image image = Toolkit.getDefaultToolkit().createImage("../J2048/src/resource/img/bg00.png");

    public Window() {
        this.setLayout(null);
        setTitle("多玩2048");
        getContentPane().setPreferredSize(frameSize);
        //setUndecorated(true);
        setIconImage(icon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); //去掉最大化按钮
        pack();    //获得最佳大小
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void initView() {
        //游戏面板组件
        modeBoard = new ModeBoard();
        setContentPane(modeBoard);
        setVisible(true);
    }

    class ModeBoard extends JPanel implements MouseListener {

        public JButton btn[] = new JButton[3];

        public ModeBoard() {
            super();
            setLayout(null);
            addComponents(btn);
            addMouseListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image,0,0, WIDTH_WIN, HEIGHT_WIN,this);
            repaint();
        }

        private void addComponents(JButton btn[]) {
            for (int i = 0; i < 3; i++) {
                btn[i] = drawBtn(i);
            }
        }

        private JButton drawBtn(int i) {

            ImageIcon imageIcon = smallerImg(new ImageIcon(f1[i])); // Icon由图片文件形成
            JButton b = new JButton(imageIcon); // 先new再进行设置，new设置好的button无效
            b.setBounds(80, i * 90 + 210, WIDTH_BUT, HEIGHT_BUT);
            //btn.setBackground(new Color(2,2,2));
            //btn.setOpaque(false);
            //btn.setEnabled(false);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false); // 是否显示外围矩形区域 选否
            b.addMouseListener(this);

            add(b);
            return b;
        }

        private ImageIcon smallerImg(ImageIcon imageIcon) {
            Image image = imageIcon.getImage(); // 但这个图片太大不适合做Icon
            // 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
            Image smallImage = image.getScaledInstance(WIDTH_BUT, HEIGHT_BUT, Image.SCALE_FAST);
            // 再由修改后的Image来生成合适的Icon
            return new ImageIcon(smallImage);
        }

        public void mouseClicked(MouseEvent e) {
            if(e.getComponent().equals(btn[0])) {
                new WinOrigin();
            }
            if(e.getComponent().equals(btn[1])) {
                new WinFall();
            }
            if(e.getComponent().equals(btn[2])) {
                new WinMerge();
            }
        }

        public void mouseEntered(MouseEvent e)   //鼠标进入组件
        {
            for(int i=0; i<3; i++) {
                if(e.getComponent().equals(btn[i])) {
                    ImageIcon imageIcon = smallerImg(new ImageIcon(f2[i]));
                    btn[i].setIcon(imageIcon);
                    break;
                }
            }
        }
        public void mouseExited(MouseEvent e)     //鼠标离开组件
        {
            for(int i=0; i<3; i++) {
                if(e.getComponent().equals(btn[i])) {
                    ImageIcon imageIcon = smallerImg(new ImageIcon(f1[i]));
                    btn[i].setIcon(imageIcon);
                    break;
                }
            }
        }

        public void mousePressed(MouseEvent e)   //鼠标被按下
        {
        }

        public void mouseReleased(MouseEvent e)   //鼠标被放开
        {
        }

    }

}