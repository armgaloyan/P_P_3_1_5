
const url1 ='http://localhost:8080/api/admin/user'
const url2 ='http://localhost:8080/api/admin'
const url3 ='http://localhost:8080/api/admin'
const url4 ='http://localhost:8080/api/admin/roles'

//Nav Bar
fetch(url1)
    .then(res => { res.json().then(
        user=>{
            let navBar = ""
            navBar += "<b class=\"text-white\">"+user.username+"</b>"
            navBar += "<span class=\"text-white\"> with roles: </span>"
            navBar += "<span class=\"text-white\">"
            user.roles.forEach((role) => navBar += role.role.replace('ROLE_','')+' ')
            navBar += "</span>"
            document.getElementById("navBar").innerHTML = navBar
        }
    )
    })

//Admin Panel

const populateRolesDropdown = (dropdownId) => {
    fetch(url4)
        .then(response => response.json())
        .then(roles => {
            const dropdown = document.getElementById(dropdownId);
            dropdown.innerHTML = ''; // Очистить существующие опции
            roles.forEach(role => {
                const option = document.createElement('option');
                option.value = role.role;
                option.text = role.role.replace('ROLE_', '');
                dropdown.appendChild(option);
            });
        });
};

// Заполнить роли для форм добавления и редактирования
populateRolesDropdown('roles');
populateRolesDropdown('rolesEdit');
populateRolesDropdown('rolesDelete');

const showTable = (users) => {
    let table = document.getElementById("tableAllUsers").innerHTML
    users.forEach((user)=> {
        table += `
                <tr id="${user.id}">
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.age}</td>                  
                    <td>`

        user.roles.forEach((role) => table += role.role.replace('ROLE_','')+" ")

        table += `
                    </td>
                    <td><button class="btn btn-info eBtn" data-toggle="modal">Edit</button></td>
                    <td><button class="btn btn-danger dBtn" data-toggle="modal">Delete</button></td>
                 `
    })
    document.getElementById("tableAllUsers").innerHTML = table
}

fetch(url2)
    .then( response => response.json())
    .then(data => showTable(data))

const on = (element, event, selector, handler) => {
    element.addEventListener(event, e => {
        if(e.target.closest(selector)){
            handler(e)
        }
    })
}

//Add new user
newUserLink.addEventListener('click', (e) => {
    username.value = ''
    age.value = ''
    pass.value = ''
    roles.value = null
})

newUserForm.addEventListener('submit', (e) => {
    e.preventDefault()
    let id = 0
    let rolesList = [];
    for(let i = 0; i < $('#roles').val().length; i++){
        if ($('#roles').val()[i]==='ROLE_ADMIN') {
            id=1
        } else {
            id=2
        }
        rolesList[i] = {id: id, role: $('#roles').val()[i]} ;
    }
    let newUser = {
        username: username.value,
        age: age.value,
        password: pass.value,
        roles: rolesList
    }
    fetch(url2, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUser)
    })
        .then(response => response.json())
        .then((data) => {
            const newUserInTable = []
            newUserInTable.push(data)
            showTable(newUserInTable)
        })
        .then(() => document.getElementById('userTable').click())
})

//Edit Modal

on(document, 'click', '.eBtn', e => {
    const lineEdit = e.target.parentNode.parentNode
    const idEditModal = lineEdit.children[0].innerHTML
    const usernameEditModal = lineEdit.children[1].innerHTML
    const ageEditModal = lineEdit.children[2].innerHTML

    idEdit.value = idEditModal
    usernameEdit.value = usernameEditModal
    ageEdit.value = ageEditModal
    $('#editModal').modal()
})

editModal.addEventListener('submit', (e) => {
    e.preventDefault()
    let id = 0
    let rolesListEdit = [];
    for(let i = 0; i < $('#rolesEdit').val().length; i++){
        if ($('#rolesEdit').val()[i]==='ROLE_ADMIN') {
            id=1
        } else {
            id=2
        }
        rolesListEdit[i] = {id: id, role: $('#rolesEdit').val()[i]} ;
    }
    let editUser = {
        id: idEdit.value,
        username: usernameEdit.value,
        age: ageEdit.value,
        password: passEdit.value,
        roles: rolesListEdit
    }
    fetch(url2, {
        method: 'PUT',
        headers: {
            'Content-Type':'application/json'
        },
        body: JSON.stringify(editUser)
    })
        .then(response => response.json())
        .then((data) => {
            const editUserInTable = []
            editUserInTable.push(data)
            showTable(editUserInTable)
        })
        .then(() => document.getElementById(idEdit.value).remove())
        .then(()=> document.getElementById('editModalClose').click())
})

//Delete Modal
on(document, 'click', '.dBtn', e => {
    const lineDelete = e.target.parentNode.parentNode
    const idDeleteModal = lineDelete.children[0].innerHTML
    const usernameDeleteModal = lineDelete.children[1].innerHTML
    const ageDeleteModal = lineDelete.children[2].innerHTML
    idDelete.value = idDeleteModal
    usernameDelete.value = usernameDeleteModal
    ageDelete.value = ageDeleteModal
    $('#deleteModal').modal()
})

deleteModal.addEventListener('submit', (e) => {
    e.preventDefault()
    fetch(url3+idDelete.value, {
        method: 'DELETE'
    })
        .then(() => document.getElementById(idDelete.value).remove())
        .then(()=> document.getElementById('deleteModalClose').click())
})
