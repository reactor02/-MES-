<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 수정</title>
<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">
<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>
<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
<link rel="stylesheet" href="/mes/static/css/P07_work/main.css">
<link rel="stylesheet" href="/mes/static/css/P03_notice/notice.css">
<%-- Quill CDN --%>
<link href="https://cdn.quilljs.com/1.3.7/quill.snow.css" rel="stylesheet">
<script src="https://cdn.quilljs.com/1.3.7/quill.min.js"></script>
<script src="/mes/static/js/03_notice/notice.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>
<div class="layout_snb">
  <div class="snbContent"><%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %></div>
  <div class="content">
  <main class="nc">

  <c:set var="ctx" value="${pageContext.request.contextPath}" />

  <div id="page-notice-edit">
    <div class="page-header">
      <h1>공지사항 수정</h1>
      <p>공지사항을 수정합니다</p>
    </div>

    <%-- 읽기전용 정보 카드 --%>
    <%-- 수정 폼 --%>
    <div class="card">
      <div class="card-title">공지 작성</div>
      <form id="noticeEditForm"
            action="${ctx}/notice/update" method="post">
        <input type="hidden" name="boardno" value="${noticeDTO.boardno}">

        <div class="form-group">
          <label class="form-label" for="editNoticeTitle">제목</label>
          <input type="text" class="form-control" id="editNoticeTitle" name="title"
                 placeholder="공지 제목을 입력하세요" value="${noticeDTO.title}">
        </div>

        <div class="form-group">
          <label class="form-label">내용</label>
          <%-- Quill이 붙을 textarea --%>
          <textarea id="noticeEditContent" name="content" style="display:none;">${noticeDTO.content}</textarea>
        </div>

        <div class="form-actions">
          <button type="button" class="btn btn-outline"
            onclick="history.back()">취소</button>
          <button type="button" class="btn btn-primary" id="noticeEditSubmitBtn">수정</button>
        </div>
      </form>
    </div>
  </div>

  </main>
  </div>
</div>
</body>
</html>