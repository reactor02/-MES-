window.addEventListener("load", function () {
    bind();
    initDetailPage();
});

function bind() {

    /* ── size 변경 (목록 페이지에만 존재) ── */
    var sizeSelect = document.getElementById("sizeSelect");
    if (sizeSelect) {
        sizeSelect.addEventListener("change", function () {
            location.href = buildListUrl(1, this.value);
        });
    }

    /* ── 검색 버튼 / 검색 인풋 엔터 ── */
    var searchBtn = document.getElementById("searchBtn");
    if (searchBtn) {
        searchBtn.addEventListener("click", doSearch);
    }
    var searchKeyword = document.getElementById("searchKeyword");
    if (searchKeyword) {
        searchKeyword.addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                e.preventDefault();
                doSearch();
            }
        });
    }

    /* ── 초기화 버튼 ── */
    var resetBtn = document.getElementById("resetBtn");
    if (resetBtn) {
        resetBtn.addEventListener("click", function () {
            location.href = "/mes/prod/list";
        });
    }

    /* ── 전체 체크박스 (목록 페이지에만 존재) ── */
    var chkAll = document.getElementById("chkAll");
    if (chkAll) {
        chkAll.addEventListener("change", function () {
            document.querySelectorAll("input[name='chk']")
                    .forEach(function (chk) { chk.checked = chkAll.checked; });
        });
        /* 개별 체크박스 변경 시 chkAll 상태 갱신 */
        document.addEventListener("change", function (e) {
            if (e.target && e.target.name === "chk") {
                var all     = document.querySelectorAll("input[name='chk']");
                var checked = document.querySelectorAll("input[name='chk']:checked");
                chkAll.checked = all.length > 0 && all.length === checked.length;
            }
        });
    }

    /* ── 오버레이 클릭 닫기 ── */
    document.querySelectorAll(".pp-modal-overlay").forEach(function (overlay) {
        overlay.addEventListener("click", function (e) {
            if (e.target === this) this.style.display = "none";
        });
    });

    /* ── 대분류 변경 → 소분류 갱신 ── */
    var regGroup = document.getElementById("regGroup");
    if (regGroup) {
        regGroup.addEventListener("change", function () {
            updateSubItemOptions(this, document.getElementById("regSubItem"));
        });
    }

    /* ── 소분류 변경 → unit / spec 자동 세팅 ── */
    var regSubItem = document.getElementById("regSubItem");
    if (regSubItem) {
        regSubItem.addEventListener("change", function () {
            var selected = this.options[this.selectedIndex];
            document.getElementById("regUnit").value = selected.dataset.unit || "";
            document.getElementById("regSpec").value = selected.dataset.spec || "";
        });
    }

    /* ── 날짜 유효성: 시작일 > 종료일 방지 ── */
    bindDateValidation("regStartDate", "regEndDate");
    bindDateValidation("editStartDate", "editEndDate");

    /* ── 수정 모달 대분류 변경 → 소분류 갱신 ── */
    var editGroup = document.getElementById("editGroup");
    if (editGroup) {
        editGroup.addEventListener("change", function () {
            updateSubItemOptions(this, document.getElementById("editSubItem"));
        });
    }

    /* ── 담당자 검색 버튼 / 엔터 ── */
    var empSearchBtn = document.getElementById("empSearchBtn");
    if (empSearchBtn) {
        empSearchBtn.addEventListener("click", function () {
            fetchEmpList(document.getElementById("empSearchKeyword").value.trim(), 1);
        });
    }
    var empSearchKeyword = document.getElementById("empSearchKeyword");
    if (empSearchKeyword) {
        empSearchKeyword.addEventListener("keydown", function (e) {
            if (e.key === "Enter") fetchEmpList(this.value.trim(), 1);
        });
    }

    /* ── 팝업 오버레이 클릭 닫기 ── */
    var empPopup = document.getElementById("empPopup");
    if (empPopup) {
        empPopup.addEventListener("click", function (e) {
            if (e.target === this) closeEmpPopup();
        });
    }

} // end bind()


