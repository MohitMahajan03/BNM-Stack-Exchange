import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.io.File;
import java.io.FileWriter;


public class cse_home extends HttpServlet
{
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();
		String pass;
		String repass;
		String email;
		String user;
		Connection conn;
		String userName;
		String url;
		String password;
		Statement stmt;
		ResultSet rs;
		PreparedStatement pstmt;
		stmt = null;
		rs = null;
		pstmt = null;
		conn = null;
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
		try
		{
			stmt = conn.createStatement();
			stmt.execute("select * from cseqanda");
			stmt.execute("drop table user_q_counts");
			stmt.execute("drop table q_counts");
			rs = stmt.getResultSet();
			//rs.first();
			out.print("<head><link rel='stylesheet' href='cse_home(java).css'></head><table border = '1'>");
			while(rs.next()) 
			{
				String name = rs.getString("username");
				String shortq = rs.getString("shorthand");
				String despath = rs.getString("despath");
				String codepath = rs.getString("codepath");
				
				 out.println("<form name='QueryForm' method='POST' action= 'CseView'>"
				 			   + "<tr><td><textarea cols = '30' rows = '10' readonly name = 'user'>" +name+ "</textarea></td>"
				 			   + "<td><textarea cols='30' rows='10' readonly name = 'summary'>" +shortq+ "</textarea></td>"
				 			   + "<td><input type = 'submit' value = 'view'></td>"
				 			   + "</tr></form>");
			}
			out.print("</table>");
			out.println("<a href = 'cseQuery.html'>Post Query</a>");
		}
		catch(Exception e)
		{
			System.out.println("Question error" + e.getMessage());
		}
		
	}
}