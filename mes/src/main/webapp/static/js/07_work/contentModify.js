window.addEventListener("load", () => {
    init();
});

let debounceTimer;

function init() {
    bind();
}

function bind() {

}

function clampNumber(el) {
    let val = el.value;

    // 빈 값 허용 (지우는 동작 방해하지 않기)
    if (val === '') return '';

    let num = Number(val);
    if (isNaN(num)) return '';

    const min = el.min !== '' ? Number(el.min) : -Infinity;
    const max = el.max !== '' ? Number(el.max) : Infinity;

    if (num < min) return min;
    if (num > max) return max;

    return num;
}

function submitContentModify() {
    const form = document.getElementById("contentModify");
    const checkedStatus = document.querySelector('input[name="status"]:checked');
    const targetQty = Number(document.getElementById("woQty").value);
    const prevQty = Number(document.getElementById("prevQty").value);
    const targetQtyEl = document.querySelector("#targetQty");
	const targetQty = Number(targetQtyEl.value);
	const maxQty = Number(targetQtyEl.max);
	
	if (!targetQty || targetQty <= 0) {
	    return alert("목표 수량을 입력하세요");
	}
	
	if (targetQty > maxQty) {
	    return alert(`목표 수량은 ${maxQty} 이하만 입력할 수 있습니다.`);
	}

    if (checkedStatus && checkedStatus.value === "30" && prevQty < targetQty) {
        alert("완료 수량이 목표 수량보다 적으면 작업을 완료할 수 없습니다");
        return;
    }

    form.submit();
}