let tabela = document.getElementById("table-body-data");
let userCount = document.getElementById("user-count");

let socket = new SockJS('/ws');
let stompClient = Stomp.over(socket);

let roomCode = document.getElementById("room-code").innerText;

stompClient.connect({}, function (frame) {
    console.log('Conectado ao WebSocket: ' + frame);

    stompClient.subscribe(('/topic/' + roomCode), function (response) {
        UpdateList();
    });
});

window.onbeforeunload = function () {
    onExit();
}

function UpdateList() {
    $.ajax({
        url: '/api/sala/'+ roomCode + '/populacao',
        type: 'GET',
        success: function (data) {
            tabela.innerHTML = '';
            for (let i = 0; i < data.length; i++) {
                createItemList(data[i].id, data[i].name);
            }
            
            userCount.innerHTML = data.length;
        },
        error: function () {
            console.log("erro");
        }
    })

}

function onExit() {
    $.ajax({
        url: '/sala/sair/' + roomCode.toString(),
        type: 'POST',
        contentType: 'text/plain',
        data: document.getElementById("user-id").value,
        success: function (){
            window.location.href = ('/');
        },
        error: function () {
            console.log("erro");
        }
    });
}

function createItemList(id, name) {
    let row = tabela.insertRow();

    row.insertCell(0).innerHTML = id;
    row.insertCell(1).innerHTML = name;
}

UpdateList();