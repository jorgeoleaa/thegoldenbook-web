/**
 * Lógica de apertura y cierre del pop up para creación de valoraciones
 */

window.addEventListener("DOMContentLoaded", function(){
	
	var openButton = document.getElementById("openCreateValoracion");
	var dialog = document.getElementById("createValoracion");
	var cancelButton = document.getElementById("cancelButton");
	
	openButton.addEventListener("click", function(){
		dialog.showModal();
	});
	
	cancelButton.addEventListener("click", function(){
		dialog.close();
	});
	
});