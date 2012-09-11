
//Engine/Rule popup
$(document).ready( function()
{

	$('.engineRule').click( function(eventObject)
		{
			alert("Clicked!");
			//$(eventObject.target).children('.popup').fadeIn("slow");
			$(".popup").css({"visibility": "visible"});
			//var $popup = $(e.target).children('.popup').fadeIn("slow");;
			//loadPopupBox($popup);
		});

	function unloadPopupBox($popup) // TO Unload the Popupbox
	{
		$popup.fadeOut("slow");
		//$('.popup').fadeOut("slow");
		
	}

	function loadPopupBox($popup) // To Load the Popupbox
	{
		$popup.fadeIn("slow");
		//$(".popup").css({"visibility": "visible"});
	}
	
});