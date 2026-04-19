<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page import="P01_auth.LoginDTO"%>
<%@page import="java.util.*"%>


<!DOCTYPE html>
<html lang="kr">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Document</title>
<link rel="stylesheet" href="/mes/static/css/P00_common/common.css">

<link rel="stylesheet" href="/mes/static/css/P00_layout/header.css">
<script src="/mes/static/js/00_layout/header.js"></script>

<link rel="stylesheet" href="/mes/static/css/P00_layout/snb.css">
<script src="/mes/static/js/00_layout/snb.js"></script>
<link rel="stylesheet" href="/mes/static/css/P01_auth/mypage.css">
</head>

<body>


	<%@ include file="/WEB-INF/views/P00_layout/header.jsp"%>

	<div class="layout_snb">
		<div class="snbContent">
			<%@ include file="/WEB-INF/views/P00_layout/snb.jsp"%>
		</div>
		<div class="content">

			<div class="snb-bro">
				<div class="title-box">
					<h1>마이페이지</h1>
					<h7>제조 실행 시스템</h7>
				</div>
				<div class="board-box">
					<div class="box-type4 radius">
						<div class="mp-title">
							<h3>사용자 정보</h3>
							<span>기본 계정 및 계정 설정</span>
							<c:if test="${ not empty error }">
								<span style="color: red;"> ${ error } </span>
							</c:if>
						</div>
						<form method="post" action="/mes/mypage">
							<input type="hidden" name="mp_empid" value="${dto.empid}">
							<div class="mp-tool">
								<div class="mp-chain">
									<span>이름</span> <input type="text" name="mp_name"
										class="input-3 radius" value="${ dto.ename }">
								</div>
								<div class="mp-chain">
									<span>연락처</span> <input type="text" name="mp_phone"
										class="input-3 radius"
										value="<fmt:formatNumber value='${dto.phone}' pattern='00000000000' groupingUsed='false'/>">
								</div>
								<div class="mp-chain">
									<span>부서</span>
									<div class="input-3 radius gray">부서명 : ${ dto.deptname }
										&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 부서번호: ${ dto.deptno }</div>
								</div>
								<div class="mp-chain">
									<span>비밀번호 변경</span> <input type="text" name="mp_pw"
										class="input-3 radius" placeholder="사용할 새 비밀번호를 입력해주세요.">
								</div>
								<div class="mp-chain">
									<span>비밀번호 확인</span> <input type="text" name="mp_pw2"
										class="input-3 radius" placeholder="새 비밀번호 확인">
								</div>
								<div class="mp-chain">
									<span>입사일</span>
									<div class="input-3 radius gray">${ dto.hiredate }</div>
								</div>
								<div class="mp-chain">
									<span> </span>
									<button type="submit" name="mp_btn" class="buttonMain">정보
										수정</button>
								</div>
							</div>
						</form>
					</div>
					<div class="box-type4 radius">
						<div class="title-box">
							<h3>내 작업</h3>
							<span>내 작업 지시서 보여주기</span>
						</div>
						
							<div class="box-type3 radius">
								<h3>내 작업 지시서</h3>
								<div class="short-box">
								    <c:if test="${ empty mywork }">
								    <div>최근 한 작업이 없습니다!</div>
								    </c:if>
									<c:forEach var="l" items="${ mywork }">
										<div>
											<a href="/mes/workorder?woId=${ l.woid }">${ l.woid } : ${ l.planid } : ${ l.workdate } : ${ l.content } : ${ l.prev_qty } : ${ l.wo_qty }</a>
											<div class='buttonMain small'>${ l.wostatusname }
											</div>
										</div>
									</c:forEach>




								</div>
								<div class="next">
									<c:forEach var="m" begin="1" end="${ page_no }">
									  <form method="get" action="mypage">
										<button name="mywork_btn" value="${ m }" class="buttonMain">${ m }</button>
									  </form>
									</c:forEach>
								</div>
							</div>
					
					
					</div>
				</div>
			</div>


		</div>
	</div>





</body>

</html>