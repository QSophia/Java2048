package com.view;

import com.main.DirectEnum;
import com.main.WaveThread;
import com.rank.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WinMerge extends JFrame {

    private final Font[] fonts = {new Font("Helvetica Neue", Font.BOLD, 48)
            , new Font("Helvetica Neue", Font.BOLD, 42)
            , new Font("Helvetica Neue", Font.BOLD, 36)
            , new Font("Helvetica Neue", Font.BOLD, 30)
            , new Font("Helvetica Neue", Font.BOLD, 24)
    };

    private final String[] f0 = {
            "src/resource/img/bg30-1.png",
            "src/resource/img/bg30-2.png",
            "../J2048/src/resource/img/help3.png", //帮助界面图片
            "src/resource/data/Result3.txt", //排行榜文件名
            "src/resource/data/gettotal3.txt",
            "resource/wav/merge3.wav", //f0[5] isMerge
            "resource/wav/move3.wav"
    };

    public boolean moved = false;
    private static int score = 0; //分数
    private static int ic = 0; //icon标记
    private final int WIDTH_WIN = 400;
    private final int HEIGHT_MD = 100;
    private final int HEIGHT_GM = 400;
    private final int HEIGHT_WIN = HEIGHT_MD + HEIGHT_GM;
    private final int S_BUT = 30 ;
    private final String TITLE = "合体模式";
    private final Color fontColor = new Color(0x383838);

    private GameBoard gameBoard;
    private Dimension gameSize = new Dimension(WIDTH_WIN, HEIGHT_WIN);
    private Image gameImg[] = new Image[2];
    public static int getScore() {
        if(score==0)
            score=2;
        return score;
    }

    public WinMerge() {
        setWin();
        //setUndecorated(false);
    }

    public void setWin() {
        setLayout(null);
        setTitle(TITLE);
        getContentPane().setPreferredSize(gameSize);

        initView();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false); //去掉最大化按钮
        pack();    //获得最佳大小，同时会影响界面生成位置
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void initView() {

        gameBoard = new GameBoard();
        gameBoard.setPreferredSize(gameSize);
        gameBoard.setBounds(0, 0, WIDTH_WIN, HEIGHT_WIN);
        gameBoard.setOpaque(false);
        gameBoard.setFocusable(true);
        add(gameBoard);
    }

    class GameBoard extends JPanel implements MouseListener, KeyListener {

        private static final int GAP_TILE = 8; //瓦片之间间隙
        private static final int ARC_TILE = 16; //瓦片圆角弧度
        private static final int SIZE_TILE = 90;//瓦片的大小
        private static final int PAINT_NUM = 10;//移动的过程分成X段绘制

        private Color bgColor = new Color(255, 255, 255, 10);
        private Tile[][] tiles = new Tile[4][4];
        private JButton btn[] = new JButton[3]; // super()跳转位置
        private JLabel lscore = new JLabel("", JLabel.CENTER);
        private boolean isOver;
        private boolean isMove;
        private boolean isMerge;
        private boolean isAnimate;

        int x,y,uValue,lValue,rValue,dValue;
        int []a=new int[4];
        array[] Array=new array[4];

        public GameBoard() {
            setLayout(null);
            initGame();
            addComponents(btn);
            addMouseListener(this);
            addKeyListener(this);
        }

        private void initGame() {

            setImg();
            setLabel();

            //初始化地图
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tiles[i][j] = new Tile();
                }
            }
            //生成两个瓦片
            createTile();
            createTile();

            isOver = false;
            isAnimate = true;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(gameImg[ic],0,0, WIDTH_WIN, HEIGHT_WIN,this);
        }

        private void addComponents(JButton btn[]) {
            final int y = 3;
            for (int i = 0; i < 3; i++) {
                btn[i] = new JButton("");
                btn[i].setBorderPainted(false);
                //btn[i].setIcon(ImageIcon(""));
                btn[i].setContentAreaFilled(false);
                btn[i].addMouseListener(this);
                btn[i].setFocusable(false);
                add(btn[i]);
            }
            btn[0].setBounds(185, y, S_BUT, S_BUT);
            btn[1].setBounds(323, y, S_BUT, S_BUT);
            btn[2].setBounds(360, y, S_BUT, S_BUT);
        }

        private void setImg() {
            for(int i=0; i<2; i++) {
                gameImg[i] = Toolkit.getDefaultToolkit().createImage(f0[i]);
            }
        }

        private void setLabel() {
            score = 0;
            lscore.setText("0");
            lscore.setFont(fonts[0]);
            lscore.setForeground(fontColor);
            lscore.setOpaque(true);
            lscore.setBackground(bgColor);
            lscore.setBounds(0, 40, 160, 60);
            add(lscore);
        }


        @Override
        public void mouseClicked(MouseEvent e) {

            if(e.getComponent().equals(btn[0])) {
                ic = 1 - ic;
            }else if(e.getComponent().equals(btn[1])) {
                new Help(f0[2], 1150, 460);
            }else if(e.getComponent().equals(btn[2])) {
                new paihangbang(f0[3]).setVisible(true);
            }
            gameBoard.setFocusable(true);
            //gameBoard.addKeyListener(this);
        }

        @Override
        public void mouseEntered(MouseEvent e)   //鼠标进入组件
        {
            for(int i=0; i<3; i++) {
                if(e.getComponent().equals(btn[i])) {
                    btn[i].setFocusable(true);
                    break;
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e)     //鼠标离开组件
        {
            for(int i=0; i<3; i++) {
                if(e.getComponent().equals(btn[i])) {
                    btn[i].setFocusable(false);
                    break;
                }
            }
            gameBoard.setFocusable(true);
            //gameBoard.addKeyListener(this);
        }




        @Override
        public void keyPressed(KeyEvent e) {
            boolean moved = false;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    initGame();
                    break;
            }
            repaint();
        }

        private void createTile() {
            //获取当前空白的瓦片，并加入列表
            List<Tile> list = getBlankTiles();
            if (!list.isEmpty()) {
                Random random = new Random();
                int index = random.nextInt(list.size());
                Tile tile = list.get(index);
                //初始化新瓦片的值为2或4
                tile.value = random.nextInt(100) > 50 ? 4 : 2;
            }
        }

        /**
         * 获取当前空白的瓦片，加入列表返回
         *
         * @return
         */
        private List<Tile> getBlankTiles() {
            List<Tile> list = new ArrayList<Tile>();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (tiles[i][j].value == 0) {
                        list.add(tiles[i][j]);
                    }
                }
            }
            return list;
        }

        private void checkGameOver() {
            lscore.setText(score + "");
            if (getBlankTiles().isEmpty()) {
                isOver = true;
                new yourScore(f0[3],f0[4],getScore()).setVisible(true);
                return;
            }
        }






        private void doMove(Tile src, Tile dst, DirectEnum directEnum) {
            dst.swap(src);
            dst.step++;
            dst.directEnum = directEnum;
            src.clear();
            isMove = true;
        }

        private void doMerge(Tile src, Tile dst, DirectEnum directEnum) {
            dst.value = dst.value << 1;
            dst.ismerge = true;
            dst.step++;
            dst.directEnum = directEnum;
            src.clear();
            score += dst.value;
            isMove = true;
            isMerge = true;
        }

        /**
         * 绘制动画效果
         */
        private void invokeAnimate() {
            if (isMerge) {
                if(ic==0)
                    new WaveThread(f0[5]).start();
                mergeAnimate();
            }else if (isMove) {
                if(ic==0)
                    new WaveThread(f0[6]).start();
            }
            if (isMerge || isMove) {
                createTile();
                isMerge = false;
                isMove = false;
            }
        }

        private void mergeAnimate() {
            isAnimate = false;
            Graphics gg = getGraphics();
            Image image = this.createImage(getWidth(), getHeight());
            Graphics g = image.getGraphics();
            g.drawImage(gameImg[ic],0,0, WIDTH_WIN, HEIGHT_WIN,this);

            int k = -3; //使下面的ex从0开始
            while (k < 4) {
                int ex = 9 - k ^ 2; //抛物线公式
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (!tiles[i][j].ismerge) {
                            drawTile(g, i, j, 0, 0, 0);
                        } else {
                            drawTile(g, i, j, 0, 0, ex);
                        }
                    }
                }
                gg.drawImage(image, 0, 0, null);
                k++;
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tiles[i][j].ismerge = false;
                }
            }
            isAnimate = true;
        }


        @Override
        public void paint(Graphics g) {

            super.paint(g);
            //g.drawImage(gameImg,0,0, WIDTH_WIN, HEIGHT_GM,this); // 图片y坐标相对当前面板而言
            if (isAnimate) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        drawTile(g, i, j, 0, 0, 0);
                    }
                }
            }
            if (isOver) {
                g.setColor(new Color(255, 255, 255, 180));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(new Color(0x3d79ca));
                g.setFont(fonts[0]);
                FontMetrics fms = getFontMetrics(fonts[0]);
                String value = "Game Over";
                g.drawString(value, (getWidth() - fms.stringWidth(value)) / 2, getHeight() / 2);
            }

        }

        private void drawTile(Graphics gg, int i, int j, int mx, int my, int ex) {
            Graphics2D g = (Graphics2D) gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

            Tile tile = tiles[i][j];
            int x = GAP_TILE + (GAP_TILE + SIZE_TILE) * j + mx - ex;
            int y = HEIGHT_MD + GAP_TILE + (GAP_TILE + SIZE_TILE) * i + my - ex;
            int s = SIZE_TILE;
            g.drawImage(tile.setImage(tile.n3), x, y, s, s, this);

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }




        public void leftExist() {
            if(y==0) {a[0]=0; return;}
            else for(int j=y-1;j>-1;j--) {
                if(tiles[x][j].value!=0) {
                    a[0]=1000*(y-j)+10*tiles[x][j].value+1;
                    lValue=tiles[x][j].value;
                    return;
                }
            }
            a[0]=0;
            return;
        }

        public void rightExist() {
            if(y==3) {a[1]=0; return;}
            else for(int j=y+1;j<4;j++) {
                if(tiles[x][j].value!=0) {
                    a[1]=1000*(j-y)+10*(tiles[x][j].value)+2;
                    rValue=tiles[x][j].value;
                    return;
                }
            }
            a[1]=0;
            return;
        }

        public void upExist() {
            if(x==0) {a[2]=0; return;}
            else for(int i=x-1;i>-1;i--) {
                if(tiles[i][y].value!=0) {
                    a[2]=1000*(x-i)+10*(tiles[i][y].value)+3;
                    uValue=tiles[i][y].value;
                    return;
                }
            }
            a[2]=0;
            return;
        }

        public void downExist() {
            if(x==3) {a[3]=0; return;}
            else for(int i=x+1;i<4;i++) {
                if(tiles[i][y].value!=0) {
                    a[3]=1000*(i-x)+10*tiles[i][y].value+4;
                    dValue=tiles[i][y].value;
                    return;
                }
            }
            a[3]=0;
            return;
        }

        public void checkValue() {
            Arrays.sort(a);
            int m=0,n=0,k=1;
            for(int i=0;i<4;i++) {
                Array[i]=new array();
                Array[i].setValue(a[i]);
            }
            for(int i=0;i<4;i++) {
                for(int j=i+1;j<4;j++) {
                    if((a[i]%1000/10)==(a[j]%1000/10) && a[i]!=0) {
                        Array[0].setValue(a[i]);
                        Array[1].setValue(a[j]);
                        m=i;
                        n=j;
                        break;
                    }
                }
                if(m>0)
                    break;
            }

            if(m>0) {
                for(int i=0;i<4;i++) {
                    if(i!=m && i!=n) {
                        k++;
                        Array[k].setValue(a[i]);
                    }
                }
            }
        }

        class array{
            private int a;
            public void setValue(int i) {
                this.a=i;
            }
            public int getValue() {
                return this.a;
            }
            public void move() {
                int m=getValue()%10;
                //System.out.println(getValue());
        		/*if(m==4) {
        			leftMoveToRight();
        		}
        		if(m==3) {
        			rightMoveToLeft();
        		}
        		if(m==2) {
        			upMoveToDown();
        		}
        		if(m==1) {
        			downMoveToUp();
        		}*/
                switch(m) {
                    case 1:leftMoveToRight();break;
                    case 2:rightMoveToLeft();break;
                    case 3:upMoveToDown();break;
                    case 4:downMoveToUp();break;
                }
            }
        }

        public void leftMoveToRight() {
            if(tiles[x][y].value==0 || tiles[x][y].value==lValue)

                for (int j = y-1; j > -1; j--) {
                    int k = j;
                    int hasMerged=0;
                    //  if(tiles[x][y].value==0) hasMerged=1;
                    while (k < y && tiles[x][k].value != 0) {
                        if (tiles[x][k + 1].value == 0) {
                            doMove(tiles[x][k], tiles[x][k + 1], DirectEnum.RIGHT);

                        } else if (tiles[x][k + 1].value == tiles[x][k].value) {
                            doMerge(tiles[x][k], tiles[x][k + 1], DirectEnum.RIGHT);
                            hasMerged=1;
                            break;
                        } else {
                            break;
                        }
                        k++;
                    }
                }
            else {
                for (int j = y-1; j > -1; j--) {
                    int k = j;
                    while (k < y-1 && tiles[x][k].value != 0 ) {
                        if (tiles[x][k + 1].value == 0) {
                            doMove(tiles[x][k], tiles[x][k + 1], DirectEnum.RIGHT);
                        }
                        else {
                            break;
                        }
                        k++;
                    }
                }
            }
        }

        public void rightMoveToLeft() {
            if(tiles[x][y].value==0 || tiles[x][y].value==rValue)
                for (int j = y+1; j < 4; j++) {
                    int k = j;
                    int hasMerged=0;
                    //if(tiles[x][y].value==0) hasMerged=1;
                    while (k > y && tiles[x][k].value != 0 ) {
                        if (tiles[x][k - 1].value == 0) {
                            doMove(tiles[x][k], tiles[x][k - 1], DirectEnum.LEFT);

                        } else if (tiles[x][k - 1].value == tiles[x][k].value ) {
                            doMerge(tiles[x][k], tiles[x][k - 1], DirectEnum.LEFT);
                            hasMerged=1;
                            break;
                        } else {
                            break;
                        }
                        k--;
                    }
                }
            else {
                for (int j = y+1; j < 4; j++) {
                    int k = j;
                    while (k > y+1 && tiles[x][k].value != 0 ) {
                        if (tiles[x][k - 1].value == 0) {
                            doMove(tiles[x][k], tiles[x][k - 1], DirectEnum.RIGHT);
                        }
                        else {
                            break;
                        }
                        k--;
                    }
                }
            }
        }

        public void downMoveToUp() {
            if(tiles[x][y].value==0 || tiles[x][y].value==dValue)
                for (int i = x+1; i < 4; i++) {
                    int k = i;
                    int hasMerged=0;
                    //if(tiles[x][y].value==0) hasMerged=1;
                    while (k > x && tiles[k][y].value != 0 ) {
                        if (tiles[k - 1][y].value == 0) {
                            doMove(tiles[k][y], tiles[k - 1][y], DirectEnum.UP);

                        } else if (tiles[k - 1][y].value == tiles[k][y].value) {
                            doMerge(tiles[k][y], tiles[k - 1][y], DirectEnum.UP);
                            hasMerged=1;
                            break;
                        } else {
                            break;
                        }
                        k--;
                    }
                }
            else {
                for (int i = x+1; i < 4; i++) {
                    int k = i;
                    while (k > x+1 && tiles[k][y].value != 0 ) {
                        if (tiles[k - 1][y].value == 0) {
                            doMove(tiles[k][y], tiles[k - 1][y], DirectEnum.UP);
                        }else {
                            break;
                        }
                        k--;
                    }
                }
            }
        }

        public void upMoveToDown() {
            if(tiles[x][y].value==0 || tiles[x][y].value==uValue)
                for (int i = x-1; i > -1; i--) {
                    int k = i;
                    int hasMerged=0;
                    //if(tiles[x][y].value==0) hasMerged=1;
                    while (k < x && tiles[k][y].value != 0 ) {
                        if (tiles[k + 1][y].value == 0) {
                            doMove(tiles[k][y], tiles[k + 1][y], DirectEnum.DOWN);

                        } else if (tiles[k + 1][y].value == tiles[k][y].value) {
                            doMerge(tiles[k][y], tiles[k + 1][y], DirectEnum.DOWN);
                            hasMerged=1;
                            break;
                        } else {
                            break;
                        }
                        k++;
                    }
                }
            else {
                for (int i = x-1; i >0; i--) {
                    int k = i;
                    while (k < x+1 && tiles[k][y].value != 0 ) {
                        if (tiles[k + 1][y].value == 0) {
                            doMove(tiles[k][y], tiles[k + 1][y], DirectEnum.UP);
                        }else {
                            break;
                        }
                        k++;
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int xx=(int)e.getY()/100-1;
            int yy=(int)e.getX()/100;

            if(xx>=0&&yy>=0&&tiles[xx][yy].value==0) {
                y=yy; x=xx;
                moved=true;
            }
            else return;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(moved==true){
                upExist();
                downExist();
                leftExist();
                rightExist();
                checkValue();

                for(int i=0;i<4;i++) {
                    Array[i].move();
                }
                invokeAnimate();
                checkGameOver();
                moved=false;
                repaint();
                return;
            }
            else return;
        }

    }

}