/* ==========================================================
   목록 URL 조립 (절대경로 /mes/prod/list + 검색 조건 유지)
   ========================================================== */
function buildListUrl(page, size) {
    var startDate = document.getElementById("startDate");
    var endDate   = document.getElementById("endDate");
    var keyword   = document.getElementById("searchKeyword");

    var url = "/mes/prod/list?page=" + page + "&size=" + size;
    if (keyword && keyword.value.trim()) {
        url += "&keyword=" + encodeURIComponent(keyword.value.trim());
    }
    if (startDate && startDate.value) {
        url += "&startDate=" + encodeURIComponent(startDate.value);
    }
    if (endDate && endDate.value) {
        url += "&endDate=" + encodeURIComponent(endDate.value);
    }
    return url;
}

function doSearch() {
    var sizeSelect = document.getElementById("sizeSelect");
    var size = sizeSelect ? sizeSelect.value : 10;
    location.href = buildListUrl(1, size);
}


/* ==========================================================
   날짜 유효성
   - 시작일 변경 시 → 종료일 min 세팅
   - 종료일 변경 시 → 시작일 max 세팅
   - change + input 이벤트 모두 리스닝 (브라우저별 타이밍 차이 방어)
   - 종료일 포커스/클릭 순간에도 시작일 기준으로 min 재계산
   ========================================================== */
function bindDateValidation(startId, endId) {
    var startEl = document.getElementById(startId);
    var endEl   = document.getElementById(endId);
    if (!startEl || !endEl) return;

    function syncMinFromStart() {
        if (startEl.value) {
            endEl.min = startEl.value;
            if (endEl.value && endEl.value < startEl.value) {
                endEl.value = startEl.value;
            }
        } else {
            endEl.removeAttribute("min");
        }
    }

    function syncMaxFromEnd() {
        if (endEl.value) {
            startEl.max = endEl.value;
            if (startEl.value && startEl.value > endEl.value) {
                startEl.value = endEl.value;
            }
        } else {
            startEl.removeAttribute("max");
        }
    }

    // 시작일 변경 → 종료일 min 세팅
    startEl.addEventListener("change", syncMinFromStart);
    startEl.addEventListener("input",  syncMinFromStart);

    // 종료일 변경 → 시작일 max 세팅
    endEl.addEventListener("change", syncMaxFromEnd);
    endEl.addEventListener("input",  syncMaxFromEnd);

    // 종료일 포커스/클릭 시 시작일 기준 min을 즉시 최신화
    // (change 이벤트가 아직 안 떨어진 상황에서도 달력이 열리기 전에 min이 반영되도록)
    endEl.addEventListener("focus", syncMinFromStart);
    endEl.addEventListener("click", syncMinFromStart);
    startEl.addEventListener("focus", syncMaxFromEnd);
    startEl.addEventListener("click", syncMaxFromEnd);
}


/* ==========================================================
   등록 모달
   ========================================================== */
function openRegisterModal() {
    resetRegisterForm();
    document.getElementById("modalRegister").style.display = "flex";
}
function closeRegisterModal() {
    resetRegisterForm();
    document.getElementById("modalRegister").style.display = "none";
}
function resetRegisterForm() {
    document.getElementById("registerForm").reset();
    document.getElementById("regSubItem").innerHTML = '<option value="">소분류 선택</option>';
    document.getElementById("regUnit").value    = "";
    document.getElementById("regSpec").value    = "";
    document.getElementById("regEmpId").value   = "";
    document.getElementById("regEmpName").value = "";

    // 이전에 세팅된 min/max 속성 제거 (모달 재오픈 시 stale 값 방지)
    var startEl = document.getElementById("regStartDate");
    var endEl   = document.getElementById("regEndDate");
    if (startEl) startEl.removeAttribute("max");
    if (endEl)   endEl.removeAttribute("min");
}


