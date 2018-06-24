package com.view;

import com.main.DirectEnum;
import com.main.WaveThread;
import com.rank.paihangbang;
import com.rank.yourScore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 下落模式
 *
 */

public class WinFall extends JFrame{

    private String[] f1 = {
            "../J2048/src/resource/icon/j1.png",
            "../J2048/src/resource/icon/j2.png",
            "../J2048/src/resource/icon/j3.png",
            "../J2048/src/resource/icon/j0.png"
    };

	final Font[] fonts = {new Font("Helvetica Neue", Font.BOLD, 48)
            , new Font("Helvetica Neue", Font.BOLD, 42)
            , new Font("Helvetica Neue", Font.BOLD, 36)
            , new Font("Helvetica Neue", Font.BOLD, 30)
            , new Font("Helvetica Neue", Font.BOLD, 24)
    };

	private final String[] f0 = {
            "../J2048/src/resource/img/bg20-1.png",
            "../J2048/src/resource/img/bg20-2.png",
            "../J2048/src/resource/img/help2.png", //帮助界面图片
            "src/resource/data/Result2.txt", //排行榜文件名
            "src/resource/data/gettotal2.txt",
            "resource/wav/merge2.wav", //f0[5] isMerge
            "resource/wav/move2.wav"
    };
	
    private static int score = 0; //分数
    private static int ic = 0; //icon标记
    private final int WIDTH_WIN = 395;
    private final int HEIGHT_MD = 100;
    private final int HEIGHT_GM = 525;
    private final int HEIGHT_WIN = HEIGHT_MD + HEIGHT_GM;
    private final int S_BUT = 26 ;
    private final String TITLE = "下落模式";
    private final Color fontColor = new Color(0xf8cbad);

    private GameBoard gameBoard;
    private Dimension gameSize = new Dimension(WIDTH_WIN, HEIGHT_WIN);
    private Image gameImg[] = new Image[2];
    public static int getScore() {
    	if(score==0)
    	score=2;
        return score;
    }
   
    public WinFall() {
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
    
    class GameBoard extends JPanel implements Runnable, MouseListener, KeyListener { //游戏面板类
    	
    	private static final int GAP_TILE = 5; //瓦片之间间隙
        private static final int ARC_TILE = 16; //瓦片圆角弧度
        private static final int SIZE_TILE = 60; //瓦片的大小
        private static final int PAINT_NUM = 20; //移动的过程分成X段绘制

        private Color bgColor = new Color(255, 255, 255, 10);
        private Tile[][] tiles = new Tile[8][6];
        private JButton btn[] = new JButton[3]; // super()跳转位置
        private JLabel lscore = new JLabel("", JLabel.CENTER);
        private boolean isOver;
        private boolean isMove;
        private boolean isMerge;
        private boolean isAnimate;
        private Thread t;
        int time = 800;
        private boolean stopMe = true;
        
        public GameBoard() {
            setLayout(null);
            initGame();
            addComponents(btn);
            addMouseListener(this);
            addKeyListener(this);
        }

        private void initGame() { //初始化游戏
        	
        	setImg();
            setLabel();
            
            //初始化地图
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    tiles[i][j] = new Tile();
                }
            }
            createTile();
            isOver = false;
            isAnimate = true;
            
            t=new Thread(this);
            t.start();
            
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(gameImg[ic],0,0, WIDTH_WIN, HEIGHT_WIN,this);
        }

        private void addComponents(JButton btn[]) {
            for (int i = 0; i < 3; i++)
                btn[i] = drawBtn(i);
        }


        private JButton drawBtn(int i) {

            ImageIcon imageIcon = smallerImg(new ImageIcon(f1[i])); // Icon由图片文件形成
            JButton b = new JButton(""); // 先new再进行设置，new设置好的button无效
            b.setBounds(272 + 40 * i,4, S_BUT, S_BUT);
            //btn.setBackground(new Color(2,2,2));
            //btn.setOpaque(false);
            //btn.setEnabled(false);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false); // 是否显示外围矩形区域 选否
            b.addMouseListener(this);
            b.setFocusable(false);

            add(b);
            return b;
        }

