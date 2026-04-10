/**
 * Ordenación de tablas admin - SoleXclusive
 * Hace click en las cabeceras <th class="sortable"> para ordenar ASC/DESC.
 */
document.addEventListener('DOMContentLoaded', function () {

    document.querySelectorAll('.admin-table').forEach(function (table) {

        var headers = table.querySelectorAll('thead th.sortable');

        headers.forEach(function (th) {
            th.addEventListener('click', function () {
                var colIndex = parseInt(th.getAttribute('data-col'));
                var tbody = table.querySelector('tbody');
                var rows = Array.from(tbody.querySelectorAll('tr'));
                var currentDir = th.getAttribute('data-dir') || 'none';
                var newDir = (currentDir === 'asc') ? 'desc' : 'asc';

                // Reset all headers
                headers.forEach(function (h) {
                    h.setAttribute('data-dir', 'none');
                    h.classList.remove('sort-asc', 'sort-desc');
                });

                th.setAttribute('data-dir', newDir);
                th.classList.add(newDir === 'asc' ? 'sort-asc' : 'sort-desc');

                rows.sort(function (a, b) {
                    var cellA = a.querySelectorAll('td')[colIndex];
                    var cellB = b.querySelectorAll('td')[colIndex];

                    if (!cellA || !cellB) return 0;

                    var valA = (cellA.querySelector('span') || cellA).textContent.trim();
                    var valB = (cellB.querySelector('span') || cellB).textContent.trim();

                    // Try date format dd/MM/yyyy HH:mm
                    var dateRegex = /^(\d{2})\/(\d{2})\/(\d{4})\s+(\d{2}):(\d{2})$/;
                    var matchA = valA.match(dateRegex);
                    var matchB = valB.match(dateRegex);

                    var result;
                    if (matchA && matchB) {
                        var dateA = new Date(matchA[3], matchA[2] - 1, matchA[1], matchA[4], matchA[5]);
                        var dateB = new Date(matchB[3], matchB[2] - 1, matchB[1], matchB[4], matchB[5]);
                        result = dateA - dateB;
                    } else {
                        // Remove currency symbol for numeric compare
                        var numA = parseFloat(valA.replace(/[€$,]/g, ''));
                        var numB = parseFloat(valB.replace(/[€$,]/g, ''));

                        if (!isNaN(numA) && !isNaN(numB)) {
                            result = numA - numB;
                        } else {
                            result = valA.localeCompare(valB, 'es', { sensitivity: 'base' });
                        }
                    }

                    return newDir === 'asc' ? result : -result;
                });

                rows.forEach(function (row) {
                    tbody.appendChild(row);
                });
            });
        });
    });
});

