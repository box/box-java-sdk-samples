/**
 * Created by dmaynard on 6/21/16.
 */
var form = document.getElementById('file-form');
var fileSelect = document.getElementById('file-select');
var uploadButton = document.getElementById('upload-button');
form.onsubmit = function (event)
{
	event.preventDefault();
	uploadButton.innerHTML = 'Uploading...';
	// The Box Auth Header. Supplied when the dashboard.jps page is rendered by the server
	var headers = {'Authorization': 'Bearer ' + accessToken};
	var uploadUrl = 'https://upload.box.com/api/2.0/files/content';
	var files = fileSelect.files;
	var formData = new FormData();
	formData.append('files', files[0], files[0].name);
	// Add the destination folder for the upload to the form
	formData.append('parent_id', '0');
	$.ajax({
		url: uploadUrl,
		headers: headers,
		type: 'POST',
		// This prevents JQuery from trying to append the form as a querystring
		processData: false,
		contentType: false,
		data: formData
	}).complete(function (data)
	{
		uploadButton.innerHTML = 'Upload';
		console.log(data.responseText);
		location.reload(true);
	});
	// TODO add error handling for production versions
}

