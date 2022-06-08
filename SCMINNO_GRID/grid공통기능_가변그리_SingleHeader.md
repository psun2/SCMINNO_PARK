# `가변그리드_SingleHeader`

가변그리드는 그리드의 필드와 Layout을 가변적으로 변경 할 수 있는 기능입니다.

![가변그리드1](./image/%EA%B0%80%EB%B3%80%EA%B7%B8%EB%A6%AC%EB%93%9C1.png)

## `사용방법`

💥 gridList에서 gridBox를 가져옵니다.

```
const gridListBox = gridList.getGridBox('gridList');
```

💥 그리드 박스의 visibleLayOutData을 초기화 할 수 있도록 합니다.

```
const gridListBox = gridList.getGridBox('gridList');`
gridListBox.visibleLayOutData = '';

ex)
gridList.getGridBox('gridList').visibleLayOutData

'WonYear↓80↓작업년도↑GrpKind↓70↓회사↑VessDistCode↓70↓선단↑VessDistName↓80↓선단명↑VessName↓80↓선박명↑EOKIND↓80↓경비분류↑EOName↓110↓경비분류명↑SORT↓70↓정렬순서↑HACHA↓80↓어기항차↑GyGwanKind↓90↓모계정↑MOGyName↓120↓모계정명↑GyCode↓90↓자계정↑GyName↓140↓자계정명↑SaupCode↓100↓사업장↑SaupName↓100↓사업장명↑FeeDate↓100↓경비일자↑SlipNo↓130↓전표번호↑HangNo↓60↓항번↑DbAmt↓120↓차변↑CrAmt↓120↓대변↑Description↓250↓적요사항↑FeeWon↓120↓어로경비↑PLUSMINUS↓100↓증감↑FeeStartDate↓100↓선급시작일↑FeeEndDate↓100↓선급종료일↑TDAY↓80↓전체기간↑JDAY↓80↓적용기간↑WORKDAY↓80↓작업기간↑FstSunDAY↓80↓선급기간↑CALFstSun↓120↓계산선급금↑FstSun↓120↓선급금↑DIFFFstSun↓120↓선급금 차이금액↑SubulKind↓80↓수불구분↑BTN_JPDETAIL↓110↓전표상세↑BTN_GRUPDOC↓130↓그룹웨어문서'

```

visibleLayOutData의 데이터는
필드명(컬럼명(colName))↓WIDTH↓라벨명 의 한 셋트를 구성하며,  
한 셋트에서는 ↓(아래 화살표)를 separator(구분자)로 사용합니다.  
셋트와 셋트 사이에는 ↑(위 화살표)를 separator(구분자)로 사용합니다.

💥 간단한 예제로 작업년도와 회사만 표기 하게 하겠습니다.

```
const gridListBox = gridList.getGridBox('gridList');`
gridListBox.visibleLayOutData = 'WonYear↓80↓작업년도↑GrpKind↓70↓회사';

ex)
gridList.getGridBox('gridList').visibleLayOutData

'WonYear↓80↓작업년도↑GrpKind↓70↓회사'
```

👀TIP  
해당 Layout setting 으로는 gridList.setLayoutData(gridId, json) 함수또는  
gridList.getGridBox('gridList').setLayoutData(json)을 이용 할 수 있습니다.

💥 변경된 레이아웃 확인

![가변그리드2](./image/%EA%B0%80%EB%B3%80%EA%B7%B8%EB%A6%AC%EB%93%9C2.png)

레이아웃은 변경이 되었지만, 뒤에 그리드는 변경 되지 않았습니다.

💥 그리드 변경
그리드 박스의 setLayout함수를 사용하여, 그리드에 변경된 Layout이 적용 될 수 있도록 합니다.

```
// readonlyType : boolean(true or false)
gridList.getGridBox('gridList').setLayout(readonlyType)
```

💥 변경 그리드 확인
![가변그리드3](./image/%EA%B0%80%EB%B3%80%EA%B7%B8%EB%A6%AC%EB%93%9C3.png)

---

## `사조 프로젝트`

사조프로젝트 기준으로 setGrid 아래 custom.init() 함수를 사용하여 손쉽게 사용 하 실 수 있습니다.

```
custom.init(gridId, treeId, [customType]);

ex)

custom.init('gridList', null, ['LIST, BOX']);
custom.init('gridList', null, [customConfigData.CUSTOMTYPEGRID]);

or

custom.init('gridList', null, ['LIST', 'BOX']);
custom.init('gridList', null, [customConfigData.CUSTOMTYPEGBOX, customConfigData.CUSTOMTYPEGLIST]);
```

```
gridList.setChangeGridJson(gridId, json, readonlyType);

or

gridList.setChangeGridStr(gridId, str, readonlyType);

ex)

// json 구조 {column {width: label}, column {width: label}, column {width: label}};
const json = {WonYear : {80: '작업년도'}, GrpKind : {70: '회사'}};
const json = {WonYear : {80: 'STD_WORKYEAR'}, GrpKind : {70: 'STD_GRPKIND'}};
gridList.setChangeGridJson('gridList', json, false);

or

const str = 'WonYear↓80↓작업년도↑GrpKind↓70↓회사';
gridList.setChangeGridStr('gridList', str, false);
```

---

## `수고하셨습니다.`
