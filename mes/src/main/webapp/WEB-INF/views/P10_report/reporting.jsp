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
<link rel="stylesheet" href="/mes/static/css/P10_report/defectReporting.css">
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
    <!-- 상단 타이틀 -->
    <div class="title-box">
        <h1>공정 종합 리포트</h1>
        <h7>실시간 생산 효율 및 품질 분석 현황</h7>
    </div>

    <div class="board-box">
        
        <!-- 1. 종합 생산 효율 (KPI) -->
        <div class="box-type2 radius">
            <h3>목표 대비 달성률</h3>
            <div class="big green">
                92 <span style="font-size: 1.5rem;">%</span>
            </div>
            <h4 style="text-align:center; color:#888;">계획: 1,000 / 실적: 920</h4>
        </div>

        <div class="box-type2 radius">
            <h3>공정 양품률 (Yield)</h3>
            <div class="big green">
                98.5 <span style="font-size: 1.5rem;">%</span>
            </div>
            <h4 style="text-align:center; color:#888;">전체 920건 중 906건 합격</h4>
        </div>

        <!-- 2. 설비 가동 상태 (실시간) -->
        <div class="box-type4 radius">
            <div class="chart-1 weather">
                <h3>설비 가동 현황</h3>
                <div style="display: flex; justify-content: space-around; align-items: center; height: 100px;">
                    <div style="text-align: center;">
                        <h4 style="color: #2ecc71;">● 가동 중</h4>
                        <p>Line A, Line C</p>
                    </div>
                    <div style="text-align: center;">
                        <h4 style="color: #e74c3c;">● 비가동</h4>
                        <p>Line B (점검)</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- 3. 품질 및 환경 상관관계 분석 (차트 영역) -->
        <div class="box-type2-1 radius" style="height: 450px;">
            <div style="width: 100%;">
                <h3>시간대별 불량 발생 & 온도 변화</h3>
                <div style="height : 350px;">
                    <!-- 여기에 불량 건수와 온도를 같이 보여주는 콤보 차트 배치 -->
                    <canvas id="quality-env-chart"></canvas>
                </div>
            </div>
        </div>

        <!-- 4. 주요 불량 원인 파레토 (Top 5) -->
        <div class="box-type2-1 radius" style="height: 450px;">
            <div style="width: 100%;">
                <h3>주요 불량 원인 (TOP 5)</h3>
                <div style="height : 350px;">
                    <!-- 기존에 만드신 막대 그래프를 활용하여 불량 빈도 순 정렬 -->
                    <canvas id="top-defect-chart"></canvas>
                </div>
            </div>
        </div>

        <!-- 하단 상세 리스트 또는 조치 이력 -->
        <div class="box-type3 radius">
            <h3>실시간 공정 이슈 및 시정조치 이력</h3>
            <div class="short-box">
                <!-- 테이블 형태로 구성하면 더 좋습니다 -->
                <div style="display: flex; justify-content: space-between; padding: 10px; border-bottom: 1px solid #eee;">
                    <span>[장비] Line B 메인 모터 과열</span>
                    <span>2026-04-18 14:20</span>
                    <span class="buttonMain small">조치완료</span>
                </div>
                <div style="display: flex; justify-content: space-between; padding: 10px; border-bottom: 1px solid #eee;">
                    <span>[품질] 함침액 점도 기준 초과</span>
                    <span>2026-04-18 15:45</span>
                    <span class="buttonMain small" style="background:#e67e22;">확인중</span>
                </div>
            </div>
        </div>

    </div>
</div>
    </div>
</div>



	<script>
	// 1. EL 파라미터 추출
	const p_id = "${param.defect_id}";
	const p_date = "${param.qc_edate}";
	const p_name = "${param.dtype_name}"; // 불량 유형 이름 추가
	const p_solution = "${param.solution}"; // 해결 이름 추가
	const p_cnt = Number("${param.defect_cnt}") || 0; 

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