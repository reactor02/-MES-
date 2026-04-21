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

<link rel="stylesheet" href="/mes/static/css/P08_quality/resultModify.css">
<script src="/mes/static/js/08_quality/resultModify.js"></script>

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
		            <h2>검사결과 등록 및 수정</h2>
		            <p class="sub-text">품질검사 결과를 등록 혹은 수정해주세요.</p>
		        </div>
		
		        <div class="button-group">
			        <a href="/mes/qcdetail?qcId=${qcInfo.qcId}">
			            <button type="button" class="buttonWhite">취소</button>
			        </a>
			        <button type="button" class="buttonMain"  onclick="validateAndSubmitResult()">수정 완료</button>
		        </div>
		    </div>
		
		    <!-- 폼 -->
		    <form method="post" action="/mes/qcresultmodify" id="resultModify">
				<input type="hidden" name="cmd" value="modify">
		        <div class="card">
		        	<div class="card-header">
		        		<input type="hidden" name="qcId" value="${qcInfo.qcId}">
						<strong>검사 정보 및 결과</strong> <span class="qcId">(${qcInfo.qcId})</span>
			            <div class="form-row right-align">
			                <label>
			                    <input type="radio" name="status" value="10" <c:if test="${qcInfo.qcStatus == 10}">checked</c:if>> 검사 전
			                </label>
			                <label>
			                    <input type="radio" name="status" value="20" <c:if test="${qcInfo.qcStatus == 20}">checked</c:if>> 검사 중
			                </label>
			                <label>
			                    <input type="radio" name="status" value="30" <c:if test="${qcInfo.qcStatus == 30}">checked</c:if>> 검사 완료
			                </label>
			                <label>
			                    <input type="radio" name="status" value="50" <c:if test="${qcInfo.qcStatus == 50}">checked</c:if>> 보류
			                </label>
			            </div>
			            
					</div>
		
		            <!-- 작업 -->
		            <div class="form-row">
		                <div class="form-group">
		                    <label>검사 예정인 작업</label>
		                    <input type="text" value="${qcInfo.woId}" placeholder="작업 코드" readonly>
		                </div>
		
		                <div class="form-group">
		                    <label>검사자</label>
		                    <input type="hidden" name="wId" value="${qcInfo.wId}">
		                    <input type="text" value="${qcInfo.wName} (${qcInfo.wId})" placeholder="작업자명 (작업자 사번)" readonly>
		                </div>
		            </div>
		
		            <!-- 완제품 / 작업일 -->
		            <div class="form-row formDate">
		                <div class="form-group">
		                    <label>제품</label>
		                    <input type="hidden" value="${qcInfo.itemId}">
		                    <input type="text" value="${qcInfo.iName} (${qcInfo.itemId})" placeholder="제품명 (제품 코드)" readonly>
		                </div>
		
		                <div class="form-group">
		                    <label>검사 시작일</label>
		                    <input type="date" value="${qcInfo.sDate}" placeholder="검사 시작일" readonly>
		                </div>
		
		                <div class="form-group">
		                    <label>검사 완료일</label>
		                    <input type="date" name="eDate" value="${qcInfo.eDate}" placeholder="검사 완료일" min="${qcInfo.sDate}">
		                </div>
		            </div>
		
		            <div class="form-row formQty">
		                <div class="form-group">
		                    <label>검사 수량</label>
		                    <input type="number" id="totalQty" value="${qcInfo.qty}" placeholder="검사 수량" readonly>
		                </div>
		
		                <div class="form-group">
		                    <label>합격 수량</label>
		                    <input type="number" id="passQty" value="${qcInfo.qty - qcInfo.defSum}" placeholder="합격 수량" readonly>
		                </div>

		                <div class="form-group">
		                    <label>총 불량 수량</label>
		                    <input type="number" id="defSum" value="${qcInfo.defSum}" placeholder="총 불량 수량" readonly>
		                </div>

		                <div class="form-group">
		                    <label>최종 입고 수량</label>
		                    <input type="number" name="inQty" id="inQty" value="${qcInfo.qty - dispose.dispose}" placeholder="최종 입고 수량" readonly>
		                </div>
		
		                <div class="form-group">
		                    <label>폐기 수량</label>
		                    <input type="number" id="disposeQty" value="${dispose.dispose}" placeholder="폐기 수량" readonly>
		                </div>

		                <div class="form-group">
		                    <label>재사용 수량</label>
		                    <input type="number" id="reworkQty" value="${dispose.rework}" placeholder="재사용 수량" readonly>
		                </div>
		            </div>
		            
		            <!-- 불량 내역 -->
		            <div class="form-row">
		                <div class="form-group">
		                
		                    <div class="defectTitle">
		                    	<label>불량 상세 내역</label>
			                    <button type="button" id="defectAdd" class="buttonSub" onclick="openModal()">추가하기</button>
		                    </div>
		                    
		                    <div class="table-contain">
			                    <table class="defList">
			                    	<thead>
			                    		<th>불량 수량</th>
			                    		<th>불량 유형</th>
			                    		<th>조치 내용</th>
			                    		<th>폐기 여부</th>
			                    	</thead>
			                    	
			                    	<tbody>
						                <c:forEach var="i" items="${defList}">
						                    <tr data-id="${i.defId }">
						                        <td>${i.defCnt}</td>
						                        <td>${i.dtName}</td>
						                        <td class="defListSol">${i.solution}</td>
						                        <td>
						                        	<c:if test="${not empty i.dispose}">
						                        		Y
						                        	</c:if>
						                        </td>
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
		    </form>
		    
	    	<!-- 모달 -->
			<div id="defectModal" class="modal-overlay" style="display:none;">
			
			    <div class="modal-box">
			        <h2 class="modal-title">불량 내역 관리</h2>
			
			        <form method="post" action="/mes/qcresultmodify" id="defect">
			            <input type="hidden" name="cmd" id="cmd" value="defectAdd">
			            <input type="hidden" name="qcId" value="${qcInfo.qcId}">
			
			            <div class="modal-row">
			                <div class="modal-group">
			                    <label>불량 수량</label>
			                    <input type="number" name="defQty" id="defQty" placeholder="불량 수량 입력" min="1" max="${qcInfo.qty}" oninput="this.value = clampNumber(this)" onkeydown="return event.key !== '-'">
			                </div>
			
			                <div class="modal-group">
			                    <label>불량 원인</label>
			                    <select name="defType" id="defType">
								    <option value="" selected disabled>불량 원인 선택</option>
								    <option value="10">사이즈 불량</option>
								    <option value="20">용액 농도 불량</option>
								    <option value="30">합침 용액량 불량</option>
								    <option value="40">포장 불량</option>
								    <option value="50">이물질</option>
								    <option value="0">기타</option>
								</select>
			                </div>
			            </div>
			
			            <div class="modal-group full">
			            	<div class="solution">
				                <label>조치 사항</label>
				                <div class="disposeChk">
					                <input type="checkbox" id="dispose" name="dispose"> 폐기 여부
				                </div>
			            	</div>
			                <textarea name="solution" placeholder="조치 사항 입력" id="solution"></textarea>
			            </div>
			
			            <!-- 버튼 -->
			            <div class="modal-footer">
			                <button type="button" class="buttonWhite" onclick="closeModal()">취소</button>
			                <button type="button" id="addBtn" class="buttonMain" onclick="validateAndSubmitDefectAdd()">등록</button>
			                <button type="button" id="defectModifyBtn" class="buttonMain" onclick="updateDefect()" style="display:none;">수정</button>
			                <button type="button" id="defectDeleteBtn" class="buttonRed" onclick="deleteDefect()" style="display:none;">삭제</button>
			            </div>
			
			        </form>
			    </div>
			</div>
			
			
        </div>
    </div>

</body>
</html>