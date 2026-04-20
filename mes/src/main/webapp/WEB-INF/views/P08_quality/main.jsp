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

<title>품질관리</title>

<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>

<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
    
<link rel="stylesheet" href="/mes/static/css/P08_quality/main.css">
<script src="/mes/static/js/08_quality/main.js"></script>

</head>
<body>
	
	<%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>
	
	<% 
		Map qcMap = (Map)request.getAttribute("qcMap");

		int size = (int)qcMap.get("size"); // 현재 페이지
		int totalPage = (int)qcMap.get("totalPage");
		
		int section = 5; // 한 번에 보여줄 페이지들의 수
		int pageNum = (int)qcMap.get("page"); // 현재 페이지
		
		int endSection = (int)Math.ceil((double)pageNum/section)*section;
		int startSection = endSection - section + 1;
		
		if (endSection > totalPage) {
			endSection = totalPage;
		}
		
		System.out.println(size);
		System.out.println(totalPage);
		System.out.println(pageNum);
		System.out.println(endSection);
		System.out.println(startSection);
		
	%>
    
    <div class="layout_snb">
        <div class="snbContent">
            <%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
        </div>
        <div class="content">
        
        	<!-- 페이지 상단 -->
		    <div class="pageName">
		        <div>
		            <h2>품질 관리</h2>
		            <p>품질검사 내역</p>
		        </div>
		
		        <button class="addBtn buttonMain" <c:if test="${(empty dto.auth) || dto.auth < 2}">style="display: none;"</c:if> >
		            검사계획 등록
		        </button>
		    </div>
		
		    <!-- 요약 카드 -->
		    <div class="summary">
		        <div class="card">
		            <p>금일 검사 수량</p>
		            <strong>${cardDTO.total}</strong>
		        </div>
		        <div class="card">
		            <p>합격 수량</p>
		            <strong>${cardDTO.pass}</strong>
		        </div>
		        <div class="card">
		            <p>불량 수량</p>
		            <strong>${cardDTO.defect}</strong>
		        </div>
		        <div class="card">
		            <p>평균 불량률</p>
		            <strong>${cardDTO.avgDefect}%</strong>
		        </div>
		    </div>
		
		    <!-- 리스트 -->
		    <div class="list">
		
		        <div class="listTop">
		            <h3>검사 기록 보기</h3>
		
		            <form method="get" action="/mes/qclist">
		                <input type="hidden" name="cmd" value="search">
		
		                <div class="search-tools">
		                    <div class="category">
		                        <select name="status">
		                            <option value="0" ${param.status == '0' ? 'selected' : ''}>전체 보기</option>
		                            <option value="10" ${param.status == '10' ? 'selected' : ''}>검사 전</option>
		                            <option value="20" ${param.status == '20' ? 'selected' : ''}>검사 중</option>
		                            <option value="30" ${param.status == '30' ? 'selected' : ''}>검사 완료</option>
		                            <option value="50" ${param.status == '50' ? 'selected' : ''}>입고 완료</option>
		                            <option value="40" ${param.status == '40' ? 'selected' : ''}>보류</option>
		                        </select>
		                        
								<div>
									<input type="date" name="startDate" value="${param.startDate}" class="date"> ~
									<input type="date" name="endDate" value="${param.endDate}" class="date"">
								</div>
		                    </div>
		
		                    <div class="search-area">
		                        <input type="text" name="keyword" value="${param.keyword}" placeholder="제품명 또는 검사자 검색">
		                        <button type="submit" class="buttonMain">검색</button>
		                        <button type="button" class="reset buttonSub">초기화</button>
		                    </div>
		                </div>
		            </form>
		        </div>
		
		        <!-- 테이블 -->
		        <table class="qc-table">
		            <thead>
		                <tr>
		                    <th>검사코드</th>
		                    <th>작업코드</th>
		                    <th>제품명 (제품코드)</th>
		                    <th>검사수량</th>
		                    <th>불량률</th>
		                    <th>검사 완료일</th>
		                    <th>검사자</th>
		                    <th>상태</th>
		                </tr>
		            </thead>
		
		            <tbody>
		                <c:forEach var="i" items="${ qcMap.list }">
		                    <tr class="qcDetail" onclick="location.href='/mes/qclist?cmd=detail&qcId=${i.qcId}'">
		                        <td>${ i.qcId }</td>
		                        <td>${ i.woId }</td>
		                        <td>${ i.iName } (${ i.itemId })</td>
		                        <td>${ i.qty }</td>
		                        <td>
		                            <fmt:formatNumber value="${ (i.defSum / i.qty) * 100 }" maxFractionDigits="1"/>%
		                        </td>
		                        <td>${ empty i.eDate ? '-' : i.eDate }</td>
		                        <td>${ i.wName } (${ i.wId })</td>
		                        <td>
		                            <c:if test="${ i.qcStatus == 10 }">
		                                <span class="status before">검사 전</span>
		                            </c:if>
		                            <c:if test="${ i.qcStatus == 20 }">
		                                <span class="status ongoing">검사 중</span>
		                            </c:if>
		                            <c:if test="${ i.qcStatus == 30 }">
		                                <span class="status qcFin">검사 완료</span>
		                            </c:if>
		                            <c:if test="${ i.qcStatus == 50 }">
		                                <span class="status hold">입고 완료</span>
		                            </c:if>
		                            <c:if test="${ i.qcStatus == 40 }">
		                                <span class="status hold">보류</span>
		                            </c:if>
		                        </td>
		                    </tr>
		                </c:forEach>
		
		                <c:if test="${ empty qcMap.list }">
		                    <tr>
		                        <td colspan="8">내용 없음</td>
		                    </tr>
		                </c:if>
		            </tbody>
		        </table>
		
		        <!-- 페이징 -->
		        <div class="page">
			    	<c:if test="<%= startSection == 1 %>">
						&lt;
					</c:if>
					<c:if test="<%= startSection != 1 %>">
						<a href="./qclist?page=<%= startSection-1 %>&size=10">
							&lt;
						</a>
					</c:if>
					<c:forEach var="i" begin="<%= startSection %>" end="<%= endSection %>">
						<a href="./qclist?page=${ i }&size=10">
							<c:if test="${qcMap.page eq i}">
								<strong>
									${ i }
								</strong>
							</c:if>
							<c:if test="${!(qcMap.page eq i)}">
									${ i }
							</c:if>
						</a>
					</c:forEach>
					
					<c:if test="<%= endSection <= totalPage %>">
						&gt;
					</c:if>
					<c:if test="<%= !(endSection <= totalPage) %>">
						<a href="./qclist?page=<%= endSection+1 %>&size=10">
							&gt;
						</a>
					</c:if>
			    </div>
        
        </div>
    </div>
	
</body>
</html>