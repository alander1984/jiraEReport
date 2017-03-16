function submitReportForm() {
 	  var msg   = $('#special_report').serialize();
        $.ajax({
          type: 'GET',
          url: 'getDataCustomTimed',
          data: msg,
          success: function(data) {
            $('#reportResult').html("Отчет успешно сформирован");
          },
          error:  function(xhr, str){
	        alert('Возникла ошибка: ' + xhr.responseCode);
          }
        });
      }