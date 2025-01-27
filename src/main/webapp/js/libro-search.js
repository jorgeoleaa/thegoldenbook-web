$(document).ready(function(){
    $("#titulo").keyup(function(){
        updateSearchResults();
    });

    $("input[name='formato']").change(function(){
        updateSearchResults();
    });

    $("input[name='genero']").change(function(){
        updateSearchResults();
    });

    $("input[name='idioma']").change(function(){
        updateSearchResults();
    });

    $("input[name='edad']").change(function(){
        updateSearchResults();
    });
});

function updateSearchResults(page = 1){
    var locale = $("input[name='locale']").val();
    var titulo = $("#titulo").val();
    var formatoId = $("input[name='formato']:checked").val() || '';
    var generoId = $("input[name='genero']:checked").val() || '';
    var idiomaId = $("input[name='idioma']:checked").val() || '';
    var clasificacionEdadId = $("input[name='edad']:checked").val() || '';
    
    $.ajax({
        type: "GET",
        url: "/TheGoldenBookWebServices/LibroServletWS",
        data: {
            'locale': locale, 
            'titulo': titulo,
            'formatoId': formatoId,
            'generoId': generoId,
            'idiomaId': idiomaId,
            'clasificacionEdadId': clasificacionEdadId,
            'page': page
        },
        dataType: "json",
        success: function(response) {
            console.log("Resultados obtenidos:", response.results);
            if (!response.results || response.results.length === 0) {
                $("#results").html("<p>No se encontraron resultados.</p>");
                return;
            }

            var htmlResultado = "<table class='book-table' style='width: 100%; border-collapse: collapse; text-align: center;'>";
            htmlResultado += "<tbody>";

            for (var i = 0; i < response.results.length; i++) {
                if (i % 4 === 0) {
                    htmlResultado += "<tr>";
                }
                htmlResultado += 
                "<td style='padding: 10px;'>" +
                "<img src='/thegoldenbook/ImageServlet?action=image-libro&libroId=" + response.results[i].id + "&imageNombre=g1.jpg' " +
                "alt='" + response.results[i].nombre + "' style='width: 125px; height: 175px; display: block; margin: 0 auto;'>" +
                "<br>" +
                "<a href='/thegoldenbook/public/LibroServlet?action=libro-detail&libroId=" + response.results[i].id + "'>" +
                response.results[i].nombre +
                "</a>" +
                "</td>";

                if (i % 4 === 3 || i === response.results.length - 1) {
                    htmlResultado += "</tr>";
                }
            }

            htmlResultado += "</tbody></table>";

            $("#results").html(htmlResultado);

            var paginationHtml = "<div class='pagination'>";

            if (response.currentPage > 1) {
                paginationHtml += "<button onclick='updateSearchResults(" + (response.currentPage - 1) + ")'>Previous</button>";
            }

            if (response.currentPage < response.lastPage) {
                paginationHtml += "<button onclick='updateSearchResults(" + (response.currentPage + 1) + ")'>Next</button>";
            }

            paginationHtml += "</div>";
            $("#pagination").html(paginationHtml);
        },
        error: function(xhr, status, error) {
            console.error("Error en la solicitud:", error);
            $("#results").html("<p>Error al cargar los datos.</p>");
        }
    });
}
