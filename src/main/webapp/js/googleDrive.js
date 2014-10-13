/**
 * Created by tefreestone on 10/9/14.
 */


function onApiLoad() {
    gapi.load('auth', {'callback': onAuthApiLoad});
    gapi.load('picker');
}
function onAuthApiLoad() {
    window.gapi.auth.authorize({
        'client_id': '2551330326-ij1p55d6nl87drbl5627o1grvp6qc34e.apps.googleusercontent.com',
        'scope': ['https://www.googleapis.com/auth/drive']
    }, handleAuthResult);
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
        .addView(new google.picker.DocsUploadView())
        .addView(new google.picker.DocsView())
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
