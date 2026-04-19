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

<title>작업 상세 페이지</title>

<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>

<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
    
<link rel="stylesheet" href="/mes/static/css/P07_work/detail.css">
<script src="/mes/static/js/07_work/detail.js"></script>

</head>
<body>

<!-- 	LOT, BOM, 공정 내용 추가해야 함 -->

	<%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>
	
	<div class="layout_snb">
		<div class="snbContent">
			<%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
		</div>
		<div class="content">
		    <!-- 상단 타이틀 영역 -->
		    <div class="page-header">
		        <div class="title-group">
		            <h2>작업기록</h2>
		            <p class="sub-text">작업코드 : ${woInfo != null ? woInfo.woId : '-'}</p>
		        </div>
		
		        <div class="button-group">
			        <a href="/mes/worklist">
			            <button type="button" class="buttonWhite">목록으로</button>
			        </a>
			        <c:if test="${woInfo.woStatus != 60}">
				        <a href="/mes/womodify?woId=${woInfo.woId}">
				            <button type="button" class="buttonMain" <c:if test="${(empty dto.auth) || dto.auth < 2}">style="display: none;"</c:if> >지시 수정</button>
				        </a>
				        <a href="/mes/contentmodify?woId=${woInfo.woId}">
				            <button type="button" class="buttonSub" <c:if test="${((dto.auth < 2) && !(woInfo.worker eq dto.empid)) || woInfo.woStatus == 30}">style="display: none;"</c:if> >내용 수정</button>
				        </a>
				    </c:if>
		        </div>
		    </div>
		
		
		    <!-- 작업정보 -->
		    <div class="card">
		        <div class="card-header">
		            <div>
		                <strong>작업정보</strong>
		                <span class="sub">생산계획 : (${woInfo != null ? woInfo.planId : '-'})</span>
		            </div>
	            	<c:if test="${ woInfo.woStatus == 10 }">
	            		<span class="status before">작업 전</span>
	            	</c:if>
	            	<c:if test="${ woInfo.woStatus == 20 }">
	            		<span class="status ongoing">작업 중</span>
	            	</c:if>
	            	<c:if test="${ woInfo.woStatus == 30 }">
	            		<span class="status finish">작업 완료</span>
	            	</c:if>
	            	<c:if test="${ woInfo.woStatus == 40 }">
	            		<span class="status qcFin">검사 완료</span>
	            	</c:if>
	            	<c:if test="${ woInfo.woStatus == 60 }">
	            		<span class="status hold">입고 완료</span>
	            	</c:if>
	            	<c:if test="${ woInfo.woStatus == 50 }">
	            		<span class="status hold">보류</span>
	            	</c:if>
	            	<c:if test="${ woInfo == null }">
	            		<span class="status">-</span>
	            	</c:if>
		        </div>
		
		        <div class="info-grid">
		            <div class="info-box">
		                <span class="label">작업일</span>
		                <span class="value">${woInfo != null ? woInfo.workDate : ' - '}</span>
		            </div>
		
		            <div class="info-box">
		                <span class="label">작업자</span>
		                <span class="value">${woInfo != null ? woInfo.wName : ' - '} (${woInfo != null ? woInfo.worker : ' - '})</span>
		            </div>
		
		            <div class="info-box">
		                <span class="label">제품</span>
		                <span class="value">${woInfo != null ? woInfo.itemName : ' - '} (${woInfo != null ? woInfo.itemId : ' - '})</span>
		            </div>
		
		            <div class="info-box">
		                <span class="label">완제품 LOT</span>
		                <span class="value"> ${woInfo.lotId != null ? woInfo.lotId : ' - '} </span>
		            </div>
		        </div>
		        
		        <hr>
		
		        <!-- 진행률 -->
		        <div class="progress-area">
		            <div class="progress-header">
		                <span class="progress-title">진행률</span>
		                <span class="percent">( ${woInfo != null ? woInfo.prevQty : ' - '} / ${woInfo != null ? woInfo.woQty : ' - '} ) <strong><fmt:formatNumber value="${woInfo != null ? (woInfo.prevQty/woInfo.woQty)*100 : ' 0 '}" maxFractionDigits="1"/>%</strong></span>
		            </div>
		
		            <div class="progress-bar">
		                <div class="progress-fill" style="width: ${woInfo != null ? (woInfo.prevQty/woInfo.woQty)*100 : '0'}%;"></div>
		            </div>
		        </div>
		    </div>
		    
		    
		    
		    <!-- 작업지시 상세사항 -->
		    <div class="card">
		    	<div class="card-header">
		    		<strong>작업지시 상세사항</strong>
		    	</div>
		    	
		    	<table class="table">
		    		<tr>
		    			<td class="woContent">
		    				<c:if test="${empty woInfo.content}">
		    					내용 없음
		    				</c:if>
		    				<c:if test="${not empty woInfo.content}">
		    					${woInfo.content}
		    				</c:if>
		    			</td>
		    		</tr>
		    	</table>
		    </div>
		
			<div class="procInfo">
			    <!-- BOM -->
			    <div class="card BOM">
			        <div class="card-header">
			            <strong>BOM</strong>
			        </div>
			
			        <table class="bomList">
			            <thead>
			                <tr>
			                    <th>자재</th>
			                    <th>규격</th>
			                    <th>소요량 (단위)</th>
			                </tr>
			            </thead>
			            <tbody>
			            	<c:forEach var="bom" items="${ bom }">
				                <tr>
				                    <td>${bom.cName}</td>
				                    <td>${bom.spec}</td>
				                    <td>${bom.ea} (${bom.unit})</td>
				                </tr>
			                </c:forEach>
			            </tbody>
			        </table>
			    </div>
			
			
			    <!-- 공정 정보 -->
			    <div class="card process">
			        <div class="card-header">
			        	<div>
				            <strong>공정 정보</strong>
				            <span class="sub">${empty process or empty process[0].procName ? '등록 필요' : process[0].procName}</span>
			        	</div>
			        </div>
					<div class="processInfo">${process[0].procInfo}</div>
						
			        <table class="processList">
			        	<thead>
			        		<tr>
			        			<th>순서</th>
			        			<th>내용</th>
			        		</tr>
			        	</thead>
			        	<tbody>
			        		<c:choose>
			        			<c:when test="${empty process}">
			        				<tr>
			        					<td colspan="2">내용 없음</td>
			        				</tr>
			        			</c:when>
			        			
			        			<c:otherwise>
			        				<c:forEach var="p" items="${process}">
			        					<tr>
			        						<td class="step">${p.stepSeq}</td>
			        						<td>${p.stepName}</td>
			        					</tr>
			        				</c:forEach>
			        			</c:otherwise>
			        		</c:choose>
			        	</tbody>
			        	
			        </table>
			        
			    </div>
			    
			</div>
		    
		</div>
	</div>

</body>
</html>