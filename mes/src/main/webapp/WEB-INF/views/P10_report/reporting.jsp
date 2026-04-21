<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="kr">
<head>
<meta charset="UTF-8">
<!-- UTF-8로 통일 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MES 공정 종합 리포트</title>
<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">
<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<link rel="stylesheet" href="/mes/static/css/P10_report/reporting.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
/* 리포트 전용 추가 스타일 (비율 조정) */
.board-box {
	display: flex;
	flex-wrap: wrap;
	gap: 20px;
	padding: 20px;
}

.box-type2 {
	flex: 1 1 20%;
	min-width: 200px;
} /* KPI 박스 */
.box-type4 {
	flex: 1 1 45%;
	min-width: 400px;
} /* 현황 박스 */
.box-type2-1 {
	flex: 1 1 48%;
	min-width: 450px;
	height: 480px;
} /* 차트 박스 */
.box-type3 {
	flex: 1 1 100%;
} /* 하단 리스트 */
.big.green {
	font-size: 3rem;
	font-weight: bold;
	color: #2ecc71;
	text-align: center;
	margin: 10px 0;
}

.issue-item {
	display: flex;
	justify-content: space-between;
	padding: 12px;
	border-bottom: 1px solid #eee;
	align-items: center;
}

.status-badge {
	padding: 4px 8px;
	border-radius: 4px;
	font-size: 0.8rem;
	color: white;
}

/* 1. 부모 컨테이너: flex를 확실히 주어 영역을 좌/우로 쪼갭니다 */
.layout_snb {
    display: flex !important;
    width: 100%;
}

/* 2. SNB 영역: 너비를 고정하고 대시보드가 침범하지 못하게 합니다 */
.snbContent {
    width: 250px !important; /* 기존 SNB 너비에 맞게 조절 */
    min-width: 250px !important;
    flex-shrink: 0;
    position: relative;
    z-index: 100;
}

