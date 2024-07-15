const form_ed = document.getElementById('formForEditing');
const id_ed = document.getElementById('edit-id');
const username_ed = document.getElementById('edit-username');
const age_ed = document.getElementById('edit-age');
const editModal = document.getElementById("editModal");
const closeEditButton = document.getElementById("editClose")
const bsEditModal = new bootstrap.Modal(editModal);

async function loadDataForEditModal(id) {
    const urlDataEd = '/api/admin/users?id=' + id;
    let usersPageEd = await fetch(urlDataEd);
    if (usersPageEd.ok) {
        await usersPageEd.json().then(user => {
            console.log('userData', JSON.stringify(user))
            id_ed.value = `${user.id}`;
            username_ed.value = `${user.username}`;
            age_ed.value = `${user.age}`;
        })
        console.log("id_ed: " + id_ed.value + " !!")
        bsEditModal.show();
    } else {
        alert(`Error, ${usersPageEd.status}`)
    }
}

async function editUser() {
    let urlEdit = '/api/admin/updateUser?id=' + id_ed.value;
    let listOfRole = [];
    console.dir(form_ed)
    for (let i = 0; i < form_ed.roles.options.length; i++) {
        if (form_ed.roles.options[i].selected) {
            let tmp = {};
            tmp["id"] = form_ed.roles.options[i].value
            listOfRole.push(tmp);
        }
    }
    let method = {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: form_ed.username.value,
            age: form_ed.age.value,
            password: form_ed.password.value,
            roles: listOfRole
        })
    }
    console.log(urlEdit, method)
    await fetch(urlEdit, method).then(() => {
        closeEditButton.click();
        getAdminPage();
    })
}