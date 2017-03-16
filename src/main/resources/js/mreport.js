/*function submitReportForm() {
    $( "#special_report" ).submit();
 	  var msg   = $('#special_report').serialize();
        $.ajax({
          type: 'GET',
          url: 'getDataCustomTimed',
          data: msg,
          success: function(data) {
            $('#reportResult').html("Report succeed prepared..."+data);
            $.fileDownload($(this).attr('action'), {
                    preparingMessageHtml: "We are preparing your report, please wait...",
                    failMessageHtml: "There was a problem generating your report, please try again.",
                    httpMethod: "POST",
                    data: $(this).serialize()
                });
          },
          error:  function(xhr, str){
	        alert('Error detected: ' + xhr.responseCode);
          }
        });
      }*/