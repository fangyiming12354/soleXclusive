/**
 * Paginación frontend de tablas admin - SoleXclusive
 * Muestra rowsPerPage filas por página en cada .admin-table
 * Compatible con sort_table.js (se re-pagina tras ordenar).
 */
(function () {
    var DEFAULT_ROWS = 7;

    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('.admin-table').forEach(initPagination);
    });

    function initPagination(table) {
        var tbody = table.querySelector('tbody');
        if (!tbody) return;

        var rowsPerPage = parseInt(table.getAttribute('data-page-size')) || DEFAULT_ROWS;

        // Crear contenedor de paginación después de la table-card
        var tableCard = table.closest('.table-card');
        if (!tableCard) return;

        var paginationNav = document.createElement('nav');
        paginationNav.className = 'pagination-nav';
        paginationNav.setAttribute('aria-label', 'Paginación');

        var paginationUl = document.createElement('ul');
        paginationUl.className = 'admin-pagination';
        paginationNav.appendChild(paginationUl);

        tableCard.parentNode.insertBefore(paginationNav, tableCard.nextSibling);

        var currentPage = 1;

        function getVisibleRows() {
            return Array.from(tbody.querySelectorAll('tr')).filter(function (row) {
                // Excluir filas de tabla vacía (las que tienen .empty-table)
                return !row.querySelector('.empty-table');
            });
        }

        function render() {
            var rows = getVisibleRows();
            var totalPages = Math.max(1, Math.ceil(rows.length / rowsPerPage));

            if (currentPage > totalPages) currentPage = totalPages;

            // Mostrar / ocultar filas
            rows.forEach(function (row, i) {
                var page = Math.floor(i / rowsPerPage) + 1;
                row.style.display = (page === currentPage) ? '' : 'none';
            });

            // Construir botones
            paginationUl.innerHTML = '';

            if (totalPages <= 1) {
                paginationNav.style.display = 'none';
                return;
            }
            paginationNav.style.display = '';

            // Botón anterior
            var prevLi = createPageItem('«', currentPage > 1, function () {
                currentPage--;
                render();
            });
            prevLi.classList.add('page-arrow');
            paginationUl.appendChild(prevLi);

            // Números de página
            var startPage = Math.max(1, currentPage - 2);
            var endPage = Math.min(totalPages, startPage + 4);
            if (endPage - startPage < 4) startPage = Math.max(1, endPage - 4);

            for (var p = startPage; p <= endPage; p++) {
                (function (pageNum) {
                    var li = createPageItem(pageNum, true, function () {
                        currentPage = pageNum;
                        render();
                    });
                    if (pageNum === currentPage) li.classList.add('active');
                    paginationUl.appendChild(li);
                })(p);
            }

            // Botón siguiente
            var nextLi = createPageItem('»', currentPage < totalPages, function () {
                currentPage++;
                render();
            });
            nextLi.classList.add('page-arrow');
            paginationUl.appendChild(nextLi);

            // Info de página
            var info = paginationNav.querySelector('.page-info');
            if (!info) {
                info = document.createElement('span');
                info.className = 'page-info';
                paginationNav.appendChild(info);
            }
            var from = (currentPage - 1) * rowsPerPage + 1;
            var to = Math.min(currentPage * rowsPerPage, rows.length);
            info.textContent = from + ' - ' + to + ' de ' + rows.length;
        }

        function createPageItem(text, enabled, onClick) {
            var li = document.createElement('li');
            li.className = 'page-item' + (enabled ? '' : ' disabled');

            var btn = document.createElement('button');
            btn.className = 'page-link';
            btn.textContent = text;
            btn.type = 'button';
            if (enabled) {
                btn.addEventListener('click', onClick);
            }
            li.appendChild(btn);
            return li;
        }

        // Render inicial
        render();

        // Observar cambios en el tbody (cuando sort_table.js reordena filas)
        var observer = new MutationObserver(function () {
            render();
        });
        observer.observe(tbody, { childList: true });
    }
})();

