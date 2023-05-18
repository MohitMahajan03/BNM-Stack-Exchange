import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.io.File;
import java.io.FileWriter;
import javax.servlet.http.*;
import java.time.LocalDateTime;
import java.time.format.*;

public class cse extends HttpServlet
{
	String question;
	String user;
	String summary;
	String code;
	public void add_query(Statement stmt, Connection conn, ResultSet rs, PreparedStatement pstmt, String user, String summary, String question, String code)
	{
		try
		{
			LocalDateTime time = LocalDateTime.now();
			DateTimeFormatter DTFobj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
			String timeString = time.format(DTFobj);
			stmt = conn.createStatement();
			stmt.execute("create table if not exists cseqanda(username varchar(50), shorthand varchar(100), despath varchar(100), codepath varchar(100))");
			String query = "insert into cseqanda(username,shorthand,despath, codepath, date_time) values(?,?,?,?,?)";
			String des_path = "C:/Users/mohit/Desktop/users/" + user + "/" + summary + " des.txt";
			String code_path = "C:/Users/mohit/Desktop/users/" + user + "/" + summary + " code.txt";
			System.out.println("file_path = " + des_path);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, user);
			pstmt.setString(2, summary);
			pstmt.setString(3, des_path);
			pstmt.setString(4, code_path);
			pstmt.setString(5, timeString);
			pstmt.executeUpdate();
			FileWriter output = new FileWriter(des_path);
		    // Writes the program to file
		    output.write(question);
		    System.out.println("Data is written to the file.");
		    output.close();
		    output = new FileWriter(code_path);
		    output.write(code);
			output.close();
			String insert = "insert into " + user; 
			query = insert + " (shorthand,despath,codepath) values(?,?,?)";
			System.out.println("User" +user);
			pstmt = conn.prepareStatement(query);
			//pstmt.setString(1, user);
			pstmt.setString(1, summary);
			pstmt.setString(2, des_path);
			pstmt.setString(3, code_path);
			pstmt.executeUpdate();
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
			stmt = null;
			rs = null;
			pstmt = null;
		}
		catch(Exception e)
		{
			System.out.println("Cannot connect to database " + e);
		}
		summary = request.getParameter("summary");
		question = request.getParameter("question");
		code = request.getParameter("code");
		try
		{
			stmt = conn.createStatement();
			stmt.execute("select * from log");
			rs = stmt.getResultSet();
			while(rs.next())
			{
				user = rs.getString("name");
			}
			/*
			 * stmt.
			 * execute("create table if not exists q_counts (username varchar(50), count int)"
			 * ); stmt.execute("select * from cseqanda"); rs = stmt.getResultSet();
			 * while(rs.next()) { insert into user_q_counts (username, count) select
			 * username, count(*) from cseqanda where username = "rohit"; }
			 */
		}
		catch(Exception e)
		{
			System.out.println("User error" + e);
		}
	//	String username = (String) session.getAttribute("username");
	//	String user = (String)request.getAttribute("user_name");
		System.out.println("User logged in is " + user);
		add_query(stmt, conn, rs, pstmt, user, summary, question, code);
		response.sendRedirect("http://localhost:8080/BNM_Stack_Exchange/CseHome");
	}
}