
import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Credit1 extends JFrame implements ActionListener 
{
	JPanel p1;
	JTabbedPane jtp;
	JButton sub1,back;
	JTextField acc,r2000,r500,r100,r50,r10;
	JLabel total;
	
	Credit1()
	{
		
		acc=new JTextField(20);
		r2000=new JTextField("0",20);
		r500=new JTextField("0",20);
		r100=new JTextField("0",20);
		r50=new JTextField("0",20);
		r10=new JTextField("0",20);
		total=new JLabel();
		sub1=new JButton("Submit");
                back=new JButton("Back");
		sub1.addActionListener(this);
		back.addActionListener(this);
                
		p1=new JPanel(new GridLayout(9,2,20,20));
		p1.add(new JLabel(" Account No :"));
		p1.add(acc);
		p1.add(new JLabel(" Currency Domination"));
		p1.add(new JLabel(" "));
		p1.add(new JLabel("  2000 x "));
		p1.add(r2000);
		p1.add(new JLabel("  500 x "));
		p1.add(r500);
		p1.add(new JLabel("  100 x "));
		p1.add(r100);
		p1.add(new JLabel("  50 x "));
		p1.add(r50);
		p1.add(new JLabel("  10 x "));
		p1.add(r10);
		p1.add(total);
                p1.add(new JLabel(" "));
                p1.add(back);
		p1.add(sub1);
		
		jtp=new JTabbedPane();
		jtp.addTab("Cash", p1);
		
		setLayout(new FlowLayout());
		add(jtp);
		setSize(650,500);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
            if(arg0.getSource()==back)
            {
                Main m=new Main(new javax.swing.JFrame(), true);
                setVisible(false);
                m.setVisible(true);
            }
            else if(arg0.getSource()==sub1)
            {
            	HomomorphicFunctions hf=new HomomorphicFunctions();
            	BigInteger id=null,wbal=null,newwbal=null;
            	int tot=0;
            	tot=(Integer.parseInt(r2000.getText())*2000);
            	tot+=(Integer.parseInt(r500.getText())*500);
            	tot+=(Integer.parseInt(r100.getText())*100);
            	tot+=(Integer.parseInt(r50.getText())*50);
            	tot+=(Integer.parseInt(r10.getText())*10);
		
            	total.setText("  Total :"+tot);
		
            	try
            	{  
            		Class.forName("com.mysql.jdbc.Driver");  
            		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/java_project","root","root");
            		Statement stmt=con.createStatement(); 
			
                        ResultSet rs=stmt.executeQuery("select pan from customer_details where Account_No="+acc.getText());
                        rs.last();
                        if(rs.getRow()!=1)
                        {
                            JOptionPane.showMessageDialog(this,"Wrong Sender's Account no.","Balance",JOptionPane.INFORMATION_MESSAGE);
                            acc.setText("");
                        }
                        else
                        {
                        String pp=rs.getString("pan");
                        rs=stmt.executeQuery("select * from keyval where pan='"+pp+"'");
                        rs.first();
                        hf.GeneratingValues(new BigInteger(rs.getString(2)),new BigInteger(rs.getString(3)));
                        
                        rs=stmt.executeQuery("select * from balance");
                        while(rs.next())
                        {
                        	id=new BigInteger(rs.getString(1));
                        	BigInteger account=new BigInteger(hf.Decryption(id)+"");
                        	BigInteger tempacc=new BigInteger(acc.getText());
                        	if(account.compareTo(tempacc)==0)
                        	{
                        		wbal=new BigInteger(rs.getString(2));
                        		break;
                        	}
                        }
						newwbal=hf.add(wbal,hf.Encryption(new BigInteger(tot+"")));
						String query="update balance set wbal=? where acc=?";
						PreparedStatement pstmt1=con.prepareStatement(query);
						pstmt1.setString(1, newwbal.toString());
						pstmt1.setString(2, id.toString());
						
                        rs=stmt.executeQuery("select t_id from value");
                        rs.first();
                        int t_id=rs.getInt(1);
					
                        query="update value set t_id=? where t_id=?";
                        PreparedStatement pstmt2=con.prepareStatement(query);
                        pstmt2.setInt(1, (t_id+1));
                        pstmt2.setInt(2, t_id);
					
                        query="insert into t_log values(?,?,?,?,?,?)";
                        PreparedStatement pstmt3=con.prepareStatement(query);
                        pstmt3.setInt(1,(t_id));
                        pstmt3.setString(2,"Credit");
                        pstmt3.setString(3,tot+"");
                        pstmt3.setInt(4,Integer.parseInt(acc.getText()));
                        pstmt3.setString(5,new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
                        pstmt3.setInt(6,0);
                                                
                         rs=stmt.executeQuery("select Email from customer_details where pan='"+pp+"'");
                        String mail=""; 
                        while(rs.next())
                        {
                            mail=rs.getString(1);
                        }
                        SendEmail sm=new SendEmail();
                        int i=sm.mail(Integer.parseInt(acc.getText()),mail,"credit",tot+"",hf.Decryption(newwbal)+"","");
                        if(i==1)
                        {
                        	pstmt1.executeUpdate();
                        	pstmt2.executeUpdate();
                        	pstmt3.executeUpdate();
                        	con.close();
                        	JOptionPane.showMessageDialog(this,"Transaction Successfull !!","Balance",JOptionPane.INFORMATION_MESSAGE);
                            Main m=new Main(new javax.swing.JFrame(), true);
                            setVisible(false);
                            m.setVisible(true);
                        }
                        else
                        	JOptionPane.showMessageDialog(this,"Check your Internet Connection and Try again","Connection Error",JOptionPane.INFORMATION_MESSAGE);
                        }
                       
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		}
	}


}
