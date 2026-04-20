<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*, P06_prod.ProdDTO"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    ProdDTO planDto = (ProdDTO) request.getAttribute("planDto");
    int planQty   = planDto.getPlanQty();
    int prevQty   = planDto.getPrevQty();
    int remainQty = planQty - prevQty;
    int progress  = (planQty > 0) ? (int)(prevQty * 100.0 / planQty) : 0;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>생산계획 상세/수정</title>

<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">
<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>
<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
<link rel="stylesheet" href="/mes/static/css/P07_work/main.css">
<link rel="stylesheet" href="/mes/static/css/P06_prod/prod.css">
<link rel="stylesheet" href="/mes/static/css/P11_masterdata/process.css">

</head>
<body>

    <%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>

    <div class="layout_snb">
        <div class="snbContent">
            <%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
        </div>
        <div class="content">
            <main class="pp">

  <div id="page-detail">
    <div class="page-header-row">
      <div>
        <h1>생산계획 상세</h1>
        <p class="page-header-desc">생산계획 상세 정보를 확인합니다</p>
      </div>
      <div class="action-header">
        <button class="btn btn-outline btn-sm" onclick="location.href='list'">목록</button>
        <%-- auth 2이상만 수정 버튼 표시 --%>
        <c:if test="${dto.auth >= 2}">
          <button class="btn btn-success-outline btn-sm" onclick="openEditModal()">수정</button>
        </c:if>
      </div>
    </div>

    <!-- 기본 정보 -->
    <div class="card dtl-card-gap">
      <div class="section-title">기본 정보</div>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">계획ID</span>
          <span class="info-value em dtl-plan-id" id="dtlPlanId">${planDto.planId}</span>
        </div>
        <div class="info-item">
          <span class="info-label">제품명</span>
          <span class="info-value em" id="dtlProductNm">${planDto.itemName}</span>
        </div>
        <div class="info-item">
          <span class="info-label">담당자</span>
          <span class="info-value" id="dtlManager">${planDto.ename}</span>
        </div>
        <div class="info-item dtl-row2">
          <span class="info-label">시작일</span>
          <span class="info-value" id="dtlStartDate">${planDto.planSdate}</span>
        </div>
        <div class="info-item dtl-row2">
          <span class="info-label">종료일</span>
          <span class="info-value" id="dtlEndDate">${planDto.planEdate}</span>
        </div>
        <div class="info-item dtl-row2">
          <span class="info-label">상태</span>
          <c:choose>
            <c:when test="${planDto.status == 0}"><span class="badge badge-gray   dtl-badge" id="dtlStatus">대기</span></c:when>
            <c:when test="${planDto.status == 1}"><span class="badge badge-blue   dtl-badge" id="dtlStatus">진행중</span></c:when>
            <c:when test="${planDto.status == 2}"><span class="badge badge-green  dtl-badge" id="dtlStatus">완료</span></c:when>
            <c:when test="${planDto.status == 3}"><span class="badge badge-yellow dtl-badge" id="dtlStatus">보류</span></c:when>
            <c:when test="${planDto.status == 4}"><span class="badge badge-red    dtl-badge" id="dtlStatus">취소</span></c:when>
          </c:choose>
        </div>
      </div>
    </div>

    <!-- 진행 현황 -->
    <div class="card">
      <div class="section-title">진행 현황</div>
      <div class="metric-grid">
        <div class="metric-card">
          <span class="metric-label">목표수량</span>
          <span class="metric-value" id="dtlTargetQty"><%= planQty %></span>
          <span class="metric-unit">EA</span>
          <div class="metric-bar primary"></div>
        </div>
        <div class="metric-card">
          <span class="metric-label">생산수량</span>
          <span class="metric-value" id="dtlProdQty"><%= prevQty %></span>
          <span class="metric-unit">EA</span>
          <div class="metric-bar success"></div>
        </div>
        <div class="metric-card">
          <span class="metric-label">잔여수량</span>
          <span class="metric-value" id="dtlRemainQty"><%= remainQty %></span>
          <span class="metric-unit">EA</span>
          <div class="metric-bar warning"></div>
        </div>
      </div>

      <div class="detail-progress-wrap">
        <div class="detail-progress-header">
          <span class="detail-progress-label">진행률</span>
          <span class="detail-progress-pct" id="dtlProgressPct"><%= progress %>%</span>
        </div>
        <div class="detail-progress-bar">
          <%-- JS에서 data-width 읽어서 width 세팅 --%>
          <div class="detail-progress-fill" id="dtlProgressFill" data-width="<%= progress %>"></div>
        </div>
        <div class="detail-progress-markers">
          <span>0%</span>
          <span>100%</span>
        </div>
      </div>
    </div>

    <!-- 공정 흐름도 -->
    <div class="card dtl-card-gap" style="margin-top:16px;">
      <div class="section-title">공정 흐름도</div>
      <div class="flow-board">
        <c:choose>
          <c:when test="${empty stepList}">
            <div class="flow-empty">등록된 공정 단계가 없습니다.</div>
          </c:when>
          <c:otherwise>
            <div class="flow-top-line">
              <c:forEach var="step" items="${stepList}" varStatus="status">
                <div class="flow-step">
                  <span class="flow-badge">${step.seq}단계</span>
                  <strong>${step.stepName}</strong>
                </div>
                <c:if test="${not status.last}">
                  <div class="flow-arrow">→</div>
                </c:if>
              </c:forEach>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>


  <!-- 수정 모달 -->
  <div id="modalEdit" class="pp-modal-overlay dtl-hidden">
    <div class="pp-modal">
      <div class="pp-modal-header">
        <h2>생산계획 수정</h2>
        <button class="pp-modal-close" onclick="closeEditModal()">&#x2715;</button>
      </div>
      <div class="pp-modal-body">
        <form id="editForm" action="/mes/prod/update" method="post">
          <input type="hidden" name="planId" value="${planDto.planId}">
          <input type="hidden" id="editItemId" name="itemId" value="${planDto.itemId}">
          <div class="form-grid">

            <div class="form-group">
              <label class="form-label" for="editGroupView">대분류</label>
              <input type="text" class="form-control reg-readonly" id="editGroupView"
                     readonly tabindex="-1">
            </div>

            <div class="form-group">
              <label class="form-label" for="editSubItemView">소분류</label>
              <input type="text" class="form-control reg-readonly" id="editSubItemView"
                     readonly tabindex="-1" value="${planDto.itemName}">
            </div>

            <div class="form-group">
              <label class="form-label" for="editUnit">단위</label>
              <input type="text" class="form-control reg-readonly" id="editUnit"
                     readonly tabindex="-1">
            </div>

            <div class="form-group">
              <label class="form-label" for="editSpec">규격</label>
              <input type="text" class="form-control reg-readonly" id="editSpec"
                     readonly tabindex="-1">
            </div>

            <div class="form-group">
              <label class="form-label" for="editEmpName">담당자 <span class="req">*</span></label>
              <div class="emp-search-wrap">
                <input type="text" class="form-control" id="editEmpName"
                       value="${planDto.ename}" readonly tabindex="-1">
                <input type="hidden" id="editEmpId" name="empId" value="${planDto.empId}">
                <button type="button" class="emp-search-btn" onclick="openEmpPopup('edit')" title="담당자 검색">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <circle cx="7" cy="7" r="5" stroke="#64748b" stroke-width="1.6"/>
                    <path d="M11 11L14 14" stroke="#64748b" stroke-width="1.6" stroke-linecap="round"/>
                  </svg>
                </button>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label" for="editQty">목표수량 <span class="req">*</span></label>
              <input type="number" class="form-control" id="editQty" name="planQty"
                     placeholder="목표 수량 입력" min="1" required value="${planDto.planQty}">
            </div>

            <div class="form-group dtl-status-group">
              <label class="form-label" for="editStatus">상태 <span class="req">*</span></label>
              <select class="form-control" id="editStatus" name="status" required>
                <option value="0" <c:if test="${planDto.status == 0}">selected</c:if>>대기</option>
                <option value="1" <c:if test="${planDto.status == 1}">selected</c:if>>진행중</option>
                <option value="2" <c:if test="${planDto.status == 2}">selected</c:if>>완료</option>
                <option value="3" <c:if test="${planDto.status == 3}">selected</c:if>>보류</option>
                <option value="4" <c:if test="${planDto.status == 4}">selected</c:if>>취소</option>
              </select>
            </div>

            <div class="form-group form-group-wide">
              <label class="form-label">기간 <span class="req">*</span></label>
              <div class="date-range-wrap">
                <input type="date" class="form-control" id="editStartDate" name="planSdate"
                       required value="${planDto.planSdate}">
                <span class="date-range-sep">~</span>
                <input type="date" class="form-control" id="editEndDate" name="planEdate"
                       required value="${planDto.planEdate}">
              </div>
            </div>

          </div>
          <div class="form-actions">
            <button type="button" class="btn btn-outline" onclick="closeEditModal()">취소</button>
            <button type="submit" class="btn btn-primary btn-sm">수정</button>
          </div>
        </form>
      </div>
    </div>
  </div>


  <!-- 담당자 검색 팝업 -->
  <div id="empPopup" class="emp-popup-overlay dtl-hidden">
    <div class="emp-popup">
      <div class="emp-popup-header">
        <h3>담당자 검색</h3>
        <button class="emp-popup-close" onclick="closeEmpPopup()">&#x2715;</button>
      </div>
      <div class="emp-popup-search">
        <input type="text" class="search-input" id="empSearchKeyword" placeholder="이름 또는 사번 검색">
        <button class="btn btn-primary btn-sm" id="empSearchBtn">검색</button>
      </div>
      <div class="emp-popup-body">
        <table>
          <thead>
            <tr>
              <th>사번</th>
              <th>이름</th>
              <th>부서</th>
            </tr>
          </thead>
          <tbody id="empListBody"></tbody>
        </table>
      </div>
      <div class="emp-popup-footer">
        <div class="emp-paging" id="empPaging"></div>
      </div>
    </div>
  </div>

  <script>
    const itemDataMap = {};
    <c:forEach var="item" items="${itemList}">
    (function() {
      var gId = '${item.gId}';
      if (!itemDataMap[gId]) itemDataMap[gId] = [];
      itemDataMap[gId].push({
        itemId   : '${item.itemId}',
        itemName : '${item.itemName}',
        unit     : '${item.unit}',
        spec     : '${item.spec}'
      });
    })();
    </c:forEach>

    // 상세 페이지에서 수정 모달 열 때 현재 아이템 ID 전달용
    const DTL_ITEM_ID = '${planDto.itemId}';
  </script>
  <script src="/mes/static/js/06_prod/prod.js"></script>

</main>
        </div>
    </div>

</body>
</html>