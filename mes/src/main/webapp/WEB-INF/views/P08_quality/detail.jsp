<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.*" %>
<%@ page import="P08_quality.QcDTO" %>
<%@ page import="P08_quality.QcDefDTO" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>품질검사 결과</title>

<script>
	const total = ${empty qcInfo.qty ? 0 : qcInfo.qty};
	const defect = ${empty qcInfo.defSum ? 0 : qcInfo.defSum};
	const pass = total - defect;
</script>

<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>

<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
    
<link rel="stylesheet" href="/mes/static/css/P08_quality/detail.css">
<script src="/mes/static/js/08_quality/detail.js"></script>

<!-- 그래프 -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>


</head>
<body>

    <%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>
    
    <%
    	List<QcDefDTO> defList = (List)request.getAttribute("defList");
    	QcDTO qcInfo = (QcDTO)request.getAttribute("qcInfo");
    	
    	System.out.println("defList : " + defList);
    	System.out.println("qcInfo : " + qcInfo);
    	
    	int inQty = 0;
    	
    	if (qcInfo != null) {
    		inQty = qcInfo.getQty();
    	}
    	
    	if (defList != null) {
    		for (int i=0; i<defList.size(); i++) {
    			
    			String dispose = defList.get(i).getDispose();
    			if ("Y".equals(dispose)) {
    				inQty = inQty-defList.get(i).getDefCnt();
    			}
    			
    		}
    	}
    	
    	System.out.println("inQty : " + inQty);
    %>
    
    <div class="layout_snb">
        <div class="snbContent">
            <%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
        </div>
        <div class="content">
        	<!-- 상단 타이틀 영역 -->
		    <div class="page-header">
		        <div class="title-group">
		            <h2>품질검사 상세 정보</h2>
		            <p class="sub-text">검사 번호 : ${qcInfo != null ? qcInfo.qcId : '-'}</p>
		        </div>
		
		        <div class="button-group">
			        <a href="/mes/qclist">
			            <button type="button" class="buttonWhite">목록으로</button>
			        </a>
			        <c:if test="${qcInfo.qcStatus != 50}">
				        <a href="/mes/qcmodify?qcId=${qcInfo.qcId}">
				            <button type="button" class="buttonMain" <c:if test="${(empty dto.auth) || dto.auth < 2}">style="display: none;"</c:if> >계획 수정</button>
				        </a>
				        <a href="/mes/qcresultmodify?qcId=${qcInfo.qcId}">
				            <button type="button" class="buttonSub" <c:if test="${(dto.auth < 2) && !(qcInfo.wId eq dto.empid)}">style="display: none;"</c:if> >결과 수정</button>
				        </a>
			        </c:if>
		        </div>
		    </div>
		
		
		    <!-- 작업정보 -->
		    <div class="card">
		        <div class="card-header">
		            <div>
		                <strong>검사정보</strong>
		            </div>
	            	<c:if test="${ qcInfo.qcStatus == 10 }">
	            		<span class="status before">검사 전</span>
	            	</c:if>
	            	<c:if test="${ qcInfo.qcStatus == 20 }">
	            		<span class="status ongoing">검사 중</span>
	            	</c:if>
	            	<c:if test="${ qcInfo.qcStatus == 30 }">
	            		<div class="finIo">
	            			<span class="status qcFin">검사 완료</span>
		            		<a href="/mes/fin?woId=${qcInfo.woId}&qcId=${qcInfo.qcId}&empId=${qcInfo.wId}&itemId=${qcInfo.itemId}&qty=<%= inQty %>&date=${qcInfo.eDate}" class="buttonMain" <c:if test="${(dto.auth < 2) && !(qcInfo.wId eq dto.empid)}">style="display: none;"</c:if> >
		            			생산품 입고
		            		</a>
	            		</div>
	            	</c:if>
	            	<c:if test="${ qcInfo.qcStatus == 50 }">
	            		<span class="status hold">입고 완료</span>
	            	</c:if>
	            	<c:if test="${ qcInfo.qcStatus == 40 }">
	            		<span class="status hold">보류</span>
	            	</c:if>
	            	<c:if test="${ qcInfo == null }">
	            		<span class="status">-</span>
	            	</c:if>
		        </div>
		
		        <div class="info-grid">
		            <div class="info-box">
		                <span class="label">검사 완료일</span>
		                <span class="value">${qcInfo != null && qcInfo.eDate != null ? qcInfo.eDate : ' - '}</span>
		            </div>
		
		            <div class="info-box">
		                <span class="label">검사자</span>
		                <span class="value">${qcInfo != null ? qcInfo.wName : ' - '} (${qcInfo != null ? qcInfo.wId : ' - '})</span>
		            </div>
		
	            	<a href="/mes/workorder?woId=${qcInfo.woId}" title="작업지시 페이지로 이동" class="woLink">
		            	<div class="info-box woBox">
			                <span class="label">작업코드 (작업일)</span>
			                <span class="value">${qcInfo != null ? qcInfo.woId : ' - '}</span>
		            	</div>
	            	</a>
		
		            <div class="info-box">
		                <span class="label">제품</span>
		                <span class="value">${qcInfo != null ? qcInfo.iName : ' - '} (${qcInfo != null ? qcInfo.itemId : ' - '})</span>
		            </div>
		        </div>
		        
		        <hr>
		
		        <!-- 진행률 -->
		        <div class="progress-area">
		            <div class="progress-header">
		                <span class="progress-title">불량률</span>
		                <span class="percent">( ${qcInfo != null ? qcInfo.defSum : ' - '} / ${qcInfo != null ? qcInfo.qty : ' - '} ) <strong><fmt:formatNumber value="${qcInfo != null ? (qcInfo.defSum/qcInfo.qty)*100 : ' 0 '}" maxFractionDigits="1"/>%</strong></span>
		            </div>
		
		            <div class="progress-bar">
		                <div class="progress-fill" style="width: ${qcInfo != null ? (qcInfo.defSum/qcInfo.qty)*100 : '0'}%;"></div>
		            </div>
		        </div>
		    </div>
		    
		    
		    
		    <!-- 작업지시 상세사항 -->
		    <div class="card">
		    	<div class="card-header">
		    		<strong>품질검사 지시사항</strong>
		    	</div>
		    	
		    	<table class="table">
		    		<tr>
		    			<td class="qcContent">
		    				<c:if test="${empty qcInfo.content}">
		    					내용 없음
		    				</c:if>
		    				<c:if test="${not empty qcInfo.content}">
		    					${qcInfo.content}
		    				</c:if>
		    			</td>
		    		</tr>
		    	</table>
		    </div>
		    
		    <!-- 검사 결과 상세 -->
		    <div class="card qcDetail">
		    	<div class="defRate">
			    	<div class="card-header">
			    		<strong>품질검사 결과 상세</strong>
			    	</div>
			    	<div class="card graphCard">
			    		<!-- 차트 영역 -->
			    		<div class="graph-wrap">
			    			<canvas id="defGraph">
			    			</canvas>
			    			<div class="center-text">
					            <div class="label">불량률</div>
					            <div class="value">
					            	<fmt:formatNumber value="${empty qcInfo.qty || empty qcInfo.defSum ? 0 : qcInfo.defSum/qcInfo.qty*100}" maxFractionDigits="1"/>%
					            </div>
					        </div>
			    		</div>
			    		
			    		<!-- 하단 요약 -->
					    <div class="summary">
					        <div class="item">
					            <div class="label">총 생산</div>
					            <div class="value">${qcInfo.qty}</div>
					        </div>
					        <div class="item">
					            <div class="label">
					                <span class="color pass"></span> 합격
					            </div>
					            <div class="value">${qcInfo.qty - qcInfo.defSum}</div>
					        </div>
					        <div class="item">
					            <div class="label">
					                <span class="color defect"></span> 불량
					            </div>
					            <div class="value">${qcInfo.defSum}</div>
					        </div>
					    </div>
			    	</div>
		    	</div>
		    	
		    	<div class="defDetail">
		    		<div class="card-header">
			    		<strong>불량 상세 내역 및 조치 결과</strong>
			    	</div>
			    	<div class="table-contain">
			    		<table class="table defList">
				    		<thead>
				    			<tr>
				    				<th>불량 원인</th>
				    				<th>수량</th>
				    				<th>조치 내용</th>
				    				<th>폐기 여부</th>
				    			</tr>
				    		</thead>
				    		
				    		<tbody>
				                <c:forEach var="i" items="${defList}">
				                    <tr>
				                        <td>${i.dtName}</td>
				                        <td class="defCnt">${i.defCnt}</td>
				                        <td>${i.solution}</td>
				                        <td>${i.dispose}</td>
				                    </tr>
				                </c:forEach>
				
				                <c:if test="${ empty defList }">
				                    <tr>
				                        <td colspan="10" style="text-align:center;">내용 없음</td>
				                    </tr>
				                </c:if>
				            </tbody>
				    	</table>
			    	</div>
		    	</div>
		    	
		    </div>
		    
        </div>
    </div>
    
</body>
</html>