/* 3. 콘텐츠 영역: SNB가 차지한 250px을 제외한 나머지만 쓰게 합니다 */
.content {
    flex: 1;
    min-width: 0; /* 내부 차트가 삐져나와도 레이아웃을 유지함 */
    width: calc(100% - 250px); 
}
</style>
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
					<h1>📊 리포팅</h1>
					<h7>실시간 생산 효율 및 품질 분석 현황 (최근 24시간 기준)</h7>
				</div>

				<div class="board-box">
					<!-- 1. KPI 영역 -->
					<div class="box-type2 radius shadow">
						<h3>오늘 목표 대비 작업률</h3>
						<div class="big green">
							<fmt:formatNumber
								value="${ (sumdate.total_prev / sumdate.total_wo) * 100 }"
								pattern="0.0" />
							<span style="font-size: 1.5rem;">%</span>
						</div>
						<h4 style="text-align: center; color: #888;">계획: ${ sumdate.total_wo }
							/ 실적: ${ sumdate.total_prev }</h4>
					</div>

					<div class="box-type2 radius shadow">
						<h3>오늘 공정 양품률</h3>
						<div class="big green" style="color: #3498db;">
							<fmt:formatNumber
								value="${ ((sumdate.total_prev - sumdate.total_cnt) / sumdate.total_prev) * 100 }"
								pattern="0.0" />
							<span style="font-size: 1.5rem;">%</span>
						</div>
						<h4 style="text-align: center; color: #888;">${ sumdate.total_prev }건
							중 ${ sumdate.total_prev - sumdate.total_cnt }건 합격</h4>
					</div>

					<!-- 2. 설비 가동 상태 -->
					<div class="box-type4 radius shadow">
						<div class="chart-1">
							<h3>설비 실시간 가동 현황</h3>
							<div
								style="display: flex; justify-content: space-around; align-items: center; height: 120px;">
								<div style="text-align: center;">
									<h4 style="color: #2ecc71; font-size: 1.2rem;">● 가동 중 (${ press.active_cnt })</h4>
									<p style="font-weight: bold;"></p>
								</div>
								<div style="border-left: 1px solid #eee; height: 60px;"></div>
								<div style="text-align: center;">
									<h4 style="color: #e74c3c; font-size: 1.2rem;">● 비가동 (${ press.error_cnt })</h4>
									<p style="font-weight: bold; color: #e74c3c;"></p>
								</div>
							</div>
						</div>
					</div>

					<!-- 3. 차트 영역 (좌: 환경-품질 상관분석, 우: 불량 원인) -->
					<div class="box-type2-1 radius shadow">
						<h3>월간 생산량</h3>
						<div style="height: 350px; padding: 10px;">
							<canvas id="month_prey_chart"></canvas>
						</div>
					</div>

					<div>
						<div class="box-type2-2 radius shadow">
							<div style="width: 100%; padding: 20px; height: 220px;">
								<canvas id="defect_top5_chart"></canvas>
							</div>
						</div>

						<div class="box-type2-2 radius shadow"
							style="height: 250px; margin-top: 15px;">
							<h4>품목별 생산 효율 (TOP 3)</h4>
							<ul class="short-box" style="padding: 10px; list-style: none;">
								<c:forEach var="item" items="${sumclear}" varStatus="status"
									end="2">
									<li
										style="display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px solid #f5f5f5;">
										<span><strong>${status.count}위</strong>
											${item.item_name}</span> <span
										style="color: #2e7d32; font-weight: bold;"> <fmt:formatNumber
												value="${(item.total_prev / item.total_wo)}" type="percent"
												maxFractionDigits="1" />
									</span>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>

					<!-- 4. 하단 상세 이슈 리스트 -->
					<div class="box-type3 radius shadow">
						<h3>실시간 공정 이슈 및 시정조치 이력</h3>
						<div class="short-box" style="padding: 10px;">



							<c:forEach var="e" items="${errormch}">
								<a></a>
								<div class="issue-item"
									style="display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid #eee;">

									<!-- 1. 분류 및 내용 -->

									<div style="flex: 2;">
										<a href="/mes/eqdetail?eqId=${ e.eq_id }"> <span
											style="font-weight: bold;">[${e.insp_type}]</span>
											${e.insp_content} (${e.eq_id})
										</a>
									</div>


									<!-- 2. 발생 시간 (종료 시간 기준 정렬이므로 종료 시간 표시) -->
									<div style="flex: 1; text-align: center; color: #666;">
										<fmt:formatDate value="${e.end_time}"
											pattern="yyyy-MM-dd HH:mm" />
									</div>

									<!-- 3. 상태 배지 (정비 종류에 따라 색상 분기 가능) -->
									<div style="flex: 0.5; text-align: right;">
										<c:choose>
											<c:when test="${not empty e.end_time}">
												<span class="buttonMain small"
													style="background: #2ecc71; border: none; padding: 5px 10px; color: #fff; border-radius: 4px;">조치완료</span>
											</c:when>
											<c:otherwise>
												<span class="buttonMain small"
													style="background: #e67e22; border: none; padding: 5px 10px; color: #fff; border-radius: 4px;">진행중</span>
											</c:otherwise>
										</c:choose>
									</div>

								</div>
							</c:forEach>

						</div>
					</div>
				</div>
				<!-- board-box 끝 -->
			</div>
		</div>
	</div>



	<script>
	
	 
	 
	 
	const ctx = document.getElementById('month_prey_chart').getContext('2d');

	const labels = [];
	const dataWo = [];    
	const dataGood = [];  

	// 1. 데이터가 리스트 형태로 잘 도는지 확인
	<c:forEach var="item" items="${sumclear}" varStatus="status">
	    labels.push('${item.item_name}');
	    dataWo.push(${item.total_wo});
	    dataGood.push(${item.total_prev - item.total_cnt});
	    
	    // 개발자 도구(F12) 콘솔에서 실제 값이 어떻게 들어가는지 확인용
	    console.log("품목[" + ${status.index} + "]: " + '${item.item_name}', 
	                "목표:" + ${item.total_wo}, 
	                "실적:" + ${item.total_prev - item.total_cnt});
	</c:forEach>

	const monthlyChart = new Chart(ctx, {
	    type: 'bar',
	    data: {
	        labels: labels,
	        datasets: [
	            {
	                label: '목표 수량',
	                data: dataWo, // [1000, 44000, ...] 처럼 순서대로 들어있어야 함
	                backgroundColor: 'rgba(52, 152, 219, 0.5)',
	                borderWidth: 1
	            },
	            {
	                label: '양품 생산량',
	                data: dataGood, // [992, 43966, ...] 처럼 순서대로 들어있어야 함
	                backgroundColor: 'rgba(46, 125, 50, 0.7)',
	                borderWidth: 1
	            }
	        ]
	    },
	    options: {
	        responsive: true,
	        scales: {
	            y: { beginAtZero: true }
	        }
	    }
	});
	
	 
	// 1. 변수명 충돌 방지를 위해 고유한 이름(defectCtx) 사용
	const defectCtx = document.getElementById('defect_top5_chart').getContext('2d');

	const defectLabels = [];
	const defectData = [];

	// 2. JSTL 반복문으로 데이터 채우기
	<c:forEach var="f" items="${topfive}">
	    defectLabels.push('${f.dtype_name}');
	    defectData.push(${f.total_cnt});
	</c:forEach>

	// 3. 도넛 차트 생성
	const defectChart = new Chart(defectCtx, {
    type: 'doughnut',
    data: {
        labels: defectLabels,
        datasets: [{
            data: defectData,
            backgroundColor: [
                'rgba(231, 76, 60, 0.7)',  
                'rgba(230, 126, 34, 0.7)', 
                'rgba(241, 196, 15, 0.7)', 
                'rgba(52, 152, 219, 0.7)', 
                'rgba(155, 89, 182, 0.7)'  
            ],
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            // 1. 차트 제목 설정
            title: {
                display: true,
                text: '주요 불량 원인 (TOP 5)',
                align: 'start',      // 왼쪽 정렬 (h3 느낌)
                padding: {
                    top: 10,
                    bottom: 20
                },
                font: {
                    size: 18,        // 적당한 크기
                    weight: 'bold',
                    family: 'Pretendard, sans-serif' // 폰트 설정
                },
                color: '#333'        // 진한 회색
            },
            // 2. 범례 설정
            legend: {
                position: 'right',
                labels: {
                    boxWidth: 12,
                    padding: 15
                }
            }
        },
        layout: {
            padding: {
                left: 10,
                right: 10,
                bottom: 10
            }
        }
    }
});
	 
	 
	
	
    
    </script>


</body>

</html>