package com.view;

import com.main.DirectEnum;
import com.main.WaveThread;
import com.view.Window1;
import com.view.Window2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Window4 extends JFrame {

    private final Font[] fonts = {new Font("Helvetica Neue", Font.BOLD, 48)
            , new Font("Helvetica Neue", Font.BOLD, 42)
            , new Font("Helvetica Neue", Font.BOLD, 36)
            , new Font("Helvetica Neue", Font.BOLD, 30)
            , new Font("Helvetica Neue", Font.BOLD, 24)
    };
    private String[] f0 = {
            "../J2048/src/resource/icon/j1.png",
            "../J2048/src/resource/icon/j2.png",
            "../J2048/src/resource/icon/j3.png",
            "../J2048/src/resource/icon/j0.png",
            "../J2048/src/resource/img/bg32.png",
            "../J2048/src/resource/img/bg32.png",
            "../J2048/src/resource/img/bg32.png"
    };

    private static int score = 0; //分数
    private static int ic = 0; //icon标记
    private final int WIDTH_WIN = 400;
    private final int HEIGHT_MD = 100;
    private final int HEIGHT_GM = 400;
    private final int HEIGHT_WIN = HEIGHT_MD + HEIGHT_GM;
    private final String TITLE = "";

    private ModeBoard modeBoard;
    private Dimension modeSize = new Dimension(WIDTH_WIN, HEIGHT_MD);
    private Dimension scoreSize = new Dimension(150, 80);
    private Image scoreImg = Toolkit.getDefaultToolkit().createImage(f0[5]);

    private GameBoard gameBoard;
    private static final int S_BUT = 35 ;
    private Dimension gameSize = new Dimension(WIDTH_WIN, HEIGHT_WIN);
    private Image modeImg = Toolkit.getDefaultToolkit().createImage(f0[4]);
    private Image gameImg = Toolkit.getDefaultToolkit().createImage(f0[6]);
    private JLabel lscore;

    public Window4() {
        setWin();
        //setUndecorated(false);
    }

    public void setWin() {
        setLayout(null);
        setTitle(TITLE);
        getContentPane().setPreferredSize(gameSize);
        //add(panel);
        initView();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); //去掉最大化按钮
        pack();    //获得最佳大小，同时会影响界面生成位置
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel panel = new JPanel() {

        //网上使用的paint()方法，这个图片会将组件掩盖掉的
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Image im=Toolkit.getDefaultToolkit().getImage(f0[4]);
            g.drawImage(im,0,0,null);
        }

    };

    public void initView() {

        //modeBoard.setBounds(0, 0, WIDTH_WIN, HEIGHT_MD);
        //gameBoard //Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException
        gameBoard = new GameBoard();
        gameBoard.setPreferredSize(gameSize);
        //gameBoard.setBackground(new Color(0xbbada0));
        gameBoard.setBounds(0, 0, WIDTH_WIN, HEIGHT_WIN);
        gameBoard.setFocusable(true);
        this.add(gameBoard);
    }

    //class mouseKey implements ActionListener, MouseListener {}

    class ModeBoard extends JPanel implements ActionListener {

        Dimension d;
        Image image;
        public JButton btn[] = new JButton[3]; // super()跳转位置

        public ModeBoard(Dimension d, Image image) {
            super();
            this.setLayout(null);
            this.d = d;
            this.image = image;
            addComponents(btn);
            //addMouseListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image,0,0, d.width, d.height,this);
        }

        private void addComponents(JButton btn[]) {

            lscore = new JLabel("0", JLabel.CENTER);
            lscore.setFont(new Font("Helvetica Neue", Font.BOLD, 22));
            lscore.setForeground(Color.WHITE);
            lscore.setOpaque(true);
            lscore.setBackground(new Color(0xbbada0));
            lscore.setBounds(0, 0, 120, 60);
            add(lscore);

            for (int i = 0; i < 3; i++) {
                btn[i] = drawBtn(i);
                btn[i].addActionListener(this);
                add(btn[i]);
            }
        }

        private JButton drawBtn(int i) {

            ImageIcon imageIcon = smallerImg(new ImageIcon(f0[i])); // Icon由图片文件形成
            JButton b = new JButton(imageIcon); // 先new再进行设置，new设置好的button无效
            b.setBounds(250 + i * 50, 15, S_BUT, S_BUT);
            //btn.setBackground(new Color(2,2,2));
            //btn.setOpaque(false);
            //btn.setEnabled(false);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false); // 是否显示外围矩形区域 选否
            //b.addMouseListener(this);
            return b;
        }

        private ImageIcon smallerImg(ImageIcon imageIcon) {
            Image image = imageIcon.getImage(); // 但这个图片太大不适合做Icon
            // 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
            Image smallImage = image.getScaledInstance(S_BUT, S_BUT, Image.SCALE_FAST);
            // 再由修改后的Image来生成合适的Icon
            return new ImageIcon(smallImage);
        }

        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==btn[0]) {
                ic=3-ic;
                ImageIcon imageIcon = smallerImg(new ImageIcon(f0[ic]));
                btn[0].setIcon(imageIcon);
            }
            if(e.getSource()==btn[1]) {
                ic=3-ic;
                ImageIcon imageIcon = smallerImg(new ImageIcon(f0[ic]));
                btn[1].setIcon(imageIcon);
            }
            if(e.getSource()==btn[2]) {
                ic=3-ic;
                ImageIcon imageIcon = smallerImg(new ImageIcon(f0[ic]));
                btn[1].setIcon(imageIcon);
            }
        }

        public void mouseClicked(MouseEvent e) {
            if(e.getComponent().equals(btn[0])) { //声音 //线程图片装填更换
                ic=3-ic;
                ImageIcon imageIcon = smallerImg(new ImageIcon(f0[ic]));
                btn[0].setIcon(imageIcon);
            }
            if(e.getComponent().equals(btn[1])) { //帮助 //面板覆盖
                new Window2();
            }
            if(e.getComponent().equals(btn[2])) { //排行榜 //用户登录窗口
                new Window1();
            }
        }

        public void mouseEntered(MouseEvent e)   //鼠标进入组件
        {
        }
        public void mouseExited(MouseEvent e)     //鼠标离开组件
        {
        }
        public void mousePressed(MouseEvent e)   //鼠标被按下
        {
        }
        public void mouseReleased(MouseEvent e)   //鼠标被放开
        {
        }

    }

    class GameBoard extends JPanel implements ActionListener, KeyListener {

        private static final int GAP_TILE = 8; //瓦片之间间隙
        private static final int ARC_TILE = 16; //瓦片圆角弧度
        private static final int SIZE_TILE = 90;//瓦片的大小
        private static final int PAINT_NUM = 20;//移动的过程分成X段绘制

        private Tile[][] tiles = new Tile[4][4];
        private Color bgColor;
        private boolean isOver;
        private boolean isMove;
        private boolean isMerge;
        private boolean isAnimate;

        public JButton btn[] = new JButton[3]; // super()跳转位置

        public GameBoard() {
            setLayout(null);
            initGame();
            bgColor = new Color(0xbbada0);
            addComponents(btn);
            addKeyListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //g.fillRect(0,0, WIDTH_WIN, HEIGHT_GM);
            g.drawImage(modeImg,0,0, WIDTH_WIN, HEIGHT_MD,this);
            g.drawImage(gameImg,0,HEIGHT_MD, WIDTH_WIN, HEIGHT_GM,this); // 图片y坐标相对当前面板而言
        }

        private void addComponents(JButton btn[]) {

            lscore = new JLabel("0", JLabel.CENTER);
            lscore.setFont(new Font("Helvetica Neue", Font.BOLD, 22));
            lscore.setForeground(Color.WHITE);
            lscore.setOpaque(true);
            lscore.setBackground(new Color(0xbbada0));
            lscore.setBounds(0, 0, 120, 60);
            add(lscore);

            for (int i = 0; i < 3; i++) {
                btn[i] = drawBtn(i);
                btn[i].addActionListener(this);
                add(btn[i]);
            }
        }

        private JButton drawBtn(int i) {

            ImageIcon imageIcon = smallerImg(new ImageIcon(f0[i])); // Icon由图片文件形成
            JButton b = new JButton(imageIcon); // 先new再进行设置，new设置好的button无效
            b.setBounds(250 + i * 50, 15, S_BUT, S_BUT);
            //btn.setBackground(new Color(2,2,2));
            //btn.setOpaque(false);
            //btn.setEnabled(false);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false); // 是否显示外围矩形区域 选否
            //b.addMouseListener(this);
            return b;
        }

        private ImageIcon smallerImg(ImageIcon imageIcon) {
            Image image = imageIcon.getImage(); // 但这个图片太大不适合做Icon
            // 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
            Image smallImage = image.getScaledInstance(S_BUT, S_BUT, Image.SCALE_FAST);
            // 再由修改后的Image来生成合适的Icon
            return new ImageIcon(smallImage);
        }

        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==btn[0]) {
                ic=3-ic;
                ImageIcon imageIcon = smallerImg(new ImageIcon(f0[ic]));
                btn[0].setIcon(imageIcon);
            }
            if(e.getSource()==btn[1]) {
                ic=3-ic;
                ImageIcon imageIcon = smallerImg(new ImageIcon(f0[ic]));
                btn[1].setIcon(imageIcon);
            }
            if(e.getSource()==btn[2]) {
                ic=3-ic;
                ImageIcon imageIcon = smallerImg(new ImageIcon(f0[ic]));
                btn[1].setIcon(imageIcon);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            boolean moved = false;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    initGame();
                    break;
                case KeyEvent.VK_LEFT:
                    moved = moveLeft();
                    invokeAnimate();
                    checkGameOver(moved);
                    break;
                case KeyEvent.VK_RIGHT:
                    moved = moveRight();
                    invokeAnimate();
                    checkGameOver(moved);
                    break;
                case KeyEvent.VK_UP:
                    moved = moveUp();
                    invokeAnimate();
                    checkGameOver(moved);
                    break;
                case KeyEvent.VK_DOWN:
                    moved = moveDown();
                    invokeAnimate();
                    checkGameOver(moved);
                    break;
            }
            repaint();
        }

        private void initGame() {
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

        private void checkGameOver(boolean moved) {
            lscore.setText(score + "");
            if (!getBlankTiles().isEmpty()) {
                return;
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    //判断是否存在可合并的两个瓦片
                    if (tiles[i][j].value == tiles[i][j + 1].value || tiles[i][j].value == tiles[i + 1][j].value) {
                        isOver = false;
                        return;
                    }
                }
            }
            isOver = true;
        }

        private boolean moveLeft() {
            isMove = false;
            for (int i = 0; i < 4; i++) {
                for (int j = 1; j < 4; j++) {
                    int k = j;
                    //当前移动瓦片不能到达边界，不能为空白瓦片，前方瓦片不能是合成瓦片
                    while (k > 0 && tiles[i][k].value != 0 && !tiles[i][k - 1].ismerge) {
                        if (tiles[i][k - 1].value == 0) {
                            doMove(tiles[i][k], tiles[i][k - 1], DirectEnum.LEFT);
                        } else if (tiles[i][k - 1].value == tiles[i][k].value) {
                            doMerge(tiles[i][k], tiles[i][k - 1], DirectEnum.LEFT);
                            break;
                        } else {
                            break;
                        }
                        k--;
                    }
                }
            }
            return isMove;
        }

        private boolean moveRight() {
            isMove = false;
            for (int i = 0; i < 4; i++) {
                for (int j = 2; j > -1; j--) {
                    int k = j;
                    //当前移动瓦片不能到达边界，不能为空白瓦片，前方瓦片不能是合成瓦片
                    while (k < 3 && tiles[i][k].value != 0 && !tiles[i][k + 1].ismerge) {
                        if (tiles[i][k + 1].value == 0) {
                            doMove(tiles[i][k], tiles[i][k + 1], DirectEnum.RIGHT);
                        } else if (tiles[i][k + 1].value == tiles[i][k].value) {
                            doMerge(tiles[i][k], tiles[i][k + 1], DirectEnum.RIGHT);
                            break;
                        } else {
                            break;
                        }
                        k++;
                    }
                }
            }
            return isMove;
        }

        private boolean moveUp() {
            isMove = false;
            for (int j = 0; j < 4; j++) {
                for (int i = 1; i < 4; i++) {
                    int k = i;
                    //当前移动瓦片不能到达边界，不能为空白瓦片，前方瓦片不能是合成瓦片
                    while (k > 0 && tiles[k][j].value != 0 && !tiles[k - 1][j].ismerge) {
                        if (tiles[k - 1][j].value == 0) {
                            doMove(tiles[k][j], tiles[k - 1][j], DirectEnum.UP);
                        } else if (tiles[k - 1][j].value == tiles[k][j].value) {
                            doMerge(tiles[k][j], tiles[k - 1][j], DirectEnum.UP);
                            break;
                        } else {
                            break;
                        }
                        k--;
                    }
                }
            }
            return isMove;
        }

        private boolean moveDown() {
            isMove = false;
            for (int j = 0; j < 4; j++) {
                for (int i = 2; i > -1; i--) {
                    int k = i;
                    //当前移动瓦片不能到达边界，不能为空白瓦片，前方瓦片不能是合成瓦片
                    while (k < 3 && tiles[k][j].value != 0 && !tiles[k + 1][j].ismerge) {
                        if (tiles[k + 1][j].value == 0) {
                            doMove(tiles[k][j], tiles[k + 1][j], DirectEnum.DOWN);
                        } else if (tiles[k + 1][j].value == tiles[k][j].value) {
                            doMerge(tiles[k][j], tiles[k + 1][j], DirectEnum.DOWN);
                            break;
                        } else {
                            break;
                        }
                        k++;
                    }
                }
            }
            return isMove;
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
            //    if(ic==0)
                    new WaveThread("../J2048/src/resource/wav/merge.wav").start();
                moveAnimate();
                mergeAnimate();
            } else if (isMove) {
            //    if(ic==0)
                    new WaveThread("../J2048/src/resource/wav/move.wav").start();
                moveAnimate();
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
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
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

        private void moveAnimate() {
            isAnimate = false;
            Graphics gg = getGraphics();
            Image image = this.createImage(getWidth(), getHeight());
            Graphics g = image.getGraphics();
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
            int k = 0; //
            while (k < PAINT_NUM) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        int step = (GAP_TILE + SIZE_TILE) * tiles[i][j].step / PAINT_NUM;
                        switch (tiles[i][j].directEnum) {
                            case LEFT:
                                drawTile(g, i, j, (PAINT_NUM - k) * step, 0, 0);
                                break;
                            case RIGHT:
                                drawTile(g, i, j, (k - PAINT_NUM) * step, 0, 0);
                                break;
                            case UP:
                                drawTile(g, i, j, 0, (PAINT_NUM - k) * step, 0);
                                break;
                            case DOWN:
                                drawTile(g, i, j, 0, (k - PAINT_NUM) * step, 0);
                                break;
                            case NONE:
                                drawTile(g, i, j, 0, 0, 0);
                                break;
                        }
                    }
                }
                gg.drawImage(image, 0, 0, null);
                k++;
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tiles[i][j].step = 0;
                    tiles[i][j].directEnum = DirectEnum.NONE;
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

            /*//绘制瓦片背景
            g.setColor(tile.getBackground());
            //注意：横坐标用j计算,纵坐标用i计算
            g.fillRoundRect(x, y, SIZE_TILE + ex * 2, SIZE_TILE + ex * 2, ARC_TILE, ARC_TILE);
            //绘制瓦片文字
            g.setColor(tile.getForeground());
            Font font = tile.getTileFont();
            g.setFont(font);
            FontMetrics fms = getFontMetrics(font);
            String value = String.valueOf(tile.value);
            //注意：横坐标用j计算,纵坐标用i计算
            g.drawString(value, GAP_TILE + (GAP_TILE + SIZE_TILE) * j
                    + (SIZE_TILE - fms.stringWidth(value)) / 2 + mx - ex
                    , GAP_TILE + (GAP_TILE + SIZE_TILE) * i
                    + (SIZE_TILE - fms.getAscent() - fms.getDescent()) / 2 + my - ex
                    + fms.getAscent());*/
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

}
