package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import DTO.FileDTO;
import query.Query;

public class FileDAO {
	
	private Connection con;
	private String dbURL;
	private String dbID;
	private String dbPassword;
	
	public FileDAO() {
		super();
		try {
			// this.dbURL = "jdbc:sqlserver://127.0.0.1:1433;databasename=STUDY";
			this.dbURL = "jdbc:sqlserver://10.11.1.71:1433;databasename=sajoerpcom";
			this.dbID = "sa";
			// this.dbPassword = "mssql";
			this.dbPassword = "sajodb#123";
			
			// 해당 SQL Conncetion 라이브러리의 클래스를 찾을 수 있도록 로드
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			// DataBase에 접근 할 수 있도록 Connection 생성
			this.con = DriverManager.getConnection(this.dbURL, this.dbID, this.dbPassword);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public FileDAO(String dnUrl, String dbId, String dbPassword) {
		super();
		try {
			this.dbURL = dnUrl;
			this.dbID = dbId;
			this.dbPassword = dbPassword;
			
			// 해당 SQL Conncetion 라이브러리의 클래스를 찾을 수 있도록 로드
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			// DataBase에 접근 할 수 있도록 Connection 생성
			this.con = DriverManager.getConnection(this.dbURL, this.dbID, this.dbPassword);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// file insert
	public int upload(String fileName, String fileRealName, String extention, long fileSize) {
		try {
			// INSERT INTO FILETEST(FILENAME, FILEREALNAME, EXTENTION, FILESIZE, DOWNCOUNT) VALUES(?, ?, ?, ?, 0)
			PreparedStatement psmt = con.prepareStatement(Query.FILE_UPLOAD);
			psmt.setString(1, fileName);
			psmt.setString(2, fileRealName);
			psmt.setString(3, extention);
			psmt.setLong(4, fileSize);
			return psmt.executeUpdate(); // insert 된 row의 수를 반환
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 오류 발생시 -1을 return 함으로서 오류임을 증명
	} // end upload()
	
	// 다운로드 전 file의 진짜 이름 SELECT
	public String getFileRealName(String fileName) {
		String fileRealName = null;
		try {
			// SELECT FILEREALNAME FROM FILETEST WHERE FILENAME = ?
			PreparedStatement psmt = con.prepareStatement(Query.FILE_REAL_NAME);
			psmt.setString(1, fileName);
			ResultSet rs = psmt.executeQuery();
			while(rs.next()) {
				fileRealName = rs.getString("FILEREALNAME");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return fileRealName;
	} // end getFileRealName()
	
	// 다운로드시 다운로드 횟수 증가
	public int downIncrease(String fileName) {
		try {
			// UPDATE FILETEST SET DOWNCOUNT = DOWNCOUNT + 1 WHERE FILENAME = ?
			PreparedStatement psmt = con.prepareStatement(Query.FILE_DOWN_COUNT_UP);
			psmt.setString(1, fileName);
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
				
				fileList.add(file);
			}
			return fileList;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null; // 오류 발생시 null을 return 함으로서 오류임을 증명
	} // end getUploadList()

}
