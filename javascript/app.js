
MF.Mofiler.init({
    scope: this,
    url: 'mofiler.com:8081',
    appKey: 'KEY-TEST-JS',
    appName: 'TestAppJS',
    identity: {'username':'johndoe'},
    useLocation: false,
    success: function(response,operation){
        var res = JSON.parse(response);
        if (operation === MF.RESTApi.K_MOFILER_API_METHOD_NAME_inject){
            console.log('This is the result from post ' + res.result);
            MF.Mofiler.getValue("mykey0", "username", "johndoe");
        }
        else if (operation === MF.RESTApi.K_MOFILER_API_METHOD_NAME_get)
            console.log('This is the result from get ' + res.value);
    },
    error: function(response,operation){
        var res = JSON.parse(response);
        var error = res.error;
        console.log("Error from server " + error);
    }
});


MF.Mofiler.injectValue('mykey' + 1, 'value1');


MF.Mofiler.getValue("mykey1", "username", "johndoe");

/*MF.Mofiler.injectValue('mykey' + 1, 'value1');
MF.Mofiler.injectValue('mykey' + 2, 'value2');
MF.Mofiler.injectValue('mykey' + 3, 'value3');
MF.Mofiler.injectValue('mykey' + 4, 'value4');
MF.Mofiler.injectValue('mykey' + 5, 'value5');
MF.Mofiler.injectValue('mykey' + 6, 'value6');
MF.Mofiler.injectValue('mykey' + 7, 'value7');
MF.Mofiler.injectValue('mykey' + 8, 'value8');
MF.Mofiler.injectValue('mykey' + 9, 'value9');
MF.Mofiler.injectValue('mykey' + 10, 'value10');
MF.Mofiler.injectValue('mykey' + 11, 'value11');
MF.Mofiler.injectValue('mykey' + 12, 'value12');
MF.Mofiler.injectValue('mykey' + 13, 'value13');


setTimeout(function(){
    MF.Mofiler.injectValue('mykey' + 14, 'value1');
    MF.Mofiler.injectValue('mykey' + 15, 'value2');
    MF.Mofiler.injectValue('mykey' + 16, 'value3');
    MF.Mofiler.injectValue('mykey' + 17, 'value4');
    MF.Mofiler.injectValue('mykey' + 18, 'value5');
    MF.Mofiler.injectValue('mykey' + 19, 'value6');
    MF.Mofiler.injectValue('mykey' + 20, 'value7');
    MF.Mofiler.injectValue('mykey' + 21, 'value8');
    MF.Mofiler.injectValue('mykey' + 22, 'value9');
},5000);*/

MF.Mofiler.flushDataToMofiler();

//on browser close should call onDestroyApp
// MF.Mofiler.onDestroyApp();

