$(document).ready(function () {

    var socket = new Socket("ws://10.65.146.148:9696");

    socket.addHandler("connection/id", function (message) {
        console.log(message);
    });

    $(".nav-btn").click(function () {
        var that = $(this);
        $(".container").load(that.attr("data-route"));
    });

});