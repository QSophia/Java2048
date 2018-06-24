package com.rank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.io.*;
 
public class xieru extends JFrame {
    private JTextField name;
    private JTextField psw;
    public xieru(final String filePath, final String TOTAL) {
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(6, 1, 5, 2));
 
        JPanel panel_1 = new JPanel();
        panel.add(panel_1);
 
        JLabel lblNewLabel = new JLabel("  ");
        panel_1.add(lblNewLabel);
 
        final JTextField name = new JTextField();
        panel_1.add(name);
        name.setColumns(10);
 
        JPanel panel_2 = new JPanel();
        panel.add(panel_2);
 
        JLabel label = new JLabel("  ");
        panel_2.add(label);
 
        final JTextField psw = new JTextField();
        panel_2.add(psw);
        psw.setColumns(10);
 
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
                String pas = psw.getText();
                sb.append(" 得分:" + pas);
                
                
                try { 
                	   try{  
                		        
                		   FileWriter fw = new FileWriter(filePath,true);//ͬʱ�������ļ�  
                		      //�����ַ����������  
                		      BufferedWriter bf = new BufferedWriter(fw);  
                		      //���������ַ����������  
                		      pulsTotal(TOTAL);
                		      bf.append(id);
                		      bf.append('\r');
                		      bf.append(pas);
                		      bf.append('\r');
                		      bf.flush();  
                		      bf.close(); 
                		     }catch (IOException e23){  
                		      ;  
                		     }                 		    
                	writefile(filePath,TOTAL);
                	
                }catch(Exception e2) {
                	;
                }
                
                reginfo.setText(sb.toString());
            }
        });
    }

    public static void readTxtFile(String filePath){
        try {
                String encoding="UTF-8";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//���ǵ������ʽ
                    BufferedReader bufferedReader = new BufferedReader(read);//���������buffer
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){//���������ȡ������
                        System.out.println(lineTxt);
                    }
                    read.close();
        }else{
            System.out.println("出错");
        }
        } catch (Exception e) {
            System.out.println("出错");
            e.printStackTrace();
        }
     
    }
    
    public static void writefile(String filePath,String TOTAL) throws IOException {	
    	try {
            String encoding="UTF-8";
            
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//���ǵ������ʽ
                BufferedReader bufferedReader = new BufferedReader(read);//���������buffer
                int a[]=new int[100];
                String b[]=new String[100];
                int num=0,tmp=1;
				while(num<getTotal(TOTAL)){//���������ȡ������
                	b[num]=bufferedReader.readLine();
                	String t=bufferedReader.readLine();
                	tmp=Integer.parseInt(t);               	
                	a[num]=tmp;
                  	num++;
                }
                int temp = 0; 
                String YH;
                for (int i = 0; i < a.length-1; i++) {         //��һ��forѭ����������Ҫ�߶����ˣ������n-1������  
                    for (int j = 0; j < a.length-1-i; j++) {       //��2��forѭ������ÿ�˱Ƚ϶��ٴ�  
                        if(a[j+1]>a[j]){                 //�����������  
                            temp = a[j];  
                            a[j] = a[j+1];  
                            a[j+1] = temp;  
                             YH=b[j];  
                            b[j] = b[j+1];  
                            b[j+1]=YH; 
                        }  
                    }  
                }
                read.close();
                File FILE = new File(filePath);
                FileWriter fileWriter = new FileWriter(FILE); 
                for (int i = 0; a[i]!=0; i++) {
                	
                	fileWriter.write(String.valueOf(b[i]+"\r"));
                    fileWriter.write(String.valueOf(a[i]+"\r"));
                    
                }
                    fileWriter.flush();
                    fileWriter.close();
    }else{
        System.out.println("出错");
    }
    } catch (Exception e) {
        System.out.println("出错");
        e.printStackTrace();
    } 
    }
    
    public static String readTxtFile(String filePath,int k){
        try {
        		
                String encoding="UTF-8";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//���ǵ������ʽ
                    BufferedReader bufferedReader = new BufferedReader(read);//���������buffer
                    String lineTxt = null;
                        for(int i=0;i<=k;i++) lineTxt = bufferedReader.readLine();
                        read.close();
                        return lineTxt;
                    
        }else{
            System.out.println("出错");
        }
        } catch (Exception e) {
            System.out.println("出错");
            e.printStackTrace();
        }
		return "";
     
    }
    
    public static int getTotal(String filePath) {
    	try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//���ǵ������ʽ
                BufferedReader bufferedReader = new BufferedReader(read);//���������buffer
                int t=Integer.parseInt(bufferedReader.readLine());
                return t;
    }else{
        System.out.println("出错");
    }
    } catch (Exception e) {
        System.out.println("出错!!!!!");
        e.printStackTrace();
    }
		return 0;
    }
    
    public static void pulsTotal(String TOTAL)throws IOException {
    	int total=getTotal(TOTAL);
    	try{File FILE = new File(TOTAL);
        FileWriter fileWriter = new FileWriter(FILE); 
        int newTotal=total+1;
        fileWriter.write(String.valueOf(newTotal+"\r"));
        fileWriter.flush();
        fileWriter.close();}
    	catch (Exception e) {
            System.out.println("出错");
            e.printStackTrace();
        }   	
    }
    
 
    public static void main(String[] args) throws IOException{
    	//String filePath = "d:\\Result.txt";//�ļ�·������
        
        //System.out.println(readTxtFile(3));
        //new xieru().setVisible(true);
    	new paihangbang("src/resource/data/Result1.txt").setVisible(true);
    	//new yourScore().setVisible(true);
        
    }
}

