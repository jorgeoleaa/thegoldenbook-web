/**
 * 
 */

window.addEventListener("DOMContentLoaded", function () {
    document.getElementById("lineas-container").addEventListener("input", function (event) {
        if (event.target.classList.contains("spinner")) {
            var spinner = event.target;
            var pedidoId = spinner.getAttribute("pedidoId");
            var lineaPedidoId = spinner.getAttribute("lineaId");
            var newUnidades = spinner.value;
            var action = "updateFromSpinner";

            updateUnidades(pedidoId, lineaPedidoId, newUnidades, action);
        }
    });
});




function updateUnidades(pedidoId, lineaPedidoId, newUnidades, action) {
    
    var xhr = new XMLHttpRequest();

    xhr.open("POST", "/TheGoldenBook/private/PedidoServlet", true); 
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest"); 

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) { 
            if (xhr.status === 200) { 
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    console.log("Linea de pedido actualizada con éxito");
                    
                    document.querySelector("#header-pedido-data h2").innerText = 
                        `Precio total: ${response.totalPrice}€`;
						
					 var spinner = document.getElementsByClassName("spinner");
					 for(let i = 0; i<spinner.length; i++){
						spinner[i].setAttribute("lineaId", parseInt(lineaPedidoId)+spinner.length);
					 }
                }
            } else {
                console.log("Error al actualizar la línea de pedido");
            }
        }
    };

   
    var data = 
        "pedidoId=" + encodeURIComponent(pedidoId) +
        "&lineaId=" + encodeURIComponent(lineaPedidoId) +
        "&newUnidades=" + encodeURIComponent(newUnidades) +
        "&action=" + encodeURIComponent(action);

    xhr.send(data);
}
