var app = {};

$(document).ready(function () {

    let socket = new Socket("ws://localhost:9696");
    let router = new Router(".container");
    app.socket = socket;
    app.router = router;

    router.load("form");


    socket.addHandler("connection/id", function (message) {
        app.socket.uuid = message.uuid;
    });

    socket.addHandler("battle/connected", function (message) {
        app.player = {};
        app.player.info = message.player;
    });

    socket.addHandler("battle/state", function (message) {
        $(".state").text(message.state);
    });

    $(".nav-btn").click(function () {
        let that = $(this);
        router.load(that.attr("data-route"));
    });

});