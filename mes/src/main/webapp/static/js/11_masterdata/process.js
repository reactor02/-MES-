const processStepModal = document.getElementById("processStepModal");
const processAddModal = document.getElementById("processAddModal");
const processEditModal = document.getElementById("processEditModal");

const openProcessStepModal = document.getElementById("openProcessStepModal");
const closeProcessStepModal = document.getElementById("closeProcessStepModal");
const openProcessAddModal = document.getElementById("openProcessAddModal");
const closeProcessAddModal = document.getElementById("closeProcessAddModal");
const closeProcessEditModal = document.getElementById("closeProcessEditModal");

const editProcessId = document.getElementById("editProcessId");
const editProcessType = document.getElementById("editProcessType");
const editProcessName = document.getElementById("editProcessName");
const editProcessInfo = document.getElementById("editProcessInfo");
const editProcessModalTitleText = document.getElementById("editProcessModalTitleText");

function openModal(modalElement) {
	if (!modalElement) {
		return;
	}

	modalElement.classList.add("is-open");
	modalElement.style.display = "flex";
}

function closeModal(modalElement) {
	if (!modalElement) {
		return;
	}

	modalElement.classList.remove("is-open");
	modalElement.style.display = "none";
}

if (openProcessStepModal) {
	openProcessStepModal.addEventListener("click", function() {
		openModal(processStepModal);
	});
}

if (closeProcessStepModal) {
	closeProcessStepModal.addEventListener("click", function() {
		closeModal(processStepModal);
	});
}

if (openProcessAddModal) {
	openProcessAddModal.addEventListener("click", function() {
		openModal(processAddModal);
	});
}

if (closeProcessAddModal) {
	closeProcessAddModal.addEventListener("click", function() {
		closeModal(processAddModal);
	});
}

if (closeProcessEditModal) {
	closeProcessEditModal.addEventListener("click", function() {
		closeModal(processEditModal);
	});
}

document.querySelectorAll(".process-icon-btn.edit").forEach(function(button) {
	button.addEventListener("click", function() {
		if (editProcessId) {
			editProcessId.value = button.dataset.processId || "";
		}

		if (editProcessType) {
			editProcessType.value = button.dataset.processType || "wo";
		}

		if (editProcessName) {
			editProcessName.value = button.dataset.processName || "";
		}

		if (editProcessInfo) {
			editProcessInfo.value = button.dataset.processInfo || "";
		}

		if (editProcessModalTitleText) {
			editProcessModalTitleText.textContent = button.dataset.processName || "공정";
		}

		openModal(processEditModal);
	});
});

[processStepModal, processAddModal, processEditModal].forEach(function(modalElement) {
	if (!modalElement) {
		return;
	}

	modalElement.addEventListener("click", function(event) {
		if (event.target === modalElement) {
			closeModal(modalElement);
		}
	});
});

document.addEventListener("keydown", function(event) {
	if (event.key !== "Escape") {
		return;
	}

	closeModal(processStepModal);
	closeModal(processAddModal);
	closeModal(processEditModal);
});
