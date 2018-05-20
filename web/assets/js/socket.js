function Socket(url) {

    this.socket = new WebSocket(url);

    this.lastSendedMessage = {};
    this.lastReceivedMessage = {};
    this.handlers = [];

    var that = this;


    this.socket.onopen = init();


    function init() {
        that.socket.onmessage = function (msg) {
            if(msg.data === "1"){
                that.socket.send("2");
            }else{
                that.handlers.forEach(function (handler) {
                    that.lastReceivedMessage = JSON.parse(msg.data);
                    if (handler.action === that.lastReceivedMessage.action) {
                        handler.method(that.lastReceivedMessage.data)
                    }
                });
            }
        };

        that.socket.onerror = function (e, r) {
            console.log(e, r)
        };

        that.socket.onclose = function () {
            that.socket.close();
        }
    }

}


Socket.prototype.send = function (action, message) {

    var msg = {
        "action": action,
        "data": message
    };

    this.lastSendedMessage = msg;
    this.socket.send(JSON.stringify(msg));

};

Socket.prototype.addHandler = function (action, method) {

    this.handlers.push({"action": action, "method": method});
};