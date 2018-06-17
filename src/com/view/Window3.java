package com.view;

import javax.swing.*;

import com.main.WaveThread;
import com.main.DirectEnum;
import java.util.Collections;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.io.*;

public class Window3 extends JFrame {

    private static int score = 0; //分数
    private final int WIDTH_WIN = 400;
    private final int HEIGHT_WIN = 500;
    private final String TITLE = "";
    private final Font[] fonts = {new Font("Helvetica Neue", Font.BOLD, 48)
            , new Font("Helvetica Neue", Font.BOLD, 42)
            , new Font("Helvetica Neue", Font.BOLD, 36)
            , new Font("Helvetica Neue", Font.BOLD, 30)
            , new Font("Helvetica Neue", Font.BOLD, 24)
    };

    private GameBoard gameBoard;
    private JLabel ltitle;
    private JLabel lsctip;
    private JLabel lscore;
    private JLabel lgatip;

    public Window3() {
        setWin();
        //setUndecorated(false);
    }

    public void setWin() {
        setLayout(null);
        initView();
        setTitle(TITLE);
        getContentPane().setPreferredSize(new Dimension(WIDTH_WIN, HEIGHT_WIN));
        //JFrame直接调用setBackground设置背景色不生效
        getContentPane().setBackground(new Color(0xfaf8ef));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); //去掉最大化按钮
        pack();    //获得最佳大小，同时会影响界面生成位置
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void initView() {			//��ʼ��ҳ��
        ltitle = new JLabel("2048", JLabel.CENTER);
        ltitle.setFont(new Font("", Font.BOLD, 50));
        ltitle.setForeground(new Color(0x776e65));
        ltitle.setBounds(0, 0, 120, 60);

        lsctip = new JLabel("SCORE", JLabel.CENTER);
        lsctip.setFont(new Font("", Font.BOLD, 16));
        lsctip.setForeground(new Color(0xeee4da));
        lsctip.setOpaque(true);//ֻ������Ϊtrue����ɫ����Ч
        lsctip.setBackground(new Color(0xbbada0));
        lsctip.setBounds(290, 5, 100, 25);

        lscore = new JLabel("0", JLabel.CENTER);
        lscore.setFont(new Font("Helvetica Neue", Font.BOLD, 22));
        lscore.setForeground(Color.WHITE);
        lscore.setOpaque(true);
        lscore.setBackground(new Color(0xbbada0));
        lscore.setBounds(290, 30, 100, 25);

        lgatip = new JLabel("����������Կ��Ʒ�����ƶ�����ESC���������¿�ʼ��Ϸ��", JLabel.LEFT);
        lgatip.setFont(new Font("Helvetica Neue", Font.ITALIC, 13));
        lgatip.setForeground(new Color(0x776e65));
        lgatip.setBounds(10, 60, 390, 30);
        //��Ϸ������
        gameBoard = new GameBoard();
        gameBoard.setPreferredSize(new Dimension(400, 400));
        gameBoard.setBackground(new Color(0xbbada0));
        gameBoard.setBounds(0, 100, 400, 400);
        gameBoard.setFocusable(true);
        //��������봰��
        this.add(ltitle);
        this.add(lsctip);
        this.add(lscore);
        this.add(lgatip);
        this.add(gameBoard);
    }

    
    class GameBoard extends JPanel implements MouseListener {
        private static final int GAP_TILE = 16; //��Ƭ֮���϶
        private static final int ARC_TILE = 16; //��ƬԲ�ǻ���
        private static final int SIZE_TILE = 80;//��Ƭ�Ĵ�С
        private static final int PAINT_NUM = 20;//�ƶ��Ĺ��̷ֳ�X�λ���

        private Tile[][] tiles = new Tile[4][4];
        private Color bgColor;
        private boolean isOver;
        private boolean isMove;
        private boolean isMerge;
        private boolean isAnimate;
        int x,y,uValue,lValue,rValue,dValue;
        int []a=new int[4];
         array[] Array=new array[4];

        public GameBoard() {
            initGame();
            bgColor = new Color(0xbbada0);
            addMouseListener(this);
        }


        private void initGame() {
            //��ʼ����ͼ
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tiles[i][j] = new Tile();
                }
            }
            //����������Ƭ
            createTile();
            createTile();

