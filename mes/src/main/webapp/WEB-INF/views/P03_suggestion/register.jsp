<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>건의사항 등록</title>
<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">
<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>
<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
<link rel="stylesheet" href="/mes/static/css/P03_suggestion/suggestion.css">

<%-- Quill CDN: suggestion.js보다 먼저 로드 --%>
<link href="https://cdn.quilljs.com/1.3.7/quill.snow.css" rel="stylesheet">
<script src="https://cdn.quilljs.com/1.3.7/quill.min.js"></script>
<script src="/mes/static/js/03_suggestion/suggestion.js"></script>
</head>
<body>

<%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>

<div class="layout_snb">
    <div class="snbContent">
        <%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %>
    </div>
    <div class="content">
    <main class="sg">

    <div id="page-suggest-register">
        <div class="page-header">
            <h1>건의사항 등록</h1>
            <p>새로운 건의사항을 등록합니다</p>
        </div>

        <div class="card">
            <div class="card-title">건의사항 작성</div>
            <form id="suggestRegisterForm"
                  action="${pageContext.request.contextPath}/suggestion/insert"
                  method="post"
                  enctype="multipart/form-data"
                  accept-charset="UTF-8">

                <div class="form-group">
                    <label class="form-label" for="suggestRegTitle">
                        제목 <span class="req">*</span>
                    </label>
                    <input type="text" class="form-control"
                           id="suggestRegTitle" name="title"
                           placeholder="건의사항 제목을 입력하세요" required>
                </div>

                <div class="form-group">
                    <label class="form-label">내용 <span class="req">*</span></label>
                    <%-- TinyMCE가 붙을 textarea, name=content으로 서버 전송 --%>
                    <textarea id="suggestRegContent" name="content"></textarea>
                </div>

                <div class="form-group">
                    <label class="form-label">첨부파일</label>
                    <input type="file" name="file1" class="form-control">
                </div>

                <div class="form-actions">
                    <button type="button" class="btn btn-outline"
                            onclick="location.href='${pageContext.request.contextPath}/suggestion/list?page=1'">
                        취소
                    </button>
                    <button type="button" class="btn btn-primary" id="suggestSubmitBtn">등록</button>
                </div>
            </form>
        </div>
    </div>

    </main>
    </div>
</div>

</body>
</html>