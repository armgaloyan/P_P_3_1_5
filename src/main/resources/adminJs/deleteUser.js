const id_del = document.getElementById('id_del');
const username_del = document.getElementById('username_del');
const age_del = document.getElementById('age_del');
const role_del = document.getElementById("delete-role")
const deleteModal = document.getElementById("deleteModal");
const closeDeleteButton = document.getElementById("closeDelete")
const bsDeleteModal = new bootstrap.Modal(deleteModal);

async function deleteModalData(id) {
    const urlForDel = '/api/admin/users?id=' + id;
    let usersPageDel = await fetch(urlForDel);
    if (usersPageDel.ok) {
        let userData =
            await usersPageDel.json().then(user => {
                id_del.value = `${user.id}`;
                username_del.value = `${user.username}`;
                age_del.value = `${user.age}`;
                role_del.value = user.roles.map(r => r.name).join(", ");
            })

        bsDeleteModal.show();
    } else {
        alert(`Error, ${usersPageDel.status}`)
    }
}

async function deleteUser() {
    let urlDel = '/api/admin/deleteUser?id=' + id_del.value;
    let method = {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        }
    }

    fetch(urlDel, method).then(() => {
        closeDeleteButton.click();
        getAdminPage();
    })
}