

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



function createTooltip(event){          
    $('<div class="tooltip">test</div>').appendTo('body');
    positionTooltip(event);        
};



function positionTooltip(event){
    var tPosX = event.pageX - 10;
    var tPosY = event.pageY - 100;
    $('div.tooltip').css({'position': 'absolute', 'top': tPosY, 'left': tPosX});
};
