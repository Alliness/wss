var app = {};

$(document).ready(function () {

    var socket = new Socket("ws://localhost:9696");
    var router = new Router(".container");
    app.socket = socket;
    app.router = router;

    socket.addHandler("connection/id", function (message) {
        app.socket = {};
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
        var that = $(this);
        router.load(that.attr("data-route"));
    });

});