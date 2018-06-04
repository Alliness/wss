$(document).ready(function () {

    setGameData();

    setPlayerInfo();
    var name = $("#name"),
        playerClass = $("#playerClass"),
        playerRace = $("#playerRace");

    $("#create").click(function () {

        $.ajax({
            url: "/game/player/new",
            data: JSON.stringify({
                "name": name.val(),
                "playerClass": playerClass.find(":selected").text(),
                "playerRace": playerRace.find(":selected").text()
            }),
            dataType: "JSON",
            method: "POST",
            success: function (resp) {
                //todo handler success state
                if (resp.success) {
                    setPlayerInfo();
                } else {

                }
            }
        });
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

                setGameOption(data.classes, playerClass);
                setGameOption(data.races, playerRace);
            }
        });

        function setGameOption(data, element) {

            $.each(data, function (k, v) {

                let option = $("<option>");

                option.attr("value", k);
                option.text(v);

                element.append(option);

            })
        }
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

                    let li = $('<li>');
                    let divList = $('<div>');
                    let divName = $('<div>');
                    let divLvl = $('<div>');
                    let button = $('<button>');
                    let img = $("<img>");

                    li.addClass('form__item');

                    divList.addClass('pl');
                    divName.addClass('pl-name').text(v.name);
                    divLvl.addClass('pl-lvl').text(v.level);

                    img.attr({
                        src: '/assets/img/' + v.icon,
                        alt: ' '
                    });

                    button.addClass('js-player-select');
                    button.attr("player-order", k);

                    divList.append(divName).append(divLvl);
                    button.append(img);

                    li.append(divList).append(button);
                    list.append(li)

                });

                $(".js-player-select").on("click", listenSelect);

            }

        });
    }

    /**
     * dirty trick.
     * onclick listener for available Players icons
     * shown selected player json data.
     */
    function listenSelect() {
        let that = $(this);
        let order = parseInt(that.attr("player-order"));
        let selectedPlayer = app.game.availablePlayers[order];
        name.val(selectedPlayer.name);

        $.each($('.js-player-select'), function (k, v) {
            $(v).removeClass('is-active');
        });

        that.addClass('is-active');

        let playerInfo = $('.tab-panel__player-info');

        playerInfo.empty();

        for (let k in selectedPlayer) {
            if (selectedPlayer.hasOwnProperty(k)) {
                let v = selectedPlayer[k];

                let p = $('<p>').addClass('player-information');
                let playerParam = $('<span>').addClass('player-param').text(k);
                let playerParamVal = $('<span>').addClass('player-param-val').text(v);

                p.append(playerParam).append(playerParamVal);

                playerInfo.append(p);
            }
        }

        console.log(selectedPlayer)
    }

});
