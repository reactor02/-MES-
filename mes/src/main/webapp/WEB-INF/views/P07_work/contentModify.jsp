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

<title>작업기록 수정</title>

<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>

<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>

<link rel="stylesheet" href="/mes/static/css/P07_work/contentModify.css">
<script src="/mes/static/js/07_work/contentModify.js"></script>

</head>
<body>

	 <%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>
    
    <div class="layout_snb">
        <div class="snbContent">
            <%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
        </div>
        <div class="content">
        	<!-- 상단 타이틀 영역 -->
		    <div class="page-header">
		        <div class="title-group">
		            <h2>작업기록 수정</h2>
		            <p class="sub-text">현재 진행 중이거나 완료된 작업 내용을 입력해주세요.</p>
		        </div>
		
		        <div class="button-group">
			        <button type="button" class="buttonWhite" onclick="location.href='/mes/workorder?woId=${woInfo.woId}'">취소</button>
			        <button type="button" class="buttonMain" onclick="submitContentModify()">수정 완료</button>
		        </div>
		    </div>
		
		    <!-- 폼 -->
		    <form method="post" action="/mes/contentmodify" id="contentModify">
		
		        <div class="card">
		        	<div class="card-header">
		        		<input type="hidden" name="woId" value="${woInfo.woId}">
						<strong>작업기록</strong> <span class="woId">(${woInfo.woId})</span>
			            <div class="form-row right-align">
			                <label>
			                    <input type="radio" name="status" value="10" <c:if test="${woInfo.woStatus == 10}">checked</c:if>> 작업전
			                </label>
			                <label>
			                    <input type="radio" name="status" value="20" <c:if test="${woInfo.woStatus == 20}">checked</c:if>> 작업 중
			                </label>
			                <label>
			                    <input type="radio" name="status" value="30" <c:if test="${woInfo.woStatus == 30}">checked</c:if>> 작업완료
			                </label>
			                <label>
			                    <input type="radio" name="status" value="50" <c:if test="${woInfo.woStatus == 50}">checked</c:if>> 보류
			                </label>
			            </div>
					</div>
		
		            <!-- 작업 -->
		            <div class="form-row">
		                <div class="form-group">
		                    <label>생산계획 코드</label>
		                    <input type="text" value="${woInfo.planId}" placeholder="생산계획 코드" readonly>
		                </div>
		
		                <div class="form-group">
		                    <label>작업자</label>
		                    <input type="hidden" name="worker" value="${woInfo.worker}">
		                    <input type="text" value="${woInfo.worker} (${woInfo.wName})" placeholder="작업자명 (작업자 사번)" readonly>
		                </div>
		            </div>
		
		            <!-- 완제품 / 작업일 -->
		            <div class="form-row">
		                <div class="form-group">
		                    <label>생산 제품</label>
		                    <input type="hidden" value="${woInfo.itemId}">
		                    <input type="text" value="${woInfo.itemId} (${woInfo.itemName})" placeholder="생산 제품명 (제품 코드)" readonly>
		                </div>
		
		                <div class="form-group">
		                    <label>작업일</label>
		                    <input type="date" value="${woInfo.workDate}" placeholder="작업일" readonly>
		                </div>
		            </div>
		
		            <!-- 목표 수량 / 완료 수량 -->
		            <div class="form-row">
		                <div class="form-group">
		                    <label>목표 수량</label>
		                    <input type="number" name="woQty" id="woQty" value="${woInfo.woQty}" placeholder="목표 수량" readonly>
		                </div>
		
		                <div class="form-group">
		                    <label>완료 수량</label>
		                    <input type="number"
						       name="prevQty"
						       id="prevQty"
						       value = "${woInfo.prevQty }"
						       placeholder="완료 수량 입력"
						       min="1"
						       max="${woInfo.woQty}"
						       oninput="this.value = clampNumber(this)"
						       onkeydown="return event.key !== '-'">
		                </div>
		                
		            </div>
		
		        </div>
		    </form>
        </div>
    </div>
    
    <c:if test="${not empty errorMsg}">
	    <script>
	        alert("${fn:escapeXml(errorMsg)}");
	    </script>
	</c:if>

</body>
</html>