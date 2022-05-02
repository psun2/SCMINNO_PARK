package query;

public class Query {
	// 업로드 될때 DataBase에 file정보를 insert 하기 위한 쿼리
	public final static String FILE_UPLOAD = "INSERT INTO FILETEST(FILENAME, FILEREALNAME, EXTENTION, FILESIZE, DOWNCOUNT)"
			+ "VALUES(?, ?, ?, ?, 0)";
	
	// 다운로드 될때 DataBase에 횟수를 Update 하기 위한 쿼리
	public final static String FILE_DOWN_COUNT_UP = "UPDATE FILETEST SET DOWNCOUNT = DOWNCOUNT + 1"
			+ "WHERE FILENAME = ?";
	
	// 업로드 된 파일의 전체 리스트
	public final static String FILE_ALL_LIST = "SELECT FILENAME, FILEREALNAME, EXTENTION, FILESIZE, DOWNCOUNT FROM FILETEST";
	
	// 파일의 실제명 SELECT
	public final static String FILE_REAL_NAME = "SELECT FILEREALNAME FROM FILETEST WHERE FILENAME = ?";
	
	// 파일 다운로드의 시퀀스를 만들어 줌
	public final static String FILE_DOWN_TEMP_SEQ = "SELECT ISNULL(MAX(CAST(SEQ AS BIGINT)), 0) + 1 AS SEQ FROM FILEDOWNTEST";
	
	// 파일 다운로드 임시테이블 INSERT
	public final static String FILE_DOWN_TEMP_INSERT = "INSERT INTO FILEDOWNTEST(SEQ, FILEREALNAMES)"
			+ "VALUES((SELECT ISNULL(MAX(CAST(SEQ AS BIGINT)), 0) + 1 FROM FILEDOWNTEST), ?)";
	
	// 파일 다운로드 임시테이블 INSERT // TEST_QUERY
	public final static String FILE_DOWN_TEMP_TEST_INSERT = "INSERT INTO FILEDOWNTEST(SEQ, FILEREALNAMES)"
			+ "VALUES(?, ?)";
	
	// 파일 다운로드 테이블의 파일명을 get 
	public final static String GET_FILNAMES_TO_SEQ = "SELECT FILEREALNAMES FROM FILEDOWNTEST WHERE SEQ = ?";
}
