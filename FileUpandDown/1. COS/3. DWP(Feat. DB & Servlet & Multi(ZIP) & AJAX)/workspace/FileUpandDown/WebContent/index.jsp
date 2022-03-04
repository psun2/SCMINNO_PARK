<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 업 & 다운로드</title>
</head>
<body>
	<form action="Upload" id="form" method="post" enctype="multipart/form-data">
		<label>
			파일:&nbsp;<input type="file" name="file0" />
		</label>
		<br />
		<label>
			파일:&nbsp;<input type="file" name="file1" />
		</label>
		<br />
		<label>
			파일:&nbsp;<input type="file" name="file2" />
		</label>
		<br />
		<label>
			파일:&nbsp;<input type="file" name="file3" />
		</label>
		<br />
		<label>
			파일:&nbsp;<input type="file" name="file4" />
		</label>
		<br />
		<button type="submit">submit 전송</button>
	</form>
	<button type="button" onclick="handelFetch();">ajax 전송</button>
	
	<script>
		function handelFetch() {
			let url = '/postUpload';
			let fetchOption = {
					headers: {},
					method: 'post'
			}
			
			var data = new FormData(document.getElementById('form'));
			
			// 파일 사이즈 유효성 검사
			for (var value of data.values()) {
				if(value instanceof File) {
					if(value.name) {
						let byteSiz = value.size;
						let kiloByteSize = byteSiz / 1024;
						let megaByteSize = kiloByteSize / 1024;
						
						// 정확한 30.1MB는 30MB를 초과 즉 소수점 이하에서 올림을 하여 계산
						let resultSize = Math.ceil(megaByteSize);
						
						let maxSize = 30;
						if(resultSize > maxSize) {
							alert(value.name + ' 이 최대크기인 30MB를 초과하였습니다.\n현재파일크기: ' + resultSize + 'MB');
							return;
						}
					}
				}

			}
			
			fetchOption.body = data;
				
			fetch(url, fetchOption) // 
			.then((response) => {
				if(response.status == 200) {
					return response.json();
				}
			}) // 
			.then((json) => {
				if(json.STATUS === 'SUCCESS') {
					location.href = json.FORWARD;
				}
			}) //
			.catch((e) => {
				console.error(e.message);
			})
		} // end handelFetch()
	</script>
</body>
</html>