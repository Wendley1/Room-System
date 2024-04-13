let userCount = document.getElementById("user-count");

let socket = new SockJS('/ws');
let stompClient = Stomp.over(socket);

let roomCode = document.getElementById("room-code").value;

stompClient.connect({}, function (frame) {
    console.log('Conectado ao WebSocket: ' + frame);

    stompClient.subscribe(('/topic/' + roomCode), function (response) {
        console.log(response);
        userCount.innerHTML = JSON.parse(response.body);
    });
});