-- �ִ� URL ���̴� 2,083���Դϴ�
-- GET ������θ� �̷������ �ٿ�ε忡�� ���� �ٿ�ε带 ���� �߰��� DWONLOAD ���̺� ���� ���� ���� �� �� �ִ� ���̺�
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

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'PK ������' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST', @level2type=N'COLUMN',@level2name=N'SEQ'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'URL ���յ� FILEREALNAME' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST', @level2type=N'COLUMN',@level2name=N'FILEREALNAMES'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'�ٿ�ε� ���� ��' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST', @level2type=N'COLUMN',@level2name=N'DOWNLOADUSER'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'�ٿ�ε���' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST', @level2type=N'COLUMN',@level2name=N'DOWNLOADDATE'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'�ٿ�ε� �ð�' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST', @level2type=N'COLUMN',@level2name=N'DOWNLOADTIME'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DESCRIPTION', @value=N'ȸ����ǥ ���� �ٿ�ε� ���̺�' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FILEDOWNTEST'
GO