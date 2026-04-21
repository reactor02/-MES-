<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html lang="kr">

<head>
<meta charset="EUC-KR">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Document</title>
<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>

<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
<link rel="stylesheet"
	href="/mes/static/css/P10_report/defectReporting.css">
	<link rel="stylesheet" href="/mes/static/css/P09_equip/main.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

</head>

<body>

	<%@ include file="/WEB-INF/views/P00_layout/header.jsp"%>

	<div class="layout_snb">
		<div class="snbContent">
			<%@ include file="/WEB-INF/views/P00_layout/snb.jsp"%>
		</div>
		<div class="content">


			<div class="snb-bro">
				<div class="title-box">
					<h1>부적합 보고서</h1>
					<h7>부적합 발생 현황 및 시정조치 관리</h7>
				</div>
				<div class="board-box">


					<div class="box-type4 radius">
						<div>
							<h3>
								검사 ID : ${ not empty param.qc_id ? param.qc_id : sessionScope.d1.qc_id }
								<br> 검사 종료일 : ${ not empty param.qc_edate ? param.qc_edate : sessionScope.d1.qc_edate }
							</h3>
						</div>
						<div class="weather"></div>
					</div>

					<div class="box-type4 radius">
						<div class="chart-1 weather">
							<h3>품질 기준</h3>
							<h4>성상 & 포장상태 : 육안 검사 & 누설 검사</h4>
							<h4>알콜함량 & 성분분석 : 63% ~ 77%, 5mL 이상</h4>
							<h4>미생물 한도 시험 : 오염이 없어야함.</h4>
							<h4>포장밀봉상태 : 누설시험 진행</h4>
						</div>
					</div>

					
		        <div class="card" style="height : 250px; padding-bottom : 0px;" >
		        	<div class="cardTitle">
			            <h3>클린룸 현황</h3>
			            <div class="type">
			            	<div class="typeTag">
				            	<div class="color success"></div>
				            	<div class="typeName">정상</div>
			            	</div>
			            	<div class="typeTag">
				            	<div class="color warning"></div>
				            	<div class="typeName">정상</div>
			            	</div>
			            	<div class="typeTag">
				            	<div class="color danger"></div>
				            	<div class="typeName">정상</div>
			            	</div>
			            </div>
		        	</div>
		            <div class= cardClean>
			            <div class="cardSmall temp warning">
				            <p>온도</p>
				            <strong>21.1</strong>
				            <p>(단위 : ℃)</p>
				        </div>
				        <div class="cardSmall humid">
				            <p>습도</p>
				            <strong>51.2</strong>
				            <p>(단위 : % RH)</p>
				        </div>
				        <div class="cardSmall press">
				            <p>차압</p>
				            <strong>+12</strong>
				            <p>(단위 : Pa)</p>
				        </div>
				        <div class="cardSmall clean">
				            <p>청정도</p>
				            <strong>30.5</strong>
				            <p>(단위 : 만/m²)</p>
				        </div>
		            </div>
		        </div>



					<div class="box-type2 radius">
						<h3>검사 전</h3>
						<div class="big lightgrey">${ not empty param.qc_qty ? param.qc_qty : (100)}
							개</div>
					</div>

					<div class="box-type2 radius">
						<h3>검사 완료</h3>
						<div class="big green">${ not empty param.qc_qty ? param.qc_clear : (100 - d1.defect_cnt)}
							개</div>

					</div>




					<div class="box-type2-1 radius">
						<div style="width: 100%;">
							<div style="width: 100%; height: 400px;">
								<canvas id="defect-type-chart"></canvas>
							</div>
						</div>
					</div>

					<div class="box-type2-1 radius">
						<div
							style="width: 100%; display: flex; flex-direction: column; justify-content: space-evenly;">
							<div style="height: 390px;">
								<canvas id="month-defect-chart"></canvas>
							</div>
						</div>
					</div>




					<div class="box-type3 radius shadow" style="height : 500px;">
    <h3>부적합 보고서 (품질 이슈)</h3>
    <div class="short-box" style="padding: 10px;">
        <c:forEach var="d" items="${defect_report}">
            <div class="issue-item" style="display: flex; justify-content: space-between; align-items: center; padding: 12px 0; border-bottom: 1px solid #eee;">
                
                <!-- 1. 분류 및 내용 (클릭 시 상세페이지 이동) -->
                <div style="flex: 2;">
                    <a href="defectreporting?defect_id=${d.defect_id}&solution=${d.solution}&qc_qty=100&qc_clear=${100 - d.defect_cnt}&qc_id=${d.qc_id}&dtype_name=${d.dtype_name}&qc_edate=${d.qc_edate}&defect_cnt=${d.defect_cnt}" 
                       style="text-decoration: none; color: inherit;">
                        <span style="font-weight: bold; color: #e74c3c;">[품질]</span> 
                        ${d.dtype_name} - ${d.defect_id} (검사ID: ${d.qc_id})
                    </a>
                </div>

                <!-- 2. 발생 날짜 -->
                <div style="flex: 1; text-align: center; color: #666;">
                    <fmt:formatDate value="${d.qc_edate}" pattern="yyyy-MM-dd HH:mm" />
                </div>

                <!-- 3. 조치 상태 버튼 -->
                <div style="flex: 0.5; text-align: right;">
                    <c:choose>
                        <c:when test="${not empty d.solution}">
                            <span class="buttonMain small" style="background: #3498db; border: none; padding: 5px 10px; color: #fff; border-radius: 4px;">조치완료</span>
                        </c:when>
                        <c:otherwise>
                            <span class="buttonMain small" style="background: #e74c3c; border: none; padding: 5px 10px; color: #fff; border-radius: 4px;">미조치</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
    </div>

   
