package file;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class FileDAO {
	
	private Connection conn;
	
	public FileDAO() {
		try {
			String dbURL = "jdbc:sqlserver://127.0.0.1:1433;databasename=STUDY";
			String dbID = "sa";
			String dbPassword = "mssql";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 라이브러리를 찾을 수 있도록 연동
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // end constructor()

	public int upload(String fileName, String fileRealName) {
		String uploadSQL = "INSERT INTO FILETEST VALUES(?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(uploadSQL);
			pstmt.setString(1, fileName);
			pstmt.setString(2, fileRealName);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 오류 발생
	} // end upload()
	
}
