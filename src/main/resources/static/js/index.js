$(document).ready(function () {
    document.getElementById("currentYear").textContent = new Date().getFullYear();
    const BASE_URL = '/veiculos/';

    $('#filters-form').on('submit', function (e) {
        e.preventDefault();

        const filtros = {
            tipo: $('#filter-tipo').val(),
            modelo: $('#filter-modelo').val(),
            cor: $('#filter-cor').val(),
            ano: $('#filter-ano').val(),
            fabricante: $('#filter-fabricante').val()
        };

        $.ajax({
            url: '/buscar',
            type: 'GET',
            data: filtros,
            success: function (veiculos) {
                const tbody = $('#vehicles-tbody');
                tbody.empty();

                if (veiculos.length === 0) {
                    tbody.append('<tr><td colspan="10" class="text-center text-muted">Nenhum veículo encontrado.</td></tr>');
                    $('#success-message').text('Busca concluída, nenhum veículo encontrado.');
                }

                veiculos.forEach(v => {
                    tbody.append(`
                        <tr>
                            <td class="text-center">
                                <span class="badge ${v.tipoVeiculo === 'CARRO' ? 'bg-primary' : 'bg-success'}">
                                    <i class="bi ${v.tipoVeiculo === 'CARRO' ? 'bi-car-front' : 'bi-bicycle'}"></i> ${v.tipoVeiculo}
                                </span>
                            </td>
                            <td class="text-center">${v.modelo}</td>
                            <td class="text-center">${v.fabricante}</td>
                            <td class="text-center">${v.ano}</td>
                            <td class="text-center">${v.cor}</td>
                            <td class="text-center">R$ ${v.preco.toFixed(2).replace('.', ',')}</td>
                            <td class="text-center">${v.tipoVeiculo === 'CARRO' ? v.quantidadePortas : '-'}</td>
                            <td class="text-center">${v.tipoVeiculo === 'CARRO' ? v.tipoCombustivel : '-'}</td>
                            <td class="text-center">${v.tipoVeiculo === 'MOTO' ? v.cilindrada + 'cc' : '-'}</td>
                            <td class="actions-cell text-center">
                                <button type="button" data-id="${v.id}" class="btn btn-primary btn-sm me-1 btn-visualizar" title="Visualizar">
                                    <i class="bi bi-eye"></i>
                                </button>
                                <button type="button" data-id="${v.id}" class="btn btn-warning btn-sm me-1 btn-editar" title="Editar">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button type="button" data-id="${v.id}" class="btn btn-danger btn-sm btn-excluir" title="Excluir">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `);
                });
            },
            error: function (xhr) {
                const message = xhr.responseJSON?.mensagem || 'Não foi possível buscar os veículos.';
                $('#error-message').text(message);
                $('#alert-error').removeClass('d-none');
                setTimeout(() => $('#alert-error').addClass('d-none'), 4000);
            }
        });
    });

    function toggleCamposModal() {
        const tipo = $('.novo-veiculo[name="tipoVeiculo"]').val();
        if (tipo === "MOTO") {
            $('.grupo-portas, .grupo-combustivel').hide();
            $('.grupo-cilindrada').show();
        } else if (tipo === "CARRO") {
            $('.grupo-portas, .grupo-combustivel').show();
            $('.grupo-cilindrada').hide();
        } else {
            $('.grupo-portas, .grupo-combustivel, .grupo-cilindrada').show();
        }
    }

    $('.novo-veiculo[name="tipoVeiculo"]').on('change', toggleCamposModal);

    $('#modalNovoVeiculo').on('shown.bs.modal', toggleCamposModal);

    $('#btn-limpar-filtros').on('click', function () {
        $('#filters-form')[0].reset();
    });

    $('#btn-refresh').on('click', function () {
        $('#filters-form').trigger('submit');
    });

    const modalNovo = new bootstrap.Modal(document.getElementById('modalNovoVeiculo'));

    $('#btn-novo-veiculo').click(function () {
        $('#form-novo-veiculo')[0].reset();
        modalNovo.show();
    });

    $('#form-novo-veiculo').submit(function (e) {
        e.preventDefault();

        const veiculo = {};
        $(this).serializeArray().forEach(({ name, value }) => {
            if (value) veiculo[name] = isNaN(value) ? value : Number(value);
        });

        $.ajax({
            url: '/veiculos',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(veiculo),
            success: function () {
                modalNovo.hide();
                $('#success-message').text('Veículo cadastrado com sucesso!');
                $('#alert-success').removeClass('d-none');
                setTimeout(() => $('#alert-success').addClass('d-none'), 4000);
                $('#btn-refresh').click();
            },
            error: function (xhr) {
                const msg = xhr.responseJSON?.message || 'Não foi possível cadastrar o veículo.';
                $('#error-message-modal').text(msg);
                $('#alert-error-modal').removeClass('d-none');
                setTimeout(() => $('#alert-error-modal').addClass('d-none'), 4000);
            }
        });
    });

    $(document).on('click', '.btn-editar', function (e) {
        e.preventDefault();
        const url = BASE_URL + $(this).attr('data-id');

        $.get(url, function (data) {
            $('#editar-id-veiculo').val(data.id);
            $('#editar-tipoVeiculo').val(data.tipoVeiculo);
            $('#editar-modelo').val(data.modelo);
            $('#editar-fabricante').val(data.fabricante);
            $('#editar-ano').val(data.ano);
            $('#editar-cor').val(data.cor);
            $('#editar-preco').val(data.preco);
            if (data.tipoVeiculo === 'CARRO') {
                $('#editar-portas').val(data.quantidadePortas);
                $('#editar-combustivel').val(data.tipoCombustivel);
                $('#editar-cilindrada').val('');
                $('.grupo-cilindrada').hide();
                $('.grupo-portas, .grupo-combustivel').show();
            } else {
                $('#editar-cilindrada').val(data.cilindrada);
                $('#editar-portas').val('');
                $('#editar-combustivel').val('');
                $('.grupo-cilindrada').show();
                $('.grupo-portas, .grupo-combustivel').hide();
            }

            $('#modalEditarVeiculo').modal('show');
        }).fail(function () {
            $('#error-message').text('Não foi possível carregar os dados do veículo.');
            $('#alert-error').removeClass('d-none');
            setTimeout(() => $('#alert-error').addClass('d-none'), 4000);
        });
    });

    $('#form-editar-veiculo').on('submit', function (e) {
        e.preventDefault();
        const veiculo = {
            modelo: $('#editar-modelo').val(),
            fabricante: $('#editar-fabricante').val(),
            ano: $('#editar-ano').val(),
            cor: $('#editar-cor').val(),
            preco: $('#editar-preco').val(),
            tipoVeiculo: $('#editar-tipoVeiculo').val(),
            quantidadePortas: $('#editar-portas').val(),
            tipoCombustivel: $('#editar-combustivel').val(),
            cilindrada: $('#editar-cilindrada').val()
        };

        const id = $('#editar-id-veiculo').val();

        $.ajax({
            url: BASE_URL + id,
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(veiculo),
            success: function (data) {
                $('#modalEditarVeiculo').modal('hide');
                $('#success-message').text("Veículo atualizado com sucesso!");
                $('#alert-success').removeClass('d-none');
                setTimeout(() => $('#alert-success').addClass('d-none'), 4000);
                $('#btn-refresh').click();
            },
            error: function (xhr) {
                const msg = xhr.responseJSON?.message || 'Não foi possível atualizar o veículo.';
                $('#modalEditarVeiculo .alert-danger').removeClass('d-none').text(msg);
                setTimeout(() => $('#modalEditarVeiculo .alert-danger').addClass('d-none'), 4000);
            }
        });
    });

    $(document).on('click', '.btn-visualizar', function (e) {
        e.preventDefault();
        const url = BASE_URL + $(this).attr('data-id');

        $.get(url, function (data) {
            $('#detalhe-tipoVeiculo').val(data.tipoVeiculo);
            $('#detalhe-modelo').val(data.modelo);
            $('#detalhe-fabricante').val(data.fabricante);
            $('#detalhe-ano').val(data.ano);
            $('#detalhe-cor').val(data.cor);
            $('#detalhe-preco').val(`R$ ${parseFloat(data.preco).toFixed(2).replace('.', ',')}`);
            $('#detalhe-createdAt').val(new Date(data.createdAt).toLocaleString('pt-BR'));
            $('#detalhe-updatedAt').val(new Date(data.updatedAt).toLocaleString('pt-BR'));
            if (data.tipoVeiculo === 'CARRO') {
                $('#detalhe-portas').val(data.quantidadePortas ?? '-');
                $('#detalhe-combustivel').val(data.tipoCombustivel);
                $('.grupo-cilindrada').hide();
                $('.grupo-portas, .grupo-combustivel').show();
            } else if (data.tipoVeiculo === 'MOTO') {
                $('#detalhe-cilindrada').val(data.cilindrada ? `${data.cilindrada}cc` : '-');
                $('.grupo-cilindrada').show();
                $('.grupo-portas, .grupo-combustivel').hide();
            }

            const modal = new bootstrap.Modal(document.getElementById('modalDetalhesVeiculo'));
            modal.show();
        }).fail(function () {
            $('#error-message').text('Não foi possível carregar os detalhes do veículo.');
            $('#alert-error').removeClass('d-none');
            setTimeout(() => $('#alert-error').addClass('d-none'), 4000);
        });
    });

    $(document).on('click', '.btn-excluir', function (e) {
        e.preventDefault();
        const url = BASE_URL + $(this).attr('data-id');

        if (!confirm('Tem certeza que deseja excluir este veículo?')) {
            return;
        }

        $.ajax({
            url: url,
            type: 'DELETE',
            success: function () {
                $('#success-message').text('Veículo excluído com sucesso!');
                $('#alert-success').removeClass('d-none');
                setTimeout(() => $('#alert-success').addClass('d-none'), 4000);
                $('#btn-refresh').click();
            },
            error: function (xhr) {
                const message = xhr.responseJSON?.mensagem || 'Não foi possível excluir o veículo.';
                $('#error-message').text(message);
                $('#alert-error').removeClass('d-none');
                setTimeout(() => $('#alert-error').addClass('d-none'), 4000);
            }
        });
    });

});
