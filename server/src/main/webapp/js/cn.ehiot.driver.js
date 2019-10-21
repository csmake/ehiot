/**
 * Copyright (c) 2012-2019 CSMAKE, Inc. 
 *
 * Generate by csmake.com for java Mon Oct 21 20:37:46 CST 2019
 */
var cn;
(function(cn){
    var ehiot;
    (function(ehiot){
        var driver=
        (function(driver){
            var _init = false; 
            var _tid = 0; 
            var _delayList = []; 
            var _monitors = new Map();
            var _listeners = new Map();
            var _ws = null;
            var _delayTimer = null;
            var _sid = '';
            var _paths=window.location.pathname.split("/"); _paths= window.location.pathname.replace(_paths[_paths.length-1],"");
            var _url =  window.location.protocol+'//'+window.location.host+(_paths)+'cn.ehiot.driver';
            function log(text,clr){
                console.log(text);
                if (typeof window.log === 'function'){
                    window.log(text,clr);
                }
                else if (typeof console !== 'undefined' && typeof console.log === 'function'){
                    console.log(text);
                }
            }
            function argsError(m,args){m='cn.ehiot.driver.'+m+' arguments is invalid:'+JSON.stringify(args); if (isasync(args) && typeof args[0].fail === 'function'){args[0].fail({message:m});}else{throw new SyntaxError(m); } }
            function isasync(args){  return (args.length >= 1 && args[0] && (typeof args[0].done === 'function'||typeof args[0].fail === 'function'||typeof args[0].always === 'function'));}
            function stringify(start, objs, names)
            {
                if ((names || []).length > 0){
                    var n = 0;
                    var args = "{"
                    for (var i = start; i < objs.length; i++){
                        if (i !== start)
                        args += ',';
                        args += '\"' + names[n++] + '\":' + JSON.stringify(objs[i]);
                    }
                    args += "}"
                    return args;
                }
                else
                {
                    return JSON.stringify(objs[start]);
                }
            }
            function _delayCall(){
                if (_init){
                    if (_delayTimer != null){
                        clearTimeout(_delayTimer);
                        _delayTimer = null;
                    }
                    while (_delayList.length > 0){
                        var f = _delayList.shift();
                        base.scall(f.cmd, f.args);
                    }
                }
                else if (_delayTimer == null){
                    _delayTimer = setInterval(function (){
                        _delayCall();
                    }
                    , 500);
                }
            };
            function isType(o, i) {
                return Object.prototype.toString.call(o) === _types[i];
            };
            function dumy(){}
            function defineProperty(_this, name, options) {
                _this[name] = function (v) {
                  var $1=arguments;  var async = isasync($1);
                    return  async ? ($1.length > 1 ? (options.set($1[0],$1[1]), $1[1]) : options.get($1[0])):($1.length > 0 ? (options.set($1[0]), $1[0]) : options.get());
                };
            };
            var base =driver.prototype;
            base.scall = function (cmd, args, names) {
                var async = isasync(args);
                if (async && !_init && cmd.indexOf('.ctor')===-1){
                  _delayList.push({ cmd:cmd, args:args });
                  _delayCall();
                  return;
                }
                var time=(new Date()).getTime();
                var message = "{'cmd':'" + cmd + "','args':" + stringify(async?1:0, args, names) + ",'sid':'" + _sid + "','time':"+time+",'tid':"+(++_tid)+"}";
                log(message,'send');
                var obj;
                function done(o){if(async&&typeof args[0].done === 'function'){args[0].done(o);}}
                function fail(o){if(async&&typeof args[0].fail === 'function'){args[0].fail(o);}else{throw o;}}
                function always(o){if(async&&typeof args[0].always === 'function'){args[0].always(o);}}
                var conv =async ? args[0].conv || function (o) { return o; }:function (o) { return o; };
                $.ajax({
                    method: "POST",
                    dataType: "text",
                    url: _url,
                    cache: false,
                    async: async,
                    data: message
                }).done(function (ret) {
                    obj = eval('('+ret+')');
                    log(obj,'recv');
              try {
                if (obj.errcode!='200') {
                    fail({'name': obj.errcode ,'message':obj.errmsg});
                    return;
                }
                obj = obj.data;
                done(conv((obj && obj._v) ? obj._v : obj));
                    } catch (e) {
                fail(e);
                    }
                }).fail(function (jqXHR, textStatus) {
                    if (textStatus === "parsererror") {
                try {
                    var obj = eval('('+jqXHR.responseText+')');
                    if (obj.errcode != '200') {
                    fail({'name': obj.errcode ,'message':obj.errmsg});
                        return;
                    }
                    obj = obj.data;
                    done(conv((obj && obj._v) ? obj._v : obj));
                } catch (e) {
                    fail(e);
                }
                    } else {
                fail({'name':500,'message': JSON.stringify(jqXHR)});
                    }
                }).always(always);
            return obj;
        };
        base.sid=function(val) {
                _sid = val;
        }
        /* public methods*/
        base.getEvent = function (type){
            var fn = 'getEvent';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],0)&&$1.length === (1+asyn)) {$2=["type"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        base.help = function (name){
            var fn = 'help';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],0)&&$1.length === (1+asyn)) {$2=["name"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        base.set = function (property){
            var fn = 'set';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],1)&&$1.length === (1+asyn)) {$2=["property"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        base.restart = function (option){
            var fn = 'restart';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],1)&&$1.length === (1+asyn)) {$2=["option"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        base.get = function (property){
            var fn = 'get';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],0)&&$1.length === (1+asyn)) {$2=["property"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        base.reset = function (option){
            var fn = 'reset';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],1)&&$1.length === (1+asyn)) {$2=["option"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        base.trigger = function (list){
            var fn = 'trigger';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],0)&&$1.length === (1+asyn)) {$2=["list"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        base.close = function (option){
            var fn = 'close';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],1)&&$1.length === (1+asyn)) {$2=["option"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        base.shutdown = function (option){
            var fn = 'shutdown';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],1)&&$1.length === (1+asyn)) {$2=["option"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        base.open = function (option){
            var fn = 'open';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],1)&&$1.length === (1+asyn)) {$2=["option"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        base.token = function (option){
            var fn = 'token';
            var $1=arguments;var asyn=isasync($1)?1:0;
            var $2 = [];
            if(isType($1[0+asyn],1)&&$1.length === (1+asyn)) {$2=["option"];}
            else{argsError(fn,$1);}
            if(asyn){
                base.scall(fn,$1,$2); 
            }else{
                return base.scall(fn, $1,$2);
            }
        };

        function driver(){
            var that=this;
            var $1=arguments;
            var fn = 'open';
            var asyn=isasync($1)?1:0;
            var $2 = ['option'];
            try{
                var args=[];
                if(asyn){
                    var cb=$1[0];
                    if(typeof cb.done === 'function'){
                        var old=cb.done;
                        cb.done=function(o){that.sid(o.sid);_init=true;old(o,that);}
                    }
                    else{
                        cb.done=function(o){that.sid(o.sid);_init=true;}
                    }
                    args.push(cb);
                }
                for(var i=asyn; i< $1.length; i++){
                    args.push($1[i]);
                }
                that.sid('cn.ehiot.driver');
                _init=true;
                var ret=driver.prototype.scall(fn,args,$2);
                if(!asyn){
                    that.sid(ret.sid);
                }
            }catch(e){
                log(e.message);
            }
            return this;
        }
    var _types=['[object Array]','[object Object]'];
    return driver;
})(driver = ehiot.driver||(ehiot.driver = {}));
ehiot.driver = driver;
return ehiot;
})(ehiot = cn.ehiot||(cn.ehiot = {}));
cn.ehiot = ehiot;
})(cn || (cn = {}));