/* ==========================================================
   수정 모달 (detail 페이지 전용)
   - JSP에서 planId/itemId/planQty/status/날짜/담당자 값은 이미 주입됨
   - JS는 대분류명/단위/규격(읽기전용 표시) + 종료일 min만 세팅
   ========================================================== */
function openEditModal() {
    var modal = document.getElementById("modalEdit");
    if (!modal) return;

    // 대분류명 / 단위 / 규격 세팅 (itemDataMap에서 조회)
    var itemIdEl = document.getElementById("editItemId");
    var currentItemId = itemIdEl ? itemIdEl.value
                        : (typeof DTL_ITEM_ID !== "undefined" ? DTL_ITEM_ID : "");

    if (currentItemId && typeof itemDataMap !== "undefined") {
        Object.keys(itemDataMap).forEach(function (gId) {
            itemDataMap[gId].forEach(function (item) {
                if (item.itemId === String(currentItemId)) {
                    var gView    = document.getElementById("editGroupView");
                    var subView  = document.getElementById("editSubItemView");
                    var unitEl   = document.getElementById("editUnit");
                    var specEl   = document.getElementById("editSpec");
                    if (gView)   gView.value   = groupLabelById(gId);
                    if (subView) subView.value = item.itemName;
                    if (unitEl)  unitEl.value  = item.unit || "";
                    if (specEl)  specEl.value  = item.spec || "";
                }
            });
        });
    }

    // 종료일 min = 현재 시작일
    var startEl = document.getElementById("editStartDate");
    var endEl   = document.getElementById("editEndDate");
    if (startEl && endEl && startEl.value) {
        endEl.min = startEl.value;
    }

    modal.classList.remove("dtl-hidden");
    modal.style.display = "flex";
}

function closeEditModal() {
    var modal = document.getElementById("modalEdit");
    if (!modal) return;
    modal.style.display = "none";
    modal.classList.add("dtl-hidden");
}

/* 대분류 ID → 화면 표기명 매핑 (JSP c:choose 규칙과 동일하게) */
function groupLabelById(gId) {
    if (String(gId) === "30") return "완제품";
    if (String(gId) === "20") return "반제품";
    // 10(자재)은 등록 대상이 아니지만, 혹시 표시될 경우를 대비해 빈 값 반환
    return "";
}


/* ==========================================================
   담당자 검색 팝업
   - mode: 'register' (등록 모달) / 'edit' (수정 모달)
   ========================================================== */
var EMP_PAGE_SIZE = 5;
var EMP_TARGET_MODE = "register";  // 선택한 담당자를 어느 모달의 input에 세팅할지

function openEmpPopup(mode) {
    EMP_TARGET_MODE = (mode === "edit") ? "edit" : "register";
    document.getElementById("empSearchKeyword").value = "";
    document.getElementById("empPopup").style.display = "flex";
    document.getElementById("empPopup").classList.remove("dtl-hidden");
    fetchEmpList("", 1);
}
function closeEmpPopup() {
    document.getElementById("empPopup").style.display = "none";
}

function fetchEmpList(keyword, page) {
    var url = "/mes/prod/api/empSearch"
            + "?keyword=" + encodeURIComponent(keyword)
            + "&page="    + page
            + "&size="    + EMP_PAGE_SIZE;

    fetch(url)
        .then(function (res) { return res.json(); })
        .then(function (data) { renderEmpList(data, keyword, page); })
        .catch(function (err) { console.error("담당자 검색 오류:", err); });
}

