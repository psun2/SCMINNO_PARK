package file;

public class Query {

	// 업로드 될때 DataBase에 file정보를 insert 하기 위한 쿼리
	final static String FILE_UPLOAD = "INSERT INTO FILETEST(FILENAME, FILEREALNAME, EXTENTION, FILESIZE, DOWNCOUNT)"
			+ "VALUES(?, ?, ?, ?, 0)";
	
	// 다운로드 될때 DataBase에 횟수를 Update 하기 위한 쿼리
	final static String FILE_DOWN_COUNT_UP = "UPDATE FILETEST SET DOWNCOUNT = DOWNCOUNT + 1"
			+ "WHERE FILENAME = ?";
	
	// 업로드 된 파일의 전체 리스트
	final static String FILE_ALL_LIST = "SELECT FILENAME, FILEREALNAME, EXTENTION, FILESIZE, DOWNCOUNT FROM FILETEST";
	
	// 파일의 실제명 SELECT
	final static String FILE_REAL_NAME = "SELECT FILEREALNAME FROM FILETEST WHERE FILENAME = ?";
}
