<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.*" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>검사계획 수정</title>

<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>

<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
    
<link rel="stylesheet" href="/mes/static/css/P08_quality/add.css">
<script src="/mes/static/js/08_quality/qcModify.js"></script>

</head>
<body>

    <%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>
    
    <div class="layout_snb">
        <div class="snbContent">
            <%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
        </div>
        <div class="content">
            <!-- 상단 -->
			<form id="qcForm" method="post" action="/mes/qcmodify">
				<input type="hidden" name="cmd" id="actionType">
				<input type=hidden name="qcId" value="${qcInfo.qcId}">
				<input type=hidden name="woId" value="${qcInfo.woId}">
				<div class="page-header">
					<div>
						<h2>검사계획 수정</h2>
						<p class="sub-text">검사계획 내용을 수정해주세요.</p>
					</div>

					<div class="button-group">
						<a href="/mes/qcdetail?qcId=${qcInfo.qcId}">
							<button type="button" class="buttonWhite" id="backBtn">취소</button>
						</a>
						<button type="button" class="buttonMain" onclick="submitForm('update')">수정 완료</button>
    					<button type="button" class="buttonRed" onclick="submitForm('delete')">삭제</button>
					</div>
				</div>


				<!-- 카드 -->
				<div class="card">
					<div class="card-header">
						<strong>검사계획 정보</strong> <span class="qcId">(${qcInfo.qcId})</span>
					</div>

					<div class="form-grid">
						<!-- 작업 -->
						<div class="form-group full left">
							<label>완료된 작업</label>
							<input type="text" id="woId" value="${qcInfo.woId}" placeholder="완료된 작업 코드" readonly>
						</div>

						<!-- 검사 예정 제품 -->
						<div class="form-group">
							<label>검사 예정 제품</label>
							<input type="text" id="item" value="${qcInfo.iName} (${qcInfo.itemId})" placeholder="제품명 (제품 코드)" readonly>
						</div>

						<!-- 검사 수량 -->
						<div class="form-group">
							<label>검사 수량</label>
							<input type="text" id="qty" value="${qcInfo.qty}" placeholder="검사 예정 수량" readonly>
						</div>
						
						<!-- 검사 시작일 -->
						<div class="form-group">
							<label>검사 시작일</label>
							<input type="date" id="qcDate" name="sDate" value="${qcInfo.sDate}" min="${qcInfo.workDate}">
						</div>

						<!-- 검사자 -->
						<div class="form-group">
							<label>검사자</label>
							<div class="inline-group">
								<input type="hidden" name="workerId" id="workerId" value="${qcInfo.wId}">
								<input type="text" placeholder="작업자 (사원번호)" name="worker" id="worker" value="${qcInfo.wName} (${qcInfo.wId})" readonly>
								<button type="button" class="buttonSub" id="workerBtn">조회</button>
							</div>
						</div>

						<!-- 전달 사항 -->
						<div class="form-group full">
							<label>전달 사항</label>
							<textarea placeholder="기타 전달 사항 입력 (선택)" name="content">${qcInfo.content}</textarea>
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