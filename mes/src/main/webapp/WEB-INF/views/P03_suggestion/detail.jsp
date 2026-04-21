<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>건의사항 상세</title>
<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">
<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>
<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
<link rel="stylesheet" href="/mes/static/css/P03_suggestion/suggestion.css">
<script src="/mes/static/js/03_suggestion/suggestion.js"></script>
</head>
<body>

<%@ include file="/WEB-INF/views/P00_layout/header.jsp" %>

<%--
    Quill이 저장한 HTML 태그를 제거해서 순수 텍스트로 바꾸는 처리.
    EL 문자열 리터럴은 \n 이스케이프를 해석하지 못하므로,
    <c:set>의 body에 실제 개행문자(LF)를 넣어 줄바꿈 치환값으로 사용한다.
--%>
<c:set var="LF">
</c:set>

<c:set var="contentClean" value="${detail.content}"/>
<c:set var="contentClean" value="${fn:replace(contentClean, '<br>',    LF)}"/>
<c:set var="contentClean" value="${fn:replace(contentClean, '<br/>',   LF)}"/>
<c:set var="contentClean" value="${fn:replace(contentClean, '<br />',  LF)}"/>
<c:set var="contentClean" value="${fn:replace(contentClean, '</p>',    LF)}"/>
<c:set var="contentClean" value="${fn:replace(contentClean, '<p>',     '')}"/>
<c:set var="contentClean" value="${fn:replace(contentClean, '</div>',  LF)}"/>
<c:set var="contentClean" value="${fn:replace(contentClean, '<div>',   '')}"/>
<c:set var="contentClean" value="${fn:replace(contentClean, '&nbsp;',  ' ')}"/>

<div class="layout_snb">
    <div class="snbContent"><%@ include file="/WEB-INF/views/P00_layout/snb.jsp" %></div>
    <div class="content">
    <main class="sg sg-wide">

    <div id="page-suggest-detail">
        <div class="page-header-row">
            <div>
                <h1>건의사항 상세</h1>
                <p>건의사항 내용을 확인합니다</p>
            </div>
            <div class="action-header">
                <button class="btn btn-outline btn-sm"
                        onclick="location.href='${pageContext.request.contextPath}/suggestion/list?page=${page}&size=${size}'">
                    목록
                </button>
                <%-- 작성자 본인 + complete != 1 일 때 삭제 버튼 표시 --%>
                <c:if test="${isOwner && detail.complete != 1}">
                    <button class="btn btn-danger btn-sm" id="btnDelete"
                            data-boardno="${detail.boardno}"
                            data-ctimems="${ctimeMs}"
                            data-action="${pageContext.request.contextPath}/suggestion/delete">
                        삭제
                    </button>
                </c:if>
                <%-- 작성자 본인이 아니면서 auth 2 이상 + complete != 1 일 때 검토완료 버튼 표시 --%>
                <c:if test="${!isOwner && dto.auth >= 2 && detail.complete != 1}">
                    <button class="btn btn-success btn-sm" id="btnComplete"
                            data-boardno="${detail.boardno}"
                            data-complete="${detail.complete}"
                            data-action="${pageContext.request.contextPath}/suggestion/detail">
                        검토완료
                    </button>
                </c:if>
            </div>
        </div>

        <div class="card">
            <div class="card-title">건의사항 정보</div>

            <div class="form-row">
                <div class="form-group form-group-no-mb">
                    <label class="form-label">번호</label>
                    <input type="text" class="form-control"
                           value="${fn:substring(detail.boardno, 5, fn:length(detail.boardno))}" readonly>
                </div>
                <div class="form-group form-group-no-mb">
                    <label class="form-label">작성일</label>
                    <input type="text" class="form-control"
                           value="${detail.ctimeStr}" readonly>
                </div>
                <div class="form-group form-group-no-mb">
                    <label class="form-label">최종수정일</label>
                    <input type="text" class="form-control"
                           value="${detail.mtimeStr}" readonly>
                </div>
            </div>

            <div class="form-row form-row-mt">
                <div class="form-group form-group-no-mb">
                    <label class="form-label">작성자</label>
                    <input type="text" class="form-control" value="${detail.ename}" readonly>
                </div>
                <div class="form-group form-group-no-mb">
                    <label class="form-label">상태</label>
                    <div class="badge-cell">
                        <c:choose>
                            <c:when test="${detail.complete == 0}">
                                <span class="badge badge-blue">검토중</span>
                            </c:when>
                            <c:when test="${detail.complete == 2}">
                                <span class="badge badge-amber">답변달림</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-gray">검토완료</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="form-group form-group-mt">
                <label class="form-label">제목</label>
                <input type="text" class="form-control" value="${detail.title}" readonly>
            </div>

            <div class="form-group">
                <label class="form-label">내용</label>
                <textarea class="form-control" rows="6" readonly>${contentClean}</textarea>
            </div>

            <%-- ===== 첨부파일 다운로드 영역 (등록 페이지의 첨부파일 위치와 동일) ===== --%>
            <div class="form-group">
                <label class="form-label">첨부파일</label>
                <c:choose>
                    <c:when test="${not empty detail.saveName}">
                        <div class="file-download-wrap">
                            <a class="btn btn-outline btn-sm"
                               href="${pageContext.request.contextPath}/suggestion/download?save=${detail.saveName}&origin=${detail.originName}">
                                <svg width="14" height="14" viewBox="0 0 14 14" fill="none" style="vertical-align:middle;margin-right:4px;">
                                    <path d="M7 1v8M3.5 5.5L7 9l3.5-3.5M2 12h10" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                                </svg>
                                ${detail.originName}
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <input type="text" class="form-control" value="첨부된 파일이 없습니다." readonly>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- ===== 답변 (무한 대댓글) ===== -->
        <div class="card">
            <div class="comment-section">
                <div class="comment-section-title">
                    답변 <span class="comment-count">${fn:length(commentList)}건</span>
                </div>

                <div id="commentList">
                    <c:forEach var="c" items="${commentList}">
                        <div class="comment-item"
                             data-comno="${c.comno}"
                             data-depth="${c.depth}"
                             style="padding-left: ${c.depth * 30}px;">
                            <div class="comment-meta">
                                <c:if test="${c.depth > 0}">
                                    <span class="reply-icon">↳</span>
                                </c:if>
                                <span class="comment-writer">${c.writer}</span>
                                <span class="comment-content-text">${c.content}</span>
                                <span class="comment-time">
                                    ${c.ctimeStr}
                                </span>
                                <%-- complete != 1 이고 작성자 본인 OR auth >= 2만 답글 버튼 표시 --%>
                                <c:if test="${canComment && detail.complete != 1}">
                                    <button class="btn-reply-small"
                                            data-comno="${c.comno}"
                                            data-depth="${c.depth}">
                                        답글
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <%-- complete != 1 이고 작성자 본인 OR auth >= 2만 댓글 입력 폼 표시 --%>
                <c:if test="${canComment && detail.complete != 1}">
                    <form id="commentForm" method="post"
                          action="${pageContext.request.contextPath}/suggestion/comment">
                        <input type="hidden" name="boardno"     value="${detail.boardno}">
                        <input type="hidden" name="parentComno" id="parentComno" value="">
                        <div class="comment-input-row">
                            <input type="text" class="comment-input"
                                   name="commentContent"
                                   placeholder="답글을 입력하세요"
                                   id="commentInput">
                            <button type="submit" class="btn btn-primary comment-submit-btn">등록</button>
                        </div>
                    </form>
                </c:if>

            </div>
        </div>
    </div>

    </main>
    </div>
</div>

</body>
</html>