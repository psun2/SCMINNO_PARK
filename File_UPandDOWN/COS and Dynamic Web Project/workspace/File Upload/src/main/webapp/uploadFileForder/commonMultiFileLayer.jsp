<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<!-- style="display: none;" -->
<div id="filelayer" >
	<div class="filelayer" style="position: fixed; top: 25%; left: calc(50% - 175px); height: 50%; width: 350px; background-color: red; z-index: 200; padding: 10px;">
		<div class="filelayer_contents_wraper" style="height: 90%; overflow-y: scroll;">
			<form class="filelayer_contents_form" id="aa">
				<div style="display: flex; justify-content: space-between; align-items: center; padding: 5px;">
					<input type="file" onchange="handleChangeValue(event);" style="height: 100%;"/>
				</div>
			</form>		
		</div>
		<div class="filelayer_btn_wraper" style="display: flex; justify-content: space-between; align-items: center; margin-top: 5px; padding: 5px; height: 10%; border-top: 1px solid black;">
			<button type="button" id="uploadBtn" style="text-align: center; width: 100px;">UPLOAD</button>
			<button type="button" id="addBtn" style="text-align: center; width: 100px;">Add</button>
			<button type="button" id="closeBtn" style="text-align: center; width: 100px;">close</button>
		</div> 
	</div>
	<div class="filelayerbackground" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgb(0, 0, 0); opacity: 0.7; z-index: 190;"></div>
</div>

<script>
	var form = document.aa;
	
	var form2 = document.getElementById('aa');
	
	var uploadBtn = document.getElementById('uploadBtn');
	var addBtn = document.getElementById('addBtn');
	var closeBtn = document.getElementById('closeBtn');
	
	var layerDiv = document.getElementById('filelayer');
	
	// 파일 삭제 버튼
	function handleClickDelete(e) {
		var obj = e.target;
		var input = obj.previousElementSibling;

		// var inputs = Array.from(document.querySelectorAll('#aa input'));
		// if(inputs.length > 1) {
			var parentDiv = input.parentElement;
			parentDiv.remove();
		// }
	} // end handleClickDelete()
	
	// 파일 input change f
	function handleChangeValue(e) {
		var obj = e.target;
		var value = obj.value;
		
		var parentDiv = obj.parentElement;
		if(!value) {
			// var inputs = Array.from(document.querySelectorAll('#aa input'));
			// if(inputs.length > 1) {
				parentDiv.remove();
			// }
		} else {
			var deleteBtn = document.createElement('button');
			deleteBtn.setAttribute('type', 'button');
			deleteBtn.innerText = '삭제';
			deleteBtn.style.height = '20px';
			deleteBtn.style.width = '80px';
			deleteBtn.addEventListener('click', handleClickDelete);
			
			parentDiv.appendChild(deleteBtn);
		}
		
	} // end handleChangeValue()
	
	// 파일 업로드 submit
	function handleClickSubmit() {
		// form.submit();
		form2.submit();
	} // end handleClickSubmit()
	
	// 파일업로드 추가
	function handleClickAdd() {
		var inputs = Array.from(document.querySelectorAll('#aa input'));
		
		/*
		inputs.forEach(function(item, index) {
			
		});
		*/
		
		/*
		for(var item of inputs) {
			console.log(item);
		}
		*/
		
		for(var i = 0; i < inputs.length; i++) {
			var value = inputs[i].value;
			
			if(!value) return;
		}

		var div = document.createElement('div');
		div.style.padding = '5px';
		div.style.display = 'flex';
		div.style.justifyContent = 'space-between';
		div.style.alignItems = 'center';
		
		var input = document.createElement('input');
		input.setAttribute('id', 'tq');
		input.setAttribute('type', 'file');
		console.log(input);
		input.addEventListener('change', handleChangeValue);
		
		div.appendChild(input);
		// form.appendChild(div);
		form2.appendChild(div);
		console.log(input)
	} // end handleClickAdd()
	
	// 팝업 종료
	function handleClickClose() {
		layerDiv.style.display = 'none';
	} // end handleClickClose()
	
	uploadBtn.addEventListener('click', handleClickSubmit);
	addBtn.addEventListener('click', handleClickAdd);
	closeBtn.addEventListener('click', handleClickClose);
</script>