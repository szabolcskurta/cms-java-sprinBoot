
$(document).ready(function() {

	if ($("#uploadImg").length) {
		$("#uploadImg").click(function() {
			var formData = new FormData();
			formData.append('file', $('#profileImg')[0].files[0]);
			var userId = $("#userId").val();
			$.ajax({
				url: '/admin/user/upload/' + userId,
				type: 'POST',
				data: formData,
				processData: false,  // tell jQuery not to process the data
				contentType: false,  // tell jQuery not to set contentType
				success: function(data) {
					$("#userPhoto").attr("src", "/upload/" + $('#profileImg')[0].files[0].name);
				}
			});
		});
	}
	if ($("#profileImg").length) {
		$('#profileImg').on('change', function() {
			//get the file name
			var fileName = $(this)[0].files[0].name;
			$(this).next('.custom-file-label').html(fileName);
		})
	}
	if ($("#userList").length) {
		$('#userList').DataTable({
			"processing": true,
			"serverSide": true,
			"paging": true,
			"lengthChange": true,
			"searching": true,
			"ordering": true,
			"info": true,
			"autoWidth": false,
			"responsive": true,
			"ajax": {
				"url": "/admin/user/list",
				"type": "POST"
			},
			"columns": [
				{ "data": "id" },
				{ "data": "firstName" },
				{ "data": "firstName" },
				{ "data": "username" },
				{ "data": "email" },
				{
					data: null,
					 render:function(data, type, row)
           			 {
             			
						return "<a href=/admin/user/profile/" + data.id + " class='btn btn-primary'>Edit</a>"+
							 " <a href=/admin/user/delete/" + data.id + " class='btn btn-danger'>Delete</a>";
            		}
				
				}
			]
		});
	}
});



