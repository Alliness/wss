var app = {};

$(document).ready(function () {

    let socket = new Socket("ws://localhost:9696");
    let router = new Router(".container");
    app.socket = socket;
    app.router = router;

    socket.addHandler("connection/id", function (message) {
        app.socket.uuid = message.uuid;
    })

    socket.addHandler("player/disconnect", function (message) {
        document.location.href="/"
    })

    $(".nav-btn").click(function () {
        let that = $(this);
        router.load(that.attr("data-route"));
    });

});