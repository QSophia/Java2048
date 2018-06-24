package com.rank;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class paihangbang extends JFrame{
	public static String nameOfFont= "Dialog";
	public static int isBold= 1;
	public static int sizeOfFont= 18;
	public static Font f = new Font(nameOfFont, isBold, sizeOfFont);
	public int distance=18;
	public String distanceToLeft() {
		String longOfDistance=" ";
		for(int i=0;i<distance;i++) {
			longOfDistance=longOfDistance+" ";
		}
		return longOfDistance;
	}

	public final int heightOfFrame = 500;
	public final int widthOfFrame = 400;
	public paihangbang(String filePath) {
		 	//JPanel panel = new JPanel();
		 	JPanel panel = new MyPanel();
	        getContentPane().add(panel, BorderLayout.CENTER);
	        panel.setLayout(new GridLayout(12, 2, 10, 5));
	 
	        JPanel panel_1 = new JPanel();
	        panel.add(panel_1);
	        JLabel lblNewLabel = new JLabel("");
	        panel_1.setOpaque(false);
	        panel_1.add(lblNewLabel);
	 
	        JPanel panel_2 = new JPanel();
	        panel.add(panel_2);
	        JLabel label = new JLabel("");
	        panel_2.setOpaque(false);
	        panel_2.add(label);

		JPanel panel_3 = new JPanel();
		panel.add(panel_2);
		JLabel label3 = new JLabel("");
		panel_3.setOpaque(false);
		panel.add(label3);

		JPanel panel_4 = new JPanel();
		panel.add(panel_2);
		JLabel label4 = new JLabel("");
		panel_4.setOpaque(false);
		panel.add(label4);
	 
	        JLabel data[]=new JLabel[20];
	        for(int i=0;i<20;i++) {
	        	data[i]=new JLabel(distanceToLeft()+xieru.readTxtFile(filePath,i));
	        	data[i].setFont(f);
	        	//System.out.println(readTxtFile(i));
	        	panel.add(data[i]);
	        }

	 
	        setSize(widthOfFrame,heightOfFrame);//���ڴ�С
	        setLocationRelativeTo(null);//����
	        setDefaultCloseOperation(EXIT_ON_CLOSE);//�˳�
	        setTitle("排行榜");//����
	        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	

}
class MyPanel extends JPanel{
    Image image=null;
    public void paintComponent(Graphics g){
        try {
            image=ImageIO.read(new File("src/resource/img/rank.png"));
            g.drawImage(image, 0, 0, 400, 480, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}