$(document).ready(function () {

    setGameData();

    setPlayerInfo();
    var name = $("#name");

    /**
     * fixme : now playerClass && playerRace is mandatory params for create new player send them to backend.
     * fixme : so require to set playerClass and playerRace from app.game.data, add selection view for those params
     */
    $("#create").click(function () {

        $.ajax({
            url: "/game/player/new",
            data: JSON.stringify({"name": name.val(), "playerClass": "", "playerRace":""}),
            dataType: "JSON",
            method: "POST",
            success: function (resp) {
                //todo handler success state
                if (resp.success) {

                } else {

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
                if (resp.success) {

                } else {

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
                if (resp.success) {
                    app.router.load("battle");
                } else {
                    //todo handle success=false
                }
            }
        })

    });

    /**
     * collect game data from backend (player classes, player races)
     * stored game data to global var "app"
     */
    function setGameData() {
        app.game = {};
        $.getJSON("/game/resources/data", function (data) {
            if (data.success) {
                app.game.data = data;

            }
        })
    }

    /**
     * render player (currently only images)info to .form_list
     * stored availablePlayers to global var "app"
     *
     */
    function setPlayerInfo() {
        var list = $(".form__list");
        $.getJSON('/game/player/available', function (data) {

            app.game.availablePlayers = data.players;

            if (data.success) {
                list.empty();
                $.each(data.players, function (k, v) {

                    list.append(
                        "<li class='form__item'>" +
                        "<button class='js-player-select' attr-player='" + k + "'><img src='/assets/img/" + v.icon + "' alt='" + v.icon + "'></button>" +
                        "</li>"
                    )
                });
                $(".js-player-select").click(listenSelect);
            }
        });
    }

    /**
     * dirty trick.
     * onclick listener for available Players icons
     * shown selected player json data.
     */
    function listenSelect() {
        var that = $(this);
        var order = parseInt(that.attr("attr-player"));

        var selectedPlayer = app.game.availablePlayers[order];
        console.log(selectedPlayer)
    }


});
