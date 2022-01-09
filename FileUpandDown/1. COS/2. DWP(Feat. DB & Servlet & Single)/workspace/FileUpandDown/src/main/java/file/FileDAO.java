package file;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileDAO {
	
	private Connection con;
	private String dbURL;
	private String dbID;
	private String dbPassword;

	public FileDAO() {
		super();
		this.dbURL = "jdbc:sqlserver://127.0.0.1:1433;databasename=STUDY";
		this.dbID = "sa";
		this.dbPassword = "mssql";
		
		try {
			// 해당 SQL Conncetion 라이브러리의 클래스를 찾을 수 있도록 로드
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			// DataBase에 접근 할 수 있도록 Connection 생성
			this.con = DriverManager.getConnection(this.dbURL, this.dbID, this.dbPassword); 
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public FileDAO(String dbURL, String dbID, String dbPassword) {
		super();
		this.dbURL = dbURL;
		this.dbID = dbID;
		this.dbPassword = dbPassword;
		
		try {
			// 해당 SQL Conncetion 라이브러리의 클래스를 찾을 수 있도록 로드
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			// DataBase에 접근 할 수 있도록 Connection 생성
			this.con = DriverManager.getConnection(this.dbURL, this.dbID, this.dbPassword);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	// file insert
	public int upload(String fileName, String fileRealName, byte[] fileByteArray, String extention, long fileSize) {
		try {
			// INSERT INTO FILETEST(FILENAME, FILEREALNAME, FILESTREAM, EXTENTION, FILESIZE, DOWNCOUNT) VALUES(?, ?, ?, ?, ?, 0)
			PreparedStatement psmt = con.prepareStatement(Query.FILE_UPLOAD);
			psmt.setString(1, fileName);
			psmt.setString(2, fileRealName);
			// psmt.setBytes(3, fileInputStream.readAllBytes()); // Stream을 byte[]로 DB에 insert
			psmt.setBytes(3, fileByteArray); // Stream을 byte[]로 DB에 insert
			psmt.setString(4, extention);
			psmt.setLong(5, fileSize);
			return psmt.executeUpdate(); // insert 된 row의 수를 반환
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 오류 발생시 -1을 return 함으로서 오류임을 증명
	} // end upload()
	
	// 다운로드 전 file의 진짜 이름 SELECT
	public Map getFile(String fileName, int seq) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			// SELECT FILEREALNAME, FILESTREAM FROM FILETEST WHERE FILENAME = ? AND SEQ = ?
			PreparedStatement psmt = con.prepareStatement(Query.FILE_FILE);
			psmt.setString(1, fileName);
			psmt.setInt(2, seq);
			ResultSet rs = psmt.executeQuery();
			while(rs.next()) {
				result.put("FILEREALNAME", rs.getString("FILEREALNAME"));
				// DB의 VARBINARY TYPE에 byte[]을 꺼낼 수 있는 getBytes 메소드 사용
				result.put("FILESTREAM", rs.getBytes("FILESTREAM"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	} // end getFile()
	
	// 다운로드시 다운로드 횟수 증가
	public int downIncrease(String fileName, int seq) {
		try {
			// UPDATE FILETEST SET DOWNCOUNT = DOWNCOUNT + 1 WHERE FILENAME = ? AND SEQ = ?
			PreparedStatement psmt = con.prepareStatement(Query.FILE_DOWN_COUNT_UP);
			psmt.setString(1, fileName);
			psmt.setInt(2, seq);
			return psmt.executeUpdate(); // update 된 row의 수를 반환
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 오류 발생시 -1을 return 함으로서 오류임을 증명
	} // end downIncrease()

	// 업로드 된 파일의 list를 반환
	public ArrayList<FileDTO> getUploadList() {
		try {
			// SELECT FILENAME, FILEREALNAME, EXTENTION, FILESIZE, DOWNCOUNT FROM FILETEST
			ArrayList<FileDTO> fileList = new ArrayList<FileDTO>();
			PreparedStatement psmt = con.prepareStatement(Query.FILE_ALL_LIST);
			ResultSet rs = psmt.executeQuery(); // 실행된 쿼리의 반환 결과물을 담아 줍니다.
			while(rs.next()) {
				FileDTO file = new FileDTO();
				file.setFileName(rs.getString("FILENAME")); // field명으로 직접 입력 가능
				file.setFileRealName(rs.getString(2)); // SELECT 쿼리의 index로 입력 가능
				file.setExtention(rs.getString("EXTENTION"));
				file.setFileSize(rs.getLong("FILESIZE"));
				file.setDownCount(rs.getInt("DOWNCOUNT"));
				file.setSeq(rs.getInt("SEQ"));
				
				fileList.add(file);
			}
			return fileList;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null; // 오류 발생시 null을 return 함으로서 오류임을 증명
	}
}
