
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
							 " <a  id='delete' href=# data-tableName='userList' data-href='/admin/user/delete' data-id="+data.id+" class='btn btn-danger'>Delete</a>";
            		}
				
				}
			]
		});
	}
	if ($("#articleList").length) {
		$('#articleList').DataTable({
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
				"url": "/admin/article/list",
				"type": "POST"
			},
			"columns": [
				{ "data": "id" },
				{ "data": "title" },
				{ "data": "createdAt",render:function(data,type,row){
					if(data){
						var createdAt = new Date(data);
						return createdAt.getFullYear()+" / "+(createdAt.getMonth()+1) +" / "+createdAt.getDate();
					}
					else{
						return "";
					}
				}},
				{ "data": "updatedAt",render:function(data,type,row){
					if(data){
						var updatedAt = new Date(data);
						return updatedAt.getFullYear()+" / "+(updatedAt.getMonth()+1) +" / "+updatedAt.getDate();
					}else{
						return "";
					}
				
				} },
				{ "data": "createBy" },
				{
					data: null,
					 render:function(data, type, row)
           			 {
             			
						return "<a href=/admin/article/edit/" + data.id + " class='btn btn-primary'>Edit</a>"+
							 " <a  id='delete' href=# data-tableName='articleList' data-href='/admin/article/delete' data-id="+data.id+" class='btn btn-danger'>Delete</a>";
            		}
				
				}
			]
		});
	}
	if ($("#pageList").length) {
		$('#pageList').DataTable({
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
				"url": "/admin/page/list",
				"type": "POST"
			},
			"columns": [
				{ "data": "id" },
				{ "data": "url" },
				
				{ "data": "homepage",render:function(data,type,row){
					var homepage = data;
					console.log(data);
					return homepage==true?"Yes":"No"
				
				}},
				
				{
					data: null,
					 render:function(data, type, row)
           			 {
             			
						return "<a href=/admin/page/edit/" + data.id + " class='btn btn-primary'>Edit</a>"+
							 " <a  id='delete' href=# data-tableName='pageList' data-href='/admin/page/delete' data-id="+data.id+" class='btn btn-danger'>Delete</a>";
            		}
				
				}
			]
		});
	}
	$("body").on ("click","#delete",function(){
			var id = $(this).attr("data-id");
			var url = $(this).attr("data-href");
			var tableName = "#"+$(this).attr("data-tableName")
			$.confirm({
			    title: 'Delete',
			    content: 'Are you sure you want to delete?',
			    buttons: {
			        confirm: function () {
					 
					 console.log(id);
			          $.post( url +"/"+ id)
						  .done(function() {
						   $(tableName).DataTable().ajax.reload();
						  })
						  .fail(function() {
						    alert( "error" );
						  });
					},
			        cancel: function () {
			            $.alert('Canceled!');
			        }
			    }
			});
	});
	
});



