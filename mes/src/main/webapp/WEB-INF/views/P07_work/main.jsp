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

<title>작업관리</title>

<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>

<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
    
<link rel="stylesheet" href="/mes/static/css/P07_work/main.css">
<script src="/mes/static/js/07_work/main.js"></script>

</head>
<body>

	<%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>
	
	<% 
		Map woMap = (Map)request.getAttribute("woMap");

		int size = (int)woMap.get("size"); // 현재 페이지
		int totalPage = (int)woMap.get("totalPage");
		
		int section = 5; // 한 번에 보여줄 페이지들의 수
		int pageNum = (int)woMap.get("page"); // 현재 페이지
		
		int endSection = (int)Math.ceil((double)pageNum/section)*section;
		int startSection = endSection - section + 1;
		
		if (endSection > totalPage) {
			endSection = totalPage;
		}
	%>

	<div class="layout_snb">
		<div class="snbContent">
			<%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
		</div>
		<div class="content">
			<div class="pageName">
			    <div>
			        <h2>작업 관리</h2>
			        <p>작업 지시서 관리 및 작업 상태 변경</p>
			    </div>
    			<button class="addBtn buttonMain" <c:if test="${(empty dto.auth) || dto.auth < 2}">style="display: none;"</c:if> >
    				작업지시 등록
    			</button>
			</div>

			<div class="list">
			    <!-- 상단 영역 -->
			    <div class="listTop">
			        <h3>작업 지시서 목록</h3>
					
					<form method="get" action="/mes/worklist">
						<input type="hidden" name="cmd" value="search">
						<div class="search-tools">
							<div class="category">
								<select name="status" >
									<option value="0" ${param.status == '0' ? 'selected' : ''}>전체보기</option>
								    <option value="10" ${param.status == '10' ? 'selected' : ''}>작업 전</option>
								    <option value="20" ${param.status == '20' ? 'selected' : ''}>작업 중</option>
								    <option value="30" ${param.status == '30' ? 'selected' : ''}>작업 완료</option>
								    <option value="35" ${param.status == '35' ? 'selected' : ''}>검사 대기</option>
								    <option value="40" ${param.status == '40' ? 'selected' : ''}>검사 완료</option>
								    <option value="60" ${param.status == '60' ? 'selected' : ''}>입고 완료</option>
								    <option value="50" ${param.status == '50' ? 'selected' : ''}>보류</option>
								</select>
								
								<div>
									<input type="date" name="startDate" value="${param.startDate}" class="date"> ~
									<input type="date" name="endDate" value="${param.endDate}" class="date" min="${param.startDate}">
								</div>
							</div>
							
					        <div class="search-area">
					            <input type="text" name="keyword" value="${param.keyword}" placeholder="제품명 또는 작업자로 검색">
					            <button type="submit" class="buttonMain">검색</button>
					            <button type="button" class="reset buttonSub">초기화</button>
					        </div>
						</div>					
					</form>
			    </div>
			
			    <!-- 테이블 -->
			    <table class="work-table">
			        <thead>
			            <tr>
			                <th>작업코드</th>
			                <th>제품명 (제품코드)</th>
			                <th>작업자</th>
			                <th>작업일</th>
			                <th>작업 수량</th>
			                <th>상태</th>
			            </tr>
			        </thead>
			        <tbody>
			        
			        	<c:forEach var="i" items="${ woMap.list }">
			        		<tr class="woDetail" onclick="location.href='/mes/worklist?cmd=detail&woId=${i.woId}'">
				                <td>${ i.woId }</td>
				                <td>${ i.itemName } (${ i.itemId })</td>
				                <td>${ i.wName } (${ i.worker })</td>
				                <td>${ i.workDate }</td>
				                <td>${ i.woQty }</td>
				                <td>
			                		<c:if test="${ i.woStatus == 10 }">
			                			<span class="status before">작업 전</span>
			                		</c:if>
			                		<c:if test="${ i.woStatus == 20 }">
			                			<span class="status ongoing">작업 중</span>
			                		</c:if>
			                		<c:if test="${ i.woStatus == 30 }">
			                			<span class="status finish">작업 완료</span>
			                		</c:if>
			                		<c:if test="${ i.woStatus == 35 }">
			                			<span class="status qcFin">검사 대기</span>
			                		</c:if>
			                		<c:if test="${ i.woStatus == 40 }">
			                			<span class="status qcFin">검사 완료</span>
			                		</c:if>
			                		<c:if test="${ i.woStatus == 60 }">
			                			<span class="status hold">입고 완료</span>
			                		</c:if>
			                		<c:if test="${ i.woStatus == 50 }">
			                			<span class="status hold">보류</span>
			                		</c:if>
				                </td>
				            </tr>
			        	</c:forEach>
			        	
			        	<c:if var="i" test="${ empty woMap.list }">
			        		<tr>
			        			<td colspan="6">내용 없음</td>
			        		</tr>
			        	</c:if>
			            <!-- 반복 -->
			        </tbody>
			    </table>
			    
			    <div class="page">
			    	<c:if test="<%= startSection == 1 %>">
						&lt;
					</c:if>
					<c:if test="<%= startSection != 1 %>">
						<a href="./worklist?page=<%= startSection-1 %>&size=10">
							&lt;
						</a>
					</c:if>
					<c:forEach var="i" begin="<%= startSection %>" end="<%= endSection %>">
						<a href="./worklist?page=${ i }&size=10">
							<c:if test="${woMap.page eq i}">
								<strong>
									${ i }
								</strong>
							</c:if>
							<c:if test="${!(woMap.page eq i)}">
									${ i }
							</c:if>
						</a>
					</c:forEach>
					
					<c:if test="<%= endSection <= totalPage %>">
						&gt;
					</c:if>
					<c:if test="<%= !(endSection <= totalPage) %>">
						<a href="./worklist?page=<%= endSection+1 %>&size=10">
							&gt;
						</a>
					</c:if>
			    </div>
			</div>
		</div>
	</div>
	
</body>
</html>