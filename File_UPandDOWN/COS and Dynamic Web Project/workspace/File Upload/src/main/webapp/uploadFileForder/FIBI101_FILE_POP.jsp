<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FIBI101_FILE_POP</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/pop_reset.css"/>
<script type="text/javascript">
	var gridId = 'gridList';
	var module = 'FIBI';
	var command = 'FIBI101POP10';
	var popParam;

	$(document).ready(function(){
		gridList.setGrid({
	    	id : gridId,
	    	module : module,
			command : command
	    });
		
		popParam = page.getLinkPopData();
		searchList();
	});

	// 공통버튼
	function commonBtnClick(btnName) {
		if(btnName == "Search") {
			searchList();
		} else if(btnName == "Select") {
			selectList();
		} else if(btnName == 'Upload') {
			upload();
		}
	} // end commonBtnClick()
	
	// 조회
	function searchList() {
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			param.putAll(popParam);
			param.put('YMD', param.get('SLIPDATE'));

			// test Data
			// param.put('INSACODE', '08111007');
			// param.put('YMD', '20210101');
			// param.put('INSACODE', 'SJ120058');
			// param.put('YMD', '20211102');
			
			gridList.gridList({
		    	id : "gridList",
		    	param : param
		    });
		}
	} // end searchList()

	// : grid 조회 시 data 적용이 완료 된 후
	function gridListEventDataBindEnd(gridId, dataLength, excelLoadType) {
		if (gridId == 'gridList') {
			if (dataLength > 0) {
				var gridDataList = gridList.getGridData(gridId);

				for (var i = 0; i < gridDataList.length; i++) {
					var saupCode = gridDataList[i].get('ERP_SUPCODE');
					saupCode = saupCode ? saupCode.trim() : saupCode;

					if (saupCode) gridList.setRowReadOnly(gridId, i, true, ['ERP_SUPCODE']);
				}
			}
		}
	} // end gridListEventDataBindEnd()

	// : grid column value 변경 시
	function gridListEventColValueChange(gridId, rowNum, colName, colValue) {
		if(gridId == 'gridList') {
			if(colName == 'ERP_SUPCODE') {
				if(colValue) {
					var param = gridList.getRowData(gridId, rowNum);
					param.put('GRPKIND', grpKind);
					param.put('COMPANYNO', param.get('SUP_COM_REGNO'));
					param.put('CUSTCODE', param.get('ERP_SUPCODE'));
					
					netUtil.send({
						module: module,
						command: 'FIBI101POP10_BILL_SAUPCODE',
						param: param,
						successFunction: 'successSaUpCallBack'
					});					
				} else {
					gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_SUPNAME', '');
				}
			}
		}
	} // end gridListEventColValueChange()

	// 사업코드 콜백 함수
	function successSaUpCallBack(json) {
		if(json && json.data) {
			if(json.data.length > 0) {
				gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_SUPCODE', json.data[0].CUSTCODE);
				gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_SUPNAME', json.data[0].CUSTNAME);
				gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_SUPNAME', json.data[0].OWNERNAME);
			} else {
				commonUtil.msgBox('SYSTEM_DATAEMPTY'); // 조회된 데이터가 없습니다.
				gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_SUPCODE', '');
				gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_SUPNAME', '');
				gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_OWNERNAME', '');
			}
		} else {
			commonUtil.msgBox('SYSTEM_DATAEMPTY'); // 조회된 데이터가 없습니다.
			gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_SUPCODE', '');
			gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_SUPNAME', '');
			gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_OWNERNAME', '');
		}
	} // end successSaUpCallBack()
	
	// ERP_거래처 코드 팝업
	function saupCodePop() {
		var rowData = gridList.getRowData(gridId, gridList.getFocusRowNum(gridId));
		var param = new DataMap();
		param.put('COMPANYNO', rowData.get('SUP_COM_REGNO'));
		var option = "height=580,width=766,resizable=yes";
		page.linkPopOpen("/FIBI/FIBI104POP10_BILL_SAUPCODE.page", param, option);
	} // end saupCodePop()
	
	// 팝업 종료시 이벤트
	function linkPopCloseEvent(data) {
		var page = data.get('PAGE');
		
		if(page == 'SAUPCODE') {				
			gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_SUPCODE', data.get('CUSTCODE'));
			gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_SUPNAME', data.get('CUSTNAME'));
			gridList.setColValue(gridId, gridList.getFocusRowNum(gridId), 'ERP_OWNERNAME', data.get('OWNERNAME'));
		}
	} // end linkPopCloseEvent()
	
	// 선택
	function selectList() {
		var selectDataList = gridList.getSelectData(gridId);

		if(selectDataList.length == 0) {
			commonUtil.msgBox("SYSTEM_ROWSEMPTY");
			return;
		} else {
			for(var i = 0; i < selectDataList.length; i++) {
				var custCode = selectDataList[i].get('ERP_SUPCODE');
				custCode = custCode ? custCode.trim() : custCode;
				
				if(!custCode) {
					var rowNum = selectDataList[i].get(configData.GRID_ROW_NUM);
					
					if(!isNaN(parseInt(rowNum))) {
						rowNum = parseInt(rowNum) + 1;
						commonUtil.msgBox('VALID_ERPCUST', rowNum); // {0} 행의 ERP 공급업체 코드를 입력해 주세요.
						return;
					} else {
						rowNum = parseInt(rowNum) + 1;
						commonUtil.msgBox('VALID_ERPCUST', rowNum); // {0} 행의 ERP 공급업체 코드를 입력해 주세요.
						return;
					}
				}
			}
			searchSelectData(selectDataList);
		}
	} // end selectList()

	// 선택된 데이터로 전표에 뿌려질 데이터 조회
	function searchSelectData(selectDataList) {
		var slipDate = popParam.get('SLIPDATE');
		// slipDate = '20210101' // testData
		// slipDate = '20211102';
		var bulGongChk = $('input[name="BULGONG"]').prop('checked');

		for(var i = 0; i < selectDataList.length; i++) {
			selectDataList[i].put('SLIPDATE', slipDate);
			selectDataList[i].put('CUSTCODE', selectDataList[i].get('ERP_SUPCODE'));
			selectDataList[i].put('CUSTNAME', selectDataList[i].get('ERP_SUPNAME'));
			selectDataList[i].put('OWNERNAME', selectDataList[i].get('ERP_OWNERNAME'));
			selectDataList[i].put('COMPANYNO', selectDataList[i].get('SUP_COM_REGNO'));
			if(bulGongChk) selectDataList[i].put('BULGONG', 'Y');
			else selectDataList[i].put('BULGONG', 'N');
		}

		var param = new DataMap();
		param.put('SELECTLIST', selectDataList);

		netUtil.send({
			url: '/FIBI/json/searchFIBI101POP10BILL.data',
			param: param,
			successFunction: 'successSearchCallBack'
		})
	} // end searchSelectData()

	// 선택된 데이터 조회 콜백
	function successSearchCallBack(json) {
		if(json && json.data) {
			if(json.data.STATUS == 'FAIL') {
				commonUtil.msgBox(json.data.MESSAGE);
			} else {
				if(json.data.SELECTLIST.length > 0) {
					closePop(json.data.SELECTLIST);
				} else { // 오류
					commonUtil.msgBox('SYSTEM_SAVEFAIL');
				}
			}
		}
	} // end successSearchCallBack()

	// 선택 된 데이터를 내보냄
	function closePop(selectDataList) {
		var param = new DataMap();
		param.put('PAGETYP', 'TAXBILL');
		param.put('SELECTLIST', selectDataList);

		page.linkPopClose(param);
	} // end closePop()
	
	
	/*
	// : grid row click even
	function gridListEventRowClick(gridId, rowNum, colName) {
		if(gridId == 'gridList') {
			if(colName == 'SUP_COM_REGNO') {
				var fileInput = $('#' + gridId).find('tr[grownum="' + rowNum + '"]').find('td[gcolname="' + colName + '"]').find('input[type="file"]');
				if(fileInput.length == 0) {
					var td = $('#' + gridId).find('tr[grownum="' + rowNum + '"]').find('td[gcolname="' + colName + '"]');
					var fileInputEle = '<input type="file" id="file' + rowNum + '" style="display: none;" />';
					$(td).append(fileInputEle);
					$('#file' + rowNum).click();					
				}
			}
		}
	} // end gridListEventRowClick()
	*/
	
	function test(gridId, rowNum, colName) {
		var fileInput = $('#' + gridId).find('tr[grownum="' + rowNum + '"]').find('td[gcolname="' + colName + '"]').find('input[type="file"]');
		var td = $('#' + gridId).find('tr[grownum="' + rowNum + '"]').find('td[gcolname="' + colName + '"]');
		var fileInputEle = '<input type="file" id="file' + rowNum + '" style="display: none;" />';
		$(td).append(fileInputEle);
		$('#file' + rowNum).click();	
		if(fileInput.length == 0) {
							
		}
	}
	
	//  : grid에 button 속성을 준 column에 button 을 클릭한 경우
	function gridListEventColBtnClick(gridId, rowNum, colName) {
		if(gridId == 'gridList') {
			if(colName == '삭제') {
				
				var gRowState = gridList.getColData(gridId, rowNum, ConfigData.GRID_ROW_STATE_ATT);
				if(gRowState == 'C') gridList.deleteRow(gridId, rowNum, false);				
				else gridList.deleteRow(gridId, rowNum, true);	
				
			}
		}
	} // end gridListEventColBtnClick()
	
	function upload() {
		
	}
