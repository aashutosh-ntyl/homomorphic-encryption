
import java.awt.event.*;
import java.sql.*;
import java.math.BigInteger;
import java.util.Random;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class Debit1 extends JFrame implements ActionListener 
{
	
	JPanel p1,p2;
	JTabbedPane tp;
	JButton sub1,sub2,back1,back2;
	JTextField accp,accs,amt1,amt2,acc2;
	private String pp;
	
	Debit1()
        {
		accp=new JTextField(20);
		acc2=new JTextField(20);
		accs=new JTextField(20);
		amt1=new JTextField(20);
		amt2=new JTextField(20);
		sub1=new JButton("Submit");
		sub1.addActionListener(this);
		sub2=new JButton("Submit");
        back1=new JButton("Back");
        back2=new JButton("Back");
		sub2.addActionListener(this);
        back1.addActionListener(this);
        back2.addActionListener(this);
		
		p1=new JPanel(new GridLayout(4,2,20,20));
		p1.add(new JLabel(" Sender's Account No :"));
		p1.add(accs);
		p1.add(new JLabel(" Payee's Account No :"));
		p1.add(accp);
		p1.add(new JLabel(" Amount :"));
		p1.add(amt1);
		p1.add(back1);
		p1.add(sub1);
		
		p2=new JPanel(new GridLayout(3,2,20,20));
		p2.add(new JLabel("  Account No :"));
		p2.add(acc2);
		p2.add(new JLabel("  Amount :"));
		p2.add(amt2);
		p2.add(back2);
		p2.add(sub2);
		
		tp=new JTabbedPane();
		tp.addTab("Account Transfer", p1);
		tp.addTab("Cash", p2);
		
		setLayout(new FlowLayout());
		add(tp);
		setSize(650,400);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		BigInteger id=null,wbal=null,fbal=null,newfbal=null,tempbal=null,newwbal=null,fmoney=null;
		int wmoney=0,fmoney2=0;
		try
		{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/java_project","root","root");
			Statement stmt=con.createStatement();
			HomomorphicFunctions hf=new HomomorphicFunctions();
			if(e.getSource()==back1)
                         {
                             Main m=new Main(new javax.swing.JFrame(), true);
                             setVisible(false);
                              m.setVisible(true);
                         } 
            else if(e.getSource()==back2)
                         {
                             Main m=new Main(new javax.swing.JFrame(), true);
                             setVisible(false);
                             m.setVisible(true);
                         } 
            else if(e.getSource()==sub1)
			{
				ResultSet rs=stmt.executeQuery("select pan from customer_details where Account_No="+accs.getText());
				rs.first();
				if(rs.getRow()!=1)
				{
					JOptionPane.showMessageDialog(this,"Wrong Sender's Account no.","Balance",JOptionPane.INFORMATION_MESSAGE);
					accs.setText("");
				}
				else
				{
                       pp=rs.getString("pan");
                       rs=stmt.executeQuery("select * from keyval where pan='"+pp+"'");
                       rs.first();
                       hf.GeneratingValues(new BigInteger(rs.getString(2)),new BigInteger(rs.getString(3)));
						
                       rs=stmt.executeQuery("select pan from customer_details where Account_No="+accp.getText());
                       rs.last();
                       if(rs.getRow()!=1)
                       {
                    	   JOptionPane.showMessageDialog(this,"Wrong Payee's Account no.","Balance",JOptionPane.INFORMATION_MESSAGE);
                           accp.setText("");
                       }
                        else
                        {                        
						pp=rs.getString("pan");
						System.out.println(pp);
                                                
                                                rs=stmt.executeQuery("select * from balance");
						while(rs.next())
						{
						id=new BigInteger(rs.getString(1));
						BigInteger account=new BigInteger(hf.Decryption(id)+"");
						BigInteger tempacc=new BigInteger(accs.getText());
						if(account.compareTo(tempacc)==0)
						{
						wbal=new BigInteger(rs.getString(2));
						fbal=new BigInteger(rs.getString(3));
						break;
						}
						}
                        String money=amt1.getText();
                        System.out.println(money);
						int strlen=money.length();
						char ch1='0';
                        char ch2='0';
                        if(money.charAt(strlen-3)=='.')
                        {
                        ch1= money.charAt(strlen-2);
                        ch2= money.charAt(strlen-1);
                        }
						/*else
						{
                        JOptionPane.showMessageDialog(this,"Wrong Format! Only two digits after the decimal.","Balance",JOptionPane.INFORMATION_MESSAGE);
                        }*/
                                             
						String cha=""+ch1+ch2;
                        System.out.println(cha);
						fmoney2=Integer.parseInt(cha);
						fmoney=new BigInteger(cha);
						float f=Float.parseFloat(money);
						wmoney=(int)f;

						if(wmoney>Integer.parseInt(hf.Decryption(wbal)+""))
						{
						JOptionPane.showMessageDialog(this,"Insufficient Balance","Balance",JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							newwbal=hf.Subtract(wbal,hf.Encryption(new BigInteger(wmoney+"")));
							if(fmoney2>Integer.parseInt(hf.Decryption(fbal)+""))
							{
							tempbal=hf.add(hf.Encryption(new BigInteger("100")),fbal);
							System.out.println(hf.Decryption(tempbal));
							newfbal=hf.Subtract(tempbal,hf.Encryption(fmoney));
							System.out.println(hf.Decryption(newfbal));
							newwbal=hf.Subtract(newwbal,hf.Encryption(new BigInteger("1")));
							System.out.println(hf.Decryption(newwbal));
							}
							else
							{
							newfbal=hf.Subtract(fbal,hf.Encryption(fmoney));
							}
							BigInteger snewbal,snewfbal;
                            snewbal=hf.Decryption(newwbal);
                            snewfbal=hf.Decryption(newfbal);
                                                
                            String query="update balance set wbal=? where acc=?";
                            PreparedStatement pstmt1=con.prepareStatement(query);
                            pstmt1.setString(1, newwbal.toString());
                            pstmt1.setString(2, id.toString());
				
                            query="update balance set fbal=? where acc=?";
                            PreparedStatement pstmt2=con.prepareStatement(query);
                            pstmt2.setString(1, newfbal.toString());
                            pstmt2.setString(2, id.toString());
                                        
                            rs=stmt.executeQuery("select * from keyval where pan='"+pp+"'");
                            rs.first();
                            hf.GeneratingValues(new BigInteger(rs.getString(2)),new BigInteger(rs.getString(3)));
                            rs=stmt.executeQuery("select * from balance");
                            while(rs.next())
                            {
                            	id=new BigInteger(rs.getString(1));
                            	BigInteger account=new BigInteger(hf.Decryption(id)+"");
                                BigInteger tempacc=new BigInteger(accp.getText());
                                if(account.compareTo(tempacc)==0)
                                {
                                      wbal=new BigInteger(rs.getString(2));
                                      fbal=new BigInteger(rs.getString(3));
                                      break;
                                }
                            }
                                        	newwbal=hf.add(wbal,hf.Encryption(new BigInteger(wmoney+"")));
                                        	newfbal=hf.add(fbal,hf.Encryption(fmoney));
					
                                        	query="update balance set wbal=? where acc=?";
                                        	PreparedStatement pstmt3=con.prepareStatement(query);
                                        	pstmt3.setString(1, newwbal.toString());
                                        	pstmt3.setString(2, id.toString());
				
                                        	query="update balance set fbal=? where acc=?";
                                        	PreparedStatement pstmt4=con.prepareStatement(query);
                                        	pstmt4.setString(1, newfbal.toString());
                                        	pstmt4.setString(2, id.toString());
                                        	
                                        	rs=stmt.executeQuery("select t_id from value");
                                        	rs.first();
                                        	int t_id=rs.getInt(1);
					
                                        	query="update value set t_id=? where t_id=?";
                                        	PreparedStatement pstmt5=con.prepareStatement(query);
                                        	pstmt5.setInt(1, (t_id+2));
                                        	pstmt5.setInt(2, t_id);
					
                                                query="insert into t_log values(?,?,?,?,?,?)";
                                                PreparedStatement pstmt6=con.prepareStatement(query);
                                                pstmt6.setInt(1,(t_id));
                                                pstmt6.setString(2,"Debit");
                                                pstmt6.setString(3,wmoney+"."+fmoney2);
                                                pstmt6.setInt(4,Integer.parseInt(accs.getText()));
                                                pstmt6.setString(5,new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
                                                pstmt6.setInt(6,0);
					
                                                query="insert into t_log values(?,?,?,?,?,?)";
                                                PreparedStatement pstmt7=con.prepareStatement(query);
                                                pstmt7.setInt(1,(t_id+1));
                                                pstmt7.setString(2,"Credit");
                                                pstmt7.setString(3,wmoney+"."+fmoney2);
                                                pstmt7.setInt(4,Integer.parseInt(accp.getText()));
                                                pstmt7.setString(5,new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
                                                pstmt7.setInt(6,0);
                                                
                                                rs=stmt.executeQuery("select Email from customer_details where Account_No="+accs.getText());
                                                String mail=""; 
                                                while(rs.next())
                                                {
                                                     mail=rs.getString(1);
                                                }
                                                SendEmail sm=new SendEmail();
                                                int i=sm.mail(Integer.parseInt(accs.getText()),mail,"Debit",amt1.getText(),snewbal+"."+snewfbal,"");
                                                
                                                rs=stmt.executeQuery("select Email from customer_details where Account_No="+accp.getText());
                                                while(rs.next())
                                                {
                                                     mail=rs.getString(1);
                                                }
                                                
                                               int j=sm.mail(Integer.parseInt(accp.getText()),mail,"Credit",amt1.getText(),hf.Decryption(newwbal)+"."+hf.Decryption(newfbal),"");
                                               if(i==1&&j==1)
                                               {
                                            	   pstmt1.executeUpdate();
                                            	   pstmt2.executeUpdate();
                                            	   pstmt3.executeUpdate();
                                            	   pstmt4.executeUpdate();
                                            	   pstmt5.executeUpdate();
                                            	   pstmt6.executeUpdate();
                                            	   pstmt7.executeUpdate();
                                            	   JOptionPane.showMessageDialog(this,"Transaction Successfull !!","Balance",JOptionPane.INFORMATION_MESSAGE);
                                            	   Main m=new Main(new javax.swing.JFrame(), true);
                                                   setVisible(false);
                                                   m.setVisible(true);
                                               }
                                               }
                                                
                                                
				}	
			}
			}
			
			else if(e.getSource()==sub2)
			{
                    ResultSet rs=stmt.executeQuery("select pan from customer_details where Account_No="+acc2.getText());
                    rs.last();
                    if(rs.getRow()!=1)
                    {
					JOptionPane.showMessageDialog(this,"Wrong Account no.","Balance",JOptionPane.INFORMATION_MESSAGE);
                    accs.setText("");
                    }
                    else
                    {
                    pp=rs.getString("pan");
                    rs=stmt.executeQuery("select * from keyval where pan='"+pp+"'");
                    rs.first();
                    hf.GeneratingValues(new BigInteger(rs.getString(2)),new BigInteger(rs.getString(3)));
                                                
                    rs=stmt.executeQuery("select * from balance");
					while(rs.next())
					{
					id=new BigInteger(rs.getString(1));
					BigInteger account=new BigInteger(hf.Decryption(id)+"");
					BigInteger tempacc=new BigInteger(acc2.getText());
					if(account.compareTo(tempacc)==0)
					{
					wbal=new BigInteger(rs.getString(2));
					fbal=new BigInteger(rs.getString(3));
					break;
					}
					
					}
					int amount=0;
					String money=amt2.getText();
					try
					{
					amount=Integer.parseInt(money+"");
					if(amount>Integer.parseInt(hf.Decryption(wbal)+""))
					{
						JOptionPane.showMessageDialog(this,"Insufficient Balance","Balance",JOptionPane.INFORMATION_MESSAGE);
						amt2.setText("");
					}
					else
					{
						
					newwbal=hf.Subtract(wbal,hf.Encryption(new BigInteger(amount+"")));
					String query="update balance set wbal=? where acc=?";
					PreparedStatement pstmt1=con.prepareStatement(query);
					pstmt1.setString(1, newwbal+"");
					pstmt1.setString(2, id+"");
					
					rs=stmt.executeQuery("select t_id from value");
					rs.first();
					int t_id=rs.getInt(1);
					int nt=t_id+1;
					
					query="update value set t_id=? where t_id=?";
					PreparedStatement pstmt2=con.prepareStatement(query);
					pstmt2.setInt(1, nt);
					pstmt2.setInt(2, t_id);
					
					query="insert into t_log values(?,?,?,?,?,?)";
					PreparedStatement pstmt3=con.prepareStatement(query);
					pstmt3.setInt(1,(t_id));
					pstmt3.setString(2,"Debit");
					pstmt3.setString(3,amount+"");
					pstmt3.setInt(4,Integer.parseInt(acc2.getText()));
                    pstmt3.setString(5,new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
                    pstmt3.setInt(6,0);
					
					
                    rs=stmt.executeQuery("select Email from customer_details where Account_No="+acc2.getText());
					String mail=""; 
                    while(rs.next())
                    {
                        mail=rs.getString(1);
                    }
                    SendEmail sm=new SendEmail();
                    int i=sm.mail(Integer.parseInt(acc2.getText()),mail,"Debit",amt2.getText(),hf.Decryption(newwbal)+"."+hf.Decryption(fbal),"");
					if(i==1)
					{
						pstmt1.executeUpdate();
						pstmt2.executeUpdate();
						pstmt3.executeUpdate();
						
						JOptionPane.showMessageDialog(this,"Transaction Successfull !!","Balance",JOptionPane.INFORMATION_MESSAGE);
						 Main m=new Main(new javax.swing.JFrame(), true);
                         setVisible(false);
                         m.setVisible(true);
                    }
					
					}
                    }
					catch(NumberFormatException nfe)
					{
					JOptionPane.showMessageDialog(this,"Wrong Amount! No floating point.","Balance",JOptionPane.INFORMATION_MESSAGE);
					amt2.setText("");
					}
                                        }
				}
                 con.close();
		}catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this,"Sorry for Inconvenience.[Database Error]","Balance",JOptionPane.INFORMATION_MESSAGE);
			System.out.println(ex);
		}
	}
	
	public static void main(String[] args) 
	{
		new Debit1();
	}

}
