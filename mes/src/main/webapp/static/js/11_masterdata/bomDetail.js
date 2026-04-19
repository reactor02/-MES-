document.addEventListener("DOMContentLoaded", function () {
    const addModal = document.getElementById("addBomDetailModal");
    const editModal = document.getElementById("editBomDetailModal");

    const openAddBtn = document.getElementById("openBomDetailAddModal");
    const closeAddBtn = document.getElementById("closeBomDetailAddModal");
    const closeEditBtn = document.getElementById("closeBomDetailEditModal");

    const addChildItemSelect = document.getElementById("add_child_item_id");
    const addEaInput = document.getElementById("add_ea");
    const addUnitInput = document.getElementById("add_unit");

    const editBomDetailIdInput = document.getElementById("edit_bom_detail_id");
    const editChildItemSelect = document.getElementById("edit_child_item_id");
    const editEaInput = document.getElementById("edit_ea");
    const editUnitInput = document.getElementById("edit_unit");

    const editButtons = document.querySelectorAll(".bom-detail-edit-btn");

    function openModal(modal) {
        if (modal) {
            modal.style.display = "flex";
        }
    }

    function closeModal(modal) {
        if (modal) {
            modal.style.display = "none";
        }
    }

    function getSelectedUnit(selectElement) {
        if (!selectElement) {
            return "";
        }

        const selectedOption = selectElement.options[selectElement.selectedIndex];
        if (!selectedOption) {
            return "";
        }

        return selectedOption.dataset.unit || "";
    }

    function setUnitFromSelect(selectElement, unitInput) {
        if (!selectElement || !unitInput) {
            return;
        }
        unitInput.value = getSelectedUnit(selectElement);
    }

    function resetAddModal() {
        if (addChildItemSelect) {
            addChildItemSelect.value = "";
        }
        if (addEaInput) {
            addEaInput.value = "";
        }
        if (addUnitInput) {
            addUnitInput.value = "";
        }
    }

    if (openAddBtn) {
        openAddBtn.addEventListener("click", function () {
            resetAddModal();
            openModal(addModal);
        });
    }

    if (closeAddBtn) {
        closeAddBtn.addEventListener("click", function () {
            closeModal(addModal);
        });
    }

    if (closeEditBtn) {
        closeEditBtn.addEventListener("click", function () {
            closeModal(editModal);
        });
    }

    if (addChildItemSelect) {
        addChildItemSelect.addEventListener("change", function () {
            setUnitFromSelect(addChildItemSelect, addUnitInput);
        });
    }

    if (editChildItemSelect) {
        editChildItemSelect.addEventListener("change", function () {
            setUnitFromSelect(editChildItemSelect, editUnitInput);
        });
    }

    editButtons.forEach(function (button) {
        button.addEventListener("click", function () {
            const bomDetailId = button.dataset.bomDetailId || "";
            const childItemId = button.dataset.childItemId || "";
            const ea = button.dataset.ea || "";
            const unit = button.dataset.unit || "";

            if (editBomDetailIdInput) {
                editBomDetailIdInput.value = bomDetailId;
            }

            if (editChildItemSelect) {
                editChildItemSelect.value = childItemId;
            }

            if (editEaInput) {
                editEaInput.value = ea;
            }

            if (editChildItemSelect && editChildItemSelect.value !== "") {
                setUnitFromSelect(editChildItemSelect, editUnitInput);
            } else if (editUnitInput) {
                editUnitInput.value = unit;
            }

            openModal(editModal);
        });
    });

    if (addModal) {
        addModal.addEventListener("click", function (event) {
            if (event.target === addModal) {
                closeModal(addModal);
            }
        });
    }

    if (editModal) {
        editModal.addEventListener("click", function (event) {
            if (event.target === editModal) {
                closeModal(editModal);
            }
        });
    }

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape") {
            closeModal(addModal);
            closeModal(editModal);
        }
    });
});
