function Router(container){
    this.container = $(container);
}

Router.prototype.load = function(page){
    this.container.load("/page/"+page+".html");
}

