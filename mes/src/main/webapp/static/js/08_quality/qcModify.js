window.addEventListener("load", () => {
    init();
});

let debounceTimer;

function init() {
    bind();
}

function bind() {
    document.querySelector("#workerBtn").addEventListener("click", openWorkerModal);

    document.querySelector("#modalCancel").addEventListener("click", closeModal);
    document.querySelector("#modalSelect").addEventListener("click", workerSelect);

    // 모달 내부 실시간 검색
    document.querySelector("#workerKeyword").addEventListener("input", handleSearchInput);
    
    document.querySelector("#addBtn").addEventListener("click", validateAndSubmit);
}

/* =========================
   실시간 검색 관련
========================= */

function handleSearchInput(e) {
    const keyword = e.target.value;

    clearTimeout(debounceTimer);

    debounceTimer = setTimeout(() => {
        searchWorker(keyword);
    }, 300);
}

function searchWorker(keyword) {
    if (!keyword) {
        renderWorkerTable([]);
        return;
    }

    fetch(`/mes/qcadd?cmd=searchWorker&keyword=${encodeURIComponent(keyword)}`)
        .then(res => res.json())
        .then(data => {
            renderWorkerTable(data);
        })
        .catch(err => console.error(err));
}

/* =========================
   모달 제어
========================= */

function openWorkerModal() {
    const modal = document.querySelector("#workerModal");

    modal.style.display = "flex";
    document.body.style.overflow = "hidden";

    // 입력 초기화 + 포커스
    const input = document.querySelector("#workerKeyword");
    input.value = "";
    input.focus();

    renderWorkerTable([]);
}

function closeModal() {
    document.querySelector("#workerModal").style.display = "none";
    document.body.style.overflow = "";
}

/* =========================
   선택 처리
========================= */

function workerSelect() {
    const selected = document.querySelector("input[name='workerRadio']:checked");

    if (!selected) {
        return alert("작업자를 선택하세요");
    }

    const empId = selected.value;
    const empName = selected.dataset.name;

    document.querySelector("#worker").value = `${empName} (${empId})`;
    document.querySelector("#workerId").value = empId;

    closeModal();
}

/* =========================
   테이블 렌더링
========================= */

function renderWorkerTable(list) {
    const tbody = document.querySelector("#workerTbody");
    tbody.innerHTML = "";

    if (list.length === 0) {
        const tr = document.createElement("tr");
        const td = document.createElement("td");

        td.colSpan = 3;
        td.style.textAlign = "center";

        const span = document.createElement("span");
        span.textContent = "조회 결과 없음";

        td.appendChild(span);
        tr.appendChild(td);
        tbody.appendChild(tr);

        return;
    }

    list.forEach(worker => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>
                <input type="radio" name="workerRadio" value="${worker.empId}" data-name="${worker.empName}">
            </td>
            <td>${worker.empId}</td>
            <td>${worker.empName}</td>
        `;

        tr.addEventListener("click", () => {
            tr.querySelector("input").checked = true;
        });

        tbody.appendChild(tr);
    });
}

function validateAndSubmit(e) {
    e.preventDefault();

    const woId = document.querySelector("#woId").value;
    const item = document.querySelector("#item").value;
    const qty = document.querySelector("#qty").value;
    const qcDate = document.querySelector("#qcDate").value;
    const workerId = document.querySelector("#workerId").value;

    if (!woId || !item || !qty) {
        return alert("검사 예정인 작업을 선택하세요");
    }

    if (!qcDate) {
        return alert("검사 시작일을 선택하세요");
    }

    if (!workerId) {
        return alert("검사자를 선택하세요");
    }

    document.querySelector("form").submit();
}

/* =========================
   수정, 삭제 submit
========================= */
function submitForm(type) {
    if (type === 'delete') {
        if (!confirm('정말 삭제하시겠습니까?')) return;
    }

    document.getElementById('actionType').value = type;
    document.getElementById('qcForm').submit();
}