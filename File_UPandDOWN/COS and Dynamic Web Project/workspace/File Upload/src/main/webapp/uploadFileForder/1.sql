CREATE OR REPLACE
PROCEDURE TEST_PROC ( 
	-- pt_TEST OUT TEST%ROWTYPE ,
	ps_TEST1 IN TEST.TEST1%TYPE , 
	ps_TEST2 IN TEST.TEST2%TYPE 
	) 
IS
    vt_TEST TEST%ROWTYPE;
BEGIN
	SELECT
        TEST1,
        TEST2
    INTO
        vt_TEST.TEST1,
        vt_TEST.TEST2
    FROM
        TEST
    WHERE
        TEST1 = ps_TEST1
        AND TEST2 = ps_TEST2;
        
        -- SELECT vt_TEST.TEST1, vt_TEST.TEST2 FROM vt_TEST; -- 테이블 또는 view 없음
        -- SELECT vt_TEST.TEST1, vt_TEST.TEST2 FROM DUAL; -- INTO절이 없음    
RETURN;
END;