$(document).ready(function () {

    $(".nav-btn").click(function () {

        var that = $(this);
        $(".container").load(that.attr("data-route"));
    });



});