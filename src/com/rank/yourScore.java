package com.rank;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class yourScore extends JFrame{

    public static String filePathOriginal = "src/resource/data/ResultO.txt";
    public static String TOTALOriginal = "src/resource/data/gettotalO.txt";
    public static String filePathMerge = "src/resource/data/ResultM.txt";
    public static String TOTALMerge = "src/resource/data/gettotalM.txt"; 
    public static String filePathFall = "src/resource/data/ResultF.txt";
    public static String TOTALFall = "src/resource/data/gettotalF.txt";
	
	
	public yourScore(final String filePath,final String TOTAL, int score) {
		final int SCORE=score;
		int total=xieru.getTotal(TOTAL);
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(6, 1, 5, 2));
 
        JPanel panel_1 = new JPanel();
        panel.add(panel_1);
 
        JLabel lblNewLabel = new JLabel("用户名");
        panel_1.add(lblNewLabel);
 
        final JTextField name = new JTextField();
        panel_1.add(name);
        name.setColumns(10);
 
        JPanel panel_2 = new JPanel();
        panel.add(panel_2);
 
        JLabel label = new JLabel("分数:  "+score);
        panel_2.add(label);

        JPanel panel_6 = new JPanel();
        panel.add(panel_6);
 
        JButton jbreg = new JButton("确认");
        panel_6.add(jbreg);
        JButton jbrest = new JButton("取消");
        panel_6.add(jbrest);
 
        JPanel panel_7 = new JPanel();
        getContentPane().add(panel_7, BorderLayout.SOUTH);
 
        final JLabel reginfo = new JLabel(" ");
        panel_7.add(reginfo);
 
        setSize(380,300);//���ڴ�С
        setLocationRelativeTo(null);//����
        setDefaultCloseOperation(EXIT_ON_CLOSE);//�˳�
        setTitle("排行榜");//����
        
        jbreg.addActionListener(new ActionListener() {//ע�ᰴť���¼�
        	 
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                String id = name.getText();
                sb.append("用户名:" + id);
                String pas = Integer.toString(SCORE);
                sb.append(" 得分:" + pas);
                //System.out.print("AS");
                
                try { 
                	   try{  
                		  // System.out.println("AS");  
                		   FileWriter fw = new FileWriter(filePath,true);//ͬʱ�������ļ�  
                		      //�����ַ����������  
                		      BufferedWriter bf = new BufferedWriter(fw);  
                		      //���������ַ����������  
                		      xieru.pulsTotal(TOTAL);
                		     // System.out.println(xieru.getTotal(TOTAL)); 
                		      bf.append(id);
                		      bf.append('\r');
                		      bf.append(pas);
                		      bf.append('\r');
                		      bf.flush();  
                		      bf.close();
                		     // System.out.println("AS"); 
                		     }catch (IOException e23){  
                		     // System.out.println("AS"); 
                		     }                 		    
                	xieru.writefile(filePath,TOTAL);
                    setVisible(false);
                	new paihangbang(filePath).setVisible(true);
                	
                }catch(Exception e2) {
                	;
                }
                
                reginfo.setText(sb.toString());
            }
        });

        jbrest.addActionListener(new ActionListener() {//ע�ᰴť���¼�

            @Override
            public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    new paihangbang(filePath).setVisible(true);
            }
        });

        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

}
