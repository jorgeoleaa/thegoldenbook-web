/**
 * JavaScript para el la jsp de profile
 */

window.addEventListener("load", function(){
	
	var editButton = document.getElementById("edit-button");
	document.getElementById("save-button").style.display = "none";
	document.getElementById("input-file").style.display = "none";
	var cancelButton = document.getElementById("cancel-button");
	cancelButton.style.display = "none";
	
	editButton.addEventListener("click", editDisplayButtons);
	cancelButton.addEventListener("click", cancelEdit);
});


function editDisplayButtons(){
	document.getElementById("save-button").style.display = "block";
	document.getElementById("input-file").style.display = "block";
	document.getElementById("cancel-button").style.display = "block";
	document.getElementById("edit-button").style.display = "none";
	
}

function cancelEdit(){
	document.getElementById("save-button").style.display = "none";
	document.getElementById("input-file").style.display = "none";
	document.getElementById("cancel-button").style.display = "none";
	document.getElementById("edit-button").style.display = "block";
}