<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.*" %>
<%@ page import="P09_equip.DTO.EqDTO" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>설비관리</title>

<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>

<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
    
<link rel="stylesheet" href="/mes/static/css/P09_equip/main.css">
<script src="/mes/static/js/09_equip/main.js"></script>

</head>
<body>

    <%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>
    
	<% 
		Map eqMap = (Map)request.getAttribute("eqMap");

		int size = (int)eqMap.get("size"); // 현재 페이지
		int totalPage = (int)eqMap.get("totalPage");
		
		int section = 5; // 한 번에 보여줄 페이지들의 수
		int pageNum = (int)eqMap.get("page"); // 현재 페이지
		
		int endSection = (int)Math.ceil((double)pageNum/section)*section;
		int startSection = endSection - section + 1;
		
		if (endSection > totalPage) {
			endSection = totalPage;
		}
	%>
	
	<%
		int eqRun = 0;
		int eqInsp = 0;
		int eqError = 0;
		int eqHold = 0;
		int total = 0;
		
		List<EqDTO> list = (List<EqDTO>)request.getAttribute("eqAllList");
		
		if (list != null) {
		    for (int i = 0; i < list.size(); i++) {
		        EqDTO dto = list.get(i);
		        String status = dto.getStatus();
		        
		        if("가동".equals(status)) {
		        	eqRun++;
		        	continue;
		        }
		        if("점검 중".equals(status)) {
		        	eqInsp++;
		        	continue;
		        }
		        if("고장".equals(status)) {
		        	eqError++;
		        	continue;
		        }
		        if("사용중단".equals(status)) {
		        	eqHold++;
		        	continue;
		        }
		    } // for
		}
		
		total = eqRun + eqInsp + eqError + eqHold;
	%>
    
    <div class="layout_snb">
        <div class="snbContent">
            <%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
        </div>
        <div class="content">
            <div class="pageName">
			    <div>
			        <h2>설비 관리</h2>
			        <p>설비 상태 점검 및 설비 관리</p>
			    </div>
			</div>
			
			<!-- 요약 카드 -->
		    <div class="summary">
		        <div class="card">
		        	<div class="cardTitle">
			            <h3>클린룸 현황</h3>
			            <div class="type">
			            	<div class="typeTag">
				            	<div class="color success"></div>
				            	<div class="typeName">정상</div>
			            	</div>
			            	<div class="typeTag">
				            	<div class="color warning"></div>
				            	<div class="typeName">경고</div>
			            	</div>
			            	<div class="typeTag">
				            	<div class="color danger"></div>
				            	<div class="typeName">위험</div>
			            	</div>
			            </div>
		        	</div>
		            <div class= cardClean>
			            <div class="cardSmall temp warning tooltip" data-msg="정상 온도 : 20℃ ~ 24℃">
				            <p>온도</p>
				            <strong>20.1</strong>
				            <p>(단위 : ℃)</p>
				        </div>
				        <div class="cardSmall humid tooltip" data-msg="정상 습도 : 40% RH ~ 60% RH">
				            <p>습도</p>
				            <strong>51.2</strong>
				            <p>(단위 : % RH)</p>
				        </div>
				        <div class="cardSmall press tooltip" data-msg="정상 차압 : +10Pa ~ +15Pa">
				            <p>차압</p>
				            <strong>+12</strong>
				            <p>(단위 : Pa)</p>
				        </div>
				        <div class="cardSmall clean tooltip" data-msg="정상 청정도 : ~35.2만/m²">
				            <p>청정도</p>
				            <strong>30.5</strong>
				            <p>(단위 : 만/m²)</p>
				        </div>
		            </div>
		        </div>
		        <div class="card">
		            <div class="cardTitle">
			            <h3>설비 상태 현황</h3>
			            <p>총 설비 수 : <%= total %>대</p>
		        	</div>
		            <div class="equipSummary">
		            	<table class="summaryTable">
		            		<tbody>
		            			<tr>
			            			<td><span class="status run">가동</span></td>
			            			<td><%= eqRun %>대</td>
		            			</tr>
		            			<tr>
			            			<td><span class="status insp">점검 중</span></td>
			            			<td><%= eqInsp %>대</td>
		            			</tr>
		            			<tr>
			            			<td><span class="status error">고장</span></td>
			            			<td><%= eqError %>대</td>
		            			</tr>
		            			<tr>
			            			<td><span class="status hold">사용중단</span></td>
			            			<td><%= eqHold %>대</td>
		            			</tr>
		            		</tbody>
		            	</table>
		            </div>
		        </div>
		    </div>

			<div class="list">
			    <!-- 상단 영역 -->
			    <div class="listTop">
			        <h3>설비 상태 및 가동률</h3>
					
					<form method="get" action="/mes/equipment">
						<input type="hidden" name="cmd" value="search">
						<div class="search-tools">
							<div class="category">
								<select name="status" >
									<option value="전체" ${empty param.status ? 'selected' : ''}>전체보기</option>
								    <option value="가동" ${param.status == '가동' ? 'selected' : ''}>가동</option>
								    <option value="점검 중" ${param.status == '점검 중' ? 'selected' : ''}>점검 중</option>
								    <option value="고장" ${param.status == '고장' ? 'selected' : ''}>고장</option>
								    <option value="사용중단" ${param.status == '사용중단' ? 'selected' : ''}>사용중단</option>
								</select>
					        </div>
					        <div class="search">
					            <input type="text" name="keyword" value="${param.keyword}" placeholder="설비코드 또는 설비명으로 검색">
					            <button type="submit" class="buttonMain">검색</button>
					            <button type="button" class="reset buttonSub">초기화</button>
					        </div>
						</div>					
					</form>
			    </div>
			
			    <!-- 테이블 -->
			    <table class="eq-table">
			        <thead>
			            <tr>
			                <th>설비코드</th>
			                <th>설비명</th>
			                <th>전체시간</th>
			                <th>가동시간</th>
			                <th>비가동시간</th>
			                <th>가동률</th>
			                <th>상태</th>
			            </tr>
			        </thead>
			        <tbody>
			        
			        	<c:forEach var="i" items="${ eqMap.list }">
			        		<tr class="eqDetail" onclick="location.href='/mes/equipment?cmd=detail&eqId=${i.eqId}'">
				                <td>${ i.eqId }</td>
				                <td>${ i.eqName }</td>
				                <td><fmt:formatNumber value="${i.totalMin / 60}" pattern="0" />시간 ${ i.totalMin%60 }분</td>
				                <td><fmt:formatNumber value="${i.runMin / 60}" pattern="0" />시간 ${ i.runMin%60 }분</td>
				                <td><fmt:formatNumber value="${i.stopMin / 60}" pattern="0" />시간 ${ i.stopMin%60 }분</td>
				                <td>${ i.runRate }%</td>
				                <td>
			                		<c:if test="${ i.status == '가동' }">
			                			<span class="status run">가동</span>
			                		</c:if>
			                		<c:if test="${ i.status == '고장' }">
			                			<span class="status error">고장</span>
			                		</c:if>
			                		<c:if test="${ i.status == '점검 중' }">
			                			<span class="status insp">점검 중</span>
			                		</c:if>
			                		<c:if test="${ i.status == '사용중단' }">
			                			<span class="status hold">사용중단</span>
			                		</c:if>
				                </td>
				            </tr>
			        	</c:forEach>
			        	
			        	<c:if var="i" test="${ empty eqMap.list }">
			        		<tr>
			        			<td colspan="7">내용 없음</td>
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
						<a href="./equipment?page=<%= startSection-1 %>&size=10">
							&lt;
						</a>
					</c:if>
					<c:forEach var="i" begin="<%= startSection %>" end="<%= endSection %>">
						<a href="./equipment?page=${ i }&size=10">
							<c:if test="${eqMap.page eq i}">
								<strong>
									${ i }
								</strong>
							</c:if>
							<c:if test="${!(eqMap.page eq i)}">
									${ i }
							</c:if>
						</a>
					</c:forEach>
					
					<c:if test="<%= endSection <= totalPage %>">
						&gt;
					</c:if>
					<c:if test="<%= !(endSection <= totalPage) %>">
						<a href="./equipment?page=<%= endSection+1 %>&size=10">
							&gt;
						</a>
					</c:if>
			    </div>
			</div>
        </div>
    </div>
    
</body>
</html>