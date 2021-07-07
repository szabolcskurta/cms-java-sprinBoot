
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
	userList();
	article();
	page();
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
						  .done(function(data) {
						   $(tableName).DataTable().ajax.reload();
							let messagData = JSON.parse(JSON.stringify(data));
							
							let  html = "<div class='alert  alert-danger' >"+messagData.message+"</div>";
							$(".content > .container-fluid ").find("#alert").prepend(html).ready(function(){
									flasMessage();
							})	
						  })
						  .fail(function() {
						   
						  });
					},
			        cancel: function () {
			            $.alert('Canceled!');
			        }
			    }
			});
	});
	
		$("body").on ("change","#pageType",function(){
						 console.log("changed");
			    $( "#pageType option:selected" ).each(function() {
			      	
					if( $( this ).val() === "LINK_TYPE"){
						console.log($( this ).val());
						 $("#article option[selected] ").removeAttr("selected");
				  }
			})
	  
					$("#article").toggleClass('d-none d-block');			
		});
	flasMessage();
});



