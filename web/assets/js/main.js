var app ={};

$(document).ready(function () {

    var socket = new Socket("ws://10.65.146.148:9696");

    socket.addHandler("connection/id", function (message) {
        app.socket = {};
        app.socket.uuid = message.uuid;
    });


    socket.addHandler("battle/connected", function (message) {
        app.player = {};
        app.player.info = message.player;
    });

    $(".nav-btn").click(function () {
        var that = $(this);
        $(".container").load(that.attr("data-route"));
    });

});