            isOver = false;
            isAnimate = true;
        }

        private void createTile() {
            //��ȡ��ǰ�հ׵���Ƭ���������б�
            List<Tile> list = getBlankTiles();
            if (!list.isEmpty()) {
                Random random = new Random();
                int index = random.nextInt(list.size());
                Tile tile = list.get(index);
                //��ʼ������Ƭ��ֵΪ2��4
                tile.value = random.nextInt(100) > 50 ? 4 : 2;
            }
        }

        /**
         * ��ȡ��ǰ�հ׵���Ƭ�������б���
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
         * ���ƶ���Ч��
         */
        private void invokeAnimate() {
            if (isMerge) {
                new WaveThread("merge.wav").start();
                moveAnimate();
                mergeAnimate();
            } else if (isMove) {
                new WaveThread("move.wav").start();
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
            int k = -3; //ʹ�����ex��0��ʼ
            while (k < 4) {
                int ex = 9 - k ^ 2; //�����߹�ʽ
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
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_NORMALIZE);
            Tile tile = tiles[i][j];
            //������Ƭ����
            g.setColor(tile.getBackground());
            //ע�⣺��������j����,��������i����
            g.fillRoundRect(GAP_TILE + (GAP_TILE + SIZE_TILE) * j + mx - ex,
                    GAP_TILE + (GAP_TILE + SIZE_TILE) * i + my - ex,
                    SIZE_TILE + ex * 2, SIZE_TILE + ex * 2, ARC_TILE, ARC_TILE);
            //������Ƭ����
            g.setColor(tile.getForeground());
            Font font = tile.getTileFont();
            g.setFont(font);
            FontMetrics fms = getFontMetrics(font);
            String value = String.valueOf(tile.value);
            //ע�⣺��������j����,��������i����
            g.drawString(value, GAP_TILE + (GAP_TILE + SIZE_TILE) * j
                    + (SIZE_TILE - fms.stringWidth(value)) / 2 + mx - ex
                    , GAP_TILE + (GAP_TILE + SIZE_TILE) * i
                    + (SIZE_TILE - fms.getAscent() - fms.getDescent()) / 2 + my - ex
                    + fms.getAscent());
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
        	/*for(int i=3;i>-1;i--)
        		for(int j=0;j<i;j++) {
        			if(a[j]<a[j+1]) {
        				int temp;
        				temp=a[j];a[j]=a[j+1];a[j+1]=temp;
        			}
        		}
        	int temp;
        	temp=a[0];a[0]=a[3];a[3]=temp;
        	temp=a[1];a[1]=a[2];a[2]=temp;*/
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
        		System.out.println(getValue());
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
                //��ǰ�ƶ���Ƭ���ܵ���߽磬����Ϊ�հ���Ƭ��ǰ����Ƭ�����Ǻϳ���Ƭ
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
                    //��ǰ�ƶ���Ƭ���ܵ���߽磬����Ϊ�հ���Ƭ��ǰ����Ƭ�����Ǻϳ���Ƭ
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
                    //��ǰ�ƶ���Ƭ���ܵ���߽磬����Ϊ�հ���Ƭ��ǰ����Ƭ�����Ǻϳ���Ƭ
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
                    //��ǰ�ƶ���Ƭ���ܵ���߽磬����Ϊ�հ���Ƭ��ǰ����Ƭ�����Ǻϳ���Ƭ
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
                //��ǰ�ƶ���Ƭ���ܵ���߽磬����Ϊ�հ���Ƭ��ǰ����Ƭ�����Ǻϳ���Ƭ
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
                    //��ǰ�ƶ���Ƭ���ܵ���߽磬����Ϊ�հ���Ƭ��ǰ����Ƭ�����Ǻϳ���Ƭ
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
                    //��ǰ�ƶ���Ƭ���ܵ���߽磬����Ϊ�հ���Ƭ��ǰ����Ƭ�����Ǻϳ���Ƭ
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
                        //��ǰ�ƶ���Ƭ���ܵ���߽磬����Ϊ�հ���Ƭ��ǰ����Ƭ�����Ǻϳ���Ƭ
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
		public void mouseClicked(MouseEvent e) {
        	
			
			
		}


		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void mousePressed(MouseEvent e) {
			if(tiles[(int)e.getY()/100][(int)e.getX()/100].value==0) {
			y=(int)e.getX()/100;
			x=(int)e.getY()/100;}	
			else return;
		}
		


		@Override
		public void mouseReleased(MouseEvent e) {
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
            repaint();
			
		}


    }

    class Tile {
        public int step; //�ƶ��Ĳ���
        public int value;//��ʾ������
        public DirectEnum directEnum;//�ƶ��ķ���
        public boolean ismerge;//�Ƿ��Ǻϲ���

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

}
