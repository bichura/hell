// $(document).ready(function() {
//
//     let csrfToken = $('meta[name="_csrf"]').attr('content');
//     let csrfHeader = $('meta[name="_csrf_header"]').attr('content');
//
//     $.ajax({
//         url: '/admin/roles',
//         type: 'GET',
//         dataType: 'json',
//         success: function(roles) {
//             populateRolesSelect('#editRoles', roles);
//             populateRolesSelect('#deleteRoles', roles);
//         }
//         // ,
//     //     error: function(error) {
//     //         console.error("Ошибка при загрузке списка ролей:", error);
//     //     }
//     });
//
//     function populateRolesSelect(selector, roles) {
//         const select = $(selector);
//         select.empty(); // Очищаем select
//         roles.forEach(role => {
//             select.append(`<option value="${role.id}">${role.name}</option>`);
//         });
//     }
//
//     $('#nav-users-table').on('click', '.btn-info', function() {
//         const userId = $(this).attr('data-id');
//         const firstName = $(this).attr('data-firstname');
//         const lastName = $(this).attr('data-lastname');
//         const age = $(this).attr('data-age');
//         const email = $(this).attr('data-email');
//         // Обработка ролей (заглушка - нужно доработать)
//         const roles = $(this).attr('data-roles'); // Здесь будет строка, например, "ROLE_ADMIN ROLE_USER"
//
//         $('#editId').val(userId);
//         $('#editFirstName').val(firstName);
//         $('#editLastName').val(lastName);
//         $('#editAge').val(age);
//         $('#editEmail').val(email);
//         $('#editRoles option').each(function() {
//             const roleId = $(this).val();
//             const roleName = $(this).text();
//             if (roles.includes(roleName)) {
//                 $(this).prop('selected', true);
//             }
//         });
//     });
//
//     $('#editUser').submit(function(event) {
//         event.preventDefault(); // Предотвращаем стандартную отправку формы
//         const formData = $(this).serializeArray(); // Используем serializeArray
//         const roleIds = $('#editRoles').val(); // Получаем массив выбранных ID ролей
//         formData.push({name: 'roles', value: JSON.stringify(roleIds)}); // Добавляем роли в formData
//         const jsonData = {};
//         formData.forEach(item => {
//             jsonData[item.name] = item.value;
//         });
//         console.log(jsonData);
//         $.ajax({
//             url: '/admin/edit',
//             type: 'POST',
//             contentType: 'application/json',
//             dataType: 'json', // Указываем тип данных, которые ожидаем от сервера
//             data: JSON.stringify(jsonData), // Отправляем данные в формате JSON
//             headers: {
//                 [csrfHeader]: csrfToken
//             },
//             success: function(response) {
//                 // Обработка успешного ответа от сервера
//                 $('#editModal').modal('hide');
//                 location.reload(); // Перезагружаем страницу
//             },
//             error: function(error) {
//                 // Обработка ошибки
//                 console.error("Ошибка при редактировании пользователя:", error);
//                 alert("Ошибка редактирования пользователя. Пожалуйста, проверьте консоль для подробностей.");
//             }
//         });
//     });
//
//     //DELETE
//     $('#nav-users-table').on('click', '.btn-danger', function() {
//         const userId = $(this).attr('data-id');
//         const firstName = $(this).attr('data-firstname');
//         const lastName = $(this).attr('data-lastname');
//         const age = $(this).attr('data-age');
//         const email = $(this).attr('data-email');
//         // Обработка ролей (заглушка - нужно доработать)
//         const roles = $(this).attr('data-roles');
//
//         $('#deleteId').val(userId);
//         $('#deleteFirstName').val(firstName);
//         $('#deleteLastName').val(lastName);
//         $('#deleteAge').val(age);
//         $('#deleteEmail').val(email);
//         $('#deleteRoles option').each(function() {
//             const roleId = $(this).val();
//             const roleName = $(this).text();
//             if (roles.includes(roleName)) {
//                 $(this).prop('selected', true);
//             }
//         });
//     });
//
//     $('#deleteUser').submit(function(event) {
//         event.preventDefault(); // Предотвращаем стандартную отправку формы
//         const userId = $('#deleteId').val();
//         const url = $(this).attr('action').replace('{id}', userId); // Заменяем {id} на реальный ID
//         $.ajax({
//             url: url,
//             type: 'POST',
//             headers: {
//                 [csrfHeader]: csrfToken
//             },
//             success: function(response) {
//                 // Обработка успешного ответа от сервера
//                 $('#deleteModal').modal('hide');
//                 location.reload(); // Перезагружаем страницу
//             },
//             error: function(error) {
//                 // Обработка ошибки
//                 console.error("Ошибка при удалении пользователя:", error);
//             }
//         });
//     });
// });

