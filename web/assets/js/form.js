$(document).ready(function () {


    $("#create").click(function () {

        var name = $("#name");

        $.ajax({
            url: "/game/player/new",
            data: JSON.stringify({"name" : name.val()}),
            dataType: "JSON",
            method: "POST",
            success: function (resp) {
                console.log(resp);
            }
        })
    });

    $("#connect").click(function () {

    })

});
