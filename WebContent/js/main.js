// The root URL for the RESTful services
var rootURL = "http://localhost:8080/smartirrigation/rest/agendas";
var estadoAtual = true;
var currentAgenda;

// Retrieve agenda list when application starts 
findAll();

// Nothing to delete in initial application state
$('#btnDelete').hide();

// Register listeners
$('#btnLogin').click(function() {
	login();
	return false;
});

$('#btnSearch').click(function() {
	search($('#searchKey').val());
	return false;
});

$('#btnOnOff').click(function() {
	mudaEstado();
	return false;
});

$('#btnAdd').click(function() {
	newAgenda();
	return false;
});

$('#btnSave').click(function() {
	if ($('#agendaId').val() == '')
		addAgenda();
	else
		updateAgenda();
	return false;
});

$('#btnDelete').click(function() {
	deleteAgenda();
	return false;
});

$('#agendaList a').live('click', function() {
	findById($(this).data('identity'));
});

// Replace broken images with generic agenda bottle
$("img").error(function(){
  $(this).attr("src", "pics/generic.jpg");

});

function mudaEstado() {
	console.log('updateAgenda');
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: rootURL + '/' + (!estadoAtual),
		dataType: "json",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			alert('Estado das agendas atualizados com sucesso');
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('Ops! Falha na atualizacao: ' + textStatus);
		}
	});
	findAll();
	limparTela();
}

function search(searchKey) {
	if (searchKey == '') 
		findAll();
	else
		findByName(searchKey);
}

function login() {
	console.log('login');
	$.ajax({
		type: 'GET',
		url: rootURL,
		dataType: "json", // data type of response
		success: renderList
	});
}

function newAgenda() {
	$('#btnDelete').hide();
	currentAgenda = {};
	renderDetails(currentAgenda); // Display empty form
}

function findAll() {
	console.log('findAll');
	$.ajax({
		type: 'GET',
		url: rootURL,
		dataType: "json", // data type of response
		success: renderList
	});
}

function findByName(searchKey) {
	console.log('findByName: ' + searchKey);
	$.ajax({
		type: 'GET',
		url: rootURL + '/search/' + searchKey,
		dataType: "json",
		success: renderList 
	});
}

function findById(id) {
	console.log('findById: ' + id);
	$.ajax({
		type: 'GET',
		url: rootURL + '/' + id,
		dataType: "json",
		success: function(data){
			$('#btnDelete').show();
			currentAgenda = data;
			renderDetails(currentAgenda);
		}
	});
}


function addAgenda() {
	console.log('addAgenda');
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: rootURL,
		dataType: "json",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			$('#btnDelete').show();
//			$('#agendaId').val(data.id);
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('Ops! Agendamento nao cadastrado! Detalhes: ' + textStatus);
		}
	});
	findAll();
	limparTela();
}

function updateAgenda() {
	console.log('updateAgenda');
	$.ajax({
		type: 'PUT',
		contentType: 'application/json',
		url: rootURL + '/' + $('#agendaId').val(),
		dataType: "json",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			alert('Agenda atualizada com sucesso');
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('Ops! Falha na atualizacao: ' + textStatus);
		}
	});
	findAll();
	limparTela();
}

function deleteAgenda() {
	console.log('deleteAgenda');
	$.ajax({
		type: 'DELETE',
		url: rootURL + '/' + $('#agendaId').val(),
		success: function(data, textStatus, jqXHR){
			alert('Agenda apagada com sucesso');
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('Ops! Erro ao apagar!');
		}
	});
	findAll();
	limparTela();
}

function renderList(data) {
	var list = data == null ? [] : (data instanceof Array ? data : [data]);
	$('#agendaList li').remove();
	var estadoAux = false;
	$.each(list, function(index, agenda) {
		var setores, repeticoes = ''; 
		$.each(agenda, function(index, setor) {
			setores = '';
			$.each(setor, function(index, s) {
			  setores += ' ' + s.descricao;
			});
		});
		(agenda.seg) ? repeticoes += ' seg': '';
		(agenda.ter) ? repeticoes += ' ter': '';
		(agenda.qua) ? repeticoes += ' qua': '';
		(agenda.qui) ? repeticoes += ' qui': '';
		(agenda.sex) ? repeticoes += ' sex': '';
		(agenda.sab) ? repeticoes += ' sab': '';
		(agenda.dom) ? repeticoes += ' dom': '';
		$('#agendaList').append('<li><a href="#" data-identity="' + agenda.id + '">' + agenda.id + ') Estado: ' + agenda.estado + ' Inicio: ' + agenda.horaInicial + ' Fim: ' + agenda.horaFinal + ' Setores:' + setores +' Repeticoes:' + repeticoes +'</a></li>');
		//configura a variavel de estado para true somente se pelo menos 1 agendamento estiver true
		if(agenda.estado)
			estadoAux = true;
	});
	estadoAtual = estadoAux;
}

