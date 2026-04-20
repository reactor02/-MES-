window.addEventListener("load", () => {
	init();
})

function init() {
	bind();
	setEndDateLimit();
}

function bind() {
	reset();
	addWo();
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

    location.href = `/mes/worklist`;
}

function addWo() {
	const addBtn = document.querySelector(".addBtn");
	
	addBtn.addEventListener ("click", () => {
		window.location.href = "/mes/workadd";
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

	// 시작일 선택 즉시 반영
	startDate.addEventListener("input", updateEndDateMin);
	startDate.addEventListener("change", updateEndDateMin);

	// 종료일 누를 때 최신 min 다시 반영
	endDate.addEventListener("focus", updateEndDateMin);
	endDate.addEventListener("click", updateEndDateMin);
	endDate.addEventListener("mousedown", updateEndDateMin);

	// 처음 로드 시에도 반영
	updateEndDateMin();
}