        private ImageIcon smallerImg(ImageIcon imageIcon) {
            Image image = imageIcon.getImage(); // 但这个图片太大不适合做Icon
            // 为把它缩小点，先要取出这个Icon的image ,然后缩放到合适的大小
            Image smallImage = image.getScaledInstance(S_BUT, S_BUT, Image.SCALE_FAST);
            // 再由修改后的Image来生成合适的Icon
            return new ImageIcon(smallImage);
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
            lscore.setBounds(0, 44, 124, 60);
            add(lscore);
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {

            if(e.getComponent().equals(btn[0])) {
                ic = 1 - ic;
            }else if(e.getComponent().equals(btn[1])) {
                new Help(f0[2], 800, 620);
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
        public void mousePressed(MouseEvent e)   //鼠标被按下
        {
        }

        @Override
        public void mouseReleased(MouseEvent e)   //鼠标被放开
        {
        }

        @Override
        public void keyPressed(KeyEvent e) { //按键事件
        	boolean moved = false;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                	t.stop();
                    initGame();
                    //t.start();
                    break;
                case KeyEvent.VK_LEFT:
                	if(isOver==false) {
                		moved = moveLeft();
                        invokeAnimate();
                        checkGameOver(moved);
                        break;
                	}
                case KeyEvent.VK_RIGHT:
                	if(isOver==false) {
                		moved = moveRight();
                        invokeAnimate();
                        checkGameOver(moved);
                        break;
                	}
                case KeyEvent.VK_DOWN:
                	if(isOver==false) {
                		moved = moveDown();
                        invokeAnimate();
                        checkGameOver(moved);
                        break;
                	}
            }
            repaint();
        }
        
        public void createTile() { //创建瓦片
        	List<Tile> list = getBlankTiles();
            if (tiles[0][3].value==0&&tiles[1][3].value==0) {
            	//初始化新瓦片的值为2,4,8,16,32,64
            	Random rnd =new Random();
                tiles[0][3].value = (int)Math.pow(2, rnd.nextInt(6)+1);
            }
            int mark=0;
            for(int i = 1; i < 8; i++) {
            	if(tiles[i][3].value!=0) {
            		mark=mark+1;
            	}
            }
            if(mark==7) {
            	//初始化新瓦片的值为2,4,8,16,32,64
            	Random rnd =new Random();
                tiles[0][3].value = (int)Math.pow(2, rnd.nextInt(6)+1);
            }
        }

        /**
         * 获取当前空白的瓦片，加入列表返回
         *
         * @return
         */
        private List<Tile> getBlankTiles() {
            List<Tile> list = new ArrayList<Tile>();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    if (tiles[i][j].value == 0) {
                        list.add(tiles[i][j]);
                    }
                }
            }
            return list;
        }

        private void checkGameOver(boolean moved) {
        	lscore.setText(score + "");
        	if (tiles[0][3].value!=0&&tiles[1][3].value!=0)
            {
        		isOver=true;
                new yourScore(f0[3],f0[4],getScore()).setVisible(true);//停止线程
                
            }
        	else
        		isOver=false;
        }

        private boolean moveLeft() {
        	isMove = false;
        	for (int i = 0; i < 8; i++) {
        		ok:
                for (int j = 1; j < 6; j++) {
                    while (tiles[i][j].value!=0) {
                    	if(tiles[i][j-1].value==0 && (i<7 && tiles[i+1][j].value==0)) {
                    		doMove(tiles[i][j],tiles[i][j-1],DirectEnum.LEFT);
                    		if(i<7 && tiles[i+1][j-1].value!=0) {
                    			createTile();
                    		}
                    		break ok;
                    	} else {
                    		break;
                    	}
                    }
                }
            }
        	return isMove;
        }

        private boolean moveRight() {
        	isMove = false;
        	for (int i = 0; i < 8; i++) {
        		ok:
                for (int j = 0; j < 5; j++) {
                    while (tiles[i][j].value!=0) {
                    	if(tiles[i][j+1].value==0 && (i<7 && tiles[i+1][j].value==0)) {
                    		doMove(tiles[i][j],tiles[i][j+1],DirectEnum.RIGHT);
                    		if(i<7 && tiles[i+1][j+1].value!=0) {
                    			createTile();
                    		}
                    		break ok;
                    	} else {
                    		break;
                    	}
                    }
                }
            }
        	return isMove;
        }

