# `TimeOut`

## `Project MyBatis 설정파일 경로`

WEB-INF -> conf -> mybatis -> mybatis-config.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<settings>
	  <setting name="cacheEnabled" value="false"/>
	  <setting name="useGeneratedKeys" value="false"/>
	  <setting name="mapUnderscoreToCamelCase" value="true"/>
	  <setting name="callSettersOnNulls" value="true"/>
	  <setting name="defaultStatementTimeout" value="10"/> <!-- seconds -->
      <setting name="logImpl" value="LOG4J"/>
	</settings>
	<typeAliases>
		<typeAlias alias="DataMap" type="project.common.bean.DataMap" />
		<typeAlias alias="StringMap" type="project.common.bean.StringMap" />
	</typeAliases>
</configuration>
```

## `TimeOut Default Setting`

```
<setting name="defaultStatementTimeout" value="10"/> <!-- seconds -->
```

## `Project내 MyBatis XML에 TimeOut 적용`

```
	<select id="FIBI206_CREATE_MAP" parameterType="hashmap" resultType="DataMap" timeout="300">
		SET NOCOUNT ON;

		DECLARE @MESSAGE VARCHAR(100);

		EXEC SAJOERPFI.DBO.SP_FIBI206_P #{GRPKIND}, #{SAUPCODE}, #{YYYYMM}, #{CHK}, @MESSAGE OUTPUT

		SELECT @MESSAGE AS MESSAGE
	</select>
```

```
timeout="300"
```
