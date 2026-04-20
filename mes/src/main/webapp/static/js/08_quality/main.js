window.addEventListener("load", () => {
	init();
})

function init() {
	bind();
	setEndDateLimit();
}

function bind() {
	reset();
	addQo();
}

function reset() {
	console.log("reset 실행");
	const resetBtn = document.querySelector(".reset");
	
	resetBtn.addEventListener("click", resetPartial);
}

function resetPartial() {
    const form = document.querySelector("form");
    
    // status 초기화 (전체보기)
    form.querySelector("select[name='status']").value = "0";

    // 날짜 초기화
    form.querySelector("input[name='startDate']").value = "";
    form.querySelector("input[name='endDate']").value = "";
    
    const keyword = form.querySelector("input[name='keyword']").value;

    location.href = `/mes/qclist`;
}

function addQo() {
	const addBtn = document.querySelector(".addBtn");
	
	addBtn.addEventListener ("click", () => {
		window.location.href = "/mes/qcadd";
	})
}


function setEndDateLimit() {
	const startDate = document.querySelector("input[name='startDate']");
	const endDate = document.querySelector("input[name='endDate']");

	if (!startDate || !endDate) return;

	function updateEndDateMin() {
		const minDate = startDate.value || "";
		endDate.min = minDate;

		if (endDate.value && endDate.value < minDate) {
			endDate.value = "";
		}
	}

	// 시작일 선택하는 즉시 종료일 제한 갱신
	startDate.addEventListener("input", updateEndDateMin);
	startDate.addEventListener("change", updateEndDateMin);

	// 종료일을 누르는 순간 최신 시작일 기준으로 다시 min 적용
	endDate.addEventListener("focus", updateEndDateMin);
	endDate.addEventListener("click", updateEndDateMin);
	endDate.addEventListener("mousedown", updateEndDateMin);

	// 첫 로드 시에도 반영
	updateEndDateMin();
}
