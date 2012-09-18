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
			.removeData('qtip') 
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
			});