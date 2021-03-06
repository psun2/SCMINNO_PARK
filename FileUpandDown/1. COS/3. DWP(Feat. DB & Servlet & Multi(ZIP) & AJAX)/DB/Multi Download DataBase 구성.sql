-- 최대 URL 길이는 2,083자입니다
-- GET 방식으로만 이루어지는 다운로드에서 다중 다운로드를 위해 중간에 DWONLOAD 테이블에 파일 명을 저장 할 수 있는 테이블
CREATE TABLE FILEDOWNTEST(
	SEQ VARCHAR(10) NOT NULL,
	FILEREALNAMES VARCHAR(MAX) NOT NULL,
	DOWNLOADUSER VARCHAR(10) NOT NULL,
	DOWNLOADDATE VARCHAR(10) NULL,
	DOWNLOADTIME VARCHAR(8) NULL,
PRIMARY KEY CLUSTERED 
(
	SEQ ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'PK 시퀀스' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST', @level2type=N'COLUMN',@level2name=N'SEQ'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'URL 조합된 FILEREALNAME' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST', @level2type=N'COLUMN',@level2name=N'FILEREALNAMES'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'다운로드 받은 자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST', @level2type=N'COLUMN',@level2name=N'DOWNLOADUSER'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'다운로드일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST', @level2type=N'COLUMN',@level2name=N'DOWNLOADDATE'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'다운로드 시간' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST', @level2type=N'COLUMN',@level2name=N'DOWNLOADTIME'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'회계전표 파일 다운로드 테이블' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST'
GO