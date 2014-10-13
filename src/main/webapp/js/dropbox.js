/**
 * Created by tefreestone on 10/9/14.
 */


function dropboxLoad() {
    var button = Dropbox.choose({
        success: function (files) {
            var linkTag = document.getElementById('link');
            linkTag.href = files[0].link;
            linkTag.textContent = files[0].link;
        },
        linkType: 'direct'
    });
    document.getElementById('container').appendChild(button);
}