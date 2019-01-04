var hashHistory = [];
var pending = {};
var nextPendingKey = 0;

var startTime = (new Date()).getTime();
hashHistory.push([(new Date()).getTime(), window.location.hash, "loading " + window.location])

window.addEventListener("hashchange", _ => {
    // console.log("The hash changed to " + window.location.hash)
    var newHash = window.location.hash;
    var t = (new Date()).getTime();
    hashHistory.push([t, window.location.hash, t - startTime])
}, false);

var publish = function publish(){
    if (hashHistory.length > 0){
        var key = (nextPendingKey++).toString();
        pending[key] = hashHistory.map(JSON.stringify).join("\r\n") + "\r\n";
        hashHistory = [];
    }
};
var publishAll; // forward declare
var publishPendingServer = function publishPendingServer(key) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState != 4) return;

        if (this.status == 200) {
            console.log("success " + key);
            delete pending[key];
            publishAll();
        } else {
            console.log("failure " + key + " status: " + this.status);
            setTimeout(publishAll, 10000);
        }
    };
    console.log("publishing pending:" + key)
    xhr.open("POST", "/API/history", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(pending[key]);
}
var publishPendingConsole = function publishPendingConsole(key) {
    console.log(pending[key])
    delete pending[key];
    publishAll();
}
var publishPending = window.location.port ? publishPendingServer : publishPendingConsole;
var publishAll = function(){
    // Indirectly recursive.
    for (var key in pending) {
        return publishPending(key);
    }
};

var startPublishing = function startPublishing(interval){
    interval = interval ? interval : 10000;
    setInterval(
        function pub(){
            publish();
            publishAll()},
        interval);
};
