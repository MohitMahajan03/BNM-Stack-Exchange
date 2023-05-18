import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
//import java.io.PrintWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.http.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;

public class login extends HttpServlet
{
	String name;
	String pass;
	String check;
	public void check_table(Statement stmt, Connection conn, ResultSet rs, PreparedStatement pstmt)
	{
		try
		{
			String query = "select password from user WHERE username = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			System.out.println("Success");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				check = rs.getString("password");
			}
			System.out.println(check);
		}
		catch(Exception e)
		{
			System.out.println("error : " + e);
		}
	}
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		Connection conn;
		String userName;
		String url;
		String password;
		Statement stmt;
		ResultSet rs;
		PreparedStatement pstmt;
		conn = null;
		stmt = null;
		rs = null;
		pstmt = null;
		try
		{
			conn = null;
			userName = "root";
			password = "ORacl3MyS@l";
			url = "jdbc:mysql://localhost:3306/test";
			Class.forName ("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url,userName,password);
			System.out.println("Database connection established");
			stmt = conn.createStatement();
			stmt = null;
			rs = null;
			pstmt = null;
		}
		catch(Exception e)
		{
			System.out.println("Cannot connect to database " + e);
		}
		name = request.getParameter("username");
		pass = request.getParameter("password");
//		PrintWriter out = response.getWriter();
		check_table(stmt, conn, rs, pstmt);
		if(pass.equals(check))
		{
//			HttpSession session = request.getSession();
//			session.setAttribute("username", name);
			request.setAttribute("user_name", name);
			try
			{
				stmt = conn.createStatement();
				stmt.execute("create table if not exists log(name varchar(50))");
				String query = "insert into log (name) values (?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, name);
				pstmt.executeUpdate();
			}
			catch(Exception e)
			{
				System.out.println("log error" + e);
			}
			response.sendRedirect("home.html");
			/*
			 * // Recipient's email ID needs to be mentioned. String to =
			 * "mohitmah50@gmail.com";
			 * 
			 * // Sender's email ID needs to be mentioned String from =
			 * "bnmstackexchange@gmail.com";
			 * 
			 * // Assuming you are sending email from localhost String host = "localhost";
			 * 
			 * // Get system properties Properties properties = System.getProperties();
			 * 
			 * // Setup mail server properties.setProperty("mail.smtp.host", host);
			 * //properties.put("mail.smtp.port", "8080");
			 * 
			 * // Get the default Session object. Session session =
			 * Session.getDefaultInstance(properties);
			 * 
			 * // Set response content type response.setContentType("text/html");
			 * PrintWriter out = response.getWriter();
			 * 
			 * try { // Create a default MimeMessage object. MimeMessage message = new
			 * MimeMessage(session);
			 * 
			 * // Set From: header field of the header. message.setFrom(new
			 * InternetAddress(from));
			 * 
			 * // Set To: header field of the header.
			 * message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			 * 
			 * // Set Subject: header field message.setSubject("This is the Subject Line!");
			 * 
			 * // Now set the actual message message.setText("This is actual message");
			 * 
			 * // Send message Transport.send(message); } catch (MessagingException mex) {
			 * mex.printStackTrace(); }
			 */		
		}
		else
		{
			response.sendRedirect("login.html");
		}
		try
		{
			conn.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}