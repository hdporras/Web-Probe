var tooltipsEnabled = false;


function toggleTooltips()
{
	var ttText = "";
	var ttTitle = "";
	
	if(!tooltipsEnabled)
	{
		tooltipsEnabled = true;
		
		ttText = 'Tooltips Enabled! You can now Follow instructions above to navigate provenance. '
			+'The Tooltips will help point out features as you go along.';
		ttTitle = 'Tips Enabled';
	}
	else
	{
		tooltipsEnabled = false;
		
		ttText = 'Tooltips have beend Disabled.';
		ttTitle	= 'Tips Disabled';
	}
	
	$("#tooltipToggle")
		.removeData('qtip')
		.qtip({
			content: {
				text: ttText,
				title: {
					text: ttTitle,
					button: true
				}
			},
			position: {
				my: 'left center', // Use the corner...
				at: 'right center' // ...and opposite corner
			},
			show: {
				event: false, // Don't specify a show event...
				ready: true // ... but show the tooltip when ready
			},
			hide: false,
			style: {
				classes: 'ui-tooltip-shadow ui-tooltip-' + 'tipped'
			}
	});
}


function createQAtips()
{
	if(tooltipsEnabled)
	{
		$(".answerConclusion").first()
			.qtip({
				content: {
					text: 'Click the Conclusion Box of your choice to browse its provenance information',
					title: {
						text: 'Tip',
						button: true
					}
				},
				position: {
					my: 'bottom left', // Use the corner...
					at: 'top center' // ...and opposite corner
				},
				show: {
					event: false, // Don't specify a show event...
					ready: true // ... but show the tooltip when ready
				},
				hide: {
					target: $("div"),
					event: 'click'
				},
				style: {
					classes: 'ui-tooltip-shadow ui-tooltip-' + 'tipped'
				}
		});
	}
}




function createProductTips()
{
	if(tooltipsEnabled)
	{
		$(".answerConclusion")
			.qtip({
				content: {
					text: 'Click the Conclusion Box of your choice to browse its provenance information',
					title: {
						text: 'Tip',
						button: true
					}
				},
				position: {
					my: 'bottom center', // Use the corner...
					at: 'top center' // ...and opposite corner
				},
				show: {
					event: false, // Don't specify a show event...
					ready: true // ... but show the tooltip when ready
				},
				hide: false, // Don't specify a hide event either!
				style: {
					classes: 'ui-tooltip-shadow ui-tooltip-' + 'jtools'
				}
		});
	}
}


function createLocalViewTips()
{
	if(tooltipsEnabled)
	{
		$("#ProductTabButton")
			.qtip({
				content: {
					text: 'The Product View will be updated with the current conclusion. Go to the Product View to view more detailed Visualizations of the Conclusion.',
					title: {
						text: 'Tip',
						button: true
					}
				},
				position: {
					my: 'top left', // Use the corner...
					at: 'bottom center' // ...and opposite corner
				},
				show: {
					event: false, // Don't specify a show event...
					ready: true // ... but show the tooltip when ready
				},
				hide: {
					target: $("div, #ProductTabButton"),//target: $("#ProductTabButton"),
					event: 'click'
				},
				style: {
					classes: 'ui-tooltip-shadow ui-tooltip-' + 'tipped'
				}
		});
	}
}

/*
var at = [
		'bottom left', 'bottom right', 'bottom center',
		'top left', 'top right', 'top center',
		'left center', 'left top', 'left bottom',
		'right center', 'right top', 'right bottom', 'center'
	],
	my = [
		'top left', 'top right', 'top center',
		'bottom left', 'bottom right', 'bottom center',
		'right center', 'right top', 'right bottom',
		'left center', 'left top', 'left bottom', 'center'
	],
	styles = [
		'red', 'blue', 'dark', 'light', 'green', 'jtools', 'plain', 'youtube', 'cluetip', 'tipsy', 'tipped', 'bootstrap'
	];

$('.structure')
			/*
			 * Lets delete the qTip data from our target element so we can apply multiple tooltips.
			 * Since the data is also stored on the tooltip element itself this isn't a problem!
			 * 
			 * Check here for more details on this: http://craigsworks.com/projects/qtip2/tutorials/advanced/#multi
			 */
/*			.removeData('qtip') 
			.qtip({
				content: {
					text: 'At its ' + at[i], 
					title: {
						text: 'My ' + my[i],
						button: true
					}
				},
				position: {
					my: my[i], // Use the corner...
					at: at[i] // ...and opposite corner
				},
				show: {
					event: false, // Don't specify a show event...
					ready: true // ... but show the tooltip when ready
				},
				hide: false, // Don't specify a hide event either!
				style: {
					classes: 'ui-tooltip-shadow ui-tooltip-' + styles[i]
				}
			});*/