/**
 * Created by tefreestone on 10/9/14.
 */


function onApiLoad(clientId) {
    gapi.load('auth', {'callback': function() {
        window.gapi.auth.authorize({
            'client_id': clientId,
            'scope': ['https://www.googleapis.com/auth/drive']
        }, handleAuthResult);
    }});
    gapi.load('picker');
}

var oauthToken;
function handleAuthResult(authResult) {
    if (authResult && !authResult.error) {
        oauthToken = authResult.access_token;
        createPicker();
    }
}
function createPicker() {
    var picker = new google.picker.PickerBuilder()
        .addView(new google.picker.DocsView())
        .addView(new google.picker.DocsUploadView())
        .setOAuthToken(oauthToken)
        .setDeveloperKey('')
        .setCallback(pickerCallback)
        .build();
    picker.setVisible(true);
}

function pickerCallback(data) {
    var url = 'nothing';
    if (data[google.picker.Response.ACTION] == google.picker.Action.PICKED) {
        var doc = data[google.picker.Response.DOCUMENTS][0];
        url = doc[google.picker.Document.URL];
    }
    var message = 'You picked: ' + url;
    document.getElementById('result').innerHTML = message;
}