function limparTela(){
	limparCheck();
	$('#estado').attr("checked", false);
	$('#agendaId').val('');
	$('#horaInicial').val('');
	$('#horaFinal').val('');
	$('#btnDelete').hide();
}
function limparCheck(){
	$('#s1').attr("checked", false);
	$('#s2').attr("checked", false);
	$('#s3').attr("checked", false);
	$('#s4').attr("checked", false);

	$('#seg').attr("checked", false);
	$('#ter').attr("checked", false);
	$('#qua').attr("checked", false);
	$('#qui').attr("checked", false);
	$('#sex').attr("checked", false);
	$('#sab').attr("checked", false);
	$('#dom').attr("checked", false);

}

function renderDetails(agenda) {
	$('#agendaId').val(agenda.id);
	var setores, s1, s2, s3, s4; 
	limparCheck();
	$.each(agenda, function(index, setor) {
		setores = '';
		$.each(setor, function(index, s) {
			switch (s.id) {
			case 1:$('#s1').attr("checked", true);
			break;
			case 2:$('#s2').attr("checked", true);
			break;
			case 3:$('#s3').attr("checked", true);
			break;
			case 4:$('#s4').attr("checked", true);
			}
			setores += ' ' + s.descricao;
		});
	});
	
	$('#horaInicial').val(agenda.horaInicial);
	$('#horaFinal').val(agenda.horaFinal);
	(agenda.estado) ? $('#estado').attr("checked", true): $('#estado').attr("checked", false);

	(agenda.seg) ? $('#seg').attr("checked", true): $('#seg').attr("checked", false);
	(agenda.ter) ? $('#ter').attr("checked", true): $('#ter').attr("checked", false);
	(agenda.qua) ? $('#qua').attr("checked", true): $('#qua').attr("checked", false);
	(agenda.qui) ? $('#qui').attr("checked", true): $('#qui').attr("checked", false);
	(agenda.sex) ? $('#sex').attr("checked", true): $('#sax').attr("checked", false);
	(agenda.sab) ? $('#sab').attr("checked", true): $('#sab').attr("checked", false);
	(agenda.dom) ? $('#dom').attr("checked", true): $('#dom').attr("checked", false);

//	$('#seg').val(agenda.seg);
//	$('#ter').val(agenda.ter);
//	$('#qua').val(agenda.qua);
//	$('#qui').val(agenda.qui);
//	$('#sex').val(agenda.sex);
//	$('#sab').val(agenda.sab);
//	$('#dom').val(agenda.dom);
}

// Helper function to serialize all the form fields into a JSON string
function formToJSON() {
	var agendaId = $('#agendaId').val();
	
	var s1 = (($('#s1').is(":checked"))?'"id":1':'');
	var s2 = (($('#s2').is(":checked"))?'"id":2':'');
	var s3 = (($('#s3').is(":checked"))?'"id":3':'');
	var s4 = (($('#s4').is(":checked"))?'"id":4':'');
	
	var aux = '[{'+s1+'},{'+s2+'},{'+s3+'},{'+s4+'}]';
	var obj = jQuery.parseJSON(aux);

	return JSON.stringify({
		"id": agendaId == "" ? null : agendaId, 
		"estado": ($('#estado').is(":checked")),
		"horaInicial": $('#horaInicial').val(),
		"horaFinal": $('#horaFinal').val(),
		"setores": obj,
		"seg": ($('#seg').is(":checked")),
		"ter": ($('#ter').is(":checked")),
		"qua": ($('#qua').is(":checked")),
		"qui": ($('#qui').is(":checked")),
		"sex": ($('#sex').is(":checked")),
		"sab": ($('#sab').is(":checked")),
		"dom": ($('#dom').is(":checked"))
//		"seg": $('#seg').val(),
//		"ter": $('#ter').val(),
//		"qui": $('#qua').val(),
//		"qui": $('#qui').val(),
//		"sex": $('#sex').val(),
//		"sab": $('#sab').val(),
//		"dom": $('#dom').val()
		});
}