$(document).ready(function() {

    let csrfToken = $('meta[name="_csrf"]').attr('content');
    let csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    $.ajax({
        url: '/admin/roles',
        type: 'GET',
        dataType: 'json',
        success: function(roles) {
            populateRolesSelect('#editRoles', roles);
            populateRolesSelect('#deleteRoles', roles);
        }
    });

    function populateRolesSelect(selector, roles) {
        const select = $(selector);
        select.empty();
        roles.forEach(role => {
            select.append(`<option value="${role.id}">${role.name}</option>`);
        });
    }

    $('#nav-users-table').on('click', '.btn-info', function() {
        const userId = $(this).data('id');
        const firstName = $(this).data('firstname');
        const lastName = $(this).data('lastname');
        const age = $(this).data('age');
        const email = $(this).data('email');
        const roles = $(this).data('roles');

        $('#editId').val(userId);
        $('#editFirstName').val(firstName);
        $('#editLastName').val(lastName);
        $('#editAge').val(age);
        $('#editEmail').val(email);
        // $('#editRoles').val(JSON.parse(roles)); //из JSON в массив
        $('#editRoles option').each(function() {
            const roleId = $(this).val();
            const roleName = $(this).text();
            if (roles.includes(roleName)) {
                $(this).prop('selected', true);
            }
        });
        // $('#editRoles option').each(function() {
        //     const roleId = parseInt($(this).val(), 10); // Преобразуем к числу
        //     $(this).prop('selected', roles.some(r => r.id === roleId));
        // });
    });

    $('#editUser').submit(function(event) {
        event.preventDefault();
        const formData = $(this).serialize();
        $.ajax({
            url: '/admin/edit',
            type: 'POST',
            data: formData,
            headers: {
                [csrfHeader]: csrfToken,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            contentType: 'application/json; charset=UTF-8', //Важно для передачи данных
            success: function(response) {
                $('#editModal').modal('hide');
                location.reload();
            },
            error: function(error) {
                console.error("Ошибка при редактировании пользователя:", error);
                alert("Ошибка редактирования пользователя. Пожалуйста, проверьте консоль для подробностей.");
            }
        });
    });

    $('#nav-users-table').on('click', '.btn-danger', function() {
        const userId = $(this).data('id');
        const firstName = $(this).data('firstname');
        const lastName = $(this).data('lastname');
        const age = $(this).data('age');
        const email = $(this).data('email');
        const roles = $(this).data('roles');

        $('#deleteId').val(userId);
        $('#deleteFirstName').val(firstName);
        $('#deleteLastName').val(lastName);
        $('#deleteAge').val(age);
        $('#deleteEmail').val(email);
    });

    $('#deleteUser').submit(function(event) {
        event.preventDefault();
        const userId = $('#deleteId').val();
        $.ajax({
            url: `/admin/remove/${userId}`,
            type: 'POST',
            headers: {
                [csrfHeader]: csrfToken
            },
            success: function(response) {
                $('#deleteModal').modal('hide');
                location.reload();
            }
        });
    });
});
