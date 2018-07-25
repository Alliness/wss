var playerActions = $("button[attr-player-action]");
var gameContent = $("#game-content");

playerActions.click(function(){
	var btn = $(this);
	var action = btn.attr("attr-player-action");
	switch(action){
		case "info":renderPlayerInfo();
	}
})




function renderPlayerInfo(){
	var info = app.game.player;
	gameContent.empty();

	for(var key in info){
		if(info.hasOwnProperty(key)){
			var p = $("<p>");
			var name = $("<span>");
			var value = $("<span>");
			p.addClass("player-information");
			name.addClass("player-param");
			name.text(key);
			value.addClass("player-param-val");
			value.text(info[key]);
			p.append(name, value);
			gameContent.append(p);
		}
	}
}
