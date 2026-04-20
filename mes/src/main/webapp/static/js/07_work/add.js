window.addEventListener("load", () => {
    init();
});

let debounceTimer;

function init() {
    bind();
}

function bind() {
	document.querySelector("#backBtn").addEventListener("click", back);

    document.querySelector("#fetchBtn").addEventListener("click", fetchPlan);
    document.querySelector("#workerBtn").addEventListener("click", openWorkerModal);

    document.querySelector("#modalCancel").addEventListener("click", closeModal);
    document.querySelector("#modalSelect").addEventListener("click", workerSelect);

    // 모달 내부 실시간 검색
    document.querySelector("#workerKeyword").addEventListener("input", handleSearchInput);
    
    // 목표 수량 clamp
    document.querySelector("#targetQty").addEventListener("input", handleTargetQtyInput);
    
    
    document.querySelector("#addBtn").addEventListener("click", validateAndSubmit);
}




function fetchPlan() {
    const planSelect = document.querySelector("#planSelect");
    const planId = planSelect.value;

    if (!planId) {
        return alert("생산 계획을 선택하세요");
    }

    fetch(`/mes/workadd?cmd=getPlan&planId=${planId}`)
        .then(res => res.json())
        .then(data => {
            document.querySelector("#planId").value = data.planId;

            document.querySelector("#itemId").value = data.itemId;
            document.querySelector("#progressQty").value = `${data.prevQty} / ${data.planQty}`;
            document.querySelector("#director").value = `${data.dName} (${data.dId})`;

            document.querySelector("#workDate").min = data.sDate;
            document.querySelector("#workDate").max = data.eDate;

            const targetQtyEl = document.querySelector("#targetQty");
            const remainQty = Number(data.planQty) - Number(data.prevQty);

            targetQtyEl.value = "";
            targetQtyEl.min = 1;
            targetQtyEl.max = remainQty > 0 ? remainQty : 0;
            
            
            planSelect.value = data.planId;
        })
        .catch(err => {
            console.error(err);
            alert("생산 계획 정보를 불러오지 못했습니다.");
        });
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

    fetch(`/mes/workadd?cmd=searchWorker&keyword=${encodeURIComponent(keyword)}`)
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

function back() {
	window.location.href = "/mes/worklist";
}

function validateAndSubmit(e) {
    e.preventDefault();

    const planId = document.querySelector("#planId").value;
    const itemId = document.querySelector("#itemId").value;
    const progressQty = document.querySelector("#progressQty").value;
    const targetQtyEl = document.querySelector("#targetQty");
    const targetQty = Number(targetQtyEl.value);
    const maxQty = Number(targetQtyEl.max);
    const workDate = document.querySelector("#workDate").value;
    const director = document.querySelector("#director").value;
    const workerId = document.querySelector("#workerId").value;

    if (!planId || !itemId || !progressQty || !director) {
        return alert("생산 계획을 선택하세요");
    }

    if (!targetQtyEl.value || targetQty <= 0) {
        return alert("목표 수량을 입력하세요");
    }

    if (!targetQtyEl.max || maxQty <= 0) {
        return alert("남은 계획 수량이 없습니다.");
    }

    if (targetQty > maxQty) {
        targetQtyEl.value = maxQty;
        return alert(`목표 수량은 ${maxQty} 이하만 입력할 수 있습니다.`);
    }

    if (!workDate) {
        return alert("작업일을 선택하세요");
    }

    if (!workerId) {
        return alert("작업자를 선택하세요");
    }

    document.querySelector("form").submit();
}


/* =========================
   목표 수량 제한
========================= */
function handleTargetQtyInput(e) {
    const el = e.target;

    // 빈 값 허용
    if (el.value === "") return;

    let num = Number(el.value);

    if (isNaN(num)) {
        el.value = "";
        return;
    }

    const min = el.min !== "" ? Number(el.min) : -Infinity;
    const max = el.max !== "" ? Number(el.max) : Infinity;

    if (num < min) {
        el.value = min;
        return;
    }

    if (num > max) {
        el.value = max;
        return;
    }

    el.value = num;
}

function clampNumber(el) {
    let val = el.value;

    if (val === '') return '';

    let num = Number(val);
    if (isNaN(num)) return '';

    const min = el.min !== '' ? Number(el.min) : -Infinity;
    const max = el.max !== '' ? Number(el.max) : Infinity;

    if (num < min) return min;
    if (num > max) return max;

    return num;
}
