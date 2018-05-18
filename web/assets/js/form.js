$(document).ready(function () {

    var name = $("#name");

    $("#create").click(function () {

        $.ajax({
            url: "/game/player/new",
            data: JSON.stringify({"name": name.val()}),
            dataType: "JSON",
            method: "POST",
            success: function (resp) {
                //todo handler success state
                if(resp.success){

                }else{

                }
            }
        })
    });

    /**
     * FOR TESTS ONLY
     */
    $("#disconnect").click(function () {

        $.ajax({
            url: "/game/player/disconnect",
            data: JSON.stringify({"name": name.val()}),
            dataType: "JSON",
            method: "POST",
            success: function (resp) {
                //todo handler success state
                if(resp.success){

                }else{

                }
            }
        })
    });


    $("#connect").click(function () {

        $.ajax({
            url: "/game/player/connect",
            data: JSON.stringify({"name": name.val(), "uuid": app.socket.uuid}),
            dataType: "JSON",
            method: "POST",
            success: function (resp) {
                if(resp.success){

                }else{
                    //todo handle success=false
                }
            }
        })

    })

});