function renderEmpList(data, keyword, page) {
    var tbody     = document.getElementById("empListBody");
    var pagingDiv = document.getElementById("empPaging");
    tbody.innerHTML = "";

    if (!data.list || data.list.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3" class="emp-empty">검색 결과가 없습니다.</td></tr>';
        pagingDiv.innerHTML = "";
        return;
    }

    data.list.forEach(function (emp) {
        var tr     = document.createElement("tr");
        tr.className = "emp-row";
        tr.innerHTML =
            '<td>' + escHtml(emp.empId)    + '</td>' +
            '<td>' + escHtml(emp.ename)    + '</td>' +
            '<td>' + escHtml(emp.deptName) + '</td>';
        tr.addEventListener("click", function () { selectEmp(emp.empId, emp.ename); });
        tbody.appendChild(tr);
    });

    renderEmpPaging(data.totalCount, page, keyword);
}

function renderEmpPaging(total, current, keyword) {
    var pagingDiv = document.getElementById("empPaging");
    var totalPage = Math.max(1, Math.ceil(total / EMP_PAGE_SIZE));
    var section   = 5;
    var endSec    = Math.ceil(current / section) * section;
    var startSec  = endSec - section + 1;
    var realEnd   = Math.min(endSec, totalPage);

    var html = "";

    html += startSec > 1
        ? '<button class="emp-page-btn" onclick="fetchEmpList(\'' + escHtml(keyword) + '\',' + (startSec - 1) + ')">[이전]</button>'
        : '<button class="emp-page-btn" disabled>[이전]</button>';

    for (var i = startSec; i <= realEnd; i++) {
        html += i === current
            ? '<button class="emp-page-btn emp-page-btn-active">' + i + '</button>'
            : '<button class="emp-page-btn" onclick="fetchEmpList(\'' + escHtml(keyword) + '\',' + i + ')">' + i + '</button>';
    }

    html += realEnd < totalPage
        ? '<button class="emp-page-btn" onclick="fetchEmpList(\'' + escHtml(keyword) + '\',' + (realEnd + 1) + ')">[다음]</button>'
        : '<button class="emp-page-btn" disabled>[다음]</button>';

    pagingDiv.innerHTML = html;
}

function selectEmp(empId, ename) {
    var prefix = (EMP_TARGET_MODE === "edit") ? "edit" : "reg";
    var nameEl = document.getElementById(prefix + "EmpName");
    var idEl   = document.getElementById(prefix + "EmpId");
    if (idEl)   idEl.value   = empId;
    if (nameEl) nameEl.value = ename;
    closeEmpPopup();
}

function escHtml(str) {
    if (!str) return "";
    return String(str)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;");
}


/* ==========================================================
   소분류 옵션 채우기 (공통)
   ========================================================== */
function updateSubItemOptions(groupSelect, subSelect, targetItemId) {
    var gId = groupSelect.value;
    subSelect.innerHTML = '<option value="">소분류 선택</option>';
    var unitEl = document.getElementById("regUnit");
    var specEl = document.getElementById("regSpec");
    if (unitEl) unitEl.value = "";
    if (specEl) specEl.value = "";

    if (!gId) return;

    var items = (typeof itemDataMap !== "undefined") ? (itemDataMap[gId] || []) : [];
    items.forEach(function (item) {
        var opt        = document.createElement("option");
        opt.value        = item.itemId;
        opt.textContent  = item.itemName;
        opt.dataset.unit = item.unit || "";
        opt.dataset.spec = item.spec || "";
        subSelect.appendChild(opt);
    });

    if (targetItemId) {
        for (var i = 0; i < subSelect.options.length; i++) {
            if (subSelect.options[i].value === String(targetItemId)) {
                subSelect.selectedIndex = i;
                if (unitEl) unitEl.value = subSelect.options[i].dataset.unit || "";
                if (specEl) specEl.value = subSelect.options[i].dataset.spec || "";
                break;
            }
        }
    }
}


/* ==========================================================
   상세 페이지 초기화
   ========================================================== */
function initDetailPage() {
    var fill = document.getElementById("dtlProgressFill");
    if (fill) {
        fill.style.width = (fill.dataset.width || 0) + "%";
    }
}