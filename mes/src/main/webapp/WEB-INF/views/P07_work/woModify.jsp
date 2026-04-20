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
	<script src="/mes/static/js/07_work/modify.js"></script>

</head>

<body>

	<%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>

	<div class="layout_snb">
		<div class="snbContent">
			<%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
		</div>
		<div class="content">
			<!-- 상단 -->
			<form id="woForm" method="post" action="/mes/womodify">
				<input type="hidden" name="cmd" id="actionType">
				<input type=hidden name="woId" value="${woInfo.woId}">
				<div class="page-header">
					<div>
						<h2>작업지시 내용 수정</h2>
						<p class="sub-text">작업지시 내용을 수정해주세요.</p>
					</div>

					<div class="button-group">
						<a href="/mes/workorder?woId=${woInfo.woId}">
							<button type="button" class="buttonWhite" id="backBtn">취소</button>
						</a>
						<button type="button" class="buttonMain" onclick="submitForm('update')">수정 완료</button>
    					<button type="button" class="buttonRed" onclick="submitForm('delete')">삭제</button>
					</div>
				</div>


				<!-- 카드 -->
				<div class="card">
					<div class="card-header">
						<strong>작업지시 정보</strong> <span class="woId">(${woInfo.woId})</span>
					</div>

					<div class="form-grid">
						<!-- 생산 계획 -->
						<div class="form-group">
							<label>생산 계획</label>
							<input type="text" id="planId" value="${woInfo.planId}" placeholder="생산계획 ID" readonly>
						</div>

						<!-- 제품 -->
						<div class="form-group">
							<label>제품</label>
							<input type="text" id="itemId" value="${woInfo.itemName} (${woInfo.itemId})" placeholder="제품명 (제품ID)" readonly>
						</div>

						<!-- 진행 현황 -->
						<div class="form-group">
							<label>생산 계획 진행 현황</label>
							<input type="text" id="progressQty" value="${woInfo.planPrev} / ${woInfo.planQty}" placeholder="현재 생산량 / 계획 수량" readonly>
						</div>

						<!-- 목표 수량 -->
						<div class="form-group">
							<label>목표 수량</label>
							<input type="number" name="targetQty" value="${woInfo.woQty}" placeholder="목표 수량 입력" id="targetQty" min="1">
						</div>

						<!-- 작업일 -->
						<div class="form-group">
							<label>작업일</label>
							<input type="date" name="workDate" value="${woInfo.workDate}" id="workDate" min="${woInfo.sDate}" max="${woInfo.eDate}">
							
						</div>

						<!-- 관리자 -->
						<div class="form-group">
							<label>관리자</label>
							<input type="text" id="director" placeholder="관리자 (사원번호)" value="${woInfo.dName}" id="director" readonly>
						</div>

						<!-- 작업자 -->
						<div class="form-group full">
							<label>작업자</label>
							<div class="inline-group">
								<input type="hidden" name="workerId" id="workerId" value="${woInfo.worker}">
								<input type="text" placeholder="작업자 (사원번호)" name="worker" id="worker" value="${woInfo.wName} (${woInfo.worker})" readonly>
								<button type="button" class="buttonSub" id="workerBtn">조회</button>
							</div>
						</div>

						<!-- 전달 사항 -->
						<div class="form-group full">
							<label>전달 사항</label>
							<textarea placeholder="기타 전달 사항 입력 (선택)" name="content">${woInfo.content}</textarea>
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