        private boolean moveDown() {
        	isMove = false;
        	for (int j = 0; j < 6; j++) {
                for (int i = 0; i < 8; i++) {
                    while (tiles[i][j].value!=0) {
                    	if(i<7 && tiles[i+1][j].value==0) {
                    		doMove(tiles[i][j],tiles[i+1][j],DirectEnum.DOWN);
                    		if(i==6 || (i<6 && tiles[i+2][j].value!=0)) {
                    			if(i<6 && tiles[i+2][j].value!=0 && tiles[i+1][j].value==tiles[i+2][j].value) {
                    				doMerge(tiles[i+1][j], tiles[i+2][j], DirectEnum.DOWN);
                    			}
                    			isMove=true;
                    		} else if (i<7 && tiles[i+1][j].value == tiles[i][j].value) {
                                doMerge(tiles[i][j], tiles[i+1][j], DirectEnum.DOWN);
                        	} 
                    		break;
                    	} else if (i<7 && tiles[i+1][j].value == tiles[i][j].value) {
                            doMerge(tiles[i][j], tiles[i+1][j], DirectEnum.DOWN);
                            break;
                    	} else if(j>0 && tiles[i][j-1].value == tiles[i][j].value) {
                    		doMerge(tiles[i][j-1],tiles[i][j],DirectEnum.RIGHT);
                    		for(int k = 1; k < 8; k++) {
                    			if(tiles[i-1][j-1].value!=0 && i>0) {
                        			doMove(tiles[i-1][j-1],tiles[i][j-1],DirectEnum.DOWN);
                        			i--;
                        		}
                    		}
                    	} else if(j<5 && tiles[i][j+1].value == tiles[i][j].value) {
                    		doMerge(tiles[i][j+1],tiles[i][j],DirectEnum.LEFT);
                    		for(int k = 1; k < 8; k++) {
                    			if(tiles[i-1][j+1].value!=0 && i>0) {
                        			doMove(tiles[i-1][j+1],tiles[i][j+1],DirectEnum.DOWN);
                        			i--;
                        		}
                    		}
                    	} else {
                    		break;
                    	}
                    }
                }
        	}
        	isMove=true;
        	return isMove;
        }
        
        private void doMove(Tile src, Tile dst, DirectEnum directEnum) {
        	dst.swap(src);
            dst.step++;
            dst.directEnum = directEnum;
            src.clear();
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
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 6; j++) {
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
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
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
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 6; j++) {
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
                t.stop();
            }

        }
        
        private void drawTile(Graphics gg, int i, int j, int mx, int my, int ex) {
            Graphics2D g = (Graphics2D) gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

            Tile tile = tiles[i][j];
            int x = GAP_TILE + (GAP_TILE + SIZE_TILE) * j + mx - ex;
            int y = HEIGHT_MD + 1 + GAP_TILE + (GAP_TILE + SIZE_TILE) * i + my - ex;
            int s = SIZE_TILE;
            g.drawImage(tile.setImage(tile.n2), x, y, s, s, this);

        }

        public void run() {
            while (true) {
            	isMove=false;
            	for (int j = 0; j < 6; j++) {
            		ok:
                    for (int i = 0; i < 8; i++) {
                        while (tiles[i][j].value!=0) {
                        	if(i<7 && tiles[i+1][j].value==0) {
                        		doMove(tiles[i][j],tiles[i+1][j],DirectEnum.DOWN);
                        		if(i==6 || (i<6 && tiles[i+2][j].value!=0)) {
                        			if(i<6 && tiles[i+2][j].value!=0 && tiles[i+1][j].value==tiles[i+2][j].value) {
                        				doMerge(tiles[i+1][j], tiles[i+2][j], DirectEnum.DOWN);
                        			}
                        			isMove=true;
                        		} else if (i<7 && tiles[i+1][j].value == tiles[i][j].value) {
                                    doMerge(tiles[i][j], tiles[i+1][j], DirectEnum.DOWN);
                            	} 
                        		break ok;
                        	} else {
                        		break;
                        	}
                        }
                    }
            	}
            	invokeAnimate();
                checkGameOver(true);
            	repaint();
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ex) {
                }
            }
        }
        
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

    }

}

	