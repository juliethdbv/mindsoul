function cambiarEstadoEspecialidad(id) {
    let idEspecialidad = id;
    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
            confirmButton: 'btn btn-success',
            cancelButton: 'btn btn-danger'
        },
        buttonsStyling: true
    })

    swalWithBootstrapButtons.fire({
        title: 'Está seguro?',
        text: "No podrá revertir esta acción!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Si, cambiar estado!',
        cancelButtonText: 'No, cancelar!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            swalWithBootstrapButtons.fire(
                'Actualizado!',
                'El estado ha sido cambiado.',
                'success'
            ).then((result) => {
                window.location.href = `cambiarEstado/${idEspecialidad}`;
            })

        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Cancelado',
                'Tu estado no se cambió :)',
                'error'
            )
        }
    })
}