</div>


				</div>
			</div>


		</div>
	</div>



	<script>
	// 1. EL 파라미터 추출
	const p_id = " ${ not empty param.qc_id ? param.qc_id : sessionScope.d1.qc_id }";
	const p_date = " ${ not empty param.qc_edate ? param.qc_edate : sessionScope.d1.qc_edate }";
	const p_name = "${ not empty param.dtype_name ? param.dtype_name : sessionScope.d1.dtype_name }"; // 불량 유형 이름 추가
	const p_solution = "${ not empty param.solution ? param.solution : sessionScope.d1.dtype_name}"; // 해결 이름 추가
	const p_cnt = Number("${ not empty param.defect_cnt ? param.defect_cnt : sessionScope.d1.defect_cnt}") || 0; 
	
	console.log('p_id', p_id)
	console.log('p_date', p_date)
	console.log('p_name', p_name)
	console.log('p_solution', p_solution)
	console.log('p_cnt', p_cnt)

	const total_qty = 100;
	const success_qty = total_qty - p_cnt;

	// 2. 차트 그릴 곳 특정
	const ctx = document.querySelector('#defect-type-chart').getContext('2d');

	// 3. 도넛 차트 생성
	new Chart(ctx, {
	    type: 'doughnut',
	    data: {
	        // 라벨 수정: p_name이 있으면 그 이름을 쓰고, 없으면 기본 '부적합' 표시
	        labels: ['합격 수량', p_name !== "" ? p_name : "부적합 유형"],
	        datasets: [{
	            data: [success_qty, p_cnt],
	            backgroundColor: [
	                'rgba(75, 192, 192, 0.8)', // 합격 (청록색)
	                'rgba(255, 99, 132, 0.8)'  // 불량 (빨간색)
	            ],
	            borderColor: [
	                'rgba(75, 192, 192, 1)',
	                'rgba(255, 99, 132, 1)'
	            ],
	            borderWidth: 1
	        }]
	    },
	    options: {
	        responsive: true,
	        maintainAspectRatio: false, // 💡 부모 컨테이너 높이에 맞춤
	        layout: {
	            padding: 0 // 💡 차트 주변 불필요한 여백 제거
	        },
	        plugins: {
	            legend: {
	                position: 'bottom',
	                labels: {
	                    color: '#222',
	                    font: { size: 14, weight: '600' },
	                    padding: 10 // 💡 범례와 차트 사이 간격을 좁힘
	                }
	            },
	            title: {
	                display: true,
	                text: p_id !== "" ? p_id +" : "+ " 부적합 현황 (" + p_date + ")" : "부적합 보고서 상세 분석",
	                color: '#222',
	                font: { size: 18, weight: 'bold' },
	                padding: { top: 10, bottom: 10 } // 💡 타이틀 아래 여백 줄임
	            }
	        },
	        // 도넛 자체의 두께를 조절해서 더 꽉 차 보이게 함
	        cutout: '60%', 
	        // 💡 아래 옵션을 추가하면 영역에 최대한 꽉 채웁니다.
	        radius: '90%' 
	    }
	});
	 
	 
	 
	 
	// 월간 불량 배열 바구니 생성
	const month_d_type = [];
	const month_d_cnt = [];
	
	//db에서 받아온 리스트를 반복문으로 바구니에 차례대로 넣고있음.
	<c:forEach var="d" items="${ dMonthChart }" >
	 month_d_type.push("${ d.dtype_name }");
	 month_d_cnt.push(Number("${ d.defect_cnt }") || 0);
	</c:forEach>
	
	 
	 //이제 집계를 할 json바구니 생성
	 const month_counts = {};
	 
	 //반복문으로 배열 바구니에 있는걸 json에서 개수 카운팅하기
	 //불량 원인이 없으면 새로 생성하고, 있으면, 개수 1 추가하는 로직.
	 month_d_type.forEach((name, index) => {
		 
		 //해당 인덱스의 수량을 가져옴
		 const count = month_d_cnt[index];
		 
		 //기존에 이름이 있으면 수량만큼 더하고, 없으면 수량만큼 새로 세팅
		 month_counts[name] = (month_counts[name] || 0) +count;
	 })
	 
	 //확인.
	 console.log('Month_d_type : ', month_d_type)
	 console.log('Month_d_cnt : ', month_d_cnt)
	 console.log('Month_counts : ', month_counts)
	 
	 //위에 있는 차트 그려질 곳 특정
	 const monthChart = document.querySelector('#month-defect-chart').getContext('2d');;
	 
	 //차트 그리기 그릴곳, 조건들?
	 new Chart(monthChart, {
		 type: 'bar',
		 data: {
			 labels: Object.keys(month_counts),
			 datasets: [{
				 label : '월간 불량 발생 건수',
				 data : Object.values(month_counts),
				 backgroundColor: [
					 'rgba(255, 99, 132, 1)', // 막대 색상들 (자동 반복됨)
	                  'rgba(54, 162, 235, 1)',
	                  'rgba(255, 206, 86, 1)',
	                  'rgba(75, 192, 192, 1)',
	                  'rgba(0, 0, 0, 1)',
	                  'rgba(153, 102, 255, 1)'
				 ],
				 borderColor: 'rgba(0, 0, 0, 1)',
				 borderWidth: 1
			 }]
		 },
		 options: {
			    responsive: true,
			    maintainAspectRatio: false,
			    plugins: {
			        // 💡 1. 분홍색 막대(범례) 숨기기
			        legend: {
			            display: false // 이 줄 하나면 분홍색 박스가 사라집니다!
			        },
			        // 💡 2. 진짜 제목을 사진의 2배 크기로 설정
			        title: {
			            display: true,
			            text: '월간 부적합 발생 현황', // 차트 상단에 노출될 제목
			            color: '#222',
			            font: {
			                size: 20, // 💡 기존보다 약 2배 정도 큰 사이즈
			                weight: 'bold'
			            },
			            padding: {
			                top: 10,
			                bottom: 30 // 차트와의 간격 확보
			            }
			        }
			    },
			    scales: {
			        x: {
			            ticks: {
			                color: '#111', // 💡 X축 레이블 (사이즈 불량 등) 색상
			                font: { size: 12, weight: '500' }
			            }
			        },
			        y: {
			            beginAtZero: true,
			            ticks: {
			                color: '#111', // 💡 Y축 레이블 (숫자) 색상
			                stepSize: 1
			            }
			        }
			    }
			}
	 });
	
	 
	 
	 
	 
	 //공공데이터 날씨 가져오기.
	
	window.addEventListener('load', bind);
	async function bind() {
		
		//오늘 날짜 자동 가져오기
		const today = new Date();
		// 연도 가져오기 + 숫자 문자로 바꾸기(달 가져오기(js에서는 0월부터 시작. 그래서 +1)).2자리로
		// 만들기.(5월이면05로. 2자리, 1자리면 0추가)
		const sysdate = today.getFullYear() +
		                String(today.getMonth()+1).padStart(2, '0') +
		                String(today.getDate()).padStart(2, '0');
		
	    const nyear = today.getFullYear();
	    const nmonth = String(today.getMonth()+1).padStart(2, '0');
	    const ndate = String(today.getDate()).padStart(2, '0');
		
		//현재 시간에서 1시간 빼기
		today.setHours(today.getHours()-1);
		
		//시간을 2자리 문자열로
        const hours = String(today.getHours()).padStart(2, '0');
		
		//시간을 2자리 문자열로
        const nowHours = String(today.getHours() + 2).padStart(2, '0');
		
		//기상청 형식에 맞게 뒤에 '00' 붙이기
		const base_time = hours + '00';
		
		//기상청 형식에 맞게 뒤에 '00' 붙이기
		const now_time = nowHours + '00';
		
		//공공 데이터 포털의 내 키
		const key = '3229955cd446f5d563c0d814551c748992c0192563f925e1ea342f6108780f20';
	    
		//공공 데이터 포털 주소
		let url = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst';
		
		//천안시 동남구 좌표
		const nx = '63';
		const ny = '110';
		
		//각 추가사항 기입
		 url += '?serviceKey='+key;
	     url += '&numOfRows=1000';
	     url += '&pageNo=1';
	     url += '&dataType=JSON';
	     url += '&base_date='+sysdate;
	     url += '&base_time='+base_time;
	     url += '&nx='+nx;
	     url += '&ny='+ny;
	     
	     try {
	    	 //fetch 호출
	    	 const response = await fetch(url);
	    	 
	    	 if(!response.ok) {
	    		 throw new Error('네트워크 응답에 문제가 있습니다.');
	    	 }
	    	 
	    	 const data = await response.json();
	    	 
	    	 //데이터 추출
	    	 const item = data.response.body.items.item;
	    	 
	    	 let weatherResult = {
	    			 temp: '',
	    			 humidity: '',
	    			 wind: '',    			 
	    			 rain: ''   			 
	    	 }
	    	 console.log('item : ', item)
	    	 console.log('weatherResult : ', weatherResult);
	    	 
	    	 
	    	 
	    	 item.forEach(function(m, index) {
	    		 
	    		 if( now_time === m.fcstTime ) {
	    		 if (m.category === 'T1H') weatherResult.temp = m.fcstValue;
	    		 if (m.category === 'REH') weatherResult.humidity = m.fcstValue;
	    		 if (m.category === 'WSD') weatherResult.wind = m.fcstValue;
	    		 if (m.category === 'RN1') weatherResult.rain = m.fcstValue;
	    			 
	    		 }
	    	 });
	    	 
	    	 console.log('weatherResult : ', weatherResult);
	    	 
	    	 console.log(data);
	    	 //화면에 뿌려주기
	    	 
	    	 const weather = document.querySelector('.weather');
	    	 if(weather) {
	    		 weather.innerHTML = `
	    		 
	    		 <h4>현재 온도 : \${ weatherResult.temp } 도</h4>
	    		 <h4>현재 습도 : \${ weatherResult.humidity } %</h4>
	    		 <h4>현재 풍속 : \${ weatherResult.wind } m/s</h4>
	    		 <h4>현재 강수량 : \(${ weatherResult.rain!= null? weatherResult.rain : '-' } ) mm</h4>
	    		 
	    		 `;
	    	 }
	    	 
	    	 console.log("천안 동남구 날씨 데이터:", weatherResult);
	    	 
	     } catch(error) {
	    	 console.log("데이터 가져오는 중 오류 발생 :", error);
	     }
	     
	}
	
    
    </script>


</body>

</html>