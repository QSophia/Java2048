package com.view;

import com.main.DirectEnum;
import com.main.WaveThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 下落模式
 *
 * 
 */

public class Window2 extends JFrame{
	
	private static int score = 0; //分数
	final Font[] fonts = {new Font("Helvetica Neue", Font.BOLD, 48)
            , new Font("Helvetica Neue", Font.BOLD, 42)
            , new Font("Helvetica Neue", Font.BOLD, 36)
            , new Font("Helvetica Neue", Font.BOLD, 30)
            , new Font("Helvetica Neue", Font.BOLD, 24)
    };
	
	GameBoard gameBoard=new GameBoard();
    private JLabel ltitle;
    private JLabel lsctip;
    private JLabel lscore;
    private JLabel lgatip;

    public Window2() {
        this.setLayout(null);
        this.setWin();
    }
    public void setWin() {
        initView();
        setTitle("2048下落模式");
        getContentPane().setPreferredSize(new Dimension(395, 625));
        //JFrame直接调用setBackground设置背景色不生效
        getContentPane().setBackground(new Color(0xfaf8ef));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); //去掉最大化按钮
        pack();    //获得最佳大小，同时会影响界面生成位置
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void initView() { //初始化窗体
    	ltitle = new JLabel("下落模式", JLabel.CENTER);
    	ltitle.setFont(new Font("", Font.BOLD, 36));
        ltitle.setForeground(new Color(0x776e65));
        ltitle.setBounds(0, 0, 160, 60);
        
        lsctip = new JLabel("SCORE", JLabel.CENTER);
        lsctip.setFont(new Font("", Font.BOLD, 16));
        lsctip.setForeground(new Color(0xeee4da));
        lsctip.setOpaque(true); //只有设置为true背景色才生效
        lsctip.setBackground(new Color(0xbbada0));
        lsctip.setBounds(290, 5, 100, 25);
        
        lscore = new JLabel("0", JLabel.CENTER);
        lscore.setFont(new Font("Helvetica Neue", Font.BOLD, 22));
        lscore.setForeground(Color.WHITE);
        lscore.setOpaque(true);
        lscore.setBackground(new Color(0xbbada0));
        lscore.setBounds(290, 30, 100, 25);
        
        lgatip = new JLabel("方向键左右控制方块，方向键下加速下落，按ESC键可以重新开始游戏。", JLabel.LEFT);
        lgatip.setFont(new Font("Helvetica Neue", Font.ITALIC, 12));
        lgatip.setForeground(new Color(0x776e65));
        lgatip.setBounds(10, 60, 390, 30);
        
        //游戏面板组件
        gameBoard = new GameBoard();
        gameBoard.setPreferredSize(new Dimension(400, 600));
        gameBoard.setBackground(new Color(0xbbada0));
        gameBoard.setBounds(0, 100, 400, 600);
        gameBoard.setFocusable(true);
        
        //把组件加入窗体
        this.add(ltitle);
        this.add(lsctip);
        this.add(lscore);
        this.add(lgatip);
        this.add(gameBoard);
        
        Thread t=new Thread(gameBoard);
    	t.start();
    }
    
    class GameBoard extends JPanel implements Runnable, KeyListener { //游戏面板类
    	private static final int GAP_TILE = 5; //瓦片之间间隙
        private static final int ARC_TILE = 16; //瓦片圆角弧度
        private static final int SIZE_TILE = 60; //瓦片的大小
        private static final int PAINT_NUM = 20; //移动的过程分成X段绘制
        
        private Tile[][] tiles = new Tile[8][6];
        private Color bgColor;
        private boolean isOver;
        private boolean isMove;
        private boolean isMerge;
        private boolean isAnimate;
        int time = 800;
        
        public GameBoard() {
            initGame();
            bgColor = new Color(0xbbada0);
            addKeyListener(this);
        }

        @Override
        public void keyPressed(KeyEvent e) { //按键事件
        	boolean moved = false;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    initGame();
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
        
        private void initGame() { //初始化游戏
        	score=0;
            //初始化地图
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    tiles[i][j] = new Tile();
                }
            }
            createTile();
            isOver = false;
            isAnimate = true;
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
        		isOver=true;
        	else
        		isOver=false;
        }

        private boolean moveLeft() {
        	isMove = false;
        	for (int i = 0; i < 8; i++) {
                for (int j = 1; j < 6; j++) {
                    while (tiles[i][j].value!=0) {
                    	if(tiles[i][j-1].value==0 && (i<7 && tiles[i+1][j].value==0)) {
                    		doMove(tiles[i][j],tiles[i][j-1],DirectEnum.LEFT);
                    		if(i<7 && tiles[i+1][j-1].value!=0) {
                    			createTile();
                    		}
                    		for(int k = 1; k < 8; k++) {
                    			if(tiles[i-1][j-1].value!=0 && i>0) {
                        			doMove(tiles[i-1][j-1],tiles[i][j-1],DirectEnum.DOWN);
                        			i--;
                        		}
                    		}
                    		break;
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
                    		for(int k = 1; k < 8; k++) {
                    			if(tiles[i-1][j+1].value!=0 && i>0) {
                        			doMove(tiles[i-1][j+1],tiles[i][j+1],DirectEnum.DOWN);
                        			i--;
                        		}
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
        	ok:
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
                new WaveThread("merge.wav").start();
                moveAnimate();
                mergeAnimate();
                createTile();
                isMerge = false;
            } else if (isMove) {
                new WaveThread("move.wav").start();
                moveAnimate();
                createTile();
                isMove = false;
            }
            /*if (isMerge || isMove) {
                createTile();
                isMerge = false;
                isMove = false;
            }*/
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

        private void moveAnimate() {
            isAnimate = false;
            Graphics gg = getGraphics();
            Image image = this.createImage(getWidth(), getHeight());
            Graphics g = image.getGraphics();
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
            int k = 0; //
            while (k < PAINT_NUM) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 6; j++) {
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
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    tiles[i][j].step = 0;
                    tiles[i][j].directEnum = DirectEnum.NONE;
                }
            }
            isAnimate = true;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
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
            }
        }

        private void drawTile(Graphics gg, int i, int j, int mx, int my, int ex) {
            Graphics2D g = (Graphics2D) gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

            Tile tile = tiles[i][j];
            int x = GAP_TILE + (GAP_TILE + SIZE_TILE) * j + mx - ex;
            int y = GAP_TILE + (GAP_TILE + SIZE_TILE) * i + my - ex;
            int s = SIZE_TILE + 10;
            g.drawImage(tile.setImage(tile.n2), x, y, s, s, this);

        }

        @Override
        public void keyTyped(KeyEvent e) {//空函数

        }

        @Override
        public void keyReleased(KeyEvent e) {//空函数
        	
        }

        public void run() {
            while (true){
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
                        	} else if (i<7 && tiles[i+1][j].value == tiles[i][j].value) {
                                doMerge(tiles[i][j], tiles[i+1][j], DirectEnum.DOWN);
                                break ok;
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
            	invokeAnimate();
                checkGameOver(true);
            	repaint();
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

}

	