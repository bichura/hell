$(document).ready(function () {

    let csrfToken = $('meta[name="_csrf"]').attr('content');
    let csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    $.ajax({
        url: '/admin/roles',
        type: 'GET',
        dataType: 'json',
        success: function (roles) {
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

    $('#nav-users-table').on('click', '.btn-info', function () {
        const id = $(this).data('id');
        const email = $(this).data('email');
        const firstName = $(this).data('firstname');
        const lastName = $(this).data('lastname');
        const age = $(this).data('age');
        const roles = $(this).data('roles');

        $('#editId').val(id);
        $('#editEmail').val(email);
        $('#editFirstName').val(firstName);
        $('#editLastName').val(lastName);
        $('#editAge').val(age);

        roles.forEach(role => {
            $(`#editRoles option[value="${role.id}"]`).prop('selected', true);
        });
    });

    $('#editUser').submit(function (event) {
        event.preventDefault();
        const userData = {
            id: $('#editId').val(),
            email: $('#editEmail').val(),
            password: $('#editPassword').val(),
            firstName: $('#editFirstName').val(),
            lastName: $('#editLastName').val(),
            age: $('#editAge').val(),
            roles: $('#editRoles').val()
        };

        $.ajax({
            url: '/admin/edit',
            type: 'POST',
            data: JSON.stringify(userData),
            headers: {
                [csrfHeader]: csrfToken,
                'Accept': 'application/json',
                'Content-Type': 'application/json; charset=UTF-8'
            },
            success: function (response) {
                $('#editModal').modal('hide');
                location.reload();
            }
        });
    });

    $('#nav-users-table').on('click', '.btn-danger', function () {
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
        $('#deleteRoles option').each(function () {
            const roleId = $(this).val();
            const roleName = $(this).text();
            if (roles.includes(roleName)) {
                $(this).prop('selected', true);
            }
        });
    });

    $('#deleteUser').submit(function (event) {
        event.preventDefault();
        const userId = $('#deleteId').val();
        $.ajax({
            url: `/admin/remove/${userId}`,
            type: 'POST',
            headers: {
                [csrfHeader]: csrfToken
            },
            success: function (response) {
                $('#deleteModal').modal('hide');
                location.reload();
            }
        });
    });
});