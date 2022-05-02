package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		ResultSet rs = null;
		try {
			// SELECT FILENAME, FILEREALNAME, EXTENTION, FILESIZE, DOWNCOUNT FROM FILETEST
			ArrayList<FileDTO> fileList = new ArrayList<FileDTO>();
			PreparedStatement psmt = con.prepareStatement(Query.FILE_ALL_LIST);
			rs = psmt.executeQuery(); // 실행된 쿼리의 반환 결과물을 담아 줍니다.
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
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return null; // 오류 발생시 null을 return 함으로서 오류임을 증명
	} // end getUploadList()

	// 압축 다운로드 전 파일명을 DB - INSERT 전 SEQ를 만들어서 보내줌
	public String getMutilFileInsertSeq() {
		ResultSet rs = null;
		try {
			PreparedStatement psmt = con.prepareStatement(Query.FILE_DOWN_TEMP_SEQ);
			rs = psmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("SEQ");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return null;
	} // end getMutilFileInsertSeq()
	
	// 압축 다운로드 전 파일명을 DB - INSERT
	public String mutilFileInsert(String fileNames) {
		ResultSet rs = null;
		try {
			/*
			// String[] keyCols = {"SEQ", "FILEREALNAMES", "DOWNLOADUSER", "DOWNLOADDATE", "DOWNLOADTIME"}; // 열 배열이 잘못되었습니다. 길이가 1이어야 합니다.
			String[] keyCols = {"SEQ"};
			int[] keyColsIndex = {1};
			PreparedStatement psmt = con.prepareStatement(Query.FILE_DOWN_TEMP_INSERT, keyCols);
			// PreparedStatement psmt = con.prepareStatement(Query.FILE_DOWN_TEMP_TEST_INSERT, keyCols);
			// psmt.setString(1, "17");
			psmt.setString(1, fileNames);
			int cnt = psmt.executeUpdate();
			*/
			
			// insert 전 시퀀스를 따로 가져옴
			String seq = getMutilFileInsertSeq();
			PreparedStatement psmt = con.prepareStatement(Query.FILE_DOWN_TEMP_TEST_INSERT);
			psmt.setString(1, seq);
			psmt.setString(2, fileNames);
			int cnt = psmt.executeUpdate();
			
			if(cnt > 0) {
				/*
				rs = psmt.getGeneratedKeys(); // insert와 동시에 설정해둔 key 값을 가져옵니다.
				
				if(rs.next()) {
					System.out.println(rs.getString(1)); // null
					System.out.println(rs.getInt(1)); // 0
					System.out.println(rs.getLong(1)); // 0
					 * SQL Server에서는 식별자에 대해 의사 열을 지원하지 않으므로 
					 * 자동 생성 키 기능을 사용해야 하는 업데이트는 IDENTITY 열이 포함된 테이블에 대해 작동해야 합니다. 
					 * SQL Server에서는 테이블당 하나의 IDENTITY 열만 허용됩니다. 
					 * SQLServerStatement 클래스의 getGeneratedKeys 메서드에 의해 반환되는 
					 * 결과 집합에는 GENERATED_KEYS라는 하나의 열만 들어 있습니다. 
					 * IDENTITY 열이 없는 테이블에서 생성된 키를 요청하는 경우 JDBC 드라이버는 null 결과 집합을 반환합니다.
					 * 
					 * 즉, MSSQL에서는 SEQ자동 증가되는 열이 없으면 NULL 반환
					 *
					
					// System.out.println(rs.getString("SEQ")); // 컬럼명으로 찾을수 없음. (반듯이 Index 번호를 이용합니다.)
					// com.microsoft.sqlserver.jdbc.SQLServerException: 열 이름 SEQ이(가) 잘못되었습니다.
					return rs.getString(1);
				}
				*/
				
				return seq;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return null;
	} // end mutilFileInsert()
	
	// 다운로드 파일명칭 get
	public String getDownloadFileNames(String seq) {
		ResultSet rs = null;
		try {
			PreparedStatement psmt = con.prepareStatement(Query.GET_FILNAMES_TO_SEQ);
			psmt.setString(1, seq);
			rs = psmt.executeQuery();
			
			if(rs.next()) return rs.getString(1);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
}
