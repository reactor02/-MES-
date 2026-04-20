<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>작업 지시 등록</title>

	<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

	<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
	<script src="/mes/static/js/00_layout/header.js"></script>

	<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
	<script src="/mes/static/js/00_layout/snb.js"></script>

	<link rel="stylesheet" href="/mes/static/css/P07_work/add.css">
	<script src="/mes/static/js/07_work/add.js"></script>

</head>

<body>

	<%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>

	<div class="layout_snb">
		<div class="snbContent">
			<%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
		</div>
		<div class="content">
			<!-- 상단 -->
			<form method="post" action="/mes/workadd">
				<div class="page-header">
					<div>
						<h2>작업지시 등록</h2>
						<p class="sub-text">작업지시 내용을 입력하세요.</p>
					</div>

					<div class="button-group">
						<button type="button" class="buttonWhite" id="backBtn">취소</button>
						<button type="button" class="buttonMain" id="addBtn">등록</button>
					</div>
				</div>


				<!-- 카드 -->
				<div class="card">
					<div class="card-header">
						<strong>작업지시 정보</strong>
					</div>

					<div class="form-grid">
						<!-- 생산 계획 -->
						<div class="form-group">
							<label>생산 계획</label>
							<div>
								<select class="planSel" id="planSelect" name="planSelect">
									<option value="" disabled selected>생산 계획 코드 선택</option>
									<c:forEach var="plan" items="${planList}">
										<option value="${plan.planId}">${plan.planId}</option>
									</c:forEach>
								</select>
								<button type="button" id="fetchBtn" class="buttonSub">조회</button>
							</div>
							<input type="hidden" id="planId" name="planId">
						</div>

						<!-- 제품 -->
						<div class="form-group">
							<label>제품</label>
							<input type="text" id="itemId" placeholder="제품 ID (제품명)" readonly>
						</div>

						<!-- 진행 현황 -->
						<div class="form-group">
							<label>생산 계획 진행 현황</label>
							<input type="text" id="progressQty" placeholder="현재 생산량 / 계획 수량" readonly>
						</div>

						<!-- 목표 수량 -->
						<div class="form-group">
						    <label>목표 수량</label>
						    <input type="number"
						           name="targetQty"
						           placeholder="목표 수량 입력"
						           id="targetQty"
						           min="1"
						           max=""
						           oninput="this.value = clampNumber(this)"
						           onkeydown="return event.key !== '-'">
						</div>

						<!-- 작업일 -->
						<div class="form-group">
							<label>작업일</label>
							<input type="date" name="workDate" id="workDate">
						</div>

						<!-- 관리자 -->
						<div class="form-group">
							<label>관리자</label>
							<input type="text" id="director" placeholder="관리자 (사원번호)" readonly>
						</div>

						<!-- 작업자 -->
						<div class="form-group full">
							<label>작업자</label>
							<div class="inline-group">
								<input type="hidden" name="workerId" id="workerId">
								<input type="text" placeholder="작업자 (사원번호)" name="worker" id="worker" readonly>
								<button type="button" class="buttonSub" id="workerBtn">조회</button>
							</div>
						</div>

						<!-- 전달 사항 -->
						<div class="form-group full">
							<label>전달 사항</label>
							<textarea placeholder="기타 전달 사항 입력 (선택)" name="content"></textarea>
						</div>
					</div>
				</div>
				
				<div id="workerModal" class="modal" style="display:none;">
				    <div class="modal-content">
				        <h3>작업자 조회</h3>
				        
				        <input type="text" id="workerKeyword" placeholder="이름 또는 사원번호 입력">
				
				        <table class="worker-table">
				            <thead style="background-color: var(--color-main-blue);">
				                <tr>
				                    <th></th>
				                    <th>사원번호</th>
				                    <th>이름</th>
				                </tr>
				            </thead>
				            <tbody id="workerTbody">
				                <!-- JS로 렌더링 -->
				            </tbody>
				        </table>
				
				        <div class="button-group">
				            <button type="button" class="buttonWhite" id="modalCancel">취소</button>
				            <button type="button" class="buttonMain" id="modalSelect">선택</button>
				        </div>
				    </div>
				</div>
			</form>

		</div>
	</div>

</body>

</html>