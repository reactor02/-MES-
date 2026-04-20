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

<title>설비 점검 이력</title>

<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>

<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
    
<link rel="stylesheet" href="/mes/static/css/P08_quality/add.css">
<link rel="stylesheet" href="/mes/static/css/P09_equip/equip.css">
<!-- <script src="/mes/static/js/09_equip/modifyInsp.js"></script> -->

</head>
<body>

    <%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>
    
    <div class="layout_snb">
        <div class="snbContent">
            <%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
        </div>
        <div class="content">
            <!-- 상단 -->
			<form method="post" action="/mes/eqlogmodify" id="eqForm">
				<input type="hidden" name="cmd" value="" id="actionType">
				<input type="hidden" name="logId" value="${logInfo.logId}">
				<div class="page-header">
					<div>
						<h2>설비 점검 이력</h2>
						<p class="sub-text" <c:if test="${dto.auth < 2 || !(dto.empid eq logInfo.wId)}">style="display: none;"</c:if> >설비 점검 이력과 결과를 등록해주세요.</p>
						
					</div>

					<div class="button-group">
						<a href="/mes/eqdetail?eqId=${logInfo.eqId}">
							<button type="button" class="buttonWhite" id="backBtn">취소</button>
						</a>
						<button type="submit" class="buttonMain" id="addBtn" <c:if test="${dto.auth < 2 || !(dto.empid eq logInfo.wId)}"> style="display:none;" </c:if> >수정</button>
<!-- 						<button type="button" class="buttonRed" onclick="submitForm('delete')">삭제</button> -->
					</div>
				</div>


				<!-- 카드 -->
				<div class="card">
					<div class="card-header">
						<strong>설비 점검 정보</strong>
					</div>

					<div class="form-grid">
						<div class="form-group">
							<label>설비</label>
							<input type="text" id="equipment" placeholder="설비명 (설비 코드)" value="${ logInfo.eqName } (${logInfo.eqId})" readonly>
							<input type="hidden" name="eqId" id="eqId" value="${logInfo.eqId}">
						</div>

						<div class="form-group">
							<label>담당자</label>
							<input type="text" id="worker" placeholder="작업자" value="${logInfo.wName} (${logInfo.wId})" readonly>
							<input type="hidden" name="wId" value="${logInfo.wId}">
						</div>

						<div class="form-group">
							<label>점검 시작 시간</label>
							<div class="timeFlex">
								<input type="date" id="sDate" name="sDate" value="${sDate}" <c:if test="${dto.auth < 2 || !(dto.empid eq logInfo.wId)}">readonly</c:if> >
								<input type="time" id="sTime" name="sTime" value="${sTime}" <c:if test="${dto.auth < 2 || !(dto.empid eq logInfo.wId)}">readonly</c:if> >
							</div>
						</div>
						
						<div class="form-group">
							<label>점검 종료 시간</label>
							<div class="timeFlex">
								<input type="date" id="eDate" name="eDate" value="${eDate}" <c:if test="${dto.auth < 2 || !(dto.empid eq logInfo.wId)}">readonly</c:if> >
								<input type="time" id="eTime" name="eTime" value="${eTime}" <c:if test="${dto.auth < 2 || !(dto.empid eq logInfo.wId)}">readonly</c:if> >
							</div>
						</div>

						<div class="form-group">
							<label>점검 항목</label>
							<input type="text" id="inspType" name="inspType" placeholder="점검 항목 입력" value="${logInfo.inspType}" <c:if test="${dto.auth < 2 || !(dto.empid eq logInfo.wId)}">readonly</c:if> >
						</div>

						<div class="form-group full">
							<label>점검 결과 및 조치사항</label>
							<textarea placeholder="점검 결과 및 조치사항 입력" id="content" name="content" <c:if test="${dto.auth < 2 || !(dto.empid eq logInfo.wId)}">readonly</c:if> >${logInfo.inspContent}</textarea>
						</div>
					</div>
				</div>
			</form>
            
        </div> <!-- content -->
    </div>
    
</body>
</html>