</script>
</head>
<body>
<%@ include file="/common/include/webdek/layout.jsp" %>
<div class="content_wrap" style="min-width:auto;overflow:hidden;">
	<div class="content_inner">
		<div class="content_serch" id="searchArea">
			<div class="btn_wrap">
				<div class="fl_l">
				</div>
				<div class="fl_r">
					<input type="button" CB="Upload SEARCH 업로드" />
					<input type="button" CB="Search SEARCH BTN_SEARCH" />
					<input type="button" CB="Select TEMP_SAVE BTN_SELECT" />
				</div>
			</div>
			<div class="search_inner">
                <table class="find_idpw" id="detail">
                    <colgroup>
                        <col style="wdith:10%">
                        <col style="width:30%">
                        <col style="wdith:10%">
                        <col style="width:20%">
                        <col style="wdith:10%">
                        <col style="width:20%">
                    </colgroup>
                    <tr>
                        <th CL="STD_SEARCH1" style="width: 70px;">검색어</th> 
                        <td>
                            <input type="text" class="input" name="SEARCH" />
                        </td>
                        <td>
	                       <label style="display : flex; justify-content : left; align-items : center; margin-left : 25px;">
	                       		<span CL="STD_BULGONG" style="width: 65px; font-weight:bold">불공제 처리</span>
	                            <input type="checkbox" name="BULGONG" style="width: 30px;" />
	                       </label>
                    	</td>
                    </tr>
                </table> 
			</div>
		</div>
		<div class="search_next_wrap">
			<div class="content_layout tabs" style="height: 415px">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span>일반</span></a></li>
				</ul>
				<div class="table_box section" id="tab1-1" style="height: 350px">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<!-- <td GH="40" GCol="rownum">1</td> -->
										<td GH="40" GCol="rowCheck">1</td>
										<td GH="85 SSKEYCODE" GCol="text,DTI_WDATE" GF="D">KEY코드</td>
										<td GH="100 파일명" GCol="text,SUP_COM_REGNO" >파일명</td>
										<td GH="180 파일크기" GCol="text,TYPENAME">파일크기</td>
										<td GH="250 확장자" GCol="text,SUP_COM_NAME">확장자</td>
										<td GH="110 업로드한사람" GCol="text,SUP_AMOUNT" GF="N 20,0">업로드한사람</td>
										<td GH="110 업로드 일자" GCol="text,TAX_AMOUNT" GF="N 20,0">업로드일자</td>
										<td GH="110 삭제" 		 GCol="btn,삭제"  		GB="DELETE DELETE BTN_DELETE"></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="add"></button>
						<button type='button' GBtn="delete"></button>
						<button type='button' GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>4</span></span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/jsp/common/commonMultiFileLayer.jsp" %